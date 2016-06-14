/**
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.xbase.formatting2;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericArrayTypeReference;
import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;
import org.eclipse.xtext.common.types.JvmTypeConstraint;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmWildcardTypeReference;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IFormattableSubDocument;
import org.eclipse.xtext.formatting2.IHiddenRegionFormatter;
import org.eclipse.xtext.formatting2.ISubFormatter;
import org.eclipse.xtext.formatting2.regionaccess.IEObjectRegion;
import org.eclipse.xtext.formatting2.regionaccess.IHiddenRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionFinder;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegionsFinder;
import org.eclipse.xtext.formatting2.regionaccess.ITextRegionAccess;
import org.eclipse.xtext.formatting2.regionaccess.ITextSegment;
import org.eclipse.xtext.formatting2.regionaccess.internal.TextSegment;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.XAbstractFeatureCall;
import org.eclipse.xtext.xbase.XAssignment;
import org.eclipse.xtext.xbase.XBasicForLoopExpression;
import org.eclipse.xtext.xbase.XBinaryOperation;
import org.eclipse.xtext.xbase.XBlockExpression;
import org.eclipse.xtext.xbase.XCasePart;
import org.eclipse.xtext.xbase.XCastedExpression;
import org.eclipse.xtext.xbase.XCatchClause;
import org.eclipse.xtext.xbase.XClosure;
import org.eclipse.xtext.xbase.XCollectionLiteral;
import org.eclipse.xtext.xbase.XConstructorCall;
import org.eclipse.xtext.xbase.XDoWhileExpression;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.XForLoopExpression;
import org.eclipse.xtext.xbase.XIfExpression;
import org.eclipse.xtext.xbase.XInstanceOfExpression;
import org.eclipse.xtext.xbase.XMemberFeatureCall;
import org.eclipse.xtext.xbase.XPostfixOperation;
import org.eclipse.xtext.xbase.XReturnExpression;
import org.eclipse.xtext.xbase.XSwitchExpression;
import org.eclipse.xtext.xbase.XSynchronizedExpression;
import org.eclipse.xtext.xbase.XThrowExpression;
import org.eclipse.xtext.xbase.XTryCatchFinallyExpression;
import org.eclipse.xtext.xbase.XTypeLiteral;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.XWhileExpression;
import org.eclipse.xtext.xbase.XbasePackage;
import org.eclipse.xtext.xbase.formatting2.ArrayBracketsFormattingReplacer;
import org.eclipse.xtext.xbase.formatting2.IndentOnceAutowrapFormatter;
import org.eclipse.xtext.xbase.formatting2.ObjectEntry;
import org.eclipse.xtext.xbase.formatting2.SeparatorEntry;
import org.eclipse.xtext.xbase.formatting2.SeparatorRegions;
import org.eclipse.xtext.xbase.formatting2.XbaseFormatterPreferenceKeys;
import org.eclipse.xtext.xbase.formatting2.XtypeFormatter;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.services.XbaseGrammarAccess;
import org.eclipse.xtext.xtype.XFunctionTypeRef;
import org.eclipse.xtext.xtype.XImportDeclaration;
import org.eclipse.xtext.xtype.XImportSection;

/**
 * @author Moritz Eysholdt - Initial implementation and API
 * @author Lorenzo Bettini - https://bugs.eclipse.org/bugs/show_bug.cgi?id=471239
 */
@SuppressWarnings("all")
public class XbaseFormatter extends XtypeFormatter {
  @Inject
  @Accessors(AccessorType.PUBLIC_GETTER)
  @Extension
  private XbaseGrammarAccess grammar;
  
