package com.aucklandmuseum.image.v2.route

import javax.annotation.Resource

import org.apache.camel.ExchangePattern
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

import com.aucklandmuseum.config.AwsConfigBean
import com.aucklandmuseum.image.v2.processor.ImageMetadataRetrievingProcessor
import com.aucklandmuseum.image.v2.processor.ImageScalingProcessor

@Component
class ImageS3ProcessRoute extends RouteBuilder {

	@Resource
	AwsConfigBean awsConfigBean

	@Resource
	ImageMetadataRetrievingProcessor imageMetadataRetrievingProcessor

	@Resource
	ImageScalingProcessor imageScalingProcessor

	@Override
	public void configure() throws Exception {
		from("seda:ImageS3Process")
				.routeId('seda:ImageS3Process')
				.setExchangePattern(ExchangePattern.InOut)
				.process(imageMetadataRetrievingProcessor)
				.split(simple('${property.imageTransforms}'))
				.process(imageScalingProcessor)
				.to('aws-s3://' + awsConfigBean.getBucketName()+ '?amazonS3Client=#amazonS3')
				.end()
	}
}
