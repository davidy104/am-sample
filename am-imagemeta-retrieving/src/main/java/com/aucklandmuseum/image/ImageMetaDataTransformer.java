package com.aucklandmuseum.image;

import static com.aucklandmuseum.image.ImageMetadataRetrievingProcessor.IMG_METADATA;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ImageMetaDataTransformer implements Expression {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageMetaDataTransformer.class);

	@SuppressWarnings("unchecked")
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		LOGGER.info("ImageMetaDataTransformer start");
		Map<String, Object> output = (Map<String, Object>) exchange.getIn()
				.getBody();

		final IImageMetadata metadata = exchange.getProperty(IMG_METADATA,
				IImageMetadata.class);

		Map<String, String> imgMetadataMap = this.covertImageMetadata(metadata);
		output.put("metadata", imgMetadataMap);

		LOGGER.info("ImageMetaDataTransformer end");
		return ((T) output);
	}

	private Map<String, String> covertImageMetadata(
			final IImageMetadata metadata) {
		Map<String, String> metadataMap = new HashMap<>();

		for (Object tempItem : metadata.getItems()) {
			Item item = (Item) tempItem;
			String keyword = item.getKeyword();
			String text = item.getText();
			metadataMap.put(keyword, text);
		}
		return metadataMap;
	}

}
