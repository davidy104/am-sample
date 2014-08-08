package com.aucklandmuseum.imgprocess;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aucklandmuseum.imgprocess.config.ApplicationConfiguration;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class VernonRouteTest {

	@Produce(uri = "direct:toJsonStart")
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(VernonRouteTest.class);

	@Resource
	private ImageResizeConfigBean imageResizeConfigBean;

	private static final String IMG_RESIZE_CONFIG_KEY = "imageResizeConfig";

	private final static String WITHAV_RESP = "/withAv.json";
	private final static String WITHOUTAV_RESP = "/withOutAv.json";

	@Test
	public void testWithAV() throws Exception {
		Map<String, String> imageResizeConfig = new HashMap<>();
		imageResizeConfig.put("normal", imageResizeConfigBean.getNormalSize());
		imageResizeConfig.put("standard",
				imageResizeConfigBean.getStandardSize());
		imageResizeConfig.put("original", null);

		Map<String, Object> body = ImageTestUtils.getRequestV1();

		String response = template.requestBodyAndHeader("direct:toJsonStart",
				body, IMG_RESIZE_CONFIG_KEY, imageResizeConfig, String.class);

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
		Map<String, String> imageResizeConfig = new HashMap<>();
		imageResizeConfig.put("normal", imageResizeConfigBean.getNormalSize());
		imageResizeConfig.put("standard",
				imageResizeConfigBean.getStandardSize());
		imageResizeConfig.put("original", null);

		Map<String, Object> body = ImageTestUtils.getRequestV2();

		String response = template.requestBodyAndHeader("direct:toJsonStart",
				body, IMG_RESIZE_CONFIG_KEY, imageResizeConfig, String.class);

		LOGGER.info("response:{} ", response);

	}

}
