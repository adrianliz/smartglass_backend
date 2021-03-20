package com.turomas.smartglass.events.converters;

import com.turomas.smartglass.events.domain.ProcessName;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class ProcessTypeConverter implements Converter<String, ProcessName> {
	@Override
	public ProcessName convert(String source) {
		for (ProcessName processName : ProcessName.values()) {
			if (processName.name().equals(source.toUpperCase())) {
				return processName;
			}
		}

		return ProcessName.UNDEFINED;
	}
}
