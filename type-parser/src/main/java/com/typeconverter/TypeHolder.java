package com.typeconverter;

import java.lang.reflect.Type;
import java.util.List;

public class TypeHolder {

	private TypeProcessor processor;
	private TypeCast caster;

	
	
	private TypeHolder(TypeProcessor processor, TypeCast caster) {
		this.processor = processor;
		this.caster = caster;
	}

	// we use this to create type Processor object which will hold the raw class and parameters(if any)
	// caster object is there for later parsing with recursion
	public static TypeHolder prepare(Type type, TypeCast caster) throws ClassNotFoundException {
		return new TypeHolder(TypeProcessor.getProcessedClass(type), caster);
	}

	// allows us to later parse any parameter
	public Object parse(Class<?> cl, String val) throws ClassNotFoundException {
		return caster.cast(cl, val);
	}
	// same as previous one but we can parse a type(in case our parameter is also parameterized)
	public Object parse(Type type, String val) throws ClassNotFoundException {
		return caster.cast(type, val);
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getRaw() {
		return (Class<T>) processor.getRaw();
	}

	public List<Type> getParameters() {
		return processor.getParams();
	}

	
	public Splitter getSplitter() {
		return caster.getSplitter();
	}

}
