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
class ImageProcessRequestTransformer implements Expression{

	@Override
	<T> T evaluate(Exchange exchange, Class<T> type) {
		String jsonReq = exchange.in.getBody(String.class)
		JsonSlurper jsonSlurper = new JsonSlurper()
		Object result = jsonSlurper.parseText(jsonReq)
		Map mapResult = (Map) result

		ImageProcessRequest imageProcessRequest = new ImageProcessRequest(imagePath:mapResult.get('imagePath'),imageFileName:mapResult.get('imageFileName'))

		Object[] transformsArray = (Object[])mapResult.get("imageTransforms")
		transformsArray.each {
			ImageTransform imageTransform = new ImageTransform(imageName:it.imageName,width:it.width,hight:it.hight)
			imageProcessRequest.imageTransforms << imageTransform
		}
		return (T)imageProcessRequest
	}
}
