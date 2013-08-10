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
import javax.ejb.EJBContext;
import javax.ejb.Stateless;

/**
 * This is a "public" EJB in the sense that all its methods should be  accessible and there is no declarative role
 * checking prior to accessing a method.
 *
 */
@Stateless
public class PublicEJB {

	@Resource
	private EJBContext ejbContext;
	
	public String getUserName() {
		try {
			return ejbContext.getCallerPrincipal() != null? ejbContext.getCallerPrincipal().getName() : null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
