package com.aucklandmuseum.image;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class StringSplitTest {
	static final String COLON = ":";

	@Test
	public void test() {
		String s = "1275:";
		Integer width = null;
		Integer hight = null;

		String widthStr = substringBefore(s, COLON);
		String hightStr = substringAfter(s, COLON);

		if (!StringUtils.isEmpty(widthStr)) {
			width = Integer.valueOf(widthStr);
		}
		if (!StringUtils.isEmpty(hightStr)) {
			hight = Integer.valueOf(hightStr);
		}

		System.out.println(width + "-" + hight);
	}

}
