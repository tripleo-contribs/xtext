/**
 * Copyright (c) 2014, 2022 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.xtext.xbase.formatting2;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeConstraint;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmWildcardTypeReference;
import org.eclipse.xtext.formatting2.AbstractJavaFormatter;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IHiddenRegionFormatter;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.XbaseGenerated;
import org.eclipse.xtext.xtype.XFunctionTypeRef;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;
import org.eclipse.xtext.xtype.XtypePackage;

/**
 * @author Moritz Eysholdt - Initial implementation and API
 */
@SuppressWarnings("all")
public class XtypeFormatter extends AbstractJavaFormatter {
  protected void _format(final XFunctionTypeRef func, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.append(this.regionFor(func).keyword("("), _function);
    EList<JvmTypeReference> _paramTypes = func.getParamTypes();
    for (final JvmTypeReference param : _paramTypes) {
      {
        document.<JvmTypeReference>format(param);
        final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        document.append(document.prepend(this.immediatelyFollowing(param).keyword(","), _function_1), _function_2);
      }
    }
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      boolean _isEmpty = func.getParamTypes().isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        it.noSpace();
      }
    };
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.append(document.prepend(this.regionFor(func).keyword(")"), _function_1), _function_2);
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.append(this.regionFor(func).keyword("=>"), _function_3);
    document.<JvmTypeReference>format(func.getReturnType());
  }

  protected void _format(final JvmParameterizedTypeReference ref, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.surround(this.regionFor(ref).keyword("<"), _function);
    EList<JvmTypeReference> _arguments = ref.getArguments();
    for (final JvmTypeReference arg : _arguments) {
      {
        document.<JvmTypeReference>format(arg);
        final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        document.append(document.prepend(this.immediatelyFollowing(arg).keyword(","), _function_1), _function_2);
      }
    }
    boolean _isEmpty = ref.getArguments().isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      document.prepend(this.regionFor(ref).keyword(">"), _function_1);
    }
  }

  protected void _format(final JvmWildcardTypeReference ref, @Extension final IFormattableDocument document) {
    boolean _isEmpty = ref.getConstraints().isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.oneSpace();
      };
      document.append(this.regionFor(ref).keyword("?"), _function);
    }
    EList<JvmTypeConstraint> _constraints = ref.getConstraints();
    for (final JvmTypeConstraint c : _constraints) {
      document.<JvmTypeConstraint>format(c);
    }
  }

  protected void _format(final JvmTypeParameter ref, @Extension final IFormattableDocument document) {
    EList<JvmTypeConstraint> _constraints = ref.getConstraints();
    for (final JvmTypeConstraint c : _constraints) {
      {
        final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        document.<JvmTypeConstraint>prepend(c, _function);
        document.<JvmTypeConstraint>format(c);
      }
    }
  }

  protected void _format(final XImportSection section, @Extension final IFormattableDocument format) {
    EList<XImportDeclaration> _importDeclarations = section.getImportDeclarations();
    for (final XImportDeclaration imp : _importDeclarations) {
      {
        format.<XImportDeclaration>format(imp);
        XImportDeclaration _lastOrNull = IterableExtensions.<XImportDeclaration>lastOrNull(section.getImportDeclarations());
        boolean _notEquals = (!Objects.equals(imp, _lastOrNull));
        if (_notEquals) {
          format.<XImportDeclaration>append(imp, XbaseFormatterPreferenceKeys.blankLinesBetweenImports);
        } else {
          format.<XImportDeclaration>append(imp, XbaseFormatterPreferenceKeys.blankLinesAfterImports);
        }
      }
    }
  }

  protected void _format(final XImportDeclaration imp, @Extension final IFormattableDocument document) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    document.append(this.regionFor(imp).keyword("import"), _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    document.append(this.regionFor(imp).feature(XtypePackage.Literals.XIMPORT_DECLARATION__STATIC), _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    document.append(this.regionFor(imp).feature(XtypePackage.Literals.XIMPORT_DECLARATION__EXTENSION), _function_2);
    List<ISemanticRegion> _keywords = this.regionFor(imp).keywords(".");
    for (final ISemanticRegion node : _keywords) {
      final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      document.surround(node, _function_3);
    }
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.prepend(this.regionFor(imp).keyword(";"), _function_4);
  }

  @XbaseGenerated
  public void format(final Object ref, final IFormattableDocument document) {
    if (ref instanceof JvmTypeParameter) {
      _format((JvmTypeParameter)ref, document);
      return;
    } else if (ref instanceof XtextResource) {
      _format((XtextResource)ref, document);
      return;
    } else if (ref instanceof XFunctionTypeRef) {
      _format((XFunctionTypeRef)ref, document);
      return;
    } else if (ref instanceof JvmParameterizedTypeReference) {
      _format((JvmParameterizedTypeReference)ref, document);
      return;
    } else if (ref instanceof JvmWildcardTypeReference) {
      _format((JvmWildcardTypeReference)ref, document);
      return;
    } else if (ref instanceof XImportDeclaration) {
      _format((XImportDeclaration)ref, document);
      return;
    } else if (ref instanceof XImportSection) {
      _format((XImportSection)ref, document);
      return;
    } else if (ref instanceof EObject) {
      _format((EObject)ref, document);
      return;
    } else if (ref == null) {
      _format((Void)null, document);
      return;
    } else if (ref != null) {
      _format(ref, document);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ref, document).toString());
    }
  }
}
