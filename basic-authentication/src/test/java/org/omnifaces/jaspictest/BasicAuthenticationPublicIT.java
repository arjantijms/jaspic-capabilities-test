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
 * This tests that we can login from a public page (a page for which no security constraints
 * have been set).
 * 
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class BasicAuthenticationPublicIT extends ArquillianBase {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return fromWar("basic-authentication");
	}

	@Test
	public void testPublicPageNotLoggedin() {
		driver.get(deploymentUrl + "index.jsp");
		
		// Not logged-in
		assertTrue(driver.getPageSource().contains("UserPrincipal: null"));
		assertTrue(driver.getPageSource().contains("User has role \"architect\": false"));
	}
	
	@Test
	public void testPublicPageLoggedin() {
		driver.get(deploymentUrl + "index.jsp?doLogin=true");
		
		// Now has to be logged-in
		assertTrue(driver.getPageSource().contains("UserPrincipal: test"));
		assertTrue(driver.getPageSource().contains("User has role \"architect\": true"));
	}
	
	@Test
	public void testPublicPageNotRememberLogin() {
		testPublicPageNotLoggedin();
		testPublicPageLoggedin();
		
		// JASPIC is stateless and login (re-authenticate) has to happen for every request
		// In the following method we do a call without logging in after one where we did login. 
		testPublicPageNotLoggedin();
	}
	
	
}