package com.murtaq.transformations;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * The transformation base class. This has to be implemented by all
 * transformations.
 */
public abstract class ITransform {

	/**
	 * Applies the transformation on an image.
	 * 
	 * @param image
	 *            The image to transform.
	 * @return The transformed image.
	 */
	public abstract Mat applyOn(Mat image);

}
