package com.aucklandmuseum.image;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aucklandmuseum.image.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class ImageRetrievingIntegrationTest {

	@Produce(uri = "direct:start")
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageRetrievingIntegrationTest.class);

	@Test
	public void test() throws Exception {
		// String expectedResponse = "rdfiser done";
		String response = template.requestBody(ImageTestUtils.getRequest(),
				String.class);
		LOGGER.info("response:{} ", response);

		Thread.sleep(10000);

	}

}
