package com.murtaq.transformations;

import org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.flip;

/**
 * Flips the image in one of four directions: None, horizontal, vertical or
 * horizontal and vertical (180 degree rotation)
 */
public class Flip extends ITransform {

	private boolean horizontal, vertical;

	public Flip(boolean horizontal, boolean vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}

	@Override
	public Mat applyOn(Mat image) {
		int flipDirection;
		if (horizontal && vertical) {
			flipDirection = -1;
		} else if (horizontal) {
			flipDirection = 0;
		} else if (vertical) {
			flipDirection = 1;
		} else {
			return image;
		}
		Mat flippedImage = new Mat();
		flip(image, flippedImage, flipDirection);
		return flippedImage;
	}

}
