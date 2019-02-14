package com.typeconverter;

import static com.typeconverter.ClassType.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumSet;
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
	private static List<Class<?>> params = new ArrayList<Class<?>>();
	private static ClassType classType;

	public TypeProcessor(Type type) {
		
	}

	public static void getProcessedClass(Type type) throws ClassNotFoundException {

		if (type instanceof Class) {
			Class<?> c = (Class<?>) type;
			classType = c.isArray() ? ARRAY : CLASS;
			return new TypeProcessor(c);
		}

		if (type instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) type;
			Class<?> cl = determine(p.getRawType());

			setParameters(p);
			return new TypeProcessor(cl);
		}

		if (type instanceof GenericArrayType) {

		}

		return new TypeProcessor(null);
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

	public List<Class<?>> getParams() {
		return params;
	}

	public ClassType getType() {
		return classType;
	}

	private static void setParameters(ParameterizedType pt) {

		for (Type t : pt.getActualTypeArguments()) {
			if (t instanceof ParameterizedType) {
				Type rt = ((ParameterizedType) t).getRawType();
				if (rt instanceof Class) {
					System.out.println((Class<?>) rt);
				}
			}
		}

	}

	public static void main(String[] args) throws ClassNotFoundException {

		TypeProcessor proc = TypeProcessor.getProcessedClass(new TypeToken<List<List<Integer>>>() {
		}.get());
		System.out.println(proc.getParams());
	}

}
