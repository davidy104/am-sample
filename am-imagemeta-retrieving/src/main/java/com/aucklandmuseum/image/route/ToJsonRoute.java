package com.aucklandmuseum.image.route;

import javax.annotation.Resource;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.image.ImageMetaRetrieveProcessor;
import com.aucklandmuseum.image.ImageMetadataMergeTransformer;
import com.aucklandmuseum.image.ImageNotFoundException;

@Component
public class ToJsonRoute extends RouteBuilder {
	@Resource
	private ImageMetaRetrieveProcessor imageMetaRetrieveProcessor;

	@Resource
	private ImageMetadataMergeTransformer imageMetadataMergeTransformer;

	@Override
	public void configure() throws Exception {

		from("direct:toJsonStart").setExchangePattern(ExchangePattern.InOut)
				.filter().simple("${body[av]} != null")
				.to("direct:imageRetrieveProcess").end().marshal()
				.json(JsonLibrary.Jackson);

		from("direct:imageRetrieveProcess").to("log:input")
				.onException(ImageNotFoundException.class).handled(true)
				.transform(constant("Something Bad Happened!"))
				.to("mock:handleExceptionError").end()
				.process(imageMetaRetrieveProcessor)
				.transform(imageMetadataMergeTransformer)
				.log("after imgmeta process ${body}");
	}
}
