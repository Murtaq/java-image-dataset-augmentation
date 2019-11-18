package com.murtaq.transformation;

import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;

import com.murtaq.main.Image;
import com.murtaq.main.ImageProvider;
import com.murtaq.transformations.ITransform;

/**
 * Transforms images.
 */
public class Transformator {

	private List<ITransform> transforms;
	private File targetFile;

	public Transformator() {
		transforms = new ArrayList<ITransform>();
		targetFile = new File("");
	}

	public Transformator(ITransform[] transforms, File targetFile) {
		this.targetFile = targetFile;
		this.transforms = new ArrayList<ITransform>(Arrays.asList(transforms));
	}

	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}

	/**
	 * Transforms all images provided by the ImageProvider and stores them on
	 * the disk at their assigned location.
	 * 
	 * @param provider
	 *            The provider for the images.
	 */
	public void transformImages(ImageProvider provider) {
		System.out.println("Transforming images...");
		while (provider.hasNext()) {
			Image image = provider.next();
			Mat transformedImage = applyTransformations(image.getImageData());

			String target;
			File f = image.getFile();
			if (targetFile.exists()) {
				target = targetFile.getPath() + File.separator + image.getTargetPath() + File.separator + f.getName();
				File parent = new File(target).getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new IllegalStateException("Couldn't create dir: " + parent);
				}
			} else {
				String[] filename = f.getName().split("\\.");
				target = f.getParentFile().getAbsolutePath() + File.separator + filename[0] + "_tf." + filename[1];
			}
			imwrite(target, transformedImage);
		}
		System.out.println("Done.");
	}

	/**
	 * Applies all transformations in the transforms list on the image.
	 * 
	 * @param image
	 *            The image to transform.
	 * @return The transformed image.
	 */
	private Mat applyTransformations(Mat image) {
		for (ITransform tf : transforms) {
			image = tf.applyOn(image);
		}
		return image;
	}

	/**
	 * Override of the toString() method to display the contained
	 * transformations.
	 */
	@Override
	public String toString() {
		String desc = "Transformator: [";
		for (int i = 0; i < transforms.size(); i++) {
			desc += transforms.get(i).toString();
			if (i < transforms.size() - 1) {
				desc += " -> ";
			}
		}
		desc += "]";
		return desc;
	}

}
