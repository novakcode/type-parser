package com.typeconverter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedTransferQueue;

public class TypeProcessor {

	private Class<?> raw;
	private List<Type> params;

	public TypeProcessor(Class<?> raw, List<Type> params) {
		this.raw = raw;
		this.params = params;
	}

	// we get the type and if its parameterized we get params. If its a generic
	// array we also get the parameterized component.
	public static TypeProcessor getProcessedClass(Type type) throws ClassNotFoundException {

		List<Type> params = new ArrayList<Type>();
		Class<?> cls = null;

		if (type instanceof Class) {
			cls = (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) type;
			cls = determine(p.getRawType());

			setParameters(params, p);

		} else if (type instanceof GenericArrayType) {
			GenericArrayType gat = (GenericArrayType) type;
			cls = gat.getClass();

			params.add(gat.getGenericComponentType());

		}

		return new TypeProcessor(cls, params);
	}

	private static Class<?> determine(Type tp) {

		Class<?> cl = (Class<?>) tp;

		if (!cl.isInterface()) {
			return cl;
		} else if (List.class.isAssignableFrom(cl)) {
			return ArrayList.class;
		} else if (Map.class.isAssignableFrom(cl)) {
			return ConcurrentSkipListMap.class;
		} else if (Set.class.isAssignableFrom(cl)) {
			return TreeSet.class;
		} else if (Queue.class.isAssignableFrom(cl)) {
			return LinkedTransferQueue.class;
		} else if (Deque.class.isAssignableFrom(cl)) {
			return LinkedBlockingDeque.class;
		}

		throw new RuntimeException("Class not valid.");

	}

	public Class<?> getRaw() {
		return raw;
	}

	public List<Type> getParams() {
		return params;
	}

	private static void setParameters(List<Type> params, ParameterizedType pt) {

		for (Type t : pt.getActualTypeArguments()) {

			params.add(t);

		}

	}

}
