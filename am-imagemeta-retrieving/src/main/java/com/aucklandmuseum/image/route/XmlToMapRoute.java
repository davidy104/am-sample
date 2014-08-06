package com.aucklandmuseum.image.route;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.springframework.stereotype.Component;

import com.aucklandmuseum.image.XMLToMapConverter;
import com.thoughtworks.xstream.XStream;

@Component
public class XmlToMapRoute extends RouteBuilder {

	@Resource
	private XMLToMapConverter xmlToMapConverter;

	@Override
	public void configure() throws Exception {
		final XStream xStream = new XStream();
		xStream.registerConverter(xmlToMapConverter);
		xStream.alias("vernon", Map.class);

		from("direct:xmlToMapStart").setExchangePattern(ExchangePattern.InOut)
				.unmarshal(new XStreamDataFormat(xStream));
	}

}
