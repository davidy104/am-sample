package com.aucklandmuseum.image;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
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

	@Test
	public void test() throws Exception {
		// String expectedResponse = "rdfiser done";
		template.requestBody(ImageTestUtils.getRequest());

		String rdfProcessResponse = mockRdfProcess.getExchanges().get(0)
				.getIn().getBody(String.class);
		LOGGER.info("rdfProcessResponse:{} ", rdfProcessResponse);

		Thread.sleep(10000);

	}

}
