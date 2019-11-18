package com.murtaq.transformations.grid;

/**
 * Moves the pixels in the specified sine based pattern, one each for the x- and
 * y-directions.
 */
public class SineFunctionTransform extends SimpleGridTransform {

	private double[] xShiftPar;
	private double[] yShiftPar;

	private final double multiplierToRad = Math.PI / 180;

	/**
	 * Creates the SineFunctionTransform.
	 * 
	 * @param gridSize
	 *            Number of pixels between grid lines.
	 * @param transformParams
	 *            <br/>
	 *            par[0] = result scaling <br/>
	 *            par[1] = x-value scaling <br/>
	 *            par[2] = y-value scaling <br/>
	 *            par[3] = offset <br/>
	 *            ==> result = par[0] * sin( par[1] * x + par[2] * y +
	 *            par[3])<br/>
	 *            par[0-3] for x-direction <br/>
	 *            par[4-7] the same way for y-direction
	 */
	public SineFunctionTransform(int gridSize, double... transformParams) {
		super(gridSize);
		xShiftPar = new double[] { 0, 0, 0, 0 };
		yShiftPar = new double[] { 0, 0, 0, 0 };
		for (int i = 0; i < transformParams.length; i++) {
			if (i <= 3) {
				xShiftPar[i] = transformParams[i];
			} else {
				yShiftPar[i - 4] = transformParams[i];
			}
		}
	}

	@Override
	protected double getXShift(int gridY, int gridX) {
		return Math.sin((xShiftPar[1] * gridX + xShiftPar[2] * gridY + xShiftPar[3]) * multiplierToRad) * xShiftPar[0];
	}

	@Override
	protected double getYShift(int gridY, int gridX) {
		return Math.sin((yShiftPar[1] * gridX + yShiftPar[2] * gridY + yShiftPar[3]) * multiplierToRad) * yShiftPar[0];
	}
}
