/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.generator.grammarAccess;

import org.eclipse.xtext.util.Wrapper;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@Deprecated
public class ResourceSaveIndicator extends Wrapper<Boolean> {

	public ResourceSaveIndicator() {
		super(Boolean.FALSE);
	}

	public boolean isSaving() {
		return get().booleanValue();
	}

}
