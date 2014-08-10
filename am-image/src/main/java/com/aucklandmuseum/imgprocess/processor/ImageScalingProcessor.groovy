package com.aucklandmuseum.imgprocess.processor;


import groovy.util.logging.Slf4j

import java.awt.image.BufferedImage

import javax.imageio.ImageIO;

import org.apache.camel.Exchange;
import org.apache.camel.Processor
import org.imgscalr.Scalr
import org.imgscalr.Scalr.Mode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import com.aucklandmuseum.imgprocess.ImageScalingBean;

@Component
@Slf4j
class ImageScalingProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		byte[] imgBytes = exchange.getProperty("imageBytes", byte[].class)
		String imageExtension = exchange.getProperty("imageExtension", String.class)
		ImageScalingBean imageScalingBean = exchange.getIn().getBody(ImageScalingBean.class);
		String fileName = imageScalingBean.imageName + "."+imageExtension

		if(imageScalingBean.imageName != 'original'){
			InputStream inputstream;
			try {
				inputstream = new ByteArrayInputStream(imgBytes)
				BufferedImage img = ImageIO.read(inputstream)
				final BufferedImage bufferedImage = Scalr.resize(img,
						Mode.AUTOMATIC,imageScalingBean.width, imageScalingBean.hight)

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
