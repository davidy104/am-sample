package com.aucklandmuseum.image.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.image.ImageMetaDataTransformer;
import com.aucklandmuseum.image.ImageMetadataRetrievingProcessor;

@Component
public class RDFiserMulticastRoute extends RouteBuilder {

	@Resource
	private ImageMetadataRetrievingProcessor imageMetadataRetrievingProcessor;

	@Resource
	private ImageMetaDataTransformer imageMetaDataTransformer;

	@Value("${imageMeta.output.queue:imageQueue}")
	private String queueEndpoint;

	@Override
	public void configure() throws Exception {
		from("direct:multicastStart").routeId("rdfisermulticastRoute")
				.setExchangePattern(ExchangePattern.InOnly).multicast()
				.parallelProcessing().to("direct:imageProcess")
				.to("direct:rdfProcess")
				.executorServiceRef("genericThreadPool").end()
				.log("after whole process ${body}");

		from("direct:rdfProcess").setExchangePattern(ExchangePattern.InOnly)
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						// for testing
						exchange.getIn().setBody("rdfiser done");
						Thread.sleep(5000);
					}
				}).log("after rdf process ${body}").to("mock:rdfProcess");

		from("direct:imageProcess")
				.process(new Processor() {
					@SuppressWarnings("unchecked")
					@Override
					public void process(Exchange exchange) throws Exception {
						List<Object> avList = null;

						final Map<String, Object> record = exchange.getIn()
								.getMandatoryBody(Map.class);

						for (final Entry<String, Object> entry : record
								.entrySet()) {
							if (entry.getKey().equals("av")) {
								if (avList == null) {
									avList = new ArrayList<>();
								}
								List<Object> photoInfo = (List<Object>) ((Map<String, Object>) entry
										.getValue())
										.get("Photo/Audio-Visual Ref");
								avList.addAll(photoInfo);
							}
						}
						exchange.getIn().setBody(avList);
					}
				})
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
