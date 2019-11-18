package com.murtaq.transformation;

import java.util.HashMap;
import java.util.Map;

import com.murtaq.parameterParsing.ParamParseException;
import com.murtaq.transformation.creators.FlipCreator;
import com.murtaq.transformation.creators.RandomTransformCreator;
import com.murtaq.transformation.creators.SineTransformCreator;
import com.murtaq.transformation.creators.TransformCreator;
import com.murtaq.transformations.ITransform;

/**
 * Statically used class to provide a single point of contact for the creation
 * of transformations.
 */
public class TransformFactory {

	private static Map<String, TransformCreator> creators;

	/*
	 * Initialization of all transform creators.
	 * TODO: Fill in newly created transform creators
	 */
	static {
		creators = new HashMap<String, TransformCreator>();

		creators.put("flip", new FlipCreator());
		creators.put("random", new RandomTransformCreator());
		creators.put("sine", new SineTransformCreator());
	}

	public static String getTransformDescriptions() {
		String desc = "";
		for (Map.Entry<String, TransformCreator> entry : creators.entrySet()) {
			desc = desc + "\n" + entry.getValue().getDesc();
		}
		return desc;
	}

	/**
	 * Creates a ITransform from the given parameter strings.
	 * 
	 * @param name
	 *            The type of the transformation.
	 * @param args
	 *            The transformation arguments.
	 * @return The created transformation
	 * @throws ParamParseException
	 *             when the name does not correspond to a known transformation
	 *             or the arguments don't match the transformations' template
	 */
	public static ITransform create(String name, String... args) throws ParamParseException {
		ITransform transform = creators.get(name.toLowerCase()).create(args);
		if (transform == null) {
			throw new ParamParseException("The transformation \"" + name + "\" does not exist.");
		}
		return transform;
	}

}
