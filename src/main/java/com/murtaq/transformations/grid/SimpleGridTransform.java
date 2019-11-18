package com.murtaq.transformations.grid;

import org.bytedeco.javacpp.indexer.DoubleIndexer;

/**
 * Simplifies the grid transform so that all implementing transformations only
 * need to implement methods to transform the pixels at grid line intersections.
 */
abstract class SimpleGridTransform extends IGridTransform {

	public SimpleGridTransform(int gridSize) {
		super(gridSize);
	}

	@Override
	protected void calcDeltaMatrices(DoubleIndexer deltaX, DoubleIndexer deltaY) {
		for (int gridY = 0, posY = 0;; gridY++, posY += getGridSize()) {
			if (posY >= getImageHeight() && posY < getImageHeight() + getGridSize() - 1) {
				posY = getImageHeight() - 1;
			} else if (posY >= getImageHeight()) {
				break;
			}
			for (int gridX = 0, posX = 0;; gridX++, posX += getGridSize()) {
				if (posX >= getImageWidth() && posX < getImageWidth() + getGridSize() - 1) {
					posX = getImageWidth() - 1;
				} else if (posX >= getImageWidth()) {
					break;
				}
				deltaX.put(posY, posX, getXShift(gridY, gridX));
				deltaY.put(posY, posX, getYShift(gridY, gridX));
			}
		}
	}

	/**
	 * Calculates the difference in x-position for pixels at grid line
	 * intersections in the delta matrix.
	 * 
	 * @param gridY
	 *            y-position of the pixel.
	 * @param gridX
	 *            x-position of the pixel.
	 * @return The difference in x-position for the grid line intersection
	 *         pixel.
	 */
	protected abstract double getXShift(int gridY, int gridX);

	/**
	 * Calculates the difference in y-position for pixels at grid line
	 * intersections in the delta matrix.
	 * 
	 * @param gridY
	 *            y-position of the pixel.
	 * @param gridX
	 *            x-position of the pixel.
	 * @return The difference in y-position for the grid line intersection
	 *         pixel.
	 */
	protected abstract double getYShift(int gridY, int gridX);

}
