package com.aucklandmuseum.image.v2.processor

import groovy.util.logging.Slf4j

import org.apache.camel.ConsumerTemplate
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.sanselan.Sanselan
import org.apache.sanselan.common.IImageMetadata
import org.apache.sanselan.common.ImageMetadata.Item
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@Slf4j
class ImageMetadataRetrievingProcessor implements Processor {

	ConsumerTemplate consumerTemplate

	@Value('${filesys.basepath:inbox}')
	String imageBasePath

	@Override
	public void process(Exchange exchange) throws Exception {
		Map imageProcessRequest = exchange.in.getBody(Map.class)
		def metadataMap = [:]

		this.consumerTemplate = exchange.getContext().createConsumerTemplate()
		Exchange imgMetadataExchange = this.consumerTemplate.receive("file://" +imageBasePath + imageProcessRequest['imagePath']
				+ "?fileName=" + imageProcessRequest['imageFileName'], 3000)
		if (!imgMetadataExchange) {
			throw new RuntimeException()
		}

		def imageTransforms = imageProcessRequest['transform']
		def originalTransform = [name:'original']
		imageTransforms << originalTransform
		exchange.setProperty("imageTransforms", imageTransforms)
		def imgMetaBody = imgMetadataExchange.getIn().getBody()

		//		File image = imgMetadataExchange.getIn().getBody(File.class)
		File image = imgMetadataExchange.getIn().getBody(File.class)
		InputStream imageStream = imgMetadataExchange.getIn().getBody(InputStream.class)
		exchange.setProperty("imageBytes", FileUtils.readFileToByteArray(image))
		exchange.setProperty("imageExtension", FilenameUtils.getExtension(image.getAbsolutePath()))
		exchange.setProperty("outputPath", imageProcessRequest['outputPath'])

		IImageMetadata metadata = Sanselan.getMetadata(imageStream,imageProcessRequest['imageFileName'])
		if(metadata) {
			metadata.getItems().each{
				Item item = (Item)it
				metadataMap.put(item.keyword, item.text)
			}
		}
		exchange.in.setBody(metadataMap, Map.class)
	}
}
