/*******************************************************************************
 * Copyright (c) 2016, 2017 TypeFox GmbH (http://www.typefox.io) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.ide.server;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.workspace.IWorkspaceConfig;

/**
 * @author Jan Koehnlein - Initial contribution and API
 * @since 2.11
 * @deprecated use the {@link IMultiRootWorkspaceConfigFactory} instead.
 * 			   This interface is scheduled to be removed with 2.22.
 */
@Deprecated//(forRemoval=true)
public interface IWorkspaceConfigFactory {
	/**
	 * Create a workspace config at the given location.
	 *
	 * @param workspaceBaseURI
	 *            the location
	 * @return the workspace configuration.
	 */
	IWorkspaceConfig getWorkspaceConfig(URI workspaceBaseURI);
}
