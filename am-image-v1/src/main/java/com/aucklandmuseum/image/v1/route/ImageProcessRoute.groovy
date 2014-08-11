package com.aucklandmuseum.image.v1.route

import javax.annotation.Resource

import net.sf.json.groovy.JsonSlurper

import org.apache.camel.Exchange
import org.apache.camel.ExchangePattern
import org.apache.camel.Expression
import org.apache.camel.builder.RouteBuilder
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component

import com.aucklandmuseum.image.v1.processor.ImageMetadataRetrievingProcessor
import com.aucklandmuseum.image.v1.processor.ImageProcessRequestTransformer
import com.aucklandmuseum.image.v1.processor.ImageScalingProcessor
@Component
class ImageProcessRoute extends RouteBuilder {

	@Resource
	ImageProcessRequestTransformer imageProcessRequestTransformer

	@Resource
	ImageMetadataRetrievingProcessor imageMetadataRetrievingProcessor

	@Resource
	ImageScalingProcessor imageScalingProcessor

	@Value('${filesys.output:outbox}')
	String outputfolder

	@Override
	public void configure() throws Exception {
		from("seda:imageProcess")
				.routeId("imageProcess")
				.setExchangePattern(ExchangePattern.InOut)
				.transform(imageProcessRequestTransformer)
				.process(imageMetadataRetrievingProcessor)
				.split(simple('${body.imageProcessRequest.imageTransforms}'))
				.process(imageScalingProcessor)
				.setBody(simple('${property.imageBytes}'))
				.to('file://' + outputfolder
				+ '?fileName=${property.imageName}').end()
	}
}
