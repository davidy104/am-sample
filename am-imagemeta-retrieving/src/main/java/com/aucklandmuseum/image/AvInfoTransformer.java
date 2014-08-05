package com.aucklandmuseum.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AvInfoTransformer implements Expression {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AvInfoTransformer.class);

	@SuppressWarnings("unchecked")
	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		LOGGER.info("AvInfoTransformer start");
		List<Object> avList = null;

		final Map<String, Object> record = exchange.getIn().getBody(Map.class);

		for (final Entry<String, Object> entry : record.entrySet()) {
			if (entry.getKey().equals("av")) {
				if (avList == null) {
					avList = new ArrayList<>();
				}

				Object avInfoObject = ((Map<String, Object>) entry.getValue())
						.get("Photo/Audio-Visual Ref");

				if (avInfoObject instanceof List) {
					List<Object> photoInfo = (List<Object>) avInfoObject;
					avList.addAll(photoInfo);
				} else {
					avList.add(avInfoObject);
				}
			}
		}
		avList = avList == null ? Collections.<Object> emptyList() : avList;

		LOGGER.info("AvInfoTransformer end");
		return (T) avList;
	}
}
