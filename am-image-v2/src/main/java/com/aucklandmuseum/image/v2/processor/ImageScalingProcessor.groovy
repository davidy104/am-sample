package com.aucklandmuseum.image.v2.processor;


import groovy.util.logging.Slf4j

import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.imgscalr.Scalr
import org.imgscalr.Scalr.Mode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Slf4j
class ImageScalingProcessor implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		byte[] imageBytes = exchange.getProperty("imageBytes")
		String imageExtension = exchange.getProperty('imageExtension')
		Map imageTransform = exchange.in.getBody();
		String fileName = imageTransform['name'] + "."+imageExtension
		InputStream imageInputStream= new ByteArrayInputStream(imageBytes)
		if(imageTransform['name'] != 'original'){
			Integer width = Integer.valueOf(imageTransform['width'])
			Integer hight = Integer.valueOf(imageTransform['hight'])
			BufferedImage img = ImageIO.read(imageInputStream)
			final BufferedImage bufferedImage = Scalr.resize(img,
					Mode.AUTOMATIC,width, hight)
			final ByteArrayOutputStream output = new ByteArrayOutputStream() {
						@Override
						public synchronized byte[] toByteArray() {
							return this.buf
						}
					}
			ImageIO.write(bufferedImage, imageExtension, output)
			imageInputStream = new ByteArrayInputStream(
					output.toByteArray(), 0, output.size())
		}

		exchange.in.setBody(imageInputStream, InputStream.class)
		//for file endpoint
		exchange.setProperty("imageName", fileName)

		//for s2 endpoint

		String outputPath = exchange.getProperty("outputPath")
		String camelAwsS3Key = !outputPath.endsWith("/")?outputPath +"/"+fileName:outputPath+fileName


		//set s3 headers: CamelAwsS3Key,CamelAwsS3ContentLength,CamelAwsS3ContentType
		exchange.in.setHeader("CamelAwsS3Key", camelAwsS3Key)
		exchange.in.setHeader("CamelAwsS3ContentLength", ((ByteArrayInputStream) imageInputStream).available())
		exchange.in.setHeader("CamelAwsS3ContentType", "image/jpeg")
	}
}
