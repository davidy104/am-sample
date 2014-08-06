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
public class ImageMetaRetrieveProcessor implements Processor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageMetadataRetrievingProcessor.class);

	@Value("${filesys.basepath:inbox}")
	private String imageBasePath;

	private ConsumerTemplate consumerTemplate;

	private static final String IMAGE_KEY = "Digital Filename";

	public static final String IMG_METADATA = "imageMetaData";

	private static final String IMG_SEPERATOR = "\\";

	private static final String IMG_META_KEY = "metadata";

	@SuppressWarnings("unchecked")
	@Override
	public void process(Exchange exchange) throws Exception {
		LOGGER.info("ImageMetaRetrieveProcessor start");
		final Map<String, Object> record = exchange.getIn().getBody(Map.class);

		Map<String, Object> avMap = (Map<String, Object>) record.get("av");
		String digitalFilename = (String) avMap.get(IMAGE_KEY);

		String[] imageInfo = imagePathFormat(digitalFilename);

		consumerTemplate = exchange.getContext().createConsumerTemplate();

		String filePath = imageInfo.length == 1 ? imageBasePath : imageBasePath
				+ imageInfo[0];
		String imageName = imageInfo.length == 1 ? imageInfo[0] : imageInfo[1];

		LOGGER.info("file path:{} ", filePath);
		LOGGER.info("image name:{} ", imageInfo[1]);

		Exchange imgExchange = null;
		File image = null;

		try {
			imgExchange = consumerTemplate.receive("file://" + filePath
					+ "?fileName=" + imageName);
			Message message = imgExchange.getIn();
			image = message.getBody(File.class);
			LOGGER.info("image: {} ", image.getAbsolutePath());
			if (image == null) {
				throw new Exception();
			}
		} catch (Exception e) {

		}

		final IImageMetadata metadata = Sanselan.getMetadata(image);
		exchange.setProperty(IMG_METADATA, metadata);
		LOGGER.info("ImageMetaRetrieveProcessor end");
	}

	private String[] imagePathFormat(final String imgPath) {
		String image = imgPath;
		if (image.indexOf(IMG_SEPERATOR) == -1) {
			if (!image.startsWith("/")) {
				image = "/" + image;
			}
			return new String[] { image };
		}

		String tempimgPath = imgPath.replace(IMG_SEPERATOR, "/");
		if (!tempimgPath.startsWith("/")) {
			tempimgPath = "/" + tempimgPath;
		}
		image = tempimgPath.substring(tempimgPath.lastIndexOf("/") + 1,
				tempimgPath.length());
		String path = tempimgPath
				.substring(0, tempimgPath.lastIndexOf("/") + 1);

		return new String[] { path, image };
	}

}
