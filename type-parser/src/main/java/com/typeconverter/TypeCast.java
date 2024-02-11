package com.typeconverter;

import java.lang.reflect.Type;

public final class TypeCast {

	private Splitter splitter;

	private TypeCast(Splitter splitter) {
		this.splitter = splitter;
	}

	@SuppressWarnings("unchecked")


	public <T> T cast(TypeToken<T> typeToken, String val) throws ClassNotFoundException {
		return (T) cast(TypeHolder.prepare(typeToken.get(), this), val);
	}

	@SuppressWarnings("unchecked")
	public <T> T cast(Class<T> cl, String val) throws ClassNotFoundException {

		return (T) cast(TypeHolder.prepare(cl, this), val);
	}

	@SuppressWarnings("unchecked")
	public <T> T cast(Type type, String val) throws ClassNotFoundException {
		return (T) cast(TypeHolder.prepare(type, this), val);
	}

	private Object cast(TypeHolder holder, String val) {

		ClassType classType = ClassUtil.getClassType(holder.getRaw(), holder.getParameters().size() > 0);
		Object obj = Parsers.getParser(classType).parse(holder, val);

		if (obj == null)
			throw new IllegalArgumentException("Can't cast.");

		return obj;
	}

	public Splitter getSplitter() {
		return splitter;
	}

	public static TypeCast newInstance() {
		return new TypeCast(Splitter.defaultSplitter());
	}

	public static TypeCast withSplitter(Splitter splitter) {
		return new TypeCast(splitter);
	}

}
