package com.murtaq.parameterParsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.murtaq.transformation.TransformFactory;
import com.murtaq.transformations.ITransform;

/**
 * Creates the transformation objects from their command line arguments.
 */
class TransformParser {

	/**
	 * Creates the transformations from the extracted command line input.
	 * 
	 * @param transformStrings
	 *            The transformations to be created.
	 * @param delimiter
	 *            The delimiter used in the arguments.
	 * @return The list of created transformations.
	 * @throws ParamParseException
	 *             when an error occurs during the transform creation in the
	 *             TransformFactory.
	 */
	List<ITransform> parseTransforms(List<String> transformStrings, String delimiter) throws ParamParseException {
		List<ITransform> parsedTransforms = new ArrayList<ITransform>();
		for (String transform : transformStrings) {
			parsedTransforms.add(createFromArray(transform.split(delimiter)));
		}
		return parsedTransforms;
	}

	/**
	 * Creates a single transformation from a parameter array. The 1st array
	 * element is the transformation name, following elements are the arguments
	 * for the transformation.
	 * 
	 * @param transformStrings
	 *            The transformation to be created.
	 * @return The created transformation.
	 * @throws ParamParseException
	 *             when an error occurs during the transform creation in the
	 *             TransformFactory.
	 */
	private ITransform createFromArray(String[] params) throws ParamParseException {
		String[] args = Arrays.copyOfRange(params, 1, params.length);
		return TransformFactory.create(params[0], args);
	}

}
