package com.baselib.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baselib.exception.TechnicalException;

public class ReflectUtil {

	private static final String CLASS_NAME = ReflectUtil.class.getName();

	private static Map<MethodIdentifier, Method> methodCache = new ConcurrentHashMap<MethodIdentifier, Method>();

	private ReflectUtil() {
	}

	public static class MethodIdentifier {

		private String className;

		private String methodName;

		private Class[] parameters;

		private int hashCode;

		public MethodIdentifier(String className, String methodName,
				Class[] parameters) {

			this.className = className;
			this.methodName = methodName;
			this.parameters = parameters;
			hashCode = calcHashCode();

		}

		public boolean equals(Object o) {

			if (this == o)
				return true;

			if (!(o instanceof MethodIdentifier))
				return false;

			final MethodIdentifier methodIdentifier = (MethodIdentifier) o;

			if (className != null ? !className
					.equals(methodIdentifier.className)
					: methodIdentifier.className != null)
				return false;

			if (methodName != null ? !methodName
					.equals(methodIdentifier.methodName)
					: methodIdentifier.methodName != null)
				return false;

			if (!Arrays.equals(parameters, methodIdentifier.parameters))
				return false;

			return true;
		}

		public int hashCode() {

			return hashCode;
		}

		private int calcHashCode() {

			int result;

			result = (className != null ? className.hashCode() : 0);
			result = 29 * result
					+ (methodName != null ? methodName.hashCode() : 0);

			for (int i = 0; parameters != null && i < parameters.length; i++) {

				Class parameter = parameters[i];
				result = 29
						* result
						+ (parameter != null ? parameter.getName().hashCode()
								: 0);
			}
			return result;
		}
	}

	public static boolean isMethod(Class clazz, String methodName,
			Class[] methodParams) {

		return (getMethod(clazz, methodName, methodParams) != null);
	}

	public static Method getMethod(Class clazz, String methodName,
			Class[] methodParams) {

		Method[] methods = clazz.getMethods();

		for (int i = 0; i < methods.length; i++) {

			Method method = methods[i];

			if (method.getName().equals(methodName)) {

				Class[] params = method.getParameterTypes();

				if (params.length == 0) {
					return method;
				} else if (params.length == methodParams.length) {

					for (int j = 0; j < params.length; j++) {

						if (params[j] != methodParams[j]) {
							return null;
						}
						return method;
					}
				}
			}
		}
		return null;
	}

	public static Object invokeMethod(Object object, String methodName,
			Object parameters) throws Throwable {

		return invokeMethod(object, methodName, new Object[] { parameters });
	}

	public static Object invokeMethod(Object object, String methodName)
			throws Throwable {

		return invokeMethod(object, methodName, null);
	}

	public static Object invokeMethod(Object object, String methodName,
			Object[] parameters) throws Throwable {

		Object result = null;

		int numberParameters = parameters == null ? 0 : parameters.length;

		Class[] parametersClasses = new Class[numberParameters];

		for (int i = 0; i < numberParameters; i++) {
			parametersClasses[i] = (parameters[i] == null) ? null
					: parameters[i].getClass();
		}

		try {

			MethodIdentifier key = new MethodIdentifier(object.getClass()
					.getName(), methodName, parametersClasses);

			Method meth = (Method) methodCache.get(key);

			if (meth == null) {

				meth = findMethod(object, methodName, parametersClasses);

				if (meth != null) {

					methodCache.put(key, meth);
				}
			}

			if (meth != null) {
				result = meth.invoke(object, parameters);
			} else {
				String text = prepareMethodNotExistMessage(methodName,
						numberParameters, parameters, object);
				throw new TechnicalException(CLASS_NAME, text.toString());
			}
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} catch (IllegalAccessException e) {
			throw new TechnicalException(
					ReflectUtil.class.getClass().getName(),
					"Method Should Be Public", e);
		}

		return result;
	}

	private static Method findMethod(Object object, String methodName,
			Class[] parametersClasses) {

		Method meth = null;

		Method[] methObject = object.getClass().getMethods();
		Class[] methodParametersClasses = null;

		for (int i = 0; i < methObject.length; i++) {

			if (methObject[i].getName().equals(methodName)) {

				// method parameters
				methodParametersClasses = methObject[i].getParameterTypes();

				// If number of parameters is the same
				if (methodParametersClasses.length == parametersClasses.length) {

					// if all this method parameters are assignable from the one
					// we pass
					boolean parametersAssignable = true;

					for (int j = 0; j < parametersClasses.length; j++) {

						if ((parametersClasses[j] != null)
								&& !ClassUtil.isAssignableFrom(
										methodParametersClasses[j],
										parametersClasses[j])) {
							parametersAssignable = false;

							break;
						}
					}

					if (parametersAssignable) {
						meth = methObject[i];

						break;
					}
				}
			}
		}
		return meth;
	}

	private static String prepareMethodNotExistMessage(String methodName,
			int numberParameters, Object[] parameters, Object object) {

		StringBuffer text = new StringBuffer();
		text.append("Method ");
		text.append(methodName);
		text.append("(");

		for (int i = 0; i < numberParameters; i++) {

			if (parameters[i] != null) {
				text.append(parameters[i].getClass().getName());
			} else {
				text.append("null");
			}

			if (i < (numberParameters - 1)) {
				text.append(",");
			}
		}

		text.append(")");
		text.append(" does not exists for ");
		text.append(object.getClass().getName());
		return text.toString();
	}
}
