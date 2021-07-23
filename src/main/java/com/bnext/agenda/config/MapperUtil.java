package com.bnext.agenda.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {
  
	private ModelMapper modelMapper = new ModelMapper();
	
	
	public <S, D> D mapTo(S source, Class<D> destClass) {
		return this.modelMapper.map(source, destClass);
	}
	
	
	public <S, D> List<D> toList(List<S> source, Class<D> destiny){
		return source.stream()
				.map(entity -> map(entity,destiny))
				.collect(Collectors.toList());
	}
	
	public <S, D> Set<D> toSet(Set<S> source, Class<D> destiny){
		return source.stream()
				.map(entity -> map(entity,destiny))
				.collect(Collectors.toSet());
	}
	
	
	public <D, T> D map(final T entity, Class<D> outClass) {
		return this.modelMapper.map(entity, outClass);
	}
	
}
