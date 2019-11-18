package com.murtaq.util;

import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.CV_INTER_CUBIC;
import static org.bytedeco.javacpp.opencv_imgproc.GaussianBlur;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.util.Random;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.indexer.UByteIndexer;

public class MatGenerationUtil {

	/**
	 * Generates an image with randomly colored grayscale pixels.
	 * 
	 * @param height
	 *            Height of the image.
	 * @param width
	 *            Width of the image.
	 * @return The generated image.
	 */
	private static Mat generateBlackWhiteNoise(int height, int width) {
		Mat noise = new Mat(height, width, CV_8U);
		UByteIndexer noiseIndexer = noise.createIndexer();

		Random r = new Random();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				noiseIndexer.put(i, j, r.nextInt(256));
			}
		}
		return noise;
	}

	/**
	 * Generates a grayscale image with random colors but smooth color
	 * gradients.
	 * 
	 * @param height
	 *            Height of the image.
	 * @param width
	 *            Width of the image.
	 * @return The generated image.
	 */
	public static Mat generateDistortedNoise(int height, int width) {
		float scalingFactor = 0.7f;
		int srcHeight = (int) Math.round(height * scalingFactor);
		int srcWidth = (int) Math.round(width * scalingFactor);
		Mat srcNoise = generateBlackWhiteNoise(srcHeight, srcWidth);
		Mat resizedNoise = new Mat(height, width, CV_8U);
		resize(srcNoise, resizedNoise, resizedNoise.size(), 0, 0, CV_INTER_CUBIC);
		Mat blurredNoise = new Mat(height, width, CV_8U);
		GaussianBlur(resizedNoise, blurredNoise, new Size(5, 5), 0d, 0d, CV_GAUSSIAN);
		return blurredNoise;
	}
}
