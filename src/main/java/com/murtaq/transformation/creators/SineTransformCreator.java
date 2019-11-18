package com.murtaq.transformation.creators;

import com.murtaq.parameterParsing.ParamParseException;
import com.murtaq.transformations.ITransform;
import com.murtaq.transformations.grid.GridTransform;
import com.murtaq.transformations.grid.SineFunctionTransform;

/**
 * Creates the sine transformation
 * 
 * @see com.murtaq.transformations.grid.SineFunctionTransform.
 */
public class SineTransformCreator extends TransformCreator {

	public SineTransformCreator() {
		super(0, 9);
	}

	@Override
	ITransform build(String[] params) throws ParamParseException {
		int gridSize;
		double[] sineParams = new double[0];
		if (params.length > 0) {
			sineParams = new double[params.length - 1];
			try {
				gridSize = Integer.parseInt(params[0]);
			} catch (NumberFormatException e) {
				throw new ParamParseException(
						"The gridSize you entered for the " + getTransformationName() + " was not a valid integer.");
			}
			for (int i = 0; i < sineParams.length; i++) {
				sineParams[i] = validateDouble(params[i + 1]);
			}
		} else {
			gridSize = -1;
		}
		return new GridTransform(new SineFunctionTransform(gridSize, sineParams));
	}

	private double validateDouble(String doubleToValidate) throws ParamParseException {
		try {
			return Double.parseDouble(doubleToValidate);
		} catch (NumberFormatException e) {
			throw new ParamParseException(
					"One of the parameters for the " + getTransformationName() + " was not a valid double.");
		}
	}

	@Override
	public String getTransformationName() {
		return "Sine";
	}

	@Override
	String getOperationDescription() {
		return getTransformationName() + ": Moves pixels using a sine function.";
	}

	@Override
	String getParamDescription() {
		String params = "Par. 1: GridSize (int). The size of the areas that the function moves. Defaults to \"-1\", which means automatic calculation.\n";
		params += "Par. 2: X-Scaling (double). Scales the result from the sine function. Defaults to \"0\".\n";
		params += "Par. 3/4: X-Multipliers (double). Multiplied with the x / y pixel position. Default to \"0\".\n";
		params += "Par. 5: X-Offset (double). Summed with the values from above. Defaults to \"0\".\n";
		params += "The resulting x-displacement is P2\u00b7sin((P3\u00b7x + P4\u00b7y + P5) \u00b7 (Pi/180)).\n";
		params += "Par. 6: Y-Scaling (double). Scales the result from the sine function. Defaults to \"0\".\n";
		params += "Par. 7/8: Y-Multipliers (double). Multiplied with the x / y pixel position. Default to \"0\".\n";
		params += "Par. 9: Y-Offset (double). Summed with the values from above. Defaults to \"0\".\n";
		params += "The resulting y-displacement is P6\u00b7sin((P7\u00b7x + P8\u00b7y + P9) \u00b7 (Pi/180)).\n";
		return params;
	}

}
