package com.murtaq.main;

import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_UNCHANGED;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Custom Iterator that provides the images to be processed by the
 * transformation pipeline.
 */
public class ImageProvider implements Iterator<Image> {

	private List<File> imageFiles;
	private File sourceFolder;

	ImageProvider(File[] imageFiles, File sourceFolder) {
		this.imageFiles = new ArrayList<File>(Arrays.asList(imageFiles));
		this.sourceFolder = sourceFolder;
	}

	public boolean hasNext() {
		return imageFiles.size() > 0;
	}

	public Image next() {
		if (imageFiles.size() <= 0) {
			throw new NoSuchElementException();
		}
		File currentImage = imageFiles.get(0);
		String targetPath = "";
		if (sourceFolder != null) {
			targetPath = Paths.get(sourceFolder.getAbsolutePath())
					.relativize(Paths.get(currentImage.getAbsolutePath()).getParent()).toString();
		}
		Image nextImage = new Image(currentImage, targetPath, imread(currentImage.getAbsolutePath(), CV_LOAD_IMAGE_UNCHANGED));
		imageFiles.remove(0);
		return nextImage;
	}

}
