package com.aucklandmuseum.imgprocess;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageResizeConfigBean {

	@Value("${image.normal.size}")
	private String normalSize;

	@Value("${image.standard.size}")
	private String standardSize;

	public String getNormalSize() {
		return normalSize;
	}

	public void setNormalSize(String normalSize) {
		this.normalSize = normalSize;
	}

	public String getStandardSize() {
		return standardSize;
	}

	public void setStandardSize(String standardSize) {
		this.standardSize = standardSize;
	}

}
