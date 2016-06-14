/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.xbase.interpreter;

import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.interpreter.ConstantExpressionEvaluationException;

@SuppressWarnings("all")
public class UnresolvableFeatureException extends ConstantExpressionEvaluationException {
  public UnresolvableFeatureException(final String message, final XExpression expression) {
    super(message, expression);
  }
}
