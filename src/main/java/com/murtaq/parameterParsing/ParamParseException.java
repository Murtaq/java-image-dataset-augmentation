package com.murtaq.parameterParsing;

/**
 * Thrown by the ParameterParser, TransformParser and ImageParser when errors
 * occur during translation of command line arguments to objects.
 */
@SuppressWarnings("serial")
public class ParamParseException extends Exception {

	public ParamParseException(String string) {
		super(string);
	}
}