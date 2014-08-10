package com.aucklandmuseum.imgprocess.processor;

import groovy.util.logging.Slf4j

import org.apache.camel.ConsumerTemplate
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.commons.io.FileUtils
import org.apache.sanselan.Sanselan
import org.apache.sanselan.common.IImageMetadata
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import com.aucklandmuseum.imgprocess.ImageNotFoundException

@Component
@Slf4j
public class ImageMetadataRetrievingProcessor implements Processor {

	@Value('${filesys.basepath:inbox}')
	String imageBasePath
	ConsumerTemplate consumerTemplate
	static final String IMAGE_KEY = "Digital Filename"
	static final String IMG_METADATA = "imageMetaData"
	static final String IMG_SEPERATOR = "\\"


	@Override
	public void process(Exchange exchange) throws Exception {
		String filePath,imageName
		final Map<String, Object> record = exchange.getIn().getBody(Map.class);
		Map<String, Object> avMap = (Map<String, Object>) record.get("av")
		consumerTemplate = exchange.getContext().createConsumerTemplate()

		String digitalFilename = (String)avMap.get(IMAGE_KEY)
		digitalFilename= (digitalFilename.startsWith("/"))?digitalFilename:"/"+digitalFilename

		if (digitalFilename.indexOf(IMG_SEPERATOR) == -1){
			imageName = digitalFilename
			filePath = imageBasePath
		}else  {
			digitalFilename = digitalFilename.replace(IMG_SEPERATOR, "/")
			imageName = digitalFilename.substring(digitalFilename.lastIndexOf("/") + 1,
					digitalFilename.length());
			filePath = imageBasePath + digitalFilename
					.substring(0, digitalFilename.lastIndexOf("/") + 1)
		}

		try {
			Exchange imgMetadataExchange = this.consumerTemplate.receive("file://" + filePath
					+ "?fileName=" + imageName, 3000)
			if (!imgMetadataExchange) {
				throw new IllegalArgumentException()
			}
			File image = imgMetadataExchange.getIn().getBody(File.class)
			exchange.setProperty("imageBytes", FileUtils.readFileToByteArray(image))
			exchange.getIn().setBody(Sanselan.getMetadata(image), IImageMetadata.class)
		} catch (e) {
			throw new ImageNotFoundException(
			"retrieving image meta data failed.", e)
		}
	}
}
