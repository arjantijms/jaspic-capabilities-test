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
package org.omnifaces.jaspictest.install;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.omnifaces.jaspictest.common.BaseServletContextListener;
import org.omnifaces.jaspictest.common.JaspicUtils;
import org.omnifaces.jaspictest.sam.TestWrappingServerAuthModule;

@WebListener
public class SamAutoRegistrationListener extends BaseServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		JaspicUtils.registerSAM(new TestWrappingServerAuthModule());
	}
	
}