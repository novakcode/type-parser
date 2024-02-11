package com.typeconverter.parsers;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;

public class CollectionParsers {

	static class CollectionParser extends Parsers {

		@Override
		public Object parse(TypeHolder holder, String val) {

			List<SplitPart> parts = holder.getSplitter().split(val);

			return parts.stream().map(e -> {

				try {
					return holder.parse(holder.getParameters().get(0), e.get());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				return null;
			}).collect(Collectors.toCollection(() -> getCollection(holder.getRaw())));

		}

		public Collection<Object> getCollection(Class<?> type) {

			if (type.equals(ArrayList.class)) {
				return new ArrayList<>();
			} else if (type.equals(Set.class)) {
				return new TreeSet<>();
			} else if (type.equals(Queue.class)) {
				return new LinkedTransferQueue<>();
			} else if (type.equals(Deque.class)) {
				return new LinkedBlockingDeque<>();
			}

			return null;
		}

	}

	static class MapParser extends Parsers {

		@Override
		public Object parse(TypeHolder holder, String val) {

			List<SplitPart> parts = holder.getSplitter().split(val);

			return parts.stream().map((e) -> {

				Object k = null, v = null;
				try {
					k = holder.parse(holder.getParameters().get(0), e.getKey());

					v = holder.parse(holder.getParameters().get(1), e.getValue());

				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				return new AbstractMap.SimpleEntry<Object, Object>(k, v);
			}).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

		}

	}

	static class ArrayParser extends Parsers {

		@Override

		public Object parse(TypeHolder holder, String val) {

			List<SplitPart> parts = holder.getSplitter().split(val);
			Class<?> component = holder.getRaw().getComponentType();
			
			// if its a primitive array, we need to create a simple object array, if its not we create new instance using the array component class
			Object[] arr = component.isPrimitive() ? new Object[parts.size()]
					: (Object[]) Array.newInstance(component, parts.size());

			parts.stream().map(e -> {

				try {
					return holder.parse(component, e.get());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				return null;

			}).collect(Collectors.toList()).toArray(arr);

			
			// here we check which primitive class is in question and create array
			if (component.isPrimitive()) {

				if (component.equals(int.class)) {
					return Arrays.stream(arr).mapToInt(e -> (int) e).toArray();
				} else if (component.equals(double.class)) {
					return Arrays.stream(arr).mapToLong(e -> (long) e).toArray();
				} else if (component.equals(long.class)) {
					return Arrays.stream(arr).mapToDouble(e -> (double) e).toArray();
				}

			}

			return arr;

		}

	}

	static class GenericArrayParser extends Parsers {

		@Override
		public Object parse(TypeHolder holder, String val) {

			List<SplitPart> parts = holder.getSplitter().split(val);
			ParameterizedType component = (ParameterizedType) holder.getParameters().get(0);

			Object[] arr = (Object[]) Array.newInstance((Class<?>) component.getRawType(), parts.size());

			parts.stream().map(e -> {
				try {

					return holder.parse(component, e.get());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				return null;
			}).collect(Collectors.toList()).toArray(arr);

			return arr;

		}

	}

	static class EnumSetParser extends Parsers {

		@Override
		public Object parse(TypeHolder holder, String val) {

			return null;

		}

	}

}
