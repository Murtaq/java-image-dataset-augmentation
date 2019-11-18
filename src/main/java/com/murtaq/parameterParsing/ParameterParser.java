package com.murtaq.parameterParsing;

import static com.murtaq.parameterParsing.Parameter.HELP;
import static com.murtaq.parameterParsing.Parameter.HELPTF;
import static com.murtaq.parameterParsing.Parameter.PARSED_IMAGES;
import static com.murtaq.parameterParsing.Parameter.RECURSION;
import static com.murtaq.parameterParsing.Parameter.SOURCE;
import static com.murtaq.parameterParsing.Parameter.TARGET;
import static com.murtaq.parameterParsing.Parameter.TRANSFORMATIONS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.murtaq.transformation.TransformFactory;
import com.murtaq.transformations.ITransform;

/**
 * Transforms the command line arguments to Java objects and returns them stored
 * in a ParameterManager. Also notifies the user of incorrect input and cancels
 * program execution.
 */
public class ParameterParser {

	private List<String[]> parameterList;

	private ParameterStorage parman;

	/**
	 * Transforms the command line arguments into Java objects and stores them
	 * in a ParameterManager. May exit the program when user input is incorrect.
	 * 
	 * @param args
	 *            The arguments to be processed.
	 * @return The ParameterManager storing the generated Java objects.
	 */
	public ParameterStorage parseParameters(String[] args) {
		parman = new ParameterStorage();
		parameterList = splitIntoParameters(args);

		try {
			if (isBooleanParameterSet(HELP)) {
				displayHelpAndExit();
			}
			if (isBooleanParameterSet(HELPTF)) {
				displayTfHelpAndExit();
			}
			initImageFiles(isBooleanParameterSet(RECURSION));
			initTarget();
			initTransformations();
		} catch (ParamParseException e) {
			System.out.println("\nThe following error occurred while parsing parameters:");
			System.out.println("    " + e.getMessage() + "\n");
			System.out.println("Use the help parameter " + HELP.namesString + " for more information on parameters.");
			System.out.println("For help with transformations use " + HELPTF.namesString + ".");
			System.exit(1);
		}
		return parman;
	}

	/**
	 * Reads all contained images at the source location given in the command
	 * line and stores them in the ParameterManager.
	 * 
	 * @param recursive
	 *            Whether the search in the given location is recursive.
	 * @throws ParamParseException
	 *             when the source file is missing or.
	 */
	private void initImageFiles(boolean recursive) throws ParamParseException {
		List<String> filepaths = readStringParameter(SOURCE);
		if (filepaths.size() == 0) {
			throw new ParamParseException(
					"No transformation source file was specified using " + SOURCE.namesString + ". This is always required.");
		}
		ImageParser imageParser = new ImageParser();
		List<File> parsedImageFiles = new ArrayList<File>();
		parman.putParameter(SOURCE, new File(""));
		if (filepaths.size() == 1) {
			File parsedFile = imageParser.readImageOrDir(filepaths.get(0));
			if (parsedFile.isDirectory()) {
				parman.putParameter(SOURCE, parsedFile);
				parsedImageFiles = imageParser.parseDirectory(filepaths.get(0), recursive);
			} else {
				parsedImageFiles.add(parsedFile);
			}
		} else {
			parsedImageFiles = imageParser.parseImageFiles(filepaths);
		}
		parman.putParameter(PARSED_IMAGES, parsedImageFiles.toArray(new File[parsedImageFiles.size()]));

		System.out.print("Found " + parsedImageFiles.size() + " image files.");
		if (!recursive && parsedImageFiles.size() == 0) {
			System.out.println(" This may be the case because you forgot the " + RECURSION.namesString + " flag.");
		} else {
			System.out.print("\n");
		}
	}

