package com.aucklandmuseum.image.v1.processor;


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

import com.aucklandmuseum.image.v1.ImageTransform

@Component
@Slf4j
class ImageScalingProcessor implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		byte[] imgBytes = exchange.getProperty("imageBytes", byte[].class)
		String imageExtension = exchange.getProperty("imageExtension", String.class)
		ImageTransform imageTransform = exchange.in.getBody(ImageTransform.class);
		String fileName = imageTransform.imageName + "."+imageExtension

		if(imageTransform.imageName != 'original'){
			InputStream inputstream
			try {
				inputstream = new ByteArrayInputStream(imgBytes)
				BufferedImage img = ImageIO.read(inputstream)
				final BufferedImage bufferedImage = Scalr.resize(img,
						Mode.AUTOMATIC,imageTransform.width, imageTransform.hight)
				final ByteArrayOutputStream output = new ByteArrayOutputStream() {
							@Override
							public synchronized byte[] toByteArray() {
								return this.buf
							}
						};
				ImageIO.write(bufferedImage, imageExtension, output)
				InputStream imageInputStream = new ByteArrayInputStream(
						output.toByteArray(), 0, output.size())
				exchange.setProperty("imageBytes", imageInputStream)
			}finally{
				if(inputstream){
					inputstream.close()
				}
			}
		}
		exchange.setProperty("imageName", fileName)
	}
}
