package com.aucklandmuseum.imgprocess;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringTest {

	//normal=1024*1024,stardand=1217*1217
	@Test
	void test() {
		String s = "normal=,stardand=1217*1217"

		def sarray = s.split(",")

		sarray.each {it->
			if(it.indexOf("=")!=-1){
				def configArray = it.split("=")

				if(configArray.length == 1){
					return
				}

				String name = configArray[0]
				String sizeconfig = configArray[1]
				println name
				println sizeconfig

				def sizeconfigArray = sizeconfig.split("\\*")
				String width = sizeconfigArray[0]
				String hight = sizeconfigArray[1]
				
				println width
				println hight
				//					




			}
		}


		println "hello"
	}
}
