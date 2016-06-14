/**
 */
package org.eclipse.xtext.xbase.testlanguages.xImportSectionTestLang;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.xtext.xtype.XImportSection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Import Section Test Language Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.xtext.xbase.testlanguages.xImportSectionTestLang.ImportSectionTestLanguageRoot#getImportSection <em>Import Section</em>}</li>
 * </ul>
 *
 * @see org.eclipse.xtext.xbase.testlanguages.xImportSectionTestLang.XImportSectionTestLangPackage#getImportSectionTestLanguageRoot()
 * @model
 * @generated
 */
public interface ImportSectionTestLanguageRoot extends EObject
{
  /**
   * Returns the value of the '<em><b>Import Section</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Import Section</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Import Section</em>' containment reference.
   * @see #setImportSection(XImportSection)
   * @see org.eclipse.xtext.xbase.testlanguages.xImportSectionTestLang.XImportSectionTestLangPackage#getImportSectionTestLanguageRoot_ImportSection()
   * @model containment="true"
   * @generated
   */
  XImportSection getImportSection();

  /**
   * Sets the value of the '{@link org.eclipse.xtext.xbase.testlanguages.xImportSectionTestLang.ImportSectionTestLanguageRoot#getImportSection <em>Import Section</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Import Section</em>' containment reference.
   * @see #getImportSection()
   * @generated
   */
  void setImportSection(XImportSection value);

} // ImportSectionTestLanguageRoot
