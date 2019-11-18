package com.murtaq.parameterParsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads locations of all image files from the source directory on the disk into
 * memory.
 */
class ImageParser {

	/**
	 * Creates a list of files from the given paths representing the location of
	 * the images to be transformed.
	 * 
	 * @param filepaths
	 *            The paths to check.
	 * @return The list of image files.
	 * @throws ParamParseException
	 *             when the file list contains a directory.
	 */
	List<File> parseImageFiles(List<String> filepaths) throws ParamParseException {
		List<File> parsedImages = new ArrayList<File>();
		for (String path : filepaths) {
			File target = readImageOrDir(path);
			if (target.isDirectory()) {
				throw new ParamParseException("Found directory \"" + target.getAbsolutePath()
						+ "\" in input image file list. You can either use one directory or multiple image files as input, but not both.");
			}
			parsedImages.add(target);
		}
		return parsedImages;
	}

	/**
	 * Tests whether the given path points to a directory and searches all image
	 * files if the test succeeds.
	 * 
	 * @param filepath
	 *            The path to be tested and searched.
	 * @param recursive
	 *            Whether the search is recursive.
	 * @return The list of image files that were found.
	 * @throws ParamParseException
	 *             when the filepath parameter does not point to a directory.
	 */
	List<File> parseDirectory(String filepath, boolean recursive) throws ParamParseException {
		File file = readImageOrDir(filepath);
		if (!file.isDirectory()) {
			throw new ParamParseException(
					"ParseDirectory method received non directory \"" + file.getAbsolutePath() + "\" as input.");
		}
		return parseDirectoryFromFile(file, recursive);
	}

	/**
	 * Searches the given directory for all image files.
	 * 
	 * @param sourceDir
	 *            The directory to be searched.
	 * @param recursive
	 *            Whether the search is recursive.
	 * @return The list of images in the directory.
	 */
	private List<File> parseDirectoryFromFile(File sourceDir, boolean recursive) {
		List<File> parsedImages = new ArrayList<File>();
		List<File> parsedSubdirs = new ArrayList<File>();
		File[] sourceFiles = sourceDir.listFiles();
		if (sourceFiles == null) return parsedImages;
		for (File fileInSource : sourceFiles) {
			if (recursive && fileInSource.isDirectory()) {
				parsedSubdirs.add(fileInSource);
			} else if (!fileInSource.isDirectory() && isImageFile(fileInSource)) {
				parsedImages.add(fileInSource);
			}
		}
		for (File dirToParse : parsedSubdirs) {
			parsedImages.addAll(parseDirectoryFromFile(dirToParse, recursive));
		}
		return parsedImages;
	}

	/**
	 * Returns a file object which points to either image file or directory and
	 * is located at the given path.
	 * 
	 * @param filepath
	 *            The path to be tested.
	 * @return The file object.
	 * @throws ParamParseException
	 *             when the path points to neither image file nor directory.
	 */
	File readImageOrDir(String filepath) throws ParamParseException {
		File newFile = new File(filepath);
		if (!newFile.exists()) {
			throw new ParamParseException("No file was found at location \"" + newFile.getAbsolutePath() + "\".");
		}
		if (newFile.isDirectory() || isImageFile(newFile)) {
			return newFile;
		}
		throw new ParamParseException("The file at location \"" + newFile.getAbsolutePath()
				+ "\" was neither recognized as image file nor as directory.");
	}

	/**
	 * Tests whether the file object points to an image file.
	 * 
	 * @param file
	 *            The object to test.
	 * @return Whether the file object points to an image file.
	 */
	boolean isImageFile(File file) {
		String type = "";
		try {
			String filetype = Files.probeContentType(file.toPath());
			type = filetype != null ? filetype.split("/")[0] : "";
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (type.equals("image")) {
			return true;
		}
		return false;
	}
}
