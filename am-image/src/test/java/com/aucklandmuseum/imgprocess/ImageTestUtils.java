package com.aucklandmuseum.imgprocess;

import java.util.HashMap;
import java.util.Map;

public class ImageTestUtils {

	public static Map<String, Object> getRequestV1() {
		Map<String, Object> request = new HashMap<>();
		request.put("person", "dav");
		Map<String, String> imageInfoMap = new HashMap<>();
		imageInfoMap.put("System ID", "321535");
		imageInfoMap.put("Digital Filename",
				"Cenotaph\\nomroll\\abc.jpg");
		imageInfoMap.put("@id", "321535");
		request.put("av", imageInfoMap);
		return request;
	}

	public static Map<String, Object> getRequestV2() {
		Map<String, Object> request = new HashMap<>();
		request.put("person", "dav");
		return request;
	}
}