  protected void _format(final XCollectionLiteral literal, @Extension final IFormattableDocument document) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(literal);
    ISemanticRegion _keyword = _regionFor.keyword("#");
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    document.append(_keyword, _function);
    ISemanticRegion _elvis = null;
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(literal);
    ISemanticRegion _keyword_1 = _regionFor_1.keyword("[");
    if (_keyword_1 != null) {
      _elvis = _keyword_1;
    } else {
      ISemanticRegionsFinder _regionFor_2 = this.textRegionExtensions.regionFor(literal);
      ISemanticRegion _keyword_2 = _regionFor_2.keyword("{");
      _elvis = _keyword_2;
    }
    final ISemanticRegion open = _elvis;
    ISemanticRegion _elvis_1 = null;
    ISemanticRegionsFinder _regionFor_3 = this.textRegionExtensions.regionFor(literal);
    ISemanticRegion _keyword_3 = _regionFor_3.keyword("]");
    if (_keyword_3 != null) {
      _elvis_1 = _keyword_3;
    } else {
      ISemanticRegionsFinder _regionFor_4 = this.textRegionExtensions.regionFor(literal);
      ISemanticRegion _keyword_4 = _regionFor_4.keyword("}");
      _elvis_1 = _keyword_4;
    }
    final ISemanticRegion close = _elvis_1;
    EList<XExpression> _elements = literal.getElements();
    this.formatCommaSeparatedList(_elements, open, close, document);
  }
  
  protected void formatCommaSeparatedList(final Collection<? extends EObject> elements, final ISemanticRegion open, final ISemanticRegion close, @Extension final IFormattableDocument format) {
    if ((Objects.equal(close, null) || Objects.equal(open, null))) {
    } else {
      boolean _isEmpty = elements.isEmpty();
      if (_isEmpty) {
        final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        format.append(open, _function);
      } else {
        IHiddenRegion _previousHiddenRegion = close.getPreviousHiddenRegion();
        boolean _isMultiline = _previousHiddenRegion.isMultiline();
        if (_isMultiline) {
          final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
            it.newLine();
          };
          format.append(open, _function_1);
          for (final EObject elem : elements) {
            {
              format.<EObject>format(elem);
              ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(elem);
              ISemanticRegion _keyword = _immediatelyFollowing.keyword(",");
              final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
                it.noSpace();
              };
              ISemanticRegion _prepend = format.prepend(_keyword, _function_2);
              final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
                it.newLine();
              };
              format.append(_prepend, _function_3);
            }
          }
          EObject _last = IterableExtensions.last(elements);
          final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
            it.newLine();
          };
          format.<EObject>append(_last, _function_2);
          final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
            it.indent();
          };
          format.<ISemanticRegion, ISemanticRegion>interior(open, close, _function_3);
        } else {
          IHiddenRegion _previousHiddenRegion_1 = close.getPreviousHiddenRegion();
          final IndentOnceAutowrapFormatter indent = new IndentOnceAutowrapFormatter(_previousHiddenRegion_1);
          ITextRegionAccess _textRegionAccess = this.getTextRegionAccess();
          int _endOffset = open.getEndOffset();
          int _offset = close.getOffset();
          int _endOffset_1 = open.getEndOffset();
          int _minus = (_offset - _endOffset_1);
          final TextSegment region = new TextSegment(_textRegionAccess, _endOffset, _minus);
          final SeparatorRegions<EObject, ISemanticRegion> items = new SeparatorRegions<EObject, ISemanticRegion>(region);
          for (final EObject ele : elements) {
            ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(ele);
            ISemanticRegion _keyword = _immediatelyFollowing.keyword(",");
            items.appendWithTrailingSeparator(ele, _keyword);
          }
          for (final ObjectEntry<EObject, ISemanticRegion> ele_1 : items) {
            {
              SeparatorEntry<EObject, ISemanticRegion> _leadingSeparator = ele_1.getLeadingSeparator();
              ISemanticRegion _separator = null;
              if (_leadingSeparator!=null) {
                _separator=_leadingSeparator.getSeparator();
              }
              final ISemanticRegion sep = _separator;
              EObject _object = ele_1.getObject();
              boolean _prependNewLineIfMultiline = this.prependNewLineIfMultiline(_object);
              if (_prependNewLineIfMultiline) {
                boolean _equals = Objects.equal(sep, null);
                if (_equals) {
                  final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
                    it.noSpace();
                    ITextSegment _region = ele_1.getRegion();
                    int _length = _region.getLength();
                    it.autowrap(_length);
                    it.setOnAutowrap(indent);
                  };
                  format.append(open, _function_4);
                } else {
                  final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
                    it.oneSpace();
                    ITextSegment _region = ele_1.getRegion();
                    int _length = _region.getLength();
                    it.autowrap(_length);
                    it.setOnAutowrap(indent);
                  };
                  format.append(sep, _function_5);
                }
              } else {
                final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
                  it.oneSpace();
                };
                format.append(sep, _function_6);
              }
              final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it) -> {
                it.noSpace();
              };
              format.prepend(sep, _function_7);
              EObject _object_1 = ele_1.getObject();
              format.<EObject>format(_object_1);
            }
          }
          final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          format.prepend(close, _function_4);
        }
      }
    }
  }
  
  protected void _format(final JvmGenericArrayTypeReference array, @Extension final IFormattableDocument document) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(array);
    ParserRule _arrayBracketsRule = this.grammar.getArrayBracketsRule();
    ISemanticRegion _ruleCallTo = _regionFor.ruleCallTo(_arrayBracketsRule);
    ArrayBracketsFormattingReplacer _arrayBracketsFormattingReplacer = new ArrayBracketsFormattingReplacer(_ruleCallTo);
    document.addReplacer(_arrayBracketsFormattingReplacer);
    JvmTypeReference _componentType = array.getComponentType();
    document.<JvmTypeReference>format(_componentType);
  }
  
  protected void _format(final JvmTypeConstraint constraint, @Extension final IFormattableDocument document) {
    JvmTypeReference _typeReference = constraint.getTypeReference();
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    JvmTypeReference _prepend = document.<JvmTypeReference>prepend(_typeReference, _function);
    document.<JvmTypeReference>format(_prepend);
  }
  
  protected void _format(final XVariableDeclaration expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("val");
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.append(_keyword, _function);
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword_1 = _regionFor_1.keyword("var");
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.append(_keyword_1, _function_1);
    JvmTypeReference _type = expr.getType();
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.<JvmTypeReference>append(_type, _function_2);
    ISemanticRegionsFinder _regionFor_2 = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword_2 = _regionFor_2.keyword("=");
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.surround(_keyword_2, _function_3);
    JvmTypeReference _type_1 = expr.getType();
    format.<JvmTypeReference>format(_type_1);
    XExpression _right = expr.getRight();
    format.<XExpression>format(_right);
  }
  
  protected void _format(final XAssignment expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ParserRule _opSingleAssignRule = this.grammar.getOpSingleAssignRule();
    ISemanticRegion _ruleCallTo = _regionFor.ruleCallTo(_opSingleAssignRule);
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.surround(_ruleCallTo, _function);
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
    String _xifexpression = null;
    boolean _isExplicitStatic = expr.isExplicitStatic();
    if (_isExplicitStatic) {
      _xifexpression = "::";
    } else {
      _xifexpression = ".";
    }
    ISemanticRegion _keyword = _regionFor_1.keyword(_xifexpression);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    format.surround(_keyword, _function_1);
    XExpression _assignable = expr.getAssignable();
    format.<XExpression>format(_assignable);
    XExpression _value = expr.getValue();
    format.<XExpression>format(_value);
  }
  
  protected void formatFeatureCallParams(final List<XExpression> params, final ISemanticRegion open, final ISemanticRegion close, @Extension final IFormattableDocument format) {
    final XClosure builder = this.builder(params);
    final Iterable<XExpression> explicitParams = this.explicitParams(params);
    List<XExpression> _list = IterableExtensions.<XExpression>toList(explicitParams);
    this.formatCommaSeparatedList(_list, open, close, format);
    this.formatBuilderWithLeadingGap(builder, format);
  }
  
  protected void formatBuilderWithLeadingGap(final XClosure closure, @Extension final IFormattableDocument format) {
    boolean _notEquals = (!Objects.equal(closure, null));
    if (_notEquals) {
      IHiddenRegion _previousHiddenRegion = this.textRegionExtensions.previousHiddenRegion(closure);
      final int offset = _previousHiddenRegion.getOffset();
      IHiddenRegion _nextHiddenRegion = this.textRegionExtensions.nextHiddenRegion(closure);
      int _offset = _nextHiddenRegion.getOffset();
      final int length = (_offset - offset);
      final ISubFormatter _function = (IFormattableSubDocument doc) -> {
        @Extension
        final IFormattableSubDocument it = doc.requireFitsInLine();
        final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it_1) -> {
          it_1.noSpace();
        };
        it.<XClosure>prepend(closure, _function_1);
        it.<XClosure>format(closure);
      };
      final ISubFormatter _function_1 = (IFormattableSubDocument it) -> {
        final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it_1) -> {
          it_1.oneSpace();
        };
        it.<XClosure>prepend(closure, _function_2);
        it.<XClosure>format(closure);
      };
      format.formatConditionally(offset, length, _function, _function_1);
    }
  }
  
  protected XClosure builder(final List<XExpression> params) {
    XClosure _xifexpression = null;
    XExpression _last = IterableExtensions.<XExpression>last(params);
    boolean _notEquals = (!Objects.equal(_last, null));
    if (_notEquals) {
      XClosure _xblockexpression = null;
      {
        XExpression _last_1 = IterableExtensions.<XExpression>last(params);
        final EObject grammarElement = this.textRegionExtensions.grammarElement(_last_1);
        XClosure _xifexpression_1 = null;
        if (((Objects.equal(grammarElement, this.grammar.getXMemberFeatureCallAccess().getMemberCallArgumentsXClosureParserRuleCall_1_1_4_0()) || 
          Objects.equal(grammarElement, this.grammar.getXFeatureCallAccess().getFeatureCallArgumentsXClosureParserRuleCall_4_0())) || 
          Objects.equal(grammarElement, this.grammar.getXConstructorCallAccess().getArgumentsXClosureParserRuleCall_5_0()))) {
          XExpression _last_2 = IterableExtensions.<XExpression>last(params);
          _xifexpression_1 = ((XClosure) _last_2);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  protected Iterable<XExpression> explicitParams(final List<XExpression> params) {
    Iterable<XExpression> _xblockexpression = null;
    {
      final XClosure builder = this.builder(params);
      Iterable<XExpression> _xifexpression = null;
      boolean _notEquals = (!Objects.equal(builder, null));
      if (_notEquals) {
        int _size = params.size();
        int _minus = (_size - 1);
        _xifexpression = IterableExtensions.<XExpression>take(params, _minus);
      } else {
        _xifexpression = params;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected void _format(final XConstructorCall expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _feature = _regionFor.feature(XbasePackage.Literals.XCONSTRUCTOR_CALL__CONSTRUCTOR);
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.prepend(_feature, _function);
    EList<JvmTypeReference> _typeArguments = expr.getTypeArguments();
    boolean _isEmpty = _typeArguments.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword = _regionFor_1.keyword("<");
      final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      format.surround(_keyword, _function_1);
      EList<JvmTypeReference> _typeArguments_1 = expr.getTypeArguments();
      for (final JvmTypeReference arg : _typeArguments_1) {
        {
          format.<JvmTypeReference>format(arg);
          ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(arg);
          ISemanticRegion _keyword_1 = _immediatelyFollowing.keyword(",");
          final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          ISemanticRegion _prepend = format.prepend(_keyword_1, _function_2);
          final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
            it.oneSpace();
          };
          format.append(_prepend, _function_3);
        }
      }
      ISemanticRegionsFinder _regionFor_2 = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword_1 = _regionFor_2.keyword(">");
      final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      format.prepend(_keyword_1, _function_2);
    }
    ISemanticRegionsFinder _regionFor_3 = this.textRegionExtensions.regionFor(expr);
    final ISemanticRegion open = _regionFor_3.keyword("(");
    ISemanticRegionsFinder _regionFor_4 = this.textRegionExtensions.regionFor(expr);
    final ISemanticRegion close = _regionFor_4.keyword(")");
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    format.prepend(open, _function_3);
    EList<XExpression> _arguments = expr.getArguments();
    this.formatFeatureCallParams(_arguments, open, close, format);
  }
  
  protected void formatFeatureCallTypeParameters(final XAbstractFeatureCall expr, @Extension final IFormattableDocument format) {
    EList<JvmTypeReference> _typeArguments = expr.getTypeArguments();
    boolean _isEmpty = _typeArguments.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword = _regionFor.keyword("<");
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      format.append(_keyword, _function);
      EList<JvmTypeReference> _typeArguments_1 = expr.getTypeArguments();
      for (final JvmTypeReference arg : _typeArguments_1) {
        {
          format.<JvmTypeReference>format(arg);
          ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(arg);
          ISemanticRegion _keyword_1 = _immediatelyFollowing.keyword(",");
          final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          ISemanticRegion _prepend = format.prepend(_keyword_1, _function_1);
          final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
            it.oneSpace();
          };
          format.append(_prepend, _function_2);
        }
      }
      ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword_1 = _regionFor_1.keyword(">");
      final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      format.surround(_keyword_1, _function_1);
    }
  }
  
  protected void _format(final XFeatureCall expr, @Extension final IFormattableDocument format) {
    this.formatFeatureCallTypeParameters(expr, format);
    boolean _isExplicitOperationCall = expr.isExplicitOperationCall();
    if (_isExplicitOperationCall) {
      ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword = _regionFor.keyword("(");
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      final ISemanticRegion open = format.prepend(_keyword, _function);
      ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
      final ISemanticRegion close = _regionFor_1.keyword(")");
      EList<XExpression> _featureCallArguments = expr.getFeatureCallArguments();
      this.formatFeatureCallParams(_featureCallArguments, open, close, format);
    } else {
      EList<XExpression> _featureCallArguments_1 = expr.getFeatureCallArguments();
      for (final XExpression arg : _featureCallArguments_1) {
        this.format(arg, format);
      }
    }
  }
  
  protected void _format(final XMemberFeatureCall expr, @Extension final IFormattableDocument format) {
    EObject top = expr;
    IEObjectRegion _regionForEObject = this.textRegionExtensions.regionForEObject(expr);
    SeparatorRegions<XMemberFeatureCall, ISemanticRegion> calls = new SeparatorRegions<XMemberFeatureCall, ISemanticRegion>(_regionForEObject);
    while ((top instanceof XMemberFeatureCall)) {
      {
        String _switchResult = null;
        final XMemberFeatureCall it = ((XMemberFeatureCall)top);
        boolean _matched = false;
        boolean _isNullSafe = it.isNullSafe();
        if (_isNullSafe) {
          _matched=true;
          _switchResult = "?.";
        }
        if (!_matched) {
          boolean _isExplicitStatic = it.isExplicitStatic();
          if (_isExplicitStatic) {
            _matched=true;
            _switchResult = "::";
          }
        }
        if (!_matched) {
          _switchResult = ".";
        }
        final String op = _switchResult;
        ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(top);
        final ISemanticRegion separator = _regionFor.keyword(op);
        calls.prependWithLeadingSeparator(((XMemberFeatureCall)top), separator);
        XExpression _memberCallTarget = ((XMemberFeatureCall)top).getMemberCallTarget();
        top = _memberCallTarget;
      }
    }
    format.<EObject>format(top);
    IHiddenRegion _nextHiddenRegion = this.textRegionExtensions.nextHiddenRegion(expr);
    final IndentOnceAutowrapFormatter indentOnce = new IndentOnceAutowrapFormatter(_nextHiddenRegion);
    for (final ObjectEntry<XMemberFeatureCall, ISemanticRegion> entry : calls) {
      {
        final XMemberFeatureCall call = entry.getObject();
        SeparatorEntry<XMemberFeatureCall, ISemanticRegion> _leadingSeparator = entry.getLeadingSeparator();
        final ISemanticRegion operator = _leadingSeparator.getSeparator();
        this.formatFeatureCallTypeParameters(call, format);
        ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(call);
        final ISemanticRegion feature = _regionFor.feature(XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE);
        boolean _notEquals = (!Objects.equal(feature, null));
        if (_notEquals) {
          ITextSegment _region = entry.getRegion();
          int _length = _region.getLength();
          int _length_1 = feature.getLength();
          int _multiply = (_length_1 * 2);
          final int autowrapLength = Math.min(_length, _multiply);
          final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          ISemanticRegion _prepend = format.prepend(operator, _function);
          final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
            it.autowrap(autowrapLength);
            it.setOnAutowrap(indentOnce);
          };
          format.append(_prepend, _function_1);
          boolean _isExplicitOperationCall = call.isExplicitOperationCall();
          if (_isExplicitOperationCall) {
            ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(call);
            ISemanticRegion _keyword = _regionFor_1.keyword("(");
            final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
              it.noSpace();
            };
            final ISemanticRegion open = format.prepend(_keyword, _function_2);
            ISemanticRegionsFinder _regionFor_2 = this.textRegionExtensions.regionFor(call);
            final ISemanticRegion close = _regionFor_2.keyword(")");
            EList<XExpression> _memberCallArguments = call.getMemberCallArguments();
            this.formatFeatureCallParams(_memberCallArguments, open, close, format);
          } else {
            EList<XExpression> _memberCallArguments_1 = call.getMemberCallArguments();
            boolean _isEmpty = _memberCallArguments_1.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              EList<XExpression> _memberCallArguments_2 = call.getMemberCallArguments();
              XClosure _builder = this.builder(_memberCallArguments_2);
              this.formatBuilderWithLeadingGap(_builder, format);
            }
          }
        }
      }
    }
  }
  
  protected AbstractRule binaryOperationPrecedence(final EObject op) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(op);
    final ISemanticRegion node = _regionFor.feature(XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE);
    if (((!Objects.equal(node, null)) && (node.getGrammarElement() instanceof RuleCall))) {
      EObject _grammarElement = node.getGrammarElement();
      return ((RuleCall) _grammarElement).getRule();
    }
    return null;
  }
  
  protected void _format(final XBinaryOperation expr, @Extension final IFormattableDocument format) {
    final AbstractRule precendece = this.binaryOperationPrecedence(expr);
    IEObjectRegion _regionForEObject = this.textRegionExtensions.regionForEObject(expr);
    final SeparatorRegions<XBinaryOperation, ISemanticRegion> calls = new SeparatorRegions<XBinaryOperation, ISemanticRegion>(_regionForEObject);
    EObject top = expr;
    while (Objects.equal(this.binaryOperationPrecedence(top), precendece)) {
      {
        ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(top);
        ISemanticRegion _feature = _regionFor.feature(XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE);
        calls.prependWithLeadingSeparator(((XBinaryOperation) top), _feature);
        XExpression _leftOperand = ((XBinaryOperation) top).getLeftOperand();
        top = _leftOperand;
      }
    }
    this.format(top, format);
    IHiddenRegion _nextHiddenRegion = this.textRegionExtensions.nextHiddenRegion(expr);
    final IndentOnceAutowrapFormatter indent = new IndentOnceAutowrapFormatter(_nextHiddenRegion);
    for (final ObjectEntry<XBinaryOperation, ISemanticRegion> ele : calls) {
      {
        SeparatorEntry<XBinaryOperation, ISemanticRegion> _leadingSeparator = ele.getLeadingSeparator();
        ISemanticRegion _separator = null;
        if (_leadingSeparator!=null) {
          _separator=_leadingSeparator.getSeparator();
        }
        final ISemanticRegion sep = _separator;
        XBinaryOperation _object = ele.getObject();
        boolean _prependNewLineIfMultiline = this.prependNewLineIfMultiline(_object);
        if (_prependNewLineIfMultiline) {
          final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
            it.oneSpace();
            ITextSegment _region = ele.getRegion();
            int _length = _region.getLength();
            it.autowrap(_length);
            it.setOnAutowrap(indent);
          };
          format.append(sep, _function);
        } else {
          final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
            it.oneSpace();
          };
          format.append(sep, _function_1);
        }
        final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        format.prepend(sep, _function_2);
        XBinaryOperation _object_1 = ele.getObject();
        XExpression _rightOperand = _object_1.getRightOperand();
        format.<XExpression>format(_rightOperand);
      }
    }
  }
  
  protected boolean prependNewLineIfMultiline(final EObject obj) {
    boolean _switchResult = false;
    boolean _matched = false;
    if (obj instanceof XMemberFeatureCall) {
      _matched=true;
      EList<XExpression> _memberCallArguments = ((XMemberFeatureCall)obj).getMemberCallArguments();
      XClosure _builder = this.builder(_memberCallArguments);
      _switchResult = this.prependNewLineIfMultiline(_builder);
    }
    if (!_matched) {
      if (obj instanceof XClosure) {
        _matched=true;
        _switchResult = false;
      }
    }
    if (!_matched) {
      if (obj instanceof XBlockExpression) {
        _matched=true;
        _switchResult = false;
      }
    }
    if (!_matched) {
      _switchResult = true;
    }
    return _switchResult;
  }
  
  protected void _format(final XSynchronizedExpression expr, @Extension final IFormattableDocument format) {
    EObject _eContainer = expr.eContainer();
    if ((_eContainer instanceof XVariableDeclaration)) {
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      format.<XSynchronizedExpression>surround(expr, _function);
    }
    final boolean multiline = (this.textRegionExtensions.isMultiline(expr.getExpression()) || this.textRegionExtensions.previousHiddenRegion(expr.getExpression()).isMultiline());
    XExpression _param = expr.getParam();
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    format.<XExpression>surround(_param, _function_1);
    if (((expr.getExpression() instanceof XBlockExpression) || multiline)) {
      ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword = _regionFor.keyword("synchronized");
      format.append(_keyword, XbaseFormatterPreferenceKeys.whitespaceBetweenKeywordAndParenthesisML);
    } else {
      ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword_1 = _regionFor_1.keyword("synchronized");
      format.append(_keyword_1, XbaseFormatterPreferenceKeys.whitespaceBetweenKeywordAndParenthesisSL);
    }
    XExpression _param_1 = expr.getParam();
    format.<XExpression>format(_param_1);
    XExpression _expression = expr.getExpression();
    this.formatBody(_expression, false, format);
  }
  
  protected void _format(final XIfExpression expr, @Extension final IFormattableDocument format) {
    EObject _eContainer = expr.eContainer();
    if ((_eContainer instanceof XVariableDeclaration)) {
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      format.<XIfExpression>surround(expr, _function);
    }
    final boolean multiline = (this.isMultilineOrInNewLine(expr.getThen()) || this.isMultilineOrInNewLine(expr.getElse()));
    XExpression _if = expr.getIf();
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    format.<XExpression>surround(_if, _function_1);
    if (((expr.getThen() instanceof XBlockExpression) || multiline)) {
      ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword = _regionFor.keyword("if");
      format.append(_keyword, XbaseFormatterPreferenceKeys.whitespaceBetweenKeywordAndParenthesisML);
    } else {
      ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
      ISemanticRegion _keyword_1 = _regionFor_1.keyword("if");
      format.append(_keyword_1, XbaseFormatterPreferenceKeys.whitespaceBetweenKeywordAndParenthesisSL);
    }
    XExpression _if_1 = expr.getIf();
    format.<XExpression>format(_if_1);
    XExpression _else = expr.getElse();
    boolean _equals = Objects.equal(_else, null);
    if (_equals) {
      XExpression _then = expr.getThen();
      this.formatBody(_then, multiline, format);
    } else {
      XExpression _then_1 = expr.getThen();
      this.formatBodyInline(_then_1, multiline, format);
      if (((expr.getElse() instanceof XIfExpression) || (!multiline))) {
        XExpression _else_1 = expr.getElse();
        final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        format.<XExpression>prepend(_else_1, _function_2);
        XExpression _else_2 = expr.getElse();
        format.<XExpression>format(_else_2);
      } else {
        XExpression _else_3 = expr.getElse();
        this.formatBody(_else_3, multiline, format);
      }
    }
  }
  
  protected void _format(final XForLoopExpression expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("for");
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.append(_keyword, _function);
    JvmFormalParameter _declaredParam = expr.getDeclaredParam();
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    JvmFormalParameter _prepend = format.<JvmFormalParameter>prepend(_declaredParam, _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.<JvmFormalParameter>append(_prepend, _function_2);
    XExpression _forExpression = expr.getForExpression();
    final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    XExpression _prepend_1 = format.<XExpression>prepend(_forExpression, _function_3);
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    XExpression _append = format.<XExpression>append(_prepend_1, _function_4);
    format.<XExpression>format(_append);
    XExpression _eachExpression = expr.getEachExpression();
    this.formatBody(_eachExpression, true, format);
  }
  
  protected void _format(final XBasicForLoopExpression expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("for");
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.append(_keyword, _function);
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword_1 = _regionFor_1.keyword("(");
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    format.append(_keyword_1, _function_1);
    ISemanticRegionsFinder _regionFor_2 = this.textRegionExtensions.regionFor(expr);
    List<ISemanticRegion> _keywords = _regionFor_2.keywords(";");
    final Consumer<ISemanticRegion> _function_2 = (ISemanticRegion it) -> {
      final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it_1) -> {
        it_1.noSpace();
      };
      ISemanticRegion _prepend = format.prepend(it, _function_3);
      final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it_1) -> {
        it_1.noSpace();
        it_1.lowPriority();
      };
      format.append(_prepend, _function_4);
    };
    _keywords.forEach(_function_2);
    ISemanticRegionsFinder _regionFor_3 = this.textRegionExtensions.regionFor(expr);
    List<ISemanticRegion> _keywords_1 = _regionFor_3.keywords(",");
    final Consumer<ISemanticRegion> _function_3 = (ISemanticRegion it) -> {
      final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it_1) -> {
        it_1.noSpace();
      };
      ISemanticRegion _prepend = format.prepend(it, _function_4);
      final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it_1) -> {
        it_1.oneSpace();
      };
      format.append(_prepend, _function_5);
    };
    _keywords_1.forEach(_function_3);
    ISemanticRegionsFinder _regionFor_4 = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword_2 = _regionFor_4.keyword(")");
    final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    format.prepend(_keyword_2, _function_4);
    EList<XExpression> _initExpressions = expr.getInitExpressions();
    final Consumer<XExpression> _function_5 = (XExpression it) -> {
      format.<XExpression>format(it);
    };
    _initExpressions.forEach(_function_5);
    XExpression _expression = expr.getExpression();
    final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.<XExpression>prepend(_expression, _function_6);
    XExpression _expression_1 = expr.getExpression();
    format.<XExpression>format(_expression_1);
    EList<XExpression> _updateExpressions = expr.getUpdateExpressions();
    XExpression _head = IterableExtensions.<XExpression>head(_updateExpressions);
    final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.<XExpression>prepend(_head, _function_7);
    EList<XExpression> _updateExpressions_1 = expr.getUpdateExpressions();
    final Consumer<XExpression> _function_8 = (XExpression it) -> {
      format.<XExpression>format(it);
    };
    _updateExpressions_1.forEach(_function_8);
    XExpression _eachExpression = expr.getEachExpression();
    this.formatBody(_eachExpression, true, format);
  }
  
  protected void _format(final XWhileExpression expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("while");
    format.append(_keyword, XbaseFormatterPreferenceKeys.whitespaceBetweenKeywordAndParenthesisML);
    XExpression _predicate = expr.getPredicate();
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    XExpression _prepend = format.<XExpression>prepend(_predicate, _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    XExpression _append = format.<XExpression>append(_prepend, _function_1);
    format.<XExpression>format(_append);
    XExpression _body = expr.getBody();
    this.formatBody(_body, true, format);
  }
  
  protected void _format(final XDoWhileExpression expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("while");
    format.append(_keyword, XbaseFormatterPreferenceKeys.whitespaceBetweenKeywordAndParenthesisML);
    XExpression _predicate = expr.getPredicate();
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    XExpression _prepend = format.<XExpression>prepend(_predicate, _function);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    XExpression _append = format.<XExpression>append(_prepend, _function_1);
    format.<XExpression>format(_append);
    XExpression _body = expr.getBody();
    this.formatBodyInline(_body, true, format);
  }
  
  protected void _format(final XBlockExpression expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    final ISemanticRegion open = _regionFor.keyword("{");
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
    final ISemanticRegion close = _regionFor_1.keyword("}");
    EObject _eContainer = expr.eContainer();
    boolean _equals = Objects.equal(_eContainer, null);
    if (_equals) {
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      format.<XBlockExpression>surround(expr, _function);
    }
    if (((!Objects.equal(open, null)) && (!Objects.equal(close, null)))) {
      boolean _isSingleLineBlock = this.isSingleLineBlock(expr);
      if (_isSingleLineBlock) {
        final ISubFormatter _function_1 = (IFormattableSubDocument f) -> {
          EList<XExpression> _expressions = expr.getExpressions();
          IFormattableSubDocument _requireFitsInLine = f.requireFitsInLine();
          this.formatExpressionsSingleline(_expressions, open, close, _requireFitsInLine);
        };
        final ISubFormatter _function_2 = (IFormattableSubDocument f) -> {
          EList<XExpression> _expressions = expr.getExpressions();
          this.formatExpressionsMultiline(_expressions, open, close, f);
        };
        format.formatConditionally(expr, _function_1, _function_2);
      } else {
        EList<XExpression> _expressions = expr.getExpressions();
        this.formatExpressionsMultiline(_expressions, open, close, format);
      }
    }
  }
  
  protected boolean isSingleLineBlock(final XBlockExpression expr) {
    return false;
  }
  
  protected void _format(final XTypeLiteral expr, @Extension final IFormattableDocument format) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("typeof");
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    format.append(_keyword, _function);
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _feature = _regionFor_1.feature(XbasePackage.Literals.XTYPE_LITERAL__TYPE);
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    ISemanticRegion _prepend = format.prepend(_feature, _function_1);
    final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    format.append(_prepend, _function_2);
    ISemanticRegionsFinder _regionFor_2 = this.textRegionExtensions.regionFor(expr);
    ParserRule _arrayBracketsRule = this.grammar.getArrayBracketsRule();
    List<ISemanticRegion> _ruleCallsTo = _regionFor_2.ruleCallsTo(_arrayBracketsRule);
    for (final ISemanticRegion region : _ruleCallsTo) {
      {
        final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        format.append(region, _function_3);
        ArrayBracketsFormattingReplacer _arrayBracketsFormattingReplacer = new ArrayBracketsFormattingReplacer(region);
        format.addReplacer(_arrayBracketsFormattingReplacer);
      }
    }
  }
  
  protected void _format(final XThrowExpression expr, @Extension final IFormattableDocument format) {
    XExpression _expression = expr.getExpression();
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.<XExpression>prepend(_expression, _function);
    XExpression _expression_1 = expr.getExpression();
    format.<XExpression>format(_expression_1);
  }
  
  protected void _format(final XReturnExpression expr, @Extension final IFormattableDocument format) {
    XExpression _expression = expr.getExpression();
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.<XExpression>prepend(_expression, _function);
    XExpression _expression_1 = expr.getExpression();
    format.<XExpression>format(_expression_1);
  }
  
  protected void _format(final XTryCatchFinallyExpression expr, @Extension final IFormattableDocument format) {
    XExpression _expression = expr.getExpression();
    this.formatBodyInline(_expression, true, format);
    EList<XCatchClause> _catchClauses = expr.getCatchClauses();
    for (final XCatchClause cc : _catchClauses) {
      {
        ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(cc);
        ISemanticRegion _keyword = _regionFor.keyword("catch");
        format.append(_keyword, XbaseFormatterPreferenceKeys.whitespaceBetweenKeywordAndParenthesisML);
        JvmFormalParameter _declaredParam = cc.getDeclaredParam();
        final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        JvmFormalParameter _prepend = format.<JvmFormalParameter>prepend(_declaredParam, _function);
        final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        JvmFormalParameter _append = format.<JvmFormalParameter>append(_prepend, _function_1);
        format.<JvmFormalParameter>format(_append);
        if (((!Objects.equal(cc, IterableExtensions.<XCatchClause>last(expr.getCatchClauses()))) || (!Objects.equal(expr.getFinallyExpression(), null)))) {
          XExpression _expression_1 = cc.getExpression();
          this.formatBodyInline(_expression_1, true, format);
        } else {
          XExpression _expression_2 = cc.getExpression();
          this.formatBody(_expression_2, true, format);
        }
      }
    }
    XExpression _finallyExpression = expr.getFinallyExpression();
    this.formatBody(_finallyExpression, true, format);
  }
  
  protected void _format(final JvmFormalParameter expr, @Extension final IFormattableDocument format) {
    JvmTypeReference _parameterType = expr.getParameterType();
    if (_parameterType!=null) {
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.oneSpace();
      };
      format.<JvmTypeReference>append(_parameterType, _function);
    }
    JvmTypeReference _parameterType_1 = expr.getParameterType();
    format.<JvmTypeReference>format(_parameterType_1);
  }
  
  protected void _format(final XExpression expr, @Extension final IFormattableDocument format) {
    EList<EObject> _eContents = expr.eContents();
    for (final EObject obj : _eContents) {
      boolean _matched = false;
      if (obj instanceof XExpression) {
        _matched=true;
        format.<XExpression>format(((XExpression)obj));
      }
    }
  }
  
  protected void _format(final XSwitchExpression expr, @Extension final IFormattableDocument format) {
    EList<XCasePart> _cases = expr.getCases();
    final Function1<XCasePart, Boolean> _function = (XCasePart it) -> {
      XExpression _then = it.getThen();
      return Boolean.valueOf((_then instanceof XBlockExpression));
    };
    final boolean containsBlockExpr = IterableExtensions.<XCasePart>exists(_cases, _function);
    final boolean switchSL = ((!containsBlockExpr) && (!this.textRegionExtensions.isMultiline(expr)));
    final boolean caseSL = ((((!containsBlockExpr) && ((!expr.getCases().isEmpty()) || (!Objects.equal(expr.getDefault(), null)))) && 
      (!IterableExtensions.<XCasePart>exists(expr.getCases(), ((Function1<XCasePart, Boolean>) (XCasePart it) -> {
        return Boolean.valueOf(this.textRegionExtensions.isMultiline(it));
      })))) && (!this.isMultilineOrInNewLine(expr.getDefault())));
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    final ISemanticRegion open = _regionFor.keyword("{");
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
    final ISemanticRegion close = _regionFor_1.keyword("}");
    ISemanticRegionsFinder _regionFor_2 = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor_2.keyword("switch");
    final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    format.append(_keyword, _function_1);
    if (switchSL) {
      final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
        it.oneSpace();
      };
      format.prepend(open, _function_2);
      final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
        it.oneSpace();
      };
      format.append(open, _function_3);
      EList<XCasePart> _cases_1 = expr.getCases();
      for (final XCasePart c : _cases_1) {
        {
          XExpression _case = c.getCase();
          format.<XExpression>format(_case);
          XExpression _then = c.getThen();
          format.<XExpression>format(_then);
          XExpression _then_1 = c.getThen();
          boolean _equals = Objects.equal(_then_1, null);
          if (_equals) {
            final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            format.<XCasePart>append(c, _function_4);
          } else {
            XExpression _then_2 = c.getThen();
            final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            XExpression _prepend = format.<XExpression>prepend(_then_2, _function_5);
            final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            format.<XExpression>append(_prepend, _function_6);
          }
        }
      }
      XExpression _default = expr.getDefault();
      boolean _notEquals = (!Objects.equal(_default, null));
      if (_notEquals) {
        ISemanticRegionsFinder _regionFor_3 = this.textRegionExtensions.regionFor(expr);
        ISemanticRegion _keyword_1 = _regionFor_3.keyword("default");
        final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        format.append(_keyword_1, _function_4);
        XExpression _default_1 = expr.getDefault();
        final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        XExpression _surround = format.<XExpression>surround(_default_1, _function_5);
        format.<XExpression>format(_surround);
      }
    } else {
      if (caseSL) {
        format.prepend(open, XbaseFormatterPreferenceKeys.bracesInNewLine);
        EList<XCasePart> _cases_2 = expr.getCases();
        boolean _isEmpty = _cases_2.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it) -> {
            it.newLine();
          };
          format.append(open, _function_6);
        }
        final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it) -> {
          it.indent();
        };
        format.<ISemanticRegion, ISemanticRegion>interior(open, close, _function_7);
        EList<XCasePart> _cases_3 = expr.getCases();
        for (final XCasePart c_1 : _cases_3) {
          {
            XExpression _case = c_1.getCase();
            format.<XExpression>format(_case);
            XExpression _then = c_1.getThen();
            format.<XExpression>format(_then);
            XExpression _then_1 = c_1.getThen();
            final Procedure1<IHiddenRegionFormatter> _function_8 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            format.<XExpression>prepend(_then_1, _function_8);
            EList<XCasePart> _cases_4 = expr.getCases();
            XCasePart _last = IterableExtensions.<XCasePart>last(_cases_4);
            boolean _notEquals_1 = (!Objects.equal(c_1, _last));
            if (_notEquals_1) {
              final Procedure1<IHiddenRegionFormatter> _function_9 = (IHiddenRegionFormatter it) -> {
                it.newLine();
              };
              format.<XCasePart>append(c_1, _function_9);
            }
          }
        }
        XExpression _default_2 = expr.getDefault();
        boolean _notEquals_1 = (!Objects.equal(_default_2, null));
        if (_notEquals_1) {
          ISemanticRegionsFinder _regionFor_4 = this.textRegionExtensions.regionFor(expr);
          ISemanticRegion _keyword_2 = _regionFor_4.keyword("default");
          final Procedure1<IHiddenRegionFormatter> _function_8 = (IHiddenRegionFormatter it) -> {
            it.newLine();
          };
          ISemanticRegion _prepend = format.prepend(_keyword_2, _function_8);
          final Procedure1<IHiddenRegionFormatter> _function_9 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          format.append(_prepend, _function_9);
          XExpression _default_3 = expr.getDefault();
          final Procedure1<IHiddenRegionFormatter> _function_10 = (IHiddenRegionFormatter it) -> {
            it.oneSpace();
          };
          XExpression _prepend_1 = format.<XExpression>prepend(_default_3, _function_10);
          format.<XExpression>format(_prepend_1);
        }
        final Procedure1<IHiddenRegionFormatter> _function_11 = (IHiddenRegionFormatter it) -> {
          it.newLine();
        };
        format.prepend(close, _function_11);
      } else {
        ISemanticRegion _prepend_2 = format.prepend(open, XbaseFormatterPreferenceKeys.bracesInNewLine);
        final Procedure1<IHiddenRegionFormatter> _function_12 = (IHiddenRegionFormatter it) -> {
          it.newLine();
        };
        format.append(_prepend_2, _function_12);
        if (((!expr.getCases().isEmpty()) || (!Objects.equal(expr.getDefault(), null)))) {
          final Procedure1<IHiddenRegionFormatter> _function_13 = (IHiddenRegionFormatter it) -> {
            it.indent();
          };
          format.<ISemanticRegion, ISemanticRegion>interior(open, close, _function_13);
        }
        EList<XCasePart> _cases_4 = expr.getCases();
        for (final XCasePart c_2 : _cases_4) {
          {
            XExpression _case = c_2.getCase();
            format.<XExpression>format(_case);
            XExpression _then = c_2.getThen();
            this.formatBodyParagraph(_then, format);
            ISemanticRegionsFinder _regionFor_5 = this.textRegionExtensions.regionFor(c_2);
            ISemanticRegion _feature = _regionFor_5.feature(XbasePackage.Literals.XCASE_PART__FALL_THROUGH);
            final Procedure1<IHiddenRegionFormatter> _function_14 = (IHiddenRegionFormatter it) -> {
              it.noSpace();
            };
            ISemanticRegion _prepend_3 = format.prepend(_feature, _function_14);
            final Procedure1<IHiddenRegionFormatter> _function_15 = (IHiddenRegionFormatter it) -> {
              it.newLine();
            };
            format.append(_prepend_3, _function_15);
          }
        }
        XExpression _default_4 = expr.getDefault();
        boolean _notEquals_2 = (!Objects.equal(_default_4, null));
        if (_notEquals_2) {
          ISemanticRegionsFinder _regionFor_5 = this.textRegionExtensions.regionFor(expr);
          ISemanticRegion _keyword_3 = _regionFor_5.keyword("default");
          final Procedure1<IHiddenRegionFormatter> _function_14 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          format.append(_keyword_3, _function_14);
          XExpression _default_5 = expr.getDefault();
          this.formatBodyParagraph(_default_5, format);
        }
      }
    }
    EList<XCasePart> _cases_5 = expr.getCases();
    for (final XCasePart c_3 : _cases_5) {
      if (((!Objects.equal(c_3.getTypeGuard(), null)) && (!Objects.equal(c_3.getCase(), null)))) {
        JvmTypeReference _typeGuard = c_3.getTypeGuard();
        final Procedure1<IHiddenRegionFormatter> _function_15 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        format.<JvmTypeReference>append(_typeGuard, _function_15);
        XExpression _case = c_3.getCase();
        final Procedure1<IHiddenRegionFormatter> _function_16 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        XExpression _prepend_3 = format.<XExpression>prepend(_case, _function_16);
        final Procedure1<IHiddenRegionFormatter> _function_17 = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        format.<XExpression>append(_prepend_3, _function_17);
      } else {
        JvmTypeReference _typeGuard_1 = c_3.getTypeGuard();
        boolean _notEquals_3 = (!Objects.equal(_typeGuard_1, null));
        if (_notEquals_3) {
          JvmTypeReference _typeGuard_2 = c_3.getTypeGuard();
          final Procedure1<IHiddenRegionFormatter> _function_18 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          format.<JvmTypeReference>append(_typeGuard_2, _function_18);
        } else {
          XExpression _case_1 = c_3.getCase();
          boolean _notEquals_4 = (!Objects.equal(_case_1, null));
          if (_notEquals_4) {
            XExpression _case_2 = c_3.getCase();
            final Procedure1<IHiddenRegionFormatter> _function_19 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            XExpression _prepend_4 = format.<XExpression>prepend(_case_2, _function_19);
            final Procedure1<IHiddenRegionFormatter> _function_20 = (IHiddenRegionFormatter it) -> {
              it.noSpace();
            };
            format.<XExpression>append(_prepend_4, _function_20);
          }
        }
      }
    }
  }
  
  protected ISemanticRegion formatClosureParams(final XClosure expr, final ISemanticRegion open, @Extension final IFormattableDocument format, final Procedure1<? super IHiddenRegionFormatter> init) {
    boolean _isExplicitSyntax = expr.isExplicitSyntax();
    if (_isExplicitSyntax) {
      ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
      final ISemanticRegion last = _regionFor.feature(XbasePackage.Literals.XCLOSURE__EXPLICIT_SYNTAX);
      EList<JvmFormalParameter> _declaredFormalParameters = expr.getDeclaredFormalParameters();
      boolean _isEmpty = _declaredFormalParameters.isEmpty();
      if (_isEmpty) {
        final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
          it.noSpace();
        };
        format.append(open, _function);
      } else {
        EList<JvmFormalParameter> _declaredFormalParameters_1 = expr.getDeclaredFormalParameters();
        for (final JvmFormalParameter param : _declaredFormalParameters_1) {
          {
            format.<JvmFormalParameter>format(param);
            ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(param);
            ISemanticRegion _keyword = _immediatelyFollowing.keyword(",");
            final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
              it.noSpace();
            };
            ISemanticRegion _prepend = format.prepend(_keyword, _function_1);
            final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            format.append(_prepend, _function_2);
          }
        }
        format.append(open, init);
        EList<JvmFormalParameter> _declaredFormalParameters_2 = expr.getDeclaredFormalParameters();
        boolean _isEmpty_1 = _declaredFormalParameters_2.isEmpty();
        boolean _not = (!_isEmpty_1);
        if (_not) {
          format.prepend(last, init);
        }
      }
      return last;
    }
    return open;
  }
  
  protected void _format(final XClosure expr, @Extension final IFormattableDocument format) {
    ISemanticRegion _elvis = null;
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("[");
    if (_keyword != null) {
      _elvis = _keyword;
    } else {
      ISemanticRegionFinder _immediatelyPreceding = this.textRegionExtensions.immediatelyPreceding(expr);
      ISemanticRegion _keyword_1 = _immediatelyPreceding.keyword("(");
      _elvis = _keyword_1;
    }
    final ISemanticRegion open = _elvis;
    ISemanticRegion _elvis_1 = null;
    ISemanticRegionsFinder _regionFor_1 = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword_2 = _regionFor_1.keyword("]");
    if (_keyword_2 != null) {
      _elvis_1 = _keyword_2;
    } else {
      ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(expr);
      ISemanticRegion _keyword_3 = _immediatelyFollowing.keyword(")");
      _elvis_1 = _keyword_3;
    }
    final ISemanticRegion close = _elvis_1;
    List<XExpression> _switchResult = null;
    XExpression _expression = expr.getExpression();
    final XExpression x = _expression;
    boolean _matched = false;
    if (x instanceof XBlockExpression) {
      _matched=true;
      _switchResult = ((XBlockExpression)x).getExpressions();
    }
    if (!_matched) {
      _switchResult = CollectionLiterals.<XExpression>newArrayList(x);
    }
    final List<XExpression> children = _switchResult;
    if ((Objects.equal(open, null) || Objects.equal(close, null))) {
    } else {
      boolean _isEmpty = children.isEmpty();
      if (_isEmpty) {
        IHiddenRegion _nextHiddenRegion = open.getNextHiddenRegion();
        boolean _containsComment = _nextHiddenRegion.containsComment();
        if (_containsComment) {
          final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
            it.newLine();
            it.indent();
          };
          format.append(open, _function);
        } else {
          final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
            it.noSpace();
          };
          format.append(open, _function_1);
        }
      } else {
        IHiddenRegion _previousHiddenRegion = close.getPreviousHiddenRegion();
        boolean _isMultiline = _previousHiddenRegion.isMultiline();
        if (_isMultiline) {
          final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
            it.oneSpace();
          };
          final ISemanticRegion last = this.formatClosureParams(expr, open, format, _function_2);
          this.formatExpressionsMultiline(children, last, close, format);
        } else {
          IHiddenRegion _previousHiddenRegion_1 = open.getPreviousHiddenRegion();
          ISemanticRegion _nextSemanticRegion = _previousHiddenRegion_1.getNextSemanticRegion();
          final int offset = _nextSemanticRegion.getEndOffset();
          int _offset = close.getOffset();
          final int length = (_offset - offset);
          final ISubFormatter _function_3 = (IFormattableSubDocument doc) -> {
            @Extension
            final IFormattableSubDocument it = doc.requireFitsInLine();
            final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it_1) -> {
              it_1.noSpace();
            };
            final ISemanticRegion last_1 = this.formatClosureParams(expr, open, it, _function_4);
            final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it_1) -> {
              it_1.noSpace();
            };
            it.append(last_1, _function_5);
            for (final XExpression c : children) {
              {
                it.<XExpression>format(c);
                ISemanticRegionFinder _immediatelyFollowing_1 = this.textRegionExtensions.immediatelyFollowing(c);
                final ISemanticRegion semicolon = _immediatelyFollowing_1.keyword(";");
                boolean _notEquals = (!Objects.equal(semicolon, null));
                if (_notEquals) {
                  final Procedure1<IHiddenRegionFormatter> _function_6 = (IHiddenRegionFormatter it_1) -> {
                    it_1.noSpace();
                  };
                  ISemanticRegion _prepend = it.prepend(semicolon, _function_6);
                  final Procedure1<IHiddenRegionFormatter> _function_7 = (IHiddenRegionFormatter it_1) -> {
                    XExpression _last = IterableExtensions.<XExpression>last(children);
                    boolean _equals = Objects.equal(c, _last);
                    if (_equals) {
                      it_1.noSpace();
                    } else {
                      it_1.oneSpace();
                    }
                  };
                  it.append(_prepend, _function_7);
                } else {
                  final Procedure1<IHiddenRegionFormatter> _function_8 = (IHiddenRegionFormatter it_1) -> {
                    XExpression _last = IterableExtensions.<XExpression>last(children);
                    boolean _equals = Objects.equal(c, _last);
                    if (_equals) {
                      it_1.noSpace();
                    } else {
                      it_1.oneSpace();
                    }
                  };
                  it.<XExpression>append(c, _function_8);
                }
              }
            }
          };
          final ISubFormatter _function_4 = (IFormattableSubDocument doc) -> {
            final Procedure1<IHiddenRegionFormatter> _function_5 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            final ISemanticRegion last_1 = this.formatClosureParams(expr, open, format, _function_5);
            this.formatExpressionsMultiline(children, last_1, close, doc);
          };
          format.formatConditionally(offset, length, _function_3, _function_4);
        }
      }
    }
  }
  
  protected void formatBody(final XExpression expr, final boolean forceMultiline, @Extension final IFormattableDocument doc) {
    boolean _equals = Objects.equal(expr, null);
    if (_equals) {
      return;
    }
    if ((expr instanceof XBlockExpression)) {
      doc.<XBlockExpression>prepend(((XBlockExpression)expr), XbaseFormatterPreferenceKeys.bracesInNewLine);
    } else {
      if ((forceMultiline || this.textRegionExtensions.previousHiddenRegion(expr).isMultiline())) {
        final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
          it.newLine();
        };
        XExpression _prepend = doc.<XExpression>prepend(expr, _function);
        final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
          it.indent();
        };
        doc.<XExpression>surround(_prepend, _function_1);
      } else {
        final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        doc.<XExpression>prepend(expr, _function_2);
      }
    }
    doc.<XExpression>format(expr);
  }
  
  protected void formatBodyInline(final XExpression expr, final boolean forceMultiline, @Extension final IFormattableDocument doc) {
    boolean _equals = Objects.equal(expr, null);
    if (_equals) {
      return;
    }
    if ((expr instanceof XBlockExpression)) {
      XBlockExpression _prepend = doc.<XBlockExpression>prepend(((XBlockExpression)expr), XbaseFormatterPreferenceKeys.bracesInNewLine);
      doc.<XBlockExpression>append(_prepend, XbaseFormatterPreferenceKeys.bracesInNewLine);
    } else {
      if ((forceMultiline || this.textRegionExtensions.previousHiddenRegion(expr).isMultiline())) {
        final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
          it.newLine();
        };
        XExpression _prepend_1 = doc.<XExpression>prepend(expr, _function);
        final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
          it.indent();
        };
        XExpression _surround = doc.<XExpression>surround(_prepend_1, _function_1);
        final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
          it.newLine();
        };
        doc.<XExpression>append(_surround, _function_2);
      } else {
        final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
          it.oneSpace();
        };
        doc.<XExpression>surround(expr, _function_3);
      }
    }
    doc.<XExpression>format(expr);
  }
  
  protected void formatBodyParagraph(final XExpression expr, @Extension final IFormattableDocument doc) {
    boolean _equals = Objects.equal(expr, null);
    if (_equals) {
      return;
    }
    if ((expr instanceof XBlockExpression)) {
      XBlockExpression _prepend = doc.<XBlockExpression>prepend(((XBlockExpression)expr), XbaseFormatterPreferenceKeys.bracesInNewLine);
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.newLine();
      };
      doc.<XBlockExpression>append(_prepend, _function);
    } else {
      final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
        it.newLine();
      };
      XExpression _prepend_1 = doc.<XExpression>prepend(expr, _function_1);
      final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
        it.indent();
      };
      XExpression _surround = doc.<XExpression>surround(_prepend_1, _function_2);
      final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
        it.newLine();
      };
      doc.<XExpression>append(_surround, _function_3);
    }
    doc.<XExpression>format(expr);
  }
  
  protected void _format(final XInstanceOfExpression expr, @Extension final IFormattableDocument doc) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("instanceof");
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    doc.surround(_keyword, _function);
    XExpression _expression = expr.getExpression();
    doc.<XExpression>format(_expression);
    JvmTypeReference _type = expr.getType();
    doc.<JvmTypeReference>format(_type);
  }
  
  protected void _format(final XCastedExpression expr, @Extension final IFormattableDocument doc) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _keyword = _regionFor.keyword("as");
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.oneSpace();
    };
    doc.surround(_keyword, _function);
    XExpression _target = expr.getTarget();
    doc.<XExpression>format(_target);
    JvmTypeReference _type = expr.getType();
    doc.<JvmTypeReference>format(_type);
  }
  
  protected void _format(final XPostfixOperation expr, @Extension final IFormattableDocument doc) {
    ISemanticRegionsFinder _regionFor = this.textRegionExtensions.regionFor(expr);
    ISemanticRegion _feature = _regionFor.feature(XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE);
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.noSpace();
    };
    doc.prepend(_feature, _function);
    XExpression _operand = expr.getOperand();
    doc.<XExpression>format(_operand);
  }
  
  protected void formatExpressionsMultiline(final Collection<? extends XExpression> expressions, final ISemanticRegion open, final ISemanticRegion close, @Extension final IFormattableDocument format) {
    final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
      it.indent();
    };
    format.<ISemanticRegion, ISemanticRegion>interior(open, close, _function);
    boolean _isEmpty = expressions.isEmpty();
    if (_isEmpty) {
      final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
        it.newLine();
      };
      format.append(open, _function_1);
    } else {
      format.append(open, XbaseFormatterPreferenceKeys.blankLinesAroundExpression);
      for (final XExpression child : expressions) {
        {
          format.<XExpression>format(child);
          ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(child);
          final ISemanticRegion sem = _immediatelyFollowing.keyword(";");
          boolean _notEquals = (!Objects.equal(sem, null));
          if (_notEquals) {
            final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
              it.noSpace();
            };
            ISemanticRegion _prepend = format.prepend(sem, _function_2);
            format.append(_prepend, XbaseFormatterPreferenceKeys.blankLinesAroundExpression);
          } else {
            format.<XExpression>append(child, XbaseFormatterPreferenceKeys.blankLinesAroundExpression);
          }
        }
      }
    }
  }
  
  protected void formatExpressionsSingleline(final Collection<? extends XExpression> expressions, final ISemanticRegion open, final ISemanticRegion close, @Extension final IFormattableDocument format) {
    boolean _isEmpty = expressions.isEmpty();
    if (_isEmpty) {
      final Procedure1<IHiddenRegionFormatter> _function = (IHiddenRegionFormatter it) -> {
        it.noSpace();
      };
      format.append(open, _function);
    } else {
      final Procedure1<IHiddenRegionFormatter> _function_1 = (IHiddenRegionFormatter it) -> {
        it.oneSpace();
      };
      format.append(open, _function_1);
      for (final XExpression child : expressions) {
        {
          format.<XExpression>format(child);
          ISemanticRegionFinder _immediatelyFollowing = this.textRegionExtensions.immediatelyFollowing(child);
          final ISemanticRegion sem = _immediatelyFollowing.keyword(";");
          boolean _notEquals = (!Objects.equal(sem, null));
          if (_notEquals) {
            final Procedure1<IHiddenRegionFormatter> _function_2 = (IHiddenRegionFormatter it) -> {
              it.noSpace();
            };
            ISemanticRegion _prepend = format.prepend(sem, _function_2);
            final Procedure1<IHiddenRegionFormatter> _function_3 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            format.append(_prepend, _function_3);
          } else {
            final Procedure1<IHiddenRegionFormatter> _function_4 = (IHiddenRegionFormatter it) -> {
              it.oneSpace();
            };
            format.<XExpression>append(child, _function_4);
          }
        }
      }
    }
  }
  
  protected boolean isMultilineOrInNewLine(final EObject obj) {
    return ((!Objects.equal(obj, null)) && (this.textRegionExtensions.isMultiline(obj) || this.textRegionExtensions.previousHiddenRegion(obj).isMultiline()));
  }
  
  public void format(final Object expr, final IFormattableDocument format) {
    if (expr instanceof JvmTypeParameter) {
      _format((JvmTypeParameter)expr, format);
      return;
    } else if (expr instanceof JvmFormalParameter) {
      _format((JvmFormalParameter)expr, format);
      return;
    } else if (expr instanceof XtextResource) {
      _format((XtextResource)expr, format);
      return;
    } else if (expr instanceof XAssignment) {
      _format((XAssignment)expr, format);
      return;
    } else if (expr instanceof XBinaryOperation) {
      _format((XBinaryOperation)expr, format);
      return;
    } else if (expr instanceof XDoWhileExpression) {
      _format((XDoWhileExpression)expr, format);
      return;
    } else if (expr instanceof XFeatureCall) {
      _format((XFeatureCall)expr, format);
      return;
    } else if (expr instanceof XMemberFeatureCall) {
      _format((XMemberFeatureCall)expr, format);
      return;
    } else if (expr instanceof XPostfixOperation) {
      _format((XPostfixOperation)expr, format);
      return;
    } else if (expr instanceof XWhileExpression) {
      _format((XWhileExpression)expr, format);
      return;
    } else if (expr instanceof XFunctionTypeRef) {
      _format((XFunctionTypeRef)expr, format);
      return;
    } else if (expr instanceof JvmGenericArrayTypeReference) {
      _format((JvmGenericArrayTypeReference)expr, format);
      return;
    } else if (expr instanceof JvmParameterizedTypeReference) {
      _format((JvmParameterizedTypeReference)expr, format);
      return;
    } else if (expr instanceof JvmWildcardTypeReference) {
      _format((JvmWildcardTypeReference)expr, format);
      return;
    } else if (expr instanceof XBasicForLoopExpression) {
      _format((XBasicForLoopExpression)expr, format);
      return;
    } else if (expr instanceof XBlockExpression) {
      _format((XBlockExpression)expr, format);
      return;
    } else if (expr instanceof XCastedExpression) {
      _format((XCastedExpression)expr, format);
      return;
    } else if (expr instanceof XClosure) {
      _format((XClosure)expr, format);
      return;
    } else if (expr instanceof XCollectionLiteral) {
      _format((XCollectionLiteral)expr, format);
      return;
    } else if (expr instanceof XConstructorCall) {
      _format((XConstructorCall)expr, format);
      return;
    } else if (expr instanceof XForLoopExpression) {
      _format((XForLoopExpression)expr, format);
      return;
    } else if (expr instanceof XIfExpression) {
      _format((XIfExpression)expr, format);
      return;
    } else if (expr instanceof XInstanceOfExpression) {
      _format((XInstanceOfExpression)expr, format);
      return;
    } else if (expr instanceof XReturnExpression) {
      _format((XReturnExpression)expr, format);
      return;
    } else if (expr instanceof XSwitchExpression) {
      _format((XSwitchExpression)expr, format);
      return;
    } else if (expr instanceof XSynchronizedExpression) {
      _format((XSynchronizedExpression)expr, format);
      return;
    } else if (expr instanceof XThrowExpression) {
      _format((XThrowExpression)expr, format);
      return;
    } else if (expr instanceof XTryCatchFinallyExpression) {
      _format((XTryCatchFinallyExpression)expr, format);
      return;
    } else if (expr instanceof XTypeLiteral) {
      _format((XTypeLiteral)expr, format);
      return;
    } else if (expr instanceof XVariableDeclaration) {
      _format((XVariableDeclaration)expr, format);
      return;
    } else if (expr instanceof JvmTypeConstraint) {
      _format((JvmTypeConstraint)expr, format);
      return;
    } else if (expr instanceof XExpression) {
      _format((XExpression)expr, format);
      return;
    } else if (expr instanceof XImportDeclaration) {
      _format((XImportDeclaration)expr, format);
      return;
    } else if (expr instanceof XImportSection) {
      _format((XImportSection)expr, format);
      return;
    } else if (expr instanceof EObject) {
      _format((EObject)expr, format);
      return;
    } else if (expr == null) {
      _format((Void)null, format);
      return;
    } else if (expr != null) {
      _format(expr, format);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(expr, format).toString());
    }
  }
  
  @Pure
  public XbaseGrammarAccess getGrammar() {
    return this.grammar;
  }
}
