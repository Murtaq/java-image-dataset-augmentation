package com.murtaq.transformation.creators;

import com.murtaq.parameterParsing.ParamParseException;
import com.murtaq.transformations.ITransform;

/**
 * Base class for all transform creators.
 */
public abstract class TransformCreator {

	private int minParams, maxParams;

	TransformCreator(int minParams, int maxParams) {
		this.minParams = minParams;
		this.maxParams = maxParams;
	}

	public ITransform create(String[] params) throws ParamParseException {
		if (params.length < minParams) {
			throw new ParamParseException("A " + getTransformationName() + "-transformation has not enough Parameters.");
		}
		if (params.length > maxParams) {
			throw new ParamParseException("A " + getTransformationName() + "-transformation has too many Parameters.");
		}
		return build(params);
	}

	/**
	 * Creates the transformation from the parameters.
	 * 
	 * @param params
	 *            The parameters for the transformation.
	 * @return The constructed transformation.
	 * @throws ParamParseException
	 *             when number or contents of arguments don't match their
	 *             allowed values.
	 */
	abstract ITransform build(String[] params) throws ParamParseException;

	public int getMinParams() {
		return minParams;
	}

	public int getMaxParams() {
		return maxParams;
	}

	public abstract String getTransformationName();

	/**
	 * Returns a short paragraph describing the performed operation.
	 * 
	 * @return The description.
	 */
	abstract String getOperationDescription();

	/**
	 * Returns a short paragraph describing the allowed parameters.
	 * 
	 * @return The parameter description.
	 */
	abstract String getParamDescription();

	/**
	 * Combines all partial descriptions in a single description for the whole
	 * transformations.
	 * 
	 * @return The complete description of the transformation.
	 */
	public String getDesc() {
		String desc = "Requires at least " + getMinParams() + " and at most " + getMaxParams() + " parameters.\n";
		return getOperationDescription() + "\n" + desc + getParamDescription() + "\n";
	}

}
