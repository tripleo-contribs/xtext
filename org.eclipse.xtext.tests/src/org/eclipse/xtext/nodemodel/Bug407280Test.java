/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.nodemodel;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.linking.Bug287988TestLanguageStandaloneSetup;
import org.eclipse.xtext.linking.bug287988Test.Model;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.testing.AbstractXtextTests;
import org.junit.Test;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class Bug407280Test extends AbstractXtextTests {
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		with(Bug287988TestLanguageStandaloneSetup.class);
	}
	
	@Test public void testFindActualSemanticObjectFor() throws Exception {
		String modelAsString = "actions attribute ref attr1 attr2;";
		int idx = modelAsString.indexOf("attr1");
		Model model = (Model) getModelAndExpect(modelAsString, 1); /* linking issue */
		INode root = NodeModelUtils.getNode(model).getRootNode();
		ILeafNode leafNodeAtOffset = NodeModelUtils.findLeafNodeAtOffset(root, idx);
		assertEquals("attr1", leafNodeAtOffset.getText());
		
		EObject semanticObject = NodeModelUtils.findActualSemanticObjectFor(leafNodeAtOffset);
		assertSame(model.getAttributes().get(0), semanticObject);
	}
	
}
