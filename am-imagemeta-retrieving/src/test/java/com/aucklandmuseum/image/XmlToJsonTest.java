package com.aucklandmuseum.image;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.xml.XmlMapper;

public class XmlToJsonTest {
	private static final String XML_REQUEST_FILE = "/cenotaph-new.xml";

	@Test
	public void test() throws Exception {
		URL url = ImageRetrievingMulticastIntegrationTest.class
				.getResource(XML_REQUEST_FILE);
		// String targetFileStr = null;
		//
		// try (FileInputStream fisTargetFile = new FileInputStream(new File(
		// url.getFile()))) {
		// targetFileStr = IOUtils.toString(fisTargetFile, "UTF-8");
		// }

		XmlMapper xmlMapper = new XmlMapper();
		List entries = xmlMapper.readValue(new File(url.getFile()), List.class);

		ObjectMapper jsonMapper = new ObjectMapper();
		String json = jsonMapper.writeValueAsString(entries);
		System.out.println(json);
	}

}
