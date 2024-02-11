package com.typeconverter;

import static com.typeconverter.ClassType.ARRAY;
import static com.typeconverter.ClassType.COLLECTION;
import static com.typeconverter.ClassType.ENUM;
import static com.typeconverter.ClassType.ENUMSET;
import static com.typeconverter.ClassType.MAP;
import static com.typeconverter.ClassType.PRIMITIVE;
import static com.typeconverter.ClassType.WRAPPER;

import java.lang.reflect.GenericArrayType;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

public class ClassUtil<T> {

	public static Class<?>[][] primitives = new Class<?>[][] { { Integer.class, int.class },
			{ Double.class, double.class }, { Character.class, char.class }, { Long.class, long.class },
			{ Short.class, short.class }, { Byte.class, byte.class }, { Boolean.class, boolean.class },
			{ Float.class, float.class }, { String.class } };

	public static boolean isPrimitive(Class<?> cl) {
		for (Class<?>[] prims : primitives) {
			for (Class<?> pri : prims) {
				if (pri.equals(cl))
					return true;
			}
		}

		return false;
	}


	public static ClassType getClassType(Class<?> cl, boolean parameterized) {

		if (cl.isArray()) {
			return ARRAY;
		}

		if (cl.getInterfaces().length > 0 && cl.getInterfaces()[0].isAssignableFrom(GenericArrayType.class)) {
			return ClassType.GENERICARRAY;
		}

		if (cl.isEnum()) {
			return ENUM;
		} else if (EnumSet.class.isAssignableFrom(cl)) {
			return ENUMSET;
		} else if (Collection.class.isAssignableFrom(cl)) {
			return COLLECTION;
		} else if (Map.class.isAssignableFrom(cl)) {
			return MAP;
		} else if (ClassUtil.isPrimitive(cl)) {
			return PRIMITIVE;
		}

		return parameterized ? ClassType.PTWRAPPER : WRAPPER;

	}

}
