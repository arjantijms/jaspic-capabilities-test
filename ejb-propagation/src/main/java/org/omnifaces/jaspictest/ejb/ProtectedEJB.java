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
package org.omnifaces.jaspictest.ejb;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;

/**
 * This is a "protected" EJB in the sense that there is role checking done prior to accessing (some) methods.
 * <p>
 * In JBoss EAP 6.1 the use of any declarative security annotation switches the bean to a different mode,
 * called "secured" in JBoss terms.
 * <p>
 * GlassFish requires the <code>@DeclareRoles</code> annotation when programmatic role checking is done (making dynamic
 * role checking impossible).
 */
@Stateless
@DeclareRoles({"architect"}) // Required by GlassFish
@PermitAll // JBoss EAP 6.1 defaults unchecked methods to DenyAll
public class ProtectedEJB {

	@Resource
	private EJBContext ejbContext;
	
	@RolesAllowed("architect")
	public String getUserName() {
		try {
			return ejbContext.getCallerPrincipal() != null? ejbContext.getCallerPrincipal().getName() : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isUserArchitect() {
		try {
			return ejbContext.isCallerInRole("architect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
}
