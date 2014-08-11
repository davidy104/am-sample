package com.aucklandmuseum.image.v1.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.amazonaws.services.s3.AmazonS3;
import com.aucklandmuseum.config.AwsConfiguration;
import com.aucklandmuseum.config.CamelSpringContextConfig;

@Configuration
@ComponentScan(basePackages = "com.aucklandmuseum.image.v1")
@Import(value = { CamelSpringContextConfig.class, AwsConfiguration.class })
public class ApplicationConfiguration {

	@Resource
	private CamelContext camelContext;

	@Resource
	private AmazonS3 amazonS3;

	@PostConstruct
	public void initializeCamelContext() throws Exception {
		SpringCamelContext springCamelContext = (SpringCamelContext) camelContext;
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("amazonS3", amazonS3);
		springCamelContext.setRegistry(registry);
	}
}
