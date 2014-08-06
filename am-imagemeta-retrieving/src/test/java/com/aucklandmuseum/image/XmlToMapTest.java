package com.aucklandmuseum.image;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
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
@Ignore
public class XmlToMapTest {
	@Produce(uri = "direct:xmlToMapStart")
	private ProducerTemplate producerTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlToMapTest.class);

	private static final String XML_REQUEST_FILE = "/cenotaph-new.xml";

	private String xmlRequest;

	@Before
	public void initial() throws Exception {
		URL url = ImageRetrievingMulticastIntegrationTest.class
				.getResource(XML_REQUEST_FILE);

		try (FileInputStream fisTargetFile = new FileInputStream(new File(
				url.getFile()))) {
			xmlRequest = IOUtils.toString(fisTargetFile, "UTF-8");
		}

	}

	@Test
	public void test() {
		Map<String, Object> responseMap = (Map<String, Object>) producerTemplate
				.requestBody(xmlRequest);
		// LOGGER.info("responseMap:{} ", responseMap);

		for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
			if ("av".equals(entry.getKey())) {
				LOGGER.info("key:{} ", entry.getKey());
				LOGGER.info("value:{} ", entry.getValue());

			}
		}

	}

}
