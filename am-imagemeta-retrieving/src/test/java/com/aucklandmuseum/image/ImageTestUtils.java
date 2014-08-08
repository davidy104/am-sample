package com.aucklandmuseum.image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageTestUtils {
	public static Map<String, Object> getRequest() {
		Map<String, Object> request = new HashMap<>();

		request.put("person", "dav");

		List<Map<String, String>> avList = new ArrayList<>();
		Map<String, String> imageInfoMap = new HashMap<>();
		imageInfoMap.put("System ID", "321535");
		imageInfoMap.put("Digital Filename", "Cenotaph\\nomroll\\abc.jpg");
		imageInfoMap.put("@id", "321535");
		avList.add(imageInfoMap);

		// imageInfoMap = new HashMap<>();
		// imageInfoMap.put("System ID", "320939");
		// imageInfoMap.put("Digital Filename", "Cenotaph\\nomroll\\efg.jpg");
		// imageInfoMap.put("@id", "320939");
		// avList.add(imageInfoMap);

		Map<String, Object> photoMap = new HashMap<>();
		photoMap.put("Photo/Audio-Visual Ref", avList);

		request.put("av", photoMap);

		return request;
	}

	public static Map<String, Object> getRequestV1() {
		Map<String, Object> request = new HashMap<>();

		request.put("person", "dav");

		Map<String, String> imageInfoMap = new HashMap<>();
		imageInfoMap.put("System ID", "321535");
		imageInfoMap.put("Digital Filename", "Cenotaph\\WeeklyNews\\1941\\abc.jpg");
//		imageInfoMap.put("Digital Filename", "Cenotaph\\nomroll\\abc.jpg");
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
