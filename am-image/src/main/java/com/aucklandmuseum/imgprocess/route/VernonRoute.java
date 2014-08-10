package com.aucklandmuseum.imgprocess.route;

import javax.annotation.Resource;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.imgprocess.processor.ImageMetadataAggregationStrategy;
import com.aucklandmuseum.imgprocess.processor.ImageMetadataRetrievingProcessor;
import com.aucklandmuseum.imgprocess.processor.ImageScalingConfigProcessor;
import com.aucklandmuseum.imgprocess.processor.ImageScalingProcessor;

@Component
public class VernonRoute extends RouteBuilder {

	@Resource
	private ImageMetadataRetrievingProcessor imageMetadataRetrievingProcessor;

	@Resource
	private ImageScalingProcessor imageScalingProcessor;

	@Resource
	private ImageScalingConfigProcessor imageScalingConfigProcessor;

	@Resource
	private ImageMetadataAggregationStrategy imageMetadataAggregationStrategy;

	@Value("${filesys.output:outbox}")
	private String outputfolder;

	@Override
	public void configure() throws Exception {
		from("direct:toJsonStart")
				.setExchangePattern(ExchangePattern.InOut)
				.filter()
				.simple("${body[av]} != null")
				.enrich("direct:imageRetrieveProcess",
						imageMetadataAggregationStrategy).end().marshal()
				.json(JsonLibrary.Jackson);

		from("direct:imageRetrieveProcess")
				.process(imageMetadataRetrievingProcessor)
				.process(imageScalingConfigProcessor)
				.split(simple("${property.imageScalings}"))
				.process(imageScalingProcessor)
				.setBody(simple("${property.imageBytes}"))
				.to("file://" + outputfolder
						+ "?fileName=${property.imageName}").end();

	}

}
