package com.oa.core.util;

import java.sql.Timestamp;

import com.oa.core.helper.StringHelper;
import org.springframework.core.convert.converter.Converter;

public class TimestampConverter implements Converter<String, Timestamp> {

	@Override
	public Timestamp convert(String source) {
		if(StringHelper.isEmpty(source)){
			return null;
		}
		if(source.length()==16){
			source+=":00";
		}
		return Timestamp.valueOf(source);
	}
}
