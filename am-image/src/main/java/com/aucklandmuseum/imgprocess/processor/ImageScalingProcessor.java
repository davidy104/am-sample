package com.aucklandmuseum.imgprocess.processor;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ImageScalingProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageScalingProcessor.class);

	// format example: 1024*1024
	private static final String COLON = "*";
	private static final String EXTENSION = ".jpeg";

	@Override
	public void process(Exchange exchange) throws Exception {
		Message message = exchange.getIn();
		byte[] imgBytes = message.getBody(byte[].class);

		String size = message.getHeader("size", String.class);
		String imageResizeTyep = message.getHeader("imageResizeType",
				String.class);

		exchange.setProperty("imageName", imageResizeTyep + EXTENSION);

		if (!imageResizeTyep.equalsIgnoreCase("original")) {
			Integer[] sizeArray = this.getImageSize(size);

			try (InputStream in = new ByteArrayInputStream(imgBytes)) {
				BufferedImage img = ImageIO.read(in);
				final BufferedImage bufferedImage = Scalr.resize(img,
						Mode.AUTOMATIC, sizeArray[0], sizeArray[1]);

				final ByteArrayOutputStream output = new ByteArrayOutputStream() {
					@Override
					public synchronized byte[] toByteArray() {
						return this.buf;
					}
				};
				ImageIO.write(bufferedImage, "jpeg", output);
				InputStream imageInputStream = new ByteArrayInputStream(
						output.toByteArray(), 0, output.size());
				message.setBody(imageInputStream, InputStream.class);
			}
		}
	}

	private Integer[] getImageSize(final String size) {
		if (size.indexOf(COLON) == -1) {
			return new Integer[] { Integer.valueOf(size), Integer.valueOf(size) };
		}

		String widthStr = substringBefore(size, COLON);
		String hightStr = substringAfter(size, COLON);

		if (StringUtils.isEmpty(widthStr) && StringUtils.isEmpty(hightStr)) {
			throw new RuntimeException("size configuration format is invalid");
		}

		Integer width = StringUtils.isEmpty(widthStr) ? Integer
				.valueOf(hightStr) : Integer.valueOf(widthStr);

		Integer hight = StringUtils.isEmpty(hightStr) ? Integer
				.valueOf(widthStr) : Integer.valueOf(hightStr);

		return new Integer[] { width, hight };
	}
}
