package com.aucklandmuseum.image;

import static com.aucklandmuseum.image.ImageMetaRetrieveProcessor.IMG_METADATA;

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
public class ImageMetadataMergeTransformer implements Expression {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageMetadataMergeTransformer.class);

	private static final String IMG_META_KEY = "metadata";

	@SuppressWarnings("unchecked")
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		final Map<String, Object> record = exchange.getIn().getBody(Map.class);
		Map<String, Object> avMap = (Map<String, Object>) record.get("av");

		Map<String, String> metadataMap = new HashMap<>();

		final IImageMetadata metadata = exchange.getProperty(IMG_METADATA,
				IImageMetadata.class);

		if (metadata != null) {
			for (Object tempItem : metadata.getItems()) {
				Item item = (Item) tempItem;
				String keyword = item.getKeyword();
				String text = item.getText();
				metadataMap.put(keyword, text);
			}
		}

		avMap.put(IMG_META_KEY, metadataMap);
		record.put("av", avMap);

		return (T) record;
	}

}
