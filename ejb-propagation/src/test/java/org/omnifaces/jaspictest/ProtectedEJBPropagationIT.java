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
package org.omnifaces.jaspictest;

import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnifaces.jaspictest.common.ArquillianBase;

/**
 * This tests that the established authenticated identity propagates correctly from the web layer 
 * to a "protected" EJB (an EJB with declarative role checking).
 * 
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class ProtectedEJBPropagationIT extends ArquillianBase {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return fromWar("ejb-propagation");
	}

	@Test
	public void testProtectedServletWithLoginCallingEJB() {
		
		driver.get(deploymentUrl + "protected/servlet-protected-ejb?doLogin=true");
		
		// Both the web (HttpServletRequest) and EJB (EJBContext) should see the same
		// user name.
		assertTrue(driver.getPageSource().contains("web username: test"));
		assertTrue(
			"Web has user principal set, but EJB not.", 
			driver.getPageSource().contains("EJB username: test")
		);
		
		// Both the web (HttpServletRequest) and EJB (EJBContext) should see that the
		// user has the role "architect".
		assertTrue(driver.getPageSource().contains("web user has role \"architect\": true"));
		assertTrue(
			"Web user principal has role \"architect\", but one in EJB doesn't.", 
			driver.getPageSource().contains("EJB user has role \"architect\": true")
		);
	}
	
	/**
	 * A small variation on the testProtectedServletWithLoginCallingEJB that tests if
	 * for authentication that happened for public resources the security context also
	 * propagates to EJB.
	 * 
	 */
	//@Test
	public void testPublicServletWithLoginCallingEJB() {
		
		driver.get(deploymentUrl + "public/servlet-protected-ejb?doLogin=true");
		
		// Both the web (HttpServletRequest) and EJB (EJBContext) should see the same
		// user name.
		assertTrue(driver.getPageSource().contains("web username: test"));
		assertTrue(
			"Web has user principal set, but EJB not.", 
			driver.getPageSource().contains("EJB username: test")
		);
		
		// Both the web (HttpServletRequest) and EJB (EJBContext) should see that the
		// user has the role "architect".
		assertTrue(driver.getPageSource().contains("web user has role \"architect\": true"));
		assertTrue(
			"Web user principal has role \"architect\", but one in EJB doesn't.", 
			driver.getPageSource().contains("EJB user has role \"architect\": true")
		);
	}
	
}