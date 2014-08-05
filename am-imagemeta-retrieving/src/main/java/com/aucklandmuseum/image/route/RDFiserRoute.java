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
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.spi.ThreadPoolProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.image.ImageMetaDataTransformer;
import com.aucklandmuseum.image.ImageMetadataRetrievingProcessor;

@Component
public class RDFiserRoute extends RouteBuilder {

	@Resource
	private ImageMetadataRetrievingProcessor imageMetadataRetrievingProcessor;

	@Resource
	private ImageMetaDataTransformer imageMetaDataTransformer;

	@Value("${imageMeta.output.queue:imageQueue}")
	private String queueEndpoint;

	@Override
	public void configure() throws Exception {
		// ExecutorService executorService = new ThreadPoolBuilder(getContext())
		// .poolSize(3).maxQueueSize(10)
		// .build("ImageRetrievingThreadPool");

		ThreadPoolProfile imageRetrievingThreadPoolProfile = new ThreadPoolProfileBuilder(
				"ImageRetrievingThreadPoolProfile").poolSize(5)
				.maxQueueSize(100).build();
		getContext().getExecutorServiceManager().registerThreadPoolProfile(
				imageRetrievingThreadPoolProfile);

		from("direct:start")
				.routeId("rdfiserRoute")
				.setExchangePattern(ExchangePattern.InOut)
				.process(new Processor() {
					@Override
					@SuppressWarnings({ "unchecked", "rawtypes" })
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

						exchange.setProperty("avList", avList);
						// for testing
						exchange.getIn().setBody("rdfiser done");
					}
				}).log("after rdf process ${body}")
				.wireTap("direct:retrieveImageMetadata")
				.executorServiceRef("ImageRetrievingThreadPoolProfile");

		from("direct:retrieveImageMetadata")
				.setBody(simple("${property.avList}"))
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
