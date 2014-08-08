package com.aucklandmuseum.imgprocess.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.Property;
import org.apache.camel.impl.DefaultMessage;
import org.springframework.stereotype.Component;

@Component
public class CustomImageSplitBean {
	public List<Message> splitMessage(
			@Header(value = "imageResizeConfig") Map<String, String> headerMap,
			@Property(value = "imageBytes") byte[] imageBytes) {
		List<Message> answer = new ArrayList<Message>();
		for (Map.Entry<String, String> entry : headerMap.entrySet()) {
			DefaultMessage message = new DefaultMessage();
			message.setHeader("size", entry.getValue());
			message.setHeader("imageResizeType", entry.getKey());
			message.setBody(imageBytes);
			answer.add(message);
		}
		return answer;
	}
}
