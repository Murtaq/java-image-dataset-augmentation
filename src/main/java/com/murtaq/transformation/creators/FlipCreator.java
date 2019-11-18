package com.murtaq.transformation.creators;

import com.murtaq.parameterParsing.ParamParseException;
import com.murtaq.transformations.Flip;
import com.murtaq.transformations.ITransform;

/**
 * Creates the flip transformation.
 * 
 * @see com.murtaq.transformations.Flip
 */
public class FlipCreator extends TransformCreator {

	public FlipCreator() {
		super(1, 1);
	}

	@Override
	ITransform build(String[] params) throws ParamParseException {
		if (params[0] == "h") {
			return new Flip(true, false);
		} else if (params[0] == "v") {
			return new Flip(false, true);
		}
		throw new ParamParseException(
				"The parameter for the flip transformation has to be either \"h\" for horizontal flipping or \"v\" for vertical flipping.");
	}

	@Override
	public String getTransformationName() {
		return "Flip";
	}

	@Override
	public String getOperationDescription() {
		return getTransformationName() + ": Flips the picture in horizontal or vertical direction.";
	}

	@Override
	public String getParamDescription() {
		return "Par. 1: \"h\" for horizontal flip, \"v\" for vertical flip.";
	}

}
