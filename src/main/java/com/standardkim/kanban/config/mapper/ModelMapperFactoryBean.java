package com.standardkim.kanban.config.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class ModelMapperFactoryBean implements FactoryBean<ModelMapper>{
	@Autowired
	private List<ModelMapperConfigurer> configurers;

	@Override
	public ModelMapper getObject() throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		configure(modelMapper);
		return modelMapper;
	}

	@Override
	public Class<?> getObjectType() {
		return ModelMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	private void configure(ModelMapper modelMapper) {
		for(ModelMapperConfigurer configurer : configurers) {
			configurer.configure(modelMapper);
		}
	}
}
