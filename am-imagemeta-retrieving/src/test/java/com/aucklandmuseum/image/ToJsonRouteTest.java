package com.aucklandmuseum.image;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aucklandmuseum.image.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ToJsonRouteTest {
	@Produce(uri = "direct:toJsonStart")
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ToJsonRouteTest.class);

	@Test
	public void testWithAV() {
		String response = template.requestBody(ImageTestUtils.getRequestV1(),
				String.class);
		LOGGER.info("response:{} ", response);

	}

	@Test
	public void testWithOutAV() {
		String response = template.requestBody(ImageTestUtils.getRequestV2(),
				String.class);
		LOGGER.info("response:{} ", response);

	}
}
