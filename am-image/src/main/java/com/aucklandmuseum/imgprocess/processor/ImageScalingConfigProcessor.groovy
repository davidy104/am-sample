package com.aucklandmuseum.imgprocess.processor

import java.util.ArrayList;
import java.util.List;

import groovy.util.logging.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.imgprocess.ImageScalingBean;

@Component
@Slf4j
class ImageScalingConfigProcessor implements Processor {

	@Value('${image.scalingConfig}')
	String imageScalingConfiguration

	static final String CONFIG_SEPREATOR = ","
	static final String SIZE_COLON = "*"

	@Override
	public void process(Exchange exchange) throws Exception {
		List<ImageScalingBean> imageScalingBeanList = new ArrayList<>()
		ImageScalingBean originalScalingConfig =  new ImageScalingBean(imageName:"original")
		imageScalingBeanList << originalScalingConfig

		def imageScalingConfigurationArray = imageScalingConfiguration.split(CONFIG_SEPREATOR)

		imageScalingConfigurationArray.each {it->
			Integer width,hight
			String name
			if(it.indexOf("=")!=-1){
				def configArray = it.split("=")

				if(configArray.length == 1){
					return
				}
				name = configArray[0]
				String sizeconfig = configArray[1]
				def sizeconfigArray = sizeconfig.split("\\*")
				if(configArray.length == 1){
					width = hight = Integer.valueOf( sizeconfigArray[0])
				}else {
					width = Integer.valueOf( sizeconfigArray[0])
					hight = Integer.valueOf( sizeconfigArray[1])
				}
			}

			ImageScalingBean imageScalingBean = new ImageScalingBean(imageName:name,width:width,hight:hight)
			imageScalingBeanList << imageScalingBean
		}

		exchange.setProperty("imageScalings", imageScalingBeanList)
	}
}
