/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.generator.parser.antlr.ex.common;

import java.util.Set;

import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.xtext.generator.grammarAccess.GrammarAccessExtensions;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 * @author Heiko Behrens
 */
@Deprecated
public class KeywordHelper extends org.eclipse.xtext.xtext.generator.parser.antlr.KeywordHelper {

	public KeywordHelper(Grammar grammar, boolean ignoreCase) {
		super(grammar, ignoreCase, new GrammarAccessExtensions());
	}

	@Override
	public void discardHelper(Grammar grammar) {
		super.discardHelper(grammar);
	}

	@Override
	public String getRuleName(String keywordValue) {
		return super.getRuleName(keywordValue);
	}

	@Override
	public String getKeywordValue(String ruleName) {
		return super.getKeywordValue(ruleName);
	}

	@Override
	public boolean isKeywordRule(String ruleName) {
		return super.isKeywordRule(ruleName);
	}

	@Override
	public Set<String> getAllKeywords() {
		return super.getAllKeywords();
	}
	
}
