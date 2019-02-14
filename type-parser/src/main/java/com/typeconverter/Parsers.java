package com.typeconverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static com.typeconverter.ClassUtil.primitives;

public abstract class Parsers implements Parse {

	public static Parse getParser(ClassType type) {
		return type.getParser();
	}

	public static class Parser {

		static class CollectionParser extends Parsers {

			@Override
			public Object parse(TypeHolder holder, String val) {

				return null;
			}

		}

		static class MapParser extends Parsers {

			@Override
			public Object parse(TypeHolder holder, String val) {
				// TODO Auto-generated method stub
				return null;
			}

		}

		static class ArrayParser extends Parsers {

			@Override
			public Object parse(TypeHolder holder, String val) {
				// TODO Auto-generated method stub
				return null;
			}

		}

		static class EnumSetParser extends Parsers {

			@Override
			public Object parse(TypeHolder holder, String val) {
				// TODO Auto-generated method stub
				return null;
			}
			
  // ff
		}
	}

	public static class SingleParser {

		static class WrapperParser extends Parsers {

			@Override
			public Object parse(TypeHolder holder, String val) {

				Object obj = parseConstructor(holder, val);

				if (obj == null)
					obj = parseWithFactoryMethod(holder, val);

				return obj;
			}

			private Object parseConstructor(TypeHolder holder, String val) {

				Optional<Constructor<?>> constructor = Arrays.asList(holder.getRaw().getDeclaredConstructors()).stream()
						.filter(c -> c.getParameterCount() == 1).findFirst();

				if (constructor.isPresent()) {
					try {

						Class<?> parameter = constructor.get().getParameterTypes()[0];

						Object obj = constructor.get().newInstance(holder.parse(parameter, val));
						return obj;
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {

					}
				}

				return null;
			}

			private Object parseWithFactoryMethod(TypeHolder holder, String val) {

				Optional<Method> method = Arrays.asList(holder.getRaw().getDeclaredMethods()).stream()
						.filter(m -> Modifier.isStatic(m.getModifiers()) && m.getReturnType().equals(holder.getRaw())
								&& m.getParameterCount() == 1)
						.findFirst();

				Object obj = null;

				if (method.isPresent()) {
					Class<?> parameter = method.get().getParameterTypes()[0];

					try {
						obj = method.get().invoke(obj, holder.parse(parameter, val));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

					}
				}

				return obj;
			}
		}

		static class PrimitiveParser extends Parsers {

			@Override
			public Object parse(TypeHolder holder, String val) {
				Optional<Parse> parse = primitiveParsers.entrySet().stream().filter(parser -> {
					for (Class<?> cl : parser.getKey()) {
						if (cl.equals(holder.getRaw())) {
							return true;
						}
					}

					return false;
				}).map(k -> k.getValue()).findFirst();

				if (parse.isPresent())
					return parse.get().parse(holder, val);

				return null;
			}

			private Map<Class<?>[], Parse> primitiveParsers = new HashMap<>();

			{
				// Integer
				primitiveParsers.put(primitives[0], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return Integer.valueOf(val);
					}
				});

				// Double
				primitiveParsers.put(ClassUtil.primitives[1], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return Double.valueOf(val);
					}
				});

				// Char
				primitiveParsers.put(ClassUtil.primitives[1], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return Character.valueOf(val.charAt(0));
					}
				});

				// Long
				primitiveParsers.put(ClassUtil.primitives[1], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return Long.valueOf(val);
					}
				});

				// Short
				primitiveParsers.put(ClassUtil.primitives[1], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return Short.valueOf(val);
					}
				});

				// Byte
				primitiveParsers.put(ClassUtil.primitives[1], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return Byte.valueOf(val);
					}
				});

				// Bool
				primitiveParsers.put(ClassUtil.primitives[1], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return Boolean.valueOf(val);
					}
				});

				// Float
				primitiveParsers.put(ClassUtil.primitives[1], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return Float.valueOf(val);
					}
				});

				// String
				primitiveParsers.put(ClassUtil.primitives[1], new Parse() {

					@Override
					public Object parse(TypeHolder holder, String val) {
						return val;
					}
				});
			}

		}

		static class EnumParser extends Parsers {

			@Override
			public Object parse(TypeHolder holder, String val) {

				if (holder.getRaw().isEnum())
					throw new IllegalArgumentException("Can't cast class to Enum. Class not enum.");

				Class<Enum<?>> enumClazz = holder.getRaw();

				Enum<?> enu = Enum.valueOf(enumClazz.asSubclass(Enum.class), val);

				return enu;
			}

		}
	}

}
