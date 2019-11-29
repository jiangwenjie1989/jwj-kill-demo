package com.jwj.im;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages="com.jwj.im.*")
@EnableJpaRepositories(basePackages="com.jwj.im.repository")
@EntityScan(basePackages="com.jwj.domain")
public class PtopApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		new SpringApplicationBuilder(PtopApplication.class).run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(PtopApplication.class).web(true).logStartupInfo(false);
	}
}
