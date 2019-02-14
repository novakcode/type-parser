package com.typeconverter;

public final class TypeCast {

	private Splitter splitter = Splitter.DEFAULT;

	private TypeCast() {

	}

	@SuppressWarnings("unchecked")
	public <T> T cast(TypeToken<T> typeToken, String val) {
		return cast((Class<T>) typeToken.get(), val);
	}

	@SuppressWarnings("unchecked")
	public <T> T cast(Class<T> cl, String val) {

		return (T) cast(TypeHolder.prepare(cl, this), val);
	}

	private Object cast(TypeHolder holder, String val) {

		ClassType classType = ClassUtil.getClassType(holder.getRaw());

		Object obj = Parsers.getParser(classType).parse(holder, val);

		if (obj == null)
			throw new IllegalArgumentException("Can't cast.");

		return obj;
	}

	public static TypeCast newInstance() {
		return new TypeCast();
	}

	public TypeCast withSplitter(Splitter splitter) {
		this.splitter = splitter;
		return this;
	}

}
