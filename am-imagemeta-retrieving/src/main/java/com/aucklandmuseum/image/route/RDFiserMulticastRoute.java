package com.aucklandmuseum.image.route;

import javax.annotation.Resource;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.image.AvInfoTransformer;
import com.aucklandmuseum.image.ImageMetaDataTransformer;
import com.aucklandmuseum.image.ImageMetadataRetrievingProcessor;

@Component
public class RDFiserMulticastRoute extends RouteBuilder {

	@Resource
	private ImageMetadataRetrievingProcessor imageMetadataRetrievingProcessor;

	@Resource
	private ImageMetaDataTransformer imageMetaDataTransformer;

	@Resource
	private AvInfoTransformer avInfoTransformer;

	@Value("${imageMeta.output.queue:imageQueue}")
	private String queueEndpoint;

	@Override
	public void configure() throws Exception {
		from("direct:multicastStart").routeId("rdfisermulticastRoute")
				.setExchangePattern(ExchangePattern.InOnly)
				.unmarshal(new JacksonDataFormat()).multicast()
				.parallelProcessing().to("direct:imageProcess")
				.to("direct:rdfProcess")
				.executorServiceRef("genericThreadPool").end()
				.log("after whole process ${body}");

		from("direct:rdfProcess").setExchangePattern(ExchangePattern.InOnly)
				.delay(5000).log("after rdf process ${body}")
				.to("mock:rdfProcess");

		from("direct:imageProcess")
				.transform(avInfoTransformer)
				.to("log:input")
				.split(body())
				.setExchangePattern(ExchangePattern.InOnly)
				.process(imageMetadataRetrievingProcessor)
				.transform(imageMetaDataTransformer)
				.log("after imgmeta process ${body}")
				.to("jms:queue:" + queueEndpoint
						+ "?jmsMessageType=Map&requestTimeout=10s").end();

	}

}
