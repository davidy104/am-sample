package com.aucklandmuseum.image;

import org.junit.Test;

public class ImagePathFormatTest {

	@Test
	public void test() {
		String s = "Cenotaph\\WeeklyNews\\1941\\1210p27r1c1_hargest.jpg";

		String s1 = s.replace("\\", "/");
		System.out.println("s1: " + s1);
		
		if(!s1.startsWith("/")){
			s1 = "/"+s1;
		}
		System.out.println("s1: " + s1);
		
		String s2 = s1.substring(s1.lastIndexOf("/")+1,s1.length());
		String s3 = s1.substring(0, s1.lastIndexOf("/")+1);
		System.out.println("s2: " + s2);
		System.out.println("s3: " + s3);
	}

}
