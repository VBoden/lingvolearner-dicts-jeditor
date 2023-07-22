package ua.vboden.converters;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConverter<S, T> implements Converter<S, T> {

	@Override
	public List<T> convertAll(List<S> sources) {
		List<T> targets = new ArrayList<>();
		sources.forEach(source -> targets.add(convert(source)));
		return targets;
	}

}
