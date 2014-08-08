package com.aucklandmuseum.imgprocess.config;

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

import com.aucklandmuseum.config.CamelSpringContextConfig;
import com.aucklandmuseum.imgprocess.route.VernonRoute;

@Configuration
@ComponentScan(basePackages = "com.aucklandmuseum.imgprocess")
@Import(value = { CamelSpringContextConfig.class })
public class ApplicationConfiguration {

	@Resource
	private CamelContext camelContext;

	@Resource
	private VernonRoute vernonRoute;

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
		springCamelContext.addRoutes(vernonRoute);
	}
}
