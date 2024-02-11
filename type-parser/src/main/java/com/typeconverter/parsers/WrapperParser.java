package com.typeconverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WrapperParser extends Parsers {

	@Override
	public Object parse(TypeHolder holder, String val) {

		Object obj = null;
		try {
			obj = parseConstructor(holder, val);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj == null)
			try {
				obj = parseWithFactoryMethod(holder, val);
			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return obj;
	}

	private Object parseWithConstructorAndSplit(TypeHolder holder, String val) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<SplitPart> parts = holder.getSplitter().split(val);
	
		Optional<Constructor<?>> constructor = Arrays.asList(holder.getRaw().getDeclaredConstructors()).stream()
				.filter(e -> e.getParameterCount() == parts.size()).findFirst();

		if (constructor.isPresent()) {

			Class<?>[] params = constructor.get().getParameterTypes();
			Object[] objects = new Object[params.length];
			for (int i = 0; i < params.length; i++)
				objects[i] = holder.parse(params[i], parts.get(i).get());

			return constructor.get().newInstance(objects);
		}

		return null;
	}

	private Object parseConstructor(TypeHolder holder, String val) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Object obj = null;

		if (holder.getSplitter().splittingObject()) {
			
			obj = parseWithConstructorAndSplit(holder, val);

			if (obj != null)
				return obj;
		}
		
		

		Optional<Constructor<?>> constructor = Arrays.asList(holder.getRaw().getDeclaredConstructors()).stream()
				.filter(c -> c.getParameterCount() == 1).findFirst();

		if (constructor.isPresent()) {
			try {

				Class<?> parameter = constructor.get().getParameterTypes()[0];

				obj = constructor.get().newInstance(holder.parse(parameter, val));
				return obj;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {

			}
		}

		return obj;
	}

	private Object parseWithFactoryMethodAndSplit(TypeHolder holder, String val)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		List<SplitPart> parts = holder.getSplitter().split(val);

		//
		Optional<Method> method = Arrays
				.asList(holder.getRaw().getDeclaredMethods()).stream().filter(e -> e.getParameterCount() == parts.size()
						&& Modifier.isStatic(e.getModifiers()) && e.getReturnType().equals(holder.getRaw()))
				.findFirst();

		//
		if (method.isPresent()) {

			//
			Class<?>[] params = method.get().getParameterTypes();

			Object[] objects = new Object[params.length];
			for (int i = 0; i < params.length; i++)
				objects[i] = holder.parse(params[i], parts.get(i).get());

			//
			return method.get().invoke(null, objects);
		}

		return null;
	}

	private Object parseWithFactoryMethod(TypeHolder holder, String val)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Object obj = null;

		if (holder.getSplitter().splittingObject()) {
			obj = parseWithFactoryMethodAndSplit(holder, val);

			if (obj != null)
				return obj;
		}

		Optional<Method> method = Arrays.asList(holder.getRaw().getDeclaredMethods()).stream()
				.filter(m -> Modifier.isStatic(m.getModifiers()) && m.getReturnType().equals(holder.getRaw())
						&& m.getParameterCount() == 1)
				.findFirst();

		if (method.isPresent()) {
			Class<?> parameter = method.get().getParameterTypes()[0];

			try {
				obj = method.get().invoke(obj, holder.parse(parameter, val));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| ClassNotFoundException e) {

			}
		}

		return obj;
	}
}
