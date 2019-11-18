package com.murtaq.main;

import java.io.File;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * A simple representation of an image.
 */
public class Image {

	private File imageFile;
	private String targetPath;
	private Mat imageData;

	Image(File imageFile, String targetPath, Mat imageData) {
		this.imageFile = imageFile;
		this.targetPath = targetPath;
		this.imageData = imageData;
	}

	public File getFile() {
		return imageFile;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public Mat getImageData() {
		return imageData;
	}

}
