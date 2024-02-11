package com.typeconverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeToken<T> {

	private Type type;

	public TypeToken() {
		Type t = getClass().getGenericSuperclass();

		if (!(t instanceof ParameterizedType)) {

			throw new RuntimeException("Parameter missing.");
		}

		ParameterizedType pt = (ParameterizedType) t;
		this.type = pt.getActualTypeArguments()[0];

	}

	public Type get() {
		return type;
	}

}
