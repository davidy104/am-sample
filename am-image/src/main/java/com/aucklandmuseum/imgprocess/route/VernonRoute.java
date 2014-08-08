package com.aucklandmuseum.imgprocess.route;

import javax.annotation.Resource;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.imgprocess.processor.CustomImageSplitBean;
import com.aucklandmuseum.imgprocess.processor.ImageMetadataMergeTransformer;
import com.aucklandmuseum.imgprocess.processor.ImageMetadataRetrievingProcessor;
import com.aucklandmuseum.imgprocess.processor.ImageScalingProcessor;

@Component
public class VernonRoute extends RouteBuilder {

	@Resource
	private ImageMetadataMergeTransformer imageMetadataMergeTransformer;

	@Resource
	private ImageMetadataRetrievingProcessor imageMetadataRetrievingProcessor;

	@Resource
	private CustomImageSplitBean customImageSplitBean;

	@Resource
	private ImageScalingProcessor imageScalingProcessor;

	@Value("${filesys.output:outbox}")
	private String outputfolder;

	@Override
	public void configure() throws Exception {
		from("direct:toJsonStart").setExchangePattern(ExchangePattern.InOut)
				.filter().simple("${body[av]} != null")
				.to("direct:imageRetrieveProcess").end().marshal()
				.json(JsonLibrary.Jackson);

		from("direct:imageRetrieveProcess")
				.process(imageMetadataRetrievingProcessor)
				.split()
				.method(customImageSplitBean, "splitMessage")
				.to("mock:aftersplit")
				.process(imageScalingProcessor)
				.to("file://" + outputfolder
						+ "?fileName=${property.imageName}").end()
				.transform(imageMetadataMergeTransformer)
				.log("after imgmeta process ${body}");

	}

}
