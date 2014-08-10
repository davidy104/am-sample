package com.aucklandmuseum.imgprocess;

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

import com.aucklandmuseum.imgprocess.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class VernonRouteTest {

	@Produce(uri = "direct:toJsonStart")
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(VernonRouteTest.class);

	private final static String WITHAV_RESP = "/withAv.json";
	private final static String WITHOUTAV_RESP = "/withOutAv.json";

	@Test
	public void testWithAV() throws Exception {
		Map<String, Object> body = ImageTestUtils.getRequestV1();
		String response = template.requestBody("direct:toJsonStart", body,
				String.class);
		LOGGER.info("response:{} ", response);

		// URL url = VernonRouteTest.class.getResource(WITHAV_RESP);
		// String expectedResponse = null;
		// try (FileInputStream fisTargetFile = new FileInputStream(new File(
		// url.getFile()))) {
		// expectedResponse = IOUtils.toString(fisTargetFile, "UTF-8");
		// }
		// assertTrue(response.equals(expectedResponse));

	}

	@Test
	public void testWithOutAV() throws Exception {

		Map<String, Object> body = ImageTestUtils.getRequestV2();

		String response = template.requestBody("direct:toJsonStart", body,
				String.class);

		LOGGER.info("response:{} ", response);

	}

}
