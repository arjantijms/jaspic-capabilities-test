/*
 * Copyright 2013 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.jaspictest.common;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.module.ServerAuthModule;

public final class JaspicUtils {

	private JaspicUtils() {
	}

	/**
	 * Registers the given SAM using the standard JASPIC {@link AuthConfigFactory} but using
	 * a small set of wrappers that just pass the calls through to the SAM.
	 * 
	 * @param serverAuthModule
	 */
	public static void registerSAM(ServerAuthModule serverAuthModule) {
		AuthConfigFactory.getFactory().registerConfigProvider(
			new TestAuthConfigProvider(serverAuthModule), 
			"HttpServlet", null, "Test authentication config provider");
	}

}
