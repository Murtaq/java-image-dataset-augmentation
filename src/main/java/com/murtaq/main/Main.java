package com.murtaq.main;

import com.murtaq.parameterParsing.Parameter;
import com.murtaq.parameterParsing.ParameterStorage;
import com.murtaq.parameterParsing.ParameterParser;
import com.murtaq.transformation.Transformator;

public class Main {

	public static void main(String[] args) {
		ParameterStorage storage = new ParameterParser().parseParameters(args);
		ImageProvider loader = new ImageProvider(storage.getParameter(Parameter.PARSED_IMAGES),
				storage.getParameter(Parameter.SOURCE));
		Transformator tf = new Transformator(storage.getParameter(Parameter.TRANSFORMATIONS),
				storage.getParameter(Parameter.TARGET));
		tf.transformImages(loader);

		System.out.println("Program finished.");
	}

}
