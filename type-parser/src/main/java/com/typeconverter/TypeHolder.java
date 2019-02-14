package com.typeconverter;

import java.lang.reflect.Type;
import java.util.List;

public class TypeHolder {

	private TypeProcessor processor;
	private TypeCast caster;

	private TypeHolder(TypeProcessor processor, TypeCast caster) {
		this.processor = processor;
		this.splitter = caster.getSplitter();
		this.caster = caster;
	}

	public static TypeHolder prepare(Type type, TypeCast caster) {
		return new TypeHolder(new TypeProcessor(type), caster);
	}

	public Object parse(Class<?> cl, String val) {
		return caster.cast(cl, val);
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getRaw() {
		return (Class<T>) processor.getRaw();
	}

	public List<Class<?>> getParameters() {
		return processor.getParams();
	}

}
