/**
 */
package org.eclipse.xtext.xbase.testlanguages.bug462047.bug462047;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.xtext.common.types.JvmGenericType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.xtext.xbase.testlanguages.bug462047.bug462047.Bug462047Element#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.xtext.xbase.testlanguages.bug462047.bug462047.Bug462047Element#getRef <em>Ref</em>}</li>
 * </ul>
 *
 * @see org.eclipse.xtext.xbase.testlanguages.bug462047.bug462047.Bug462047Package#getBug462047Element()
 * @model
 * @generated
 */
public interface Bug462047Element extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.xtext.xbase.testlanguages.bug462047.bug462047.Bug462047Package#getBug462047Element_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.xtext.xbase.testlanguages.bug462047.bug462047.Bug462047Element#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Ref</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ref</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ref</em>' reference.
   * @see #setRef(JvmGenericType)
   * @see org.eclipse.xtext.xbase.testlanguages.bug462047.bug462047.Bug462047Package#getBug462047Element_Ref()
   * @model
   * @generated
   */
  JvmGenericType getRef();

  /**
   * Sets the value of the '{@link org.eclipse.xtext.xbase.testlanguages.bug462047.bug462047.Bug462047Element#getRef <em>Ref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ref</em>' reference.
   * @see #getRef()
   * @generated
   */
  void setRef(JvmGenericType value);

} // Bug462047Element
