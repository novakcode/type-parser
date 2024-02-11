package com.typeconverter;

import static com.typeconverter.ClassUtil.primitives;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PrimitiveParsers {

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
			primitiveParsers.put(ClassUtil.primitives[2], new Parse() {

				@Override
				public Object parse(TypeHolder holder, String val) {
					return Character.valueOf(val.charAt(0));
				}
			});

			// Long
			primitiveParsers.put(ClassUtil.primitives[3], new Parse() {

				@Override
				public Object parse(TypeHolder holder, String val) {
					return Long.valueOf(val);
				}
			});

			// Short
			primitiveParsers.put(ClassUtil.primitives[4], new Parse() {

				@Override
				public Object parse(TypeHolder holder, String val) {
					return Short.valueOf(val);
				}
			});

			// Byte
			primitiveParsers.put(ClassUtil.primitives[5], new Parse() {

				@Override
				public Object parse(TypeHolder holder, String val) {
					return Byte.valueOf(val);
				}
			});

			// Bool
			primitiveParsers.put(ClassUtil.primitives[6], new Parse() {

				@Override
				public Object parse(TypeHolder holder, String val) {
					return Boolean.valueOf(val);
				}
			});

			// Float
			primitiveParsers.put(ClassUtil.primitives[7], new Parse() {

				@Override
				public Object parse(TypeHolder holder, String val) {
					return Float.valueOf(val);
				}
			});

			// String
			primitiveParsers.put(ClassUtil.primitives[8], new Parse() {

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

			if (!holder.getRaw().isEnum())
				throw new IllegalArgumentException("Can't cast class to Enum. Class not enum.");

			Class<Enum<?>> enumClazz = holder.getRaw();

			Enum<?> enu = Enum.valueOf(enumClazz.asSubclass(Enum.class), val);

			return enu;
		}

	}

}
