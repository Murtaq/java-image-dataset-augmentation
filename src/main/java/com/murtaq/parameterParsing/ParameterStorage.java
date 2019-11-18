package com.murtaq.parameterParsing;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the data produced by the ParameterParser for later use.
 */
public class ParameterStorage {

	private Map<Parameter, Object> parameters;

	public ParameterStorage() {
		parameters = new HashMap<>();
	}

	/**
	 * Stores the data that is being produced by the ParameterParser as
	 * key-object pair with types Parameter and T in the parameter map.
	 * 
	 * @param <T>
	 *            The type of the stored object.
	 * @param param
	 *            The parameter that is used as key to store the object.
	 * @param toAdd
	 *            The object that is stored.
	 */
	<T> void putParameter(Parameter param, T toAdd) {
		if (!param.type.equals(toAdd.getClass())) {
			throw new IllegalArgumentException("The ParameterManager received a " + toAdd.getClass().getName().toLowerCase()
					+ " for the " + param.name() + "-Parameter but should have received a " + param.type.getName() + ".");
		}
		parameters.put(param, toAdd);
	}

	/**
	 * Returns the object of type T from the parameter map. Warning: Incorrect
	 * usage may lead to ClassCastException! Casts the returned T to the type
	 * expected by the caller (= param.type).
	 * 
	 * @param <T>
	 *            The expected return type of the object.
	 * @param param
	 *            The key for the parameter map.
	 * @return The T that is stored in the Parameter param.
	 */
	public <T> T getParameter(Parameter param) {
		@SuppressWarnings("unchecked")
		T t = (T) parameters.get(param);

		if (t == null) {
			throw new IllegalArgumentException("The ParameterManager was asked to return a value for the "
					+ param.toString().toLowerCase() + "-parameter, but the parameter was not yet initialized.");
		}
		return t;
	}

}
