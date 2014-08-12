package com.aucklandmuseum.image.v1.route;

import javax.annotation.Resource;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.image.v1.processor.ImageMetadataRetrievingProcessor;
import com.aucklandmuseum.image.v1.processor.ImageProcessRequestUnmarshaller;
import com.aucklandmuseum.image.v1.processor.ImageScalingProcessor;

@Component
public class ImageProcessRoute extends RouteBuilder {

	@Resource
	private ImageProcessRequestUnmarshaller imageProcessRequestUnmarshaller;

	@Resource
	private ImageMetadataRetrievingProcessor imageMetadataRetrievingProcessor;

	@Resource
	private ImageScalingProcessor imageScalingProcessor;

	@Value("${filesys.output:outbox}")
	private String outputfolder;

	@Override
	public void configure() throws Exception {
		from("seda:imageProcess")
				.routeId("imageProcess")
				.setExchangePattern(ExchangePattern.InOut)
				.transform(imageProcessRequestUnmarshaller)
				.process(imageMetadataRetrievingProcessor)
				.split(simple("${property.imageScalingConfigs}"))
				.process(imageScalingProcessor)
				.to("file://" + outputfolder
						+ "?fileName=${property.imageName}").end();
	}
}