	/**
	 * Initializes the target parameter after verifying its validity.
	 * 
	 * @throws ParamParseException
	 *             when the target is not valid (multiple targets, doesn't exist
	 *             or not a directory or image file).
	 */
	private void initTarget() throws ParamParseException {
		List<String> targetPaths = readStringParameter(TARGET);
		if (targetPaths.size() > 1) {
			throw new ParamParseException(
					"You can only specify a single target file, which must be either directory or image file.");
		}
		if (targetPaths.size() == 0) {
			System.out.println("No output file for the transformed images was specified using " + TARGET.namesString
					+ ". Transformed images will be added to their source file directories.");
			parman.putParameter(TARGET, new File(""));
			return;
		}
		File target = new File(targetPaths.get(0));
		if (!target.exists()) {
			throw new ParamParseException("The specified target file \"" + target.getAbsolutePath() + "\" does not exist.");
		}
		if (target.isDirectory() || new ImageParser().isImageFile(target)) {
			parman.putParameter(TARGET, target);
		} else {
			throw new ParamParseException(
					"The specified target file \"" + target.getAbsolutePath() + "\" is neither an image file nor a directory.");
		}
	}

	/**
	 * Initializes the transformations parameter using the TransformParser.
	 * 
	 * @throws ParamParseException
	 *             when no transformations were specified or an error occurs
	 *             during transform parsing.
	 */
	private void initTransformations() throws ParamParseException {
		TransformParser tfParser = new TransformParser();
		List<ITransform> transformations = tfParser.parseTransforms(readStringParameter(TRANSFORMATIONS), ":");
		if (transformations.size() == 0) {
			throw new ParamParseException(
					"No transformations were specified using " + TRANSFORMATIONS.namesString + ". All images left unchanged.");
		}
		parman.putParameter(TRANSFORMATIONS, transformations.toArray(new ITransform[transformations.size()]));
	}

	/**
	 * Splits the command line arguments in multiple lists (one for each
	 * parameter) and returns them.
	 * 
	 * @param args
	 *            The command line arguments.
	 * @return The lists consisting of parameter name and respective arguments.
	 */
	private List<String[]> splitIntoParameters(String[] args) {
		List<String[]> parameterList = new ArrayList<String[]>();
		List<String> newParameter = new ArrayList<String>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				newParameter.clear();
				newParameter.add(args[i]);
				while (i + 1 < args.length && !args[i + 1].startsWith("-")) {
					i++;
					newParameter.add(args[i]);
				}
				parameterList.add(newParameter.toArray(new String[newParameter.size()]));
			}
		}
		return parameterList;
	}

	/**
	 * Checks the parameter list for a boolean parameter.
	 * 
	 * @param param
	 *            The parameter to check for.
	 * @return Whether the parameter is set in the parameter list.
	 * @throws ParamParseException
	 *             when the user has added arguments to the boolean parameter.
	 */
	private boolean isBooleanParameterSet(Parameter param) throws ParamParseException {
		for (String[] parameter : parameterList) {
			for (String parName : param.names) {
				if (parameter[0].equals(parName)) {
					if (parameter.length > 1) {
						throw new ParamParseException("Parameters of type boolean don't have options. Your input for "
								+ parameter[0] + " had " + (parameter.length - 1) + "option(s).");
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Searches the parameter list for a specific parameter and returns the
	 * affiliated strings.
	 * 
	 * @param param
	 *            The parameter to filter.
	 * @return The extracted text from the command line arguments.
	 */
	private List<String> readStringParameter(Parameter param) {
		List<String> result = new ArrayList<String>();
		for (String[] parameter : parameterList) {
			for (String filterParam : param.names) {
				if (parameter[0].equals(filterParam)) {
					for (int i = 1; i < parameter.length; i++) {
						result.add(parameter[i]);
					}
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * Prints the help and exits the program.
	 */
	private void displayHelpAndExit() {
		System.out.println();
		System.out.println("The following parameters are required for a transformation:");
		printParam(SOURCE);
		printParam(TRANSFORMATIONS);
		System.out.println("The following parameters are optional:");
		printParam(TARGET);
		printParam(RECURSION);
		printParam(HELP);
		printParam(HELPTF);
		System.exit(0);
	}

	/**
	 * Prints the parameter and its description.
	 */
	private void printParam(Parameter param) {
		System.out.println("    " + param.namesString + ": " + param.description);
	}

	/**
	 * Prints the help for parameters and exits the program.
	 */
	private void displayTfHelpAndExit() {
		System.out.println();
		System.out.println(
				"Parameters for the transformation functions are passed by appending a colon to the parameter, i.e. random:5:2");
		System.out.println("Multiple transforms can be chained together by separating them with a space, i.e. random:5:2 flip:v");
		System.out.println(TransformFactory.getTransformDescriptions());
		System.exit(0);
	}
}
