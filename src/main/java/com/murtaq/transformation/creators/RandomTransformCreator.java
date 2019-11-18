package com.murtaq.transformation.creators;

import com.murtaq.parameterParsing.ParamParseException;
import com.murtaq.transformations.ITransform;
import com.murtaq.transformations.grid.GridTransform;
import com.murtaq.transformations.grid.RandomTransform;

/**
 * Creates the random transformation.
 * 
 * @see com.murtaq.transformations.grid.RandomTransform
 */
public class RandomTransformCreator extends TransformCreator {

	public RandomTransformCreator() {
		super(0, 3);
	}

	@Override
	ITransform build(String[] params) throws ParamParseException {
		int gridSize = -1;
		double scale = 1;
		boolean regenerateDistMat = false;
		if (params.length >= 1) {
			try {
				gridSize = Integer.parseInt(params[0]);
			} catch (NumberFormatException e) {
				throw new ParamParseException(
						"The gridSize you entered for the " + getTransformationName() + "-Transform was not a valid integer.");
			}
		}
		if (params.length >= 2) {
			try {
				scale = Double.parseDouble(params[1]);
			} catch (NumberFormatException e) {
				throw new ParamParseException(
						"The scale you entered for the " + getTransformationName() + "-Transform was not a valid double.");
			}
		}
		if (params.length == 3) {
			if (params[2].equals("t")) {
				regenerateDistMat = true;
			} else if (params[2].equals("f")) {
				regenerateDistMat = false;
			} else {
				throw new ParamParseException("The regenerateDistortionMatrix boolean you entered for the "
						+ getTransformationName() + "-Transform was not recognized.");
			}
		}
		return new GridTransform(new RandomTransform(gridSize, scale, regenerateDistMat));
	}

	@Override
	public String getTransformationName() {
		return "Random";
	}

	@Override
	String getOperationDescription() {
		return getTransformationName() + ": Randomly enlarges, shrinks or shifts parts of the image.";
	}

	@Override
	String getParamDescription() {
		String params = "Par. 1: GridSize (int). The size of the areas that will be distorted. Defaults to \"-1\", the value for automatic calculation.\n";
		params += "Par. 2: Scale (double). Multiplier for the pixel movement vectors. Defaults to \"1\".\n";
		params += "Par. 3: Regenerate (t / f). Generate a new distortion matrix for each picture. Setting this to true may increase the transformation duration significantly. Defaults to false.\n";
		return params;
	}

}
