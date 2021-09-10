package com.standardkim.kanban.config;

import com.standardkim.kanban.config.mapper.ModelMapperFactoryBean;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
	@Bean
	public ModelMapperFactoryBean modelMapperFactoryBean() {
		ModelMapperFactoryBean factoryBean = new ModelMapperFactoryBean();
		return factoryBean;
	}

	@Bean
	public ModelMapper modelMapper() throws Exception {
		ModelMapper modelMapper = modelMapperFactoryBean().getObject();
		modelMapper.getConfiguration()
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
		return modelMapper;
	}
}
