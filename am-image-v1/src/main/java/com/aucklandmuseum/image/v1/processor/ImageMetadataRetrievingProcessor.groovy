package com.aucklandmuseum.image.v1.processor

import groovy.util.logging.Slf4j

import org.apache.camel.ConsumerTemplate
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.sanselan.Sanselan
import org.apache.sanselan.common.IImageMetadata
import org.apache.sanselan.common.ImageMetadata.Item
import org.springframework.stereotype.Component

import com.aucklandmuseum.image.v1.ImageNotFoundException
import com.aucklandmuseum.image.v1.ImageProcessRequest
import com.aucklandmuseum.image.v1.ImageTransform

@Component
@Slf4j
class ImageMetadataRetrievingProcessor implements Processor {

	ConsumerTemplate consumerTemplate

	@Override
	public void process(Exchange exchange) throws Exception {
		ImageProcessRequest imageProcessRequest = exchange.in.getBody(ImageProcessRequest.class)
		def metadataMap = [:]
		try {
			Exchange imgMetadataExchange = this.consumerTemplate.receive("file://" + imageProcessRequest.imagePath
					+ "?fileName=" + imageProcessRequest.imageFileName, 3000)
			if (!imgMetadataExchange) {
				throw new RuntimeException()
			}
			imageProcessRequest.imageTransforms << new ImageTransform(imageName:"original")
			exchange.setProperty("imageScalingConfigs", imageProcessRequest.imageTransforms)
			File image = imgMetadataExchange.getIn().getBody(File.class)
			exchange.setProperty("imageExtension", FilenameUtils.getExtension(image.getAbsolutePath()))
			exchange.setProperty("imageBytes", FileUtils.readFileToByteArray(image))


			IImageMetadata metadata = Sanselan.getMetadata(image)
			if(metadata) {
				metadata.getItems().each{
					Item item = (Item)it
					metadataMap.put(item.keyword, item.text)
				}
			}
			exchange.in.setBody(metadataMap, Map.class)
		} catch (e) {
			throw new ImageNotFoundException(
			"retrieving image meta data failed.", e)
		}
	}
}
