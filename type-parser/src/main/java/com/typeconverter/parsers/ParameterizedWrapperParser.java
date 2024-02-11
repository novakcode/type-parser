package com.typeconverter.parsers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ParameterizedWrapperParser extends Parsers {

	@Override
	public Object parse(TypeHolder holder, String val) {

		Object obj = null;

//
		try {
			obj = parseConstructor(holder, val);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (obj == null)
			try {
				obj = parseWithFactoryMethod(holder, val);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| ClassNotFoundException | InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return obj;
	}

	public Object parseConstructor(TypeHolder holder, String val) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ClassNotFoundException {

		Object obj = null;

		
		// if we have more than one parameter, we will use splitter to split val and parse each part with each parameter
		if (holder.getParameters().size() > 1) {
			obj = parseMultipleParameters(holder, val);

			if (obj != null)
				return obj;
		}

		
		// if parameter size is 1 we try finding a constructor with one argument, which is an object. This matches a constructor with a generic type
		
		
		
		Optional<Constructor<?>> constructor = Optional.empty();
		Class<?> raw = holder.getRaw();
		Type param = holder.getParameters().get(0);

		List<Constructor<?>> constructors = Arrays.asList(raw.getConstructors()).stream()
				.filter(e -> e.getParameterCount() == 1).collect(Collectors.toList());

		if (constructors.size() == 0) {
			return null;
		}

		constructor = constructors.stream().filter(e -> e.getParameterTypes()[0].equals(Object.class)).findFirst();

		if (constructor.isPresent()) {

			return constructor.get().newInstance(holder.parse(param, val));
		}

		// finally we try just taking any constructor with one argument and parse the argument type and val
		
		constructor = constructors.stream().findFirst();
		param = constructor.get().getParameterTypes()[0];

		return constructor.get().newInstance(holder.parse(param, val));

	}

	public Object parseMultipleParameters(TypeHolder holder, String val) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Splitter splitter = holder.getSplitter();
		Class<?> raw = holder.getRaw();

		if (splitter.splittingObject()) {

			List<SplitPart> parts = splitter.split(val);
			Optional<Constructor<?>> constructor = Arrays.asList(raw.getConstructors()).stream()
					.filter(e -> e.getParameterCount() == parts.size()).filter(e -> {

						for (Class<?> t : e.getParameterTypes()) {
							if (!t.equals(Object.class))
								return false;
						}

						return true;
					}).findFirst();

			// checks if size of splittet val is same as parameters size
			// each part is a new val which is to be parsed as each param the raw class has
			if (parts.size() == holder.getParameters().size() && constructor.isPresent()) {

				Object[] args = new Object[holder.getParameters().size()];

				for (int i = 0; i < args.length; i++) {

					args[i] = holder.parse(holder.getParameters().get(i), parts.get(i).get());

				}
				return constructor.get().newInstance(args);

			}
		}

		return null;
	}

	public Object parseWithFactoryMethod(TypeHolder holder, String val)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, InstantiationException {

		Object obj = null;
		
		if (holder.getParameters().size() > 1) {
			obj = parseMultipleParametersWithFactoryMethod(holder, val);
			
			if(obj != null)
				return obj;
		}

		Class<?> raw = holder.getRaw();
		Type param = holder.getParameters().get(0);

		List<Method> methods = Arrays.asList(raw.getDeclaredMethods()).stream().filter(
				e -> e.getParameterCount() == 1 && e.getReturnType().equals(raw) && Modifier.isStatic(e.getModifiers()))
				.collect(Collectors.toList());

		if (methods.size() == 0) {
			return null;
		}

		Optional<Method> method = Optional.empty();
		method = methods.stream().filter(e -> e.getParameterTypes()[0].equals(Object.class)).findFirst();

		if (method.isPresent()) {
			return method.get().invoke(null, holder.parse(param, val));
		}

		method = methods.stream().findFirst();
		param = method.get().getParameterTypes()[0];

		return method.get().invoke(null, holder.parse(param, val));
	}

	
	
	public Object parseMultipleParametersWithFactoryMethod(TypeHolder holder, String val) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Splitter splitter = holder.getSplitter();
		Class<?> raw = holder.getRaw();

		if (splitter.splittingObject()) {

			List<SplitPart> parts = splitter.split(val);
			Optional<Method> method = Arrays.asList(raw.getDeclaredMethods()).stream()
					.filter(e -> e.getParameterCount() == parts.size()).filter(e -> {

						for (Class<?> t : e.getParameterTypes()) {
							if (!t.equals(Object.class))
								return false;
						}

						return true;
					}).findFirst();

			// checks if size of splittet val is same as parameters size
			// each part is a new val which is to be parsed as each param the raw class has
			if (parts.size() == holder.getParameters().size() && method.isPresent()) {

				Object[] args = new Object[holder.getParameters().size()];

				for (int i = 0; i < args.length; i++) {
					
					args[i] = holder.parse(holder.getParameters().get(i), parts.get(i).get());

				}
				
				
				return method.get().invoke(null, args);
			}
		}

		return null;
	}

}
