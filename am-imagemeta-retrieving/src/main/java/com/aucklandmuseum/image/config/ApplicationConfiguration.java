package com.aucklandmuseum.image.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import com.aucklandmuseum.config.CamelActivemqConfig;
import com.aucklandmuseum.config.CamelSpringContextConfig;
import com.aucklandmuseum.image.route.RDFiserMulticastRoute;
import com.aucklandmuseum.image.route.RDFiserRoute;
import com.aucklandmuseum.image.route.ToJsonRoute;
import com.aucklandmuseum.image.route.XmlToMapRoute;

@Configuration
@ComponentScan(basePackages = "com.aucklandmuseum.image")
@Import(value = { CamelSpringContextConfig.class, CamelActivemqConfig.class })
public class ApplicationConfiguration {

	@Resource
	private CamelContext camelContext;

	@Resource
	private RDFiserRoute rdfiserRoute;

	@Resource
	private RDFiserMulticastRoute rdfiserMulticastRoute;

	@Resource
	private XmlToMapRoute xmlToMapRoute;

	@Resource
	private ToJsonRoute toJsonRoute;

	@Bean
	public static PropertyPlaceholderConfigurer properties() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		org.springframework.core.io.Resource[] resources = new ClassPathResource[] { new ClassPathResource(
				"image.properties") };
		ppc.setLocations(resources);
		ppc.setIgnoreUnresolvablePlaceholders(true);
		return ppc;
	}

	@PostConstruct
	public void initializeCamelContext() throws Exception {
		SpringCamelContext springCamelContext = (SpringCamelContext) camelContext;
		springCamelContext.addRoutes(rdfiserRoute);
		springCamelContext.addRoutes(rdfiserMulticastRoute);
		springCamelContext.addRoutes(xmlToMapRoute);
		springCamelContext.addRoutes(toJsonRoute);
	}
}
