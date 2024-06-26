/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.xbase.lib;

import java.io.Serializable;
import java.util.Objects;

import com.google.common.annotations.GwtCompatible;

/**
 * An immutable pair of {@link #getKey() key} and {@link #getValue() value}. A pair is considered to be
 * {@link #equals(Object) equal} to another pair if both the key and the value are equal.
 * 
 * @param <K>
 *            the key-type of the pair.
 * @param <V>
 *            the value-type of the pair.
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@GwtCompatible public final class Pair<K, V> implements Serializable {

	private static final long serialVersionUID = 736548906251305939L;
	private final K k;
	private final V v;
	
	/**
	 * Creates a new instance with the given key and value.
	 * May be used instead of the constructor for convenience reasons.
	 * 
	 * @param k
	 *            the key. May be <code>null</code>.
	 * @param v
	 *            the value. May be <code>null</code>.
	 * @return a newly created pair. Never <code>null</code>.
	 * @since 2.3
	 */
	@Pure
	public static <K, V> Pair<K, V> of(K k, V v) {
		return new Pair<K, V>(k, v);
	}

	/**
	 * Creates a new instance with the given key and value.
	 * 
	 * @param k
	 *            the key. May be <code>null</code>.
	 * @param v
	 *            the value. May be <code>null</code>.
	 * 
	 */
	@Pure
	public Pair(K k, V v) {
		this.k = k;
		this.v = v;
	}

	/**
	 * Returns the key.
	 * 
	 * @return the key.
	 */
	@Pure
	public K getKey() {
		return k;
	}

	/**
	 * Returns the value.
	 * 
	 * @return the value.
	 */
	@Pure
	public V getValue() {
		return v;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Pair))
			return false;
		Pair<?, ?> e = (Pair<?, ?>) o;
		return Objects.equals(k, e.getKey()) && Objects.equals(v, e.getValue());
	}

	@Override
	public int hashCode() {
		return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
	}

	@Override
	public String toString() {
		return k + "->" + v;
	}

}
