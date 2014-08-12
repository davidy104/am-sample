package com.aucklandmuseum.image.v1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

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

import com.aucklandmuseum.image.v1.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ImageProcessTest {

	@Produce
	private ProducerTemplate producerTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageProcessTest.class);

	@SuppressWarnings("rawtypes")
	@Test
	public void testWithImageMeta() throws Exception {
		String request = new String(Files.readAllBytes(Paths
				.get(ImageProcessTest.class.getResource("/withMetaReq.json")
						.getPath())));
		LOGGER.info("request:{} ", request);
		Map response = producerTemplate.requestBody("seda:imageProcess",
				request, Map.class);
		LOGGER.info("response:{} ", response);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testWithOutImageMeta() throws Exception {
		String request = new String(Files.readAllBytes(Paths
				.get(ImageProcessTest.class.getResource("/withoutMetaReq.json")
						.getPath())));
		LOGGER.info("request:{} ", request);
		Map response = producerTemplate.requestBody("seda:imageProcess",
				request, Map.class);
		LOGGER.info("response:{} ", response);
	}
}
