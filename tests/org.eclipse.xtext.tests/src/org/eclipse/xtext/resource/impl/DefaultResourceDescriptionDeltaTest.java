/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.resource.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IReferenceDescription;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

/**
 * @author Sven Efftinge - Initial contribution and API
 */
public class DefaultResourceDescriptionDeltaTest extends TestCase {
	public static QualifiedName FOO = QualifiedName.create("foo");
	public static QualifiedName FOP = QualifiedName.create("fop");
	public static QualifiedName BAR = QualifiedName.create("bar");
	
	@SuppressWarnings("serial")
	static class TestResDesc extends AbstractResourceDescription {

		protected TestResDesc() {
		}

		public List<IEObjectDescription> exported = new ArrayList<IEObjectDescription>() {
			@Override
			public boolean add(IEObjectDescription o) {
				return super.add(o);
			}
		};
		
		public Set<QualifiedName> imported = Sets.newHashSet();

		public Iterable<QualifiedName> getImportedNames() {
			return imported;
		}

		public URI getURI() {
			return URI.createURI("foo://test");
		}

		public Iterable<IReferenceDescription> getReferenceDescriptions() {
			return Collections.emptyList();
		}
		
		@Override
		protected List<IEObjectDescription> computeExportedObjects() {
			return exported;
		}
		
	}
	
	public void testHasChanges_1() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		TestResDesc resourceDesc2 = new TestResDesc();
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_2() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_3() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		resourceDesc2.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_4() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.imported.add(BAR);
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		resourceDesc2.imported.add(BAR);
		resourceDesc2.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_5() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOP);
		resourceDesc2.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_6() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOP);
		resourceDesc2.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_7() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		resourceDesc2.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, null));
		
		assertTrue(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_8() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.exported.add(
				EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		resourceDesc2.exported.add(
				EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", new String("bar"))));
		
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	/** see https://bugs.eclipse.org/bugs/show_bug.cgi?id=356063 */
	public void testHasChanges_9() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.exported.add(
				EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, ImmutableMap.of("foo", "bar", "qux", "quux")));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.exported.add(
				EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, ImmutableMap.of("qux", "quux", "foo", "bar")));
	
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_DifferentTypes() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		resourceDesc2.exported.add(new EObjectDescription(BAR, EcorePackage.Literals.EANNOTATION__CONTENTS, Collections.singletonMap("foo", "bar")){
			@Override
			public URI getEObjectURI() {
				return EcoreUtil.getURI(EcorePackage.Literals.EANNOTATION);
			}
		}
		);
		
		assertTrue(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_DifferentURIs() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		resourceDesc2.exported.add(new EObjectDescription(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")){
			@Override
			public URI getEObjectURI() {
				return super.getEObjectURI().appendFragment("foo");
			}
		}
		);
		
		assertTrue(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanges_MultipleEObjects() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		resourceDesc.exported.add(EObjectDescription.create(FOO, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		resourceDesc2.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		resourceDesc2.exported.add(EObjectDescription.create(FOO, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		
		assertFalse(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
	public void testHasChanged_differentOrder() throws Exception {
		TestResDesc resourceDesc = new TestResDesc();
		resourceDesc.imported.add(FOO);
		resourceDesc.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		resourceDesc.exported.add(EObjectDescription.create(FOO, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		
		TestResDesc resourceDesc2 = new TestResDesc();
		resourceDesc2.imported.add(FOO);
		resourceDesc2.exported.add(EObjectDescription.create(FOO, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		resourceDesc2.exported.add(EObjectDescription.create(BAR, EcorePackage.Literals.EANNOTATION, Collections.singletonMap("foo", "bar")));
		
		assertTrue(new DefaultResourceDescriptionDelta(resourceDesc, resourceDesc2).haveEObjectDescriptionsChanged());
	}
	
}
