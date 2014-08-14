package com.aucklandmuseum.image.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.services.s3.AmazonS3;
import com.aucklandmuseum.config.AwsConfigBean;
import com.aucklandmuseum.image.v2.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ImageProcessIntegrationTest {
	@Produce
	private ProducerTemplate producerTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImageProcessIntegrationTest.class);

	@Resource
	private AmazonS3 amazonS3;

	@Resource
	private AwsConfigBean awsConfigBean;

	@Test
	public void testWithMetadata() {
		Map response = producerTemplate.requestBody("seda:ImageS3Process",
				getWithMetaRequest(), Map.class);
		LOGGER.info("response:{} ", response);
	}

	private Map<String, Object> getWithMetaRequest() {
		Map<String, Object> requestMap = new HashMap<>();
		List<Map<String, String>> transforms = new ArrayList<>();
		requestMap.put("imagePath", "/Cenotaph/nomroll/");
		requestMap.put("imageFileName", "abc.jpg");
		requestMap.put("outputPath", "/vernon/av/1/");

		Map<String, String> transform = new HashMap<>();
		transform.put("name", "standard");
		transform.put("width", "1024");
		transform.put("hight", "1024");
		transforms.add(transform);

		transform = new HashMap<>();
		transform.put("name", "thumbnail");
		transform.put("width", "1217");
		transform.put("hight", "1217");
		transforms.add(transform);

		requestMap.put("transform", transforms);
		return requestMap;
	}

	@Test
//	@Ignore("when need to delete images from s3")
	public void testS3Delete() throws Exception {
		amazonS3.deleteObject(awsConfigBean.getBucketName(),
				"/vernon/av/1/original.jpg");
		amazonS3.deleteObject(awsConfigBean.getBucketName(),
				"/vernon/av/1/standard.jpg");
		amazonS3.deleteObject(awsConfigBean.getBucketName(),
				"/vernon/av/1/thumbnail.jpg");
	}
}
