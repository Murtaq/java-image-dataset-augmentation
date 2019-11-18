package com.murtaq.transformations.grid;

import static org.bytedeco.javacpp.opencv_core.CV_64F;

import org.bytedeco.javacpp.indexer.DoubleIndexer;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;

import com.murtaq.transformations.ITransform;

/**
 * Performs the transformation for all grid based transformations.
 */
public class GridTransform extends ITransform {

	private IGridTransform transform;

	public GridTransform(IGridTransform transform) {
		this.transform = transform;
	}

	@Override
	public Mat applyOn(Mat image) {

		int height = image.arrayHeight();
		int width = image.arrayWidth();

		Mat deltaX = new Mat(height, width, CV_64F);
		Mat deltaY = new Mat(height, width, CV_64F);

		DoubleIndexer indexDx = deltaX.createIndexer();
		DoubleIndexer indexDy = deltaY.createIndexer();

		transform.fillDeltaMatrices(indexDx, indexDy);

		try {
			return genNewImage(image, indexDx, indexDy);
		} finally {
			deltaX.close();
			deltaY.close();
		}
	}

	/**
	 * Applies the grid based transformation specified by the transform field.
	 * 
	 * @param srcImage
	 *            The image to transform.
	 * @param indexDx
	 *            The pixel changes in x-direction.
	 * @param indexDy
	 *            The pixel changes in y-direction.
	 * @return The transformed image.
	 */
	private Mat genNewImage(Mat srcImage, DoubleIndexer indexDx, DoubleIndexer indexDy) {
		double targetX, targetY;
		int targetXi, targetYi, targetXi1, targetYi1;
		double deltaX, deltaY;
		double w, h;
		int nextI, nextJ;

		int height = srcImage.arrayHeight();
		int width = srcImage.arrayWidth();

		int gridSize = transform.getGridSize();

		Mat newImage = new Mat(height, width, srcImage.type());

		UByteIndexer srcIndexer = srcImage.createIndexer();
		UByteIndexer dstIndexer = newImage.createIndexer();

		// Loop over all grid points
		for (int i = 0; i < height; i += gridSize) {
			for (int j = 0; j < width; j += gridSize) {
				// Calculation of next grid points
				nextI = i + gridSize;
				nextJ = j + gridSize;
				w = gridSize;
				h = gridSize;
				if (nextI >= height) {
					nextI = height - 1;
					h = nextI - i + 1;
				}
				if (nextJ >= width) {
					nextJ = width - 1;
					w = nextJ - j + 1;
				}
				// Loop over each pixel in each grid square
				// H and W are height and width of the section
				for (double di = 0; di < h; di++) {
					for (double dj = 0; dj < w; dj++) {
						// Calculation of delta value (change of position) for
						// current pixel
						deltaX = bilinearInterpolation(di / h, dj / w, indexDx.get(i, j), indexDx.get(i, nextJ),
								indexDx.get(nextI, j), indexDx.get(nextI, nextJ));
						deltaY = bilinearInterpolation(di / h, dj / w, indexDy.get(i, j), indexDy.get(i, nextJ),
								indexDy.get(nextI, j), indexDy.get(nextI, nextJ));
						targetX = j + dj + deltaX;
						targetY = i + di + deltaY;
						if (targetX > width - 1) {
							targetX = width - 1;
						}
						if (targetY > height - 1) {
							targetY = height - 1;
						}
						if (targetX < 0) {
							targetX = 0;
						}
						if (targetY < 0) {
							targetY = 0;
						}

						targetXi = (int) targetX;
						targetYi = (int) targetY;

						targetXi1 = (int) Math.ceil(targetX);
						targetYi1 = (int) Math.ceil(targetY);

						// Calculation and overwriting of color values channel
						// by channel for the current pixel
						if (srcImage.channels() == 1) {
							byte target = (byte) bilinearInterpolation(targetY - targetYi, targetX - targetXi,
									srcIndexer.get(targetYi, targetXi), srcIndexer.get(targetYi, targetXi1),
									srcIndexer.get(targetYi1, targetXi), srcIndexer.get(targetYi1, targetXi1));
							dstIndexer.put((long) (i + di), (long) (j + dj), target);
						} else {
							for (int ll = 0; ll < srcImage.channels(); ll++) {
								byte target = (byte) bilinearInterpolation(targetY - targetYi, targetX - targetXi,
										srcIndexer.get(targetYi, targetXi, ll), srcIndexer.get(targetYi, targetXi1, ll),
										srcIndexer.get(targetYi1, targetXi, ll), srcIndexer.get(targetYi1, targetXi1, ll));
								dstIndexer.put((long) (i + di), (long) (j + dj), ll, target);
							}
						}
					}
				}
			}
		}
		return newImage;
	}

	/*
	 * Calculates the bilinear interpolation for the pixel at position (x,y) See
	 * https://en.wikipedia.org/wiki/Bilinear_interpolation
	 */
	private double bilinearInterpolation(double x, double y, double v11, double v12, double v21, double v22) {
		return (v11 * (1 - y) + v12 * y) * (1 - x) + (v21 * (1 - y) + v22 * y) * x;
	}
}
