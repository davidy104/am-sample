package com.aucklandmuseum.image;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.io.IOUtils;
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
public class ImageRetrievingMulticastIntegrationTest {

	@Produce(uri = "direct:multicastStart")
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageRetrievingMulticastIntegrationTest.class);

	// mock:multiPfirst
	@EndpointInject(uri = "mock:rdfProcess")
	private MockEndpoint mockRdfProcess;

	private static final String XML_REQUEST_FILE = "/cenotaph-new.xml";

	@Test
	@Ignore
	public void test() throws Exception {
		// String expectedResponse = "rdfiser done";
		template.requestBody(ImageTestUtils.getRequest());

		String rdfProcessResponse = mockRdfProcess.getExchanges().get(0)
				.getIn().getBody(String.class);
		LOGGER.info("rdfProcessResponse:{} ", rdfProcessResponse);

		Thread.sleep(10000);
	}

	@Test
	public void testSendXmlRequest() throws Exception {
		URL url = ImageRetrievingMulticastIntegrationTest.class
				.getResource(XML_REQUEST_FILE);
		String targetFileStr = null;

		try (FileInputStream fisTargetFile = new FileInputStream(new File(
				url.getFile()))) {
			targetFileStr = IOUtils.toString(fisTargetFile, "UTF-8");
		}

		template.requestBody(targetFileStr);

		Map<String, Object> rdfProcessResponse = mockRdfProcess.getExchanges()
				.get(0).getIn().getBody(Map.class);

	}

}
