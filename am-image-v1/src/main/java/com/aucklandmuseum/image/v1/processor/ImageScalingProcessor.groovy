package com.aucklandmuseum.image.v1.processor;


import groovy.util.logging.Slf4j

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr
import org.imgscalr.Scalr.Mode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import com.aucklandmuseum.image.v1.ImageTransform

@Component
@Slf4j
class ImageScalingProcessor implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		byte[] imageBytes = exchange.getProperty("imageBytes")
		String imageExtension = exchange.getProperty("imageExtension", String.class)
		ImageTransform imageTransform = exchange.in.getBody(ImageTransform.class);
		String fileName = imageTransform.imageName + "."+imageExtension
		InputStream imageInputStream= new ByteArrayInputStream(imageBytes)
		if(imageTransform.imageName != 'original'){
			BufferedImage img = ImageIO.read(imageInputStream)
			final BufferedImage bufferedImage = Scalr.resize(img,
					Mode.AUTOMATIC,imageTransform.width, imageTransform.hight)
			final ByteArrayOutputStream output = new ByteArrayOutputStream() {
						@Override
						public synchronized byte[] toByteArray() {
							return this.buf
						}
					};
			ImageIO.write(bufferedImage, imageExtension, output)
			imageInputStream = new ByteArrayInputStream(
					output.toByteArray(), 0, output.size())
		}

		exchange.in.setBody(imageInputStream, InputStream.class)
		exchange.setProperty("imageName", fileName)

		//set s3 headers: CamelAwsS3Key,CamelAwsS3ContentLength,CamelAwsS3ContentType
		exchange.in.setHeader("CamelAwsS3Key", exchange.getProperty("outputPath"))
		exchange.in.setHeader("CamelAwsS3ContentLength", ((ByteArrayInputStream) imageInputStream).available())
		exchange.in.setHeader("CamelAwsS3ContentType", "image/jpeg")
	}
}
