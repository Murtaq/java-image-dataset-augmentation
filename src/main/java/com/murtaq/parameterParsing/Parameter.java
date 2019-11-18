package com.murtaq.parameterParsing;

import java.io.File;

import com.murtaq.transformations.ITransform;

/**
 * Represents a parameter, storing its name, description, command line arguments
 * and type of content.
 */
public enum Parameter {

	SOURCE("The distortion source file(s). Can be one or more image files or a single directory.",
			new String[] { "-source", "-s" }, File.class),

	PARSED_IMAGES("Used to provide data type and put functionality for ParameterManager (<- only for documentation).",
			new String[] {}, File[].class),

	TARGET("The target file. Can be an image file or directory.", new String[] { "-target", "-t" }, File.class),

	TRANSFORMATIONS("The transformations to apply on the image file.", new String[] { "-transformations", "-tf" },
			ITransform[].class),

	RECURSION("Enables recursive searching for image files, if source file is directory.", new String[] { "-recursive", "-r" },
			Boolean.class),

	HELP("Displays this help.", new String[] { "-help", "-h" }, Boolean.class),

	HELPTF("Displays a list of avaliable transformations and their parameters.", new String[] { "-helpTransformations", "-ht" },
			Boolean.class);

	final String description;
	final String[] names;
	final String namesString;
	final Class<?> type;

	private Parameter(String desc, String[] names, Class<?> type) {
		this.description = desc;
		this.names = names;
		this.namesString = getNamesString(names);
		this.type = type;
	}

	/**
	 * Constructs the string that will be printed in the help section
	 * representing the parameter names.
	 * 
	 * @param names
	 *            The different names for the parameter.
	 * @return The constructed string.
	 */
	private String getNamesString(String[] names) {
		String result = "";
		for (String name : names) {
			result = result + name + " / ";
		}
		if (result.equals("")) {
			return "";
		}
		return result.substring(0, result.length() - 3);
	}

}
