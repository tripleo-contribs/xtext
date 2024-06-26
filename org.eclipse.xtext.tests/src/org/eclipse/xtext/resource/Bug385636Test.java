/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.resource;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.bug385636.Program;
import org.eclipse.xtext.testing.AbstractXtextTests;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Holger Schill - Initial contribution and API
 */
public class Bug385636Test extends AbstractXtextTests {

	private DefaultLocationInFileProvider locationInFileProvider;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		with(Bug385636StandaloneSetup.class);
		locationInFileProvider = new DefaultLocationInFileProvider();
	}

	@Test
	public void testNPECausedBySyntaxError() throws Exception{
		Program model = (Program) getResourceFor(getAsStream("VARIABLES OTHER TEST2 ENDVARIABLES END")).getContents().get(0);
		TreeIterator<EObject> allContents = model.eAllContents();
		while(allContents.hasNext()){
			EObject next = allContents.next();
			locationInFileProvider.getFullTextRegion(next);
		}
	}
}
