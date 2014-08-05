package com.aucklandmuseum.image;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aucklandmuseum.image.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class MessageConsumerIntegrationTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MessageConsumerIntegrationTest.class);
	@Resource
	private CamelContext camelContext;

	@Test
//	@Ignore
	public void test() {
		ConsumerTemplate consumerTemplate = camelContext
				.createConsumerTemplate();
		Exchange exchange = consumerTemplate.receive("jms:queue:imageQueue",
				3000);

		LOGGER.info("receive message:{} ", exchange);

		if (exchange != null && exchange.getIn().getBody(Map.class) != null) {
			Map<String, Object> message = exchange.getIn().getBody(Map.class);
			for (final Entry<String, Object> entry : message.entrySet()) {
				LOGGER.info("key:{} ", entry.getKey());
				LOGGER.info("value:{} ", entry.getValue());
			}
		}

	}

}
