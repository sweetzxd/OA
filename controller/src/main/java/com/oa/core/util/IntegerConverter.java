package com.oa.core.util;

import org.springframework.core.convert.converter.Converter;

public class IntegerConverter implements Converter<String, Integer> {

	@Override
	public Integer convert(String str) {
		if(str!=null&&str.length()>0){
			return Integer.parseInt(str);
		}
		return 0;
	}
}
