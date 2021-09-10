package com.standardkim.kanban.config.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

public abstract class ModelMapperConverter<S, D> implements ModelMapperConfigurer {
	protected abstract Converter<S,D> converter();

	@Override
	public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(converter());
    }
}
