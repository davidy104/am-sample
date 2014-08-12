package com.aucklandmuseum.image.v1.processor

import groovy.util.logging.Slf4j
import net.sf.json.groovy.JsonSlurper

import org.apache.camel.Exchange
import org.apache.camel.Expression
import org.springframework.stereotype.Component

import com.aucklandmuseum.image.v1.ImageProcessRequest
import com.aucklandmuseum.image.v1.ImageTransform
@Component
@Slf4j
class ImageProcessRequestUnmarshaller implements Expression{

	static final String IMG_SEPERATOR = "\\"

	@Override
	<T> T evaluate(Exchange exchange, Class<T> type) {
		String jsonReq = exchange.in.getBody(String.class)
		JsonSlurper jsonSlurper = new JsonSlurper()
		Object result = jsonSlurper.parseText(jsonReq)
		Map mapResult = (Map) result
		String sourceImagePath
		String sourceImageName

		String sourceImage = mapResult.get('sourceImage')
		sourceImage= (sourceImage.startsWith("/"))?sourceImage:"/"+sourceImage

		if (sourceImage.indexOf(IMG_SEPERATOR) == -1){
			sourceImageName = sourceImage
			sourceImagePath = ''
		}else  {
			sourceImage = sourceImage.replace(IMG_SEPERATOR, "/")
			sourceImageName = sourceImage.substring(sourceImage.lastIndexOf("/") + 1,
					sourceImage.length());
			sourceImagePath =  sourceImage
					.substring(0, sourceImage.lastIndexOf("/") + 1)
		}

		ImageProcessRequest imageProcessRequest = new ImageProcessRequest(sourceImageName:sourceImageName,sourceImagePath:sourceImagePath)

		Object[] transformsArray = (Object[])mapResult.get("transform")
		transformsArray.each {
			ImageTransform imageTransform = new ImageTransform(imageName:it.imageName,width:Integer.valueOf(it.width),hight:Integer.valueOf(it.hight))
			imageProcessRequest.imageTransforms << imageTransform
		}

		return (T)imageProcessRequest
	}
}
