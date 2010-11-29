/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.tests.parser;

import java.util.List;

import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.xbase.XAssignment;
import org.eclipse.xtext.xbase.XBinaryOperation;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XbasePackage;
import org.eclipse.xtext.xbase.tests.AbstractXbaseTestCase;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class XbaseNodeModelTest extends AbstractXbaseTestCase {

	@Override
	protected XExpression expression(String string) throws Exception {
		return super.expression(string, false);
	}
	
	public void testAssignment_rhs_01() throws Exception {
		String text = "a=b";
		XAssignment assignment = (XAssignment) expression(text);
		List<INode> nodesForFeature = NodeModelUtils.findNodesForFeature(assignment, XbasePackage.Literals.XASSIGNMENT__VALUE);
		assertEquals(1, nodesForFeature.size());
		String nodeText = nodesForFeature.get(0).getText();
		assertEquals("b", nodeText);
	}
	
	public void testAssignment_rhs_02() throws Exception {
		String text = "a=b+=c";
		XAssignment assignment = (XAssignment) expression(text);
		List<INode> nodesForFeature = NodeModelUtils.findNodesForFeature(assignment, XbasePackage.Literals.XASSIGNMENT__VALUE);
		assertEquals(1, nodesForFeature.size());
		String nodeText = nodesForFeature.get(0).getText();
		assertEquals("b+=c", nodeText);
	}
	
	public void testAssignment_rhs_03() throws Exception {
		String text = "a=b+=c";
		XAssignment assignment = (XAssignment) ((XAssignment) expression(text)).getValue();
		List<INode> nodesForFeature = NodeModelUtils.findNodesForFeature(assignment, XbasePackage.Literals.XASSIGNMENT__VALUE);
		assertEquals(1, nodesForFeature.size());
		String nodeText = nodesForFeature.get(0).getText();
		assertEquals("c", nodeText);
	}
	
	public void testAssignment_rhs_04() throws Exception {
		String text = "a=b+=c";
		XAssignment assignment = (XAssignment) ((XAssignment) expression(text)).getValue();
		List<INode> nodesForFeature = NodeModelUtils.findNodesForFeature(assignment, XbasePackage.Literals.XASSIGNMENT__ASSIGNABLE);
		assertEquals(1, nodesForFeature.size());
		String nodeText = nodesForFeature.get(0).getText();
		assertEquals("b", nodeText);
	}
	
	public void testAssignment_rhs_05() throws Exception {
		String text = "a = b += c = d";
		XAssignment assignment = (XAssignment) expression(text);
		List<INode> nodesForFeature = NodeModelUtils.findNodesForFeature(assignment, XbasePackage.Literals.XASSIGNMENT__VALUE);
		assertEquals(1, nodesForFeature.size());
		String nodeText = nodesForFeature.get(0).getText();
		assertEquals(" b += c = d", nodeText);
	}
	
	public void testBinaryOperation_01() throws Exception {
		String text = "a+b+c+d";
		XBinaryOperation operation = (XBinaryOperation) expression(text);
		List<INode> nodesForFeature = NodeModelUtils.findNodesForFeature(operation, XbasePackage.Literals.XBINARY_OPERATION__LEFT_OPERAND);
		assertEquals(1, nodesForFeature.size());
		String nodeText = nodesForFeature.get(0).getText();
		assertEquals("a+b+c", nodeText);
	}
	
	public void testBinaryOperation_02() throws Exception {
		String text = "a+b+c+d";
		XBinaryOperation operation = (XBinaryOperation) expression(text);
		List<INode> nodesForFeature = NodeModelUtils.findNodesForFeature(operation, XbasePackage.Literals.XBINARY_OPERATION__RIGHT_OPERAND);
		assertEquals(1, nodesForFeature.size());
		String nodeText = nodesForFeature.get(0).getText();
		assertEquals("d", nodeText);
	}
	
	public void testBinaryOperation_03() throws Exception {
		String text = "a-b-c+d";
		XBinaryOperation operation = (XBinaryOperation) expression(text);
		List<INode> nodesForFeature = NodeModelUtils.findNodesForFeature(operation, XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE);
		assertEquals(1, nodesForFeature.size());
		String nodeText = nodesForFeature.get(0).getText();
		assertEquals("+", nodeText);
	}
	
}
