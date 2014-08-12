package com.aucklandmuseum.image.v1.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import com.amazonaws.services.s3.AmazonS3;
import com.aucklandmuseum.config.AwsConfiguration;
import com.aucklandmuseum.config.CamelSpringContextConfig;
import com.aucklandmuseum.image.v1.route.ImageProcessRoute;
import com.aucklandmuseum.image.v1.route.ImageS3ProcessRoute;

@Configuration
@ComponentScan(basePackages = "com.aucklandmuseum.image.v1")
@Import(value = { CamelSpringContextConfig.class, AwsConfiguration.class })
public class ApplicationConfiguration {

	@Resource
	private CamelContext camelContext;

	@Resource
	private AmazonS3 amazonS3;

	@Resource
	private ImageProcessRoute imageProcessRoute;

	@Resource
	private ImageS3ProcessRoute imageS3ProcessRoute;

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
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("amazonS3", amazonS3);
		springCamelContext.setRegistry(registry);
		springCamelContext.addRoutes(imageProcessRoute);
		springCamelContext.addRoutes(imageS3ProcessRoute);

	}
}
