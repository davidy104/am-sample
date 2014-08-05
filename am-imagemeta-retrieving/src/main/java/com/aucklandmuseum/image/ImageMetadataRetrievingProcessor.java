package com.aucklandmuseum.image;

import java.io.File;
import java.util.Map;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageMetadataRetrievingProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageMetadataRetrievingProcessor.class);

	@Value("${filesys.basepath:inbox}")
	private String imageBasePath;

	private ConsumerTemplate consumerTemplate;

	private static final String IMAGE_KEY = "Digital Filename";

	public static final String IMG_METADATA = "imageMetaData";

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		LOGGER.info("ImageMetadataRetrievingProcessor start");
		Map<String, Object> avInfoMap = (Map<String, Object>) exchange.getIn()
				.getBody();
		String[] imageInfo = imagePathFormat((String) avInfoMap.get(IMAGE_KEY));

		consumerTemplate = exchange.getContext().createConsumerTemplate();

		String filePath = imageBasePath + imageInfo[0];
		LOGGER.info("file path:{} ", filePath);

		LOGGER.info("image name:{} ", imageInfo[1]);

		Exchange imgExchange = consumerTemplate.receive("file://" + filePath
				+ "?fileName=" + imageInfo[1]);
		Message message = imgExchange.getIn();
		File image = message.getBody(File.class);
		LOGGER.info("image: {} ", image.getAbsolutePath());
		final IImageMetadata metadata = Sanselan.getMetadata(image);
		exchange.setProperty(IMG_METADATA, metadata);
		LOGGER.info("metadata: {} ", metadata);
		// for testing
		Thread.sleep(5000);
		LOGGER.info("ImageMetadataRetrievingProcessor end");
	}

	private String[] imagePathFormat(final String imgPath) {
		String tempimgPath = imgPath.replace("\\", "/");
		if (!tempimgPath.startsWith("/")) {
			tempimgPath = "/" + tempimgPath;
		}
		String image = tempimgPath.substring(tempimgPath.lastIndexOf("/") + 1,
				tempimgPath.length());
		String path = tempimgPath
				.substring(0, tempimgPath.lastIndexOf("/") + 1);

		return new String[] { path, image };
	}

}
