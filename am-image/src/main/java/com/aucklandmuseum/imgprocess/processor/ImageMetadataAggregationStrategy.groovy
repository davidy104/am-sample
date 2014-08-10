package com.aucklandmuseum.imgprocess.processor;

import groovy.util.logging.Slf4j

import org.apache.camel.Exchange
import org.apache.camel.processor.aggregate.AggregationStrategy
import org.apache.sanselan.common.IImageMetadata
import org.apache.sanselan.common.ImageMetadata.Item
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Slf4j
class ImageMetadataAggregationStrategy implements AggregationStrategy {
	static final String IMG_META_KEY = "metadata"

	@Override
	public Exchange aggregate(Exchange originalExchange,
			Exchange imageMetaExchange) {
		def metadataMap = [:]

		final Map<String, Object> record = originalExchange.getIn().getBody(
				Map.class)
		Map<String, Object> avMap = (Map<String, Object>) record.get("av")

		final IImageMetadata metadata = imageMetaExchange.getIn().getBody(
				IImageMetadata.class)

		if(metadata) {
			metadata.getItems().each() {
				Item item = (Item)it
				metadataMap.put(item.keyword, item.text)
			}
		}

		avMap.put(IMG_META_KEY, metadataMap)
		record.put("av", avMap)
		originalExchange.getIn().setBody(record)
		return originalExchange
	}
}
