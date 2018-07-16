package com.vm.rest.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.openapi.OpenApiCustomizer;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.rs.security.oauth2.filters.AccessTokenValidatorClient;
import org.apache.cxf.rs.security.oauth2.filters.OAuthRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.vm.rest.auth.service.impl.DealsServiceImpl;

@SpringBootApplication
public class TheJaxRsCXFApplication {

	@Autowired
	private Bus bus;

	public static void main(String[] args) {
		SpringApplication.run(TheJaxRsCXFApplication.class, args);
	}

	@Bean
	public Server rsServer() {
		JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
		endpoint.setBus(bus);
		endpoint.setServiceBeans(Arrays.<Object>asList(new DealsServiceImpl()));
		endpoint.setFeatures(Arrays.asList(createOpenApiFeature()));
		endpoint.setProvider(oAuthRequestFilter());
		return endpoint.create();
	}

	public OpenApiFeature createOpenApiFeature() {
		OpenApiFeature openApiFeature = new OpenApiFeature();
		openApiFeature.setPrettyPrint(true);
		OpenApiCustomizer customizer = new OpenApiCustomizer();
		customizer.setDynamicBasePath(true);
		openApiFeature.setCustomizer(customizer);
		openApiFeature.setTitle("Spring Boot CXF REST Application");
		openApiFeature.setContactName("The Apache CXF team");
		openApiFeature.setDescription("This sample project demonstrates how to use CXF JAX-RS services"
				+ " with Spring Boot. This demo has two JAX-RS class resources being"
				+ " deployed in a single JAX-RS endpoint.");
		openApiFeature.setVersion("1.0.0");
		return openApiFeature;
	}

	@Bean
	public OAuthRequestFilter oAuthRequestFilter() {
		OAuthRequestFilter filter = new OAuthRequestFilter();
		filter.setTokenValidator(accessTokenValidatorClient());
//		filter.setTokenValidator(new JwtAccessTokenValidator());//this didnt work at all
		return filter;
	}

	private AccessTokenValidatorClient accessTokenValidatorClient() {
		AccessTokenValidatorClient client = new AccessTokenValidatorClient();
		client.setTokenValidatorClient(jaxRsClientFactoryBean());
		return client;
	}

	private WebClient jaxRsClientFactoryBean() {
		JAXRSClientFactoryBean factory = new JAXRSClientFactoryBean();
		factory.setAddress("https://myfuse-1.auth0.com/userinfo");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		factory.setHeaders(headers);
		return factory.createWebClient();
	}

}
