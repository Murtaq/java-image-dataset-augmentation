package com.murtaq.transformations.grid;

import static com.murtaq.util.MatGenerationUtil.generateDistortedNoise;
import static org.bytedeco.javacpp.opencv_core.CV_64F;
import static org.bytedeco.javacpp.opencv_core.NORM_MINMAX;
import static org.bytedeco.javacpp.opencv_core.normalize;
// import static org.bytedeco.javacpp.opencv_core.abs;
import static org.bytedeco.javacpp.opencv_imgproc.Sobel;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.indexer.DoubleIndexer;

/**
 * Randomly distorts the image by generating a distortion matrix for the grid
 * line intersection pixels.
 */
public class RandomTransform extends SimpleGridTransform {

	private DoubleIndexer xIndexer, yIndexer;
	private double scale;
	private int lastImageHeight, lastImageWidth;
	private boolean regenerateDistMat;

	/**
	 * Creates a new RandomTransform.
	 * 
	 * @param gridSize
	 *            The pixel distance between grid lines.
	 * @param scale
	 *            The scaling of the calculated distortion (recommended to be
	 *            lower than the grid size as grid lines will overlap, causing
	 *            unexpected distortions).
	 * @param regenerateDistMat
	 *            Whether to create a new distortion matrix after each image.
	 */
	public RandomTransform(int gridSize, double scale, boolean regenerateDistMat) {
		super(gridSize);
		this.scale = scale;
		this.regenerateDistMat = regenerateDistMat;
		initNewDistortion();
	}

	/**
	 * Generates a new distortion matrix for the image and stores the result in
	 * the x and yIndexer fields.
	 */
	private void initNewDistortion() {
		int gridHeight = calcGridPoints(getImageHeight(), getGridSize());
		int gridWidth = calcGridPoints(getImageWidth(), getGridSize());

		lastImageHeight = getImageHeight();
		lastImageWidth = getImageWidth();

		Mat grid = generateDistortedNoise(gridHeight, gridWidth);
		Mat gridX = new Mat(gridHeight, gridWidth, CV_64F);
		Mat gridY = new Mat(gridHeight, gridWidth, CV_64F);

		Sobel(grid, gridX, CV_64F, 1, 0);
		Sobel(grid, gridY, CV_64F, 0, 1);

		normalize(gridX.clone(), gridX, -1.0, 1.0, NORM_MINMAX, CV_64F, new Mat());
		normalize(gridY.clone(), gridY, -1.0, 1.0, NORM_MINMAX, CV_64F, new Mat());

		xIndexer = gridX.createIndexer();
		yIndexer = gridY.createIndexer();
	}

	/**
	 * Calculates the number of grid points that fit in the specified number of
	 * pixels.
	 * 
	 * @param numPixels
	 *            The number of pixels.
	 * @param gridSize
	 *            The pixel distance between grid lines.
	 * @return The number of grid lines that fit in the number of pixels.
	 */
	private int calcGridPoints(int numPixels, int gridSize) {
		if (gridSize == 1) {
			return numPixels;
		}
		int rest = numPixels % gridSize;
		int result = (numPixels - rest) / gridSize;
		if (rest > 0 && (numPixels - 1) % gridSize != 0) {
			return result + 2;
		}
		return result + 1;
	}

	@Override
	protected void calcDeltaMatrices(DoubleIndexer deltaX, DoubleIndexer deltaY) {
		if (regenerateDistMat || getImageHeight() != lastImageHeight || getImageWidth() != lastImageWidth) {
			initNewDistortion();
		}
		super.calcDeltaMatrices(deltaX, deltaY);
	}

	@Override
	protected double getXShift(int gridY, int gridX) {
		return xIndexer.get(gridY, gridX) * scale;
	}

	@Override
	protected double getYShift(int gridY, int gridX) {
		return yIndexer.get(gridY, gridX) * scale;
	}
}
