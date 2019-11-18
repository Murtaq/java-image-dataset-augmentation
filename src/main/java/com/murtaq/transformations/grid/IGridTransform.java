package com.murtaq.transformations.grid;

import org.bytedeco.javacpp.indexer.DoubleIndexer;

/**
 * Base class for all grid based transformations.
 */
abstract class IGridTransform {

	private final int gridSize;
	private int imageHeight, imageWidth;

	IGridTransform(int gridSize) {
		this.gridSize = gridSize;
		this.imageHeight = 100;
		this.imageWidth = 100;
	}

	/**
	 * Creates and fills the delta matrices, which contain the difference in
	 * pixel positions in x- and y- direction for each pixel.
	 * 
	 * @param deltaX
	 *            The indexer for the x-direction.
	 * @param deltaY
	 *            The indexer for the y-direction.
	 */
	void fillDeltaMatrices(DoubleIndexer deltaX, DoubleIndexer deltaY) {
		imageHeight = (int) deltaX.height();
		imageWidth = (int) deltaX.width();
		calcDeltaMatrices(deltaX, deltaY);
	}

	/**
	 * Calculates the distortion (= change in pixel positions) for the delta
	 * matrices.
	 * 
	 * @param deltaX
	 *            The indexer for the x-direction.
	 * @param deltaY
	 *            The indexer for the y-direction.
	 */
	protected abstract void calcDeltaMatrices(DoubleIndexer deltaX, DoubleIndexer deltaY);

	public int getGridSize() {
		if (gridSize == -1) {
			return calcGridSize(imageHeight, imageWidth);
		}
		return gridSize;
	}

	/**
	 * Calculates a default value for the grid size when no user supplied value
	 * is available.
	 * 
	 * @param height
	 *            The height of the image.
	 * @param width
	 *            The width of the image.
	 * @return The calculated grid size.
	 */
	private int calcGridSize(long height, long width) {
		long lower = height < width ? height : width;
		int size = (int) Math.round(0.35 * Math.sqrt(lower));
		return size < 2 ? 2 : size;
	}

	protected int getImageHeight() {
		return imageHeight;
	}

	protected int getImageWidth() {
		return imageWidth;
	}
}
