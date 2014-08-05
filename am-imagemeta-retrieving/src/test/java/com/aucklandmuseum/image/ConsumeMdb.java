package com.aucklandmuseum.image;

import org.apache.camel.Consume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
public class ConsumeMdb {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConsumeMdb.class);

//	@Consume(uri = "activemq:queue:imageQueue")
//	public String onMyMessage(String message) {
//		LOGGER.info("Message = {}", message);
//		return "Hello " + message;
//	}
}