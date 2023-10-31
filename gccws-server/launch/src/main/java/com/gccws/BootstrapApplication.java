package com.gccws;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@SpringBootApplication
@EnableScheduling
public class BootstrapApplication extends SpringBootServletInitializer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BootstrapApplication.class);
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BootstrapApplication.class, args);
		LOG.info("\n\nServer Running\n\nhttp://localhost:9401/swagger-ui.html\n\n");
	}
	
	 @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	      return builder.sources(BootstrapApplication.class);
	  }
}
