/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.parsetree;

import java.util.List;

import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.dummy.DummyTestLanguageStandaloneSetup;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.testing.AbstractXtextTests;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author Jan K�hnlein - Initial contribution and API
 */
public class CommentTest extends AbstractXtextTests{

    @Override
    public void setUp() throws Exception {
        super.setUp();
        with(DummyTestLanguageStandaloneSetup.class);
    }

    @Test public void testSingleLineComment() throws Exception {
        String model = "// comment\n/*element foo;\nelement bar;*/";
        INode node = getRootNode(model);
        List<ILeafNode> leafNodes = Lists.newArrayList(node.getLeafNodes());
        assertEquals(2, leafNodes.size());
        assertTrue(leafNodes.get(0).getGrammarElement() instanceof TerminalRule);
        assertTrue(leafNodes.get(1).getGrammarElement() instanceof TerminalRule);
    }
}
