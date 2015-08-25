/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xtext.generator.parser.antlr.postProcessing;

public class SuppressWarningsProcessor {

	public String process(String content) {
		String result = content.replaceFirst("public class ", "@SuppressWarnings(\"all\")\n" +
				"public class ");
		return result;
	}

}
