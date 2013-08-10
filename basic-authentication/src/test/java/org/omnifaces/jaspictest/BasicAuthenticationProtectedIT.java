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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnifaces.jaspictest.common.ArquillianBase;

/**
 * This tests that we can login from a protected page  (a page for which security constraints
 * have been set) and then access it.
 * 
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class BasicAuthenticationProtectedIT extends ArquillianBase {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return fromWar("basic-authentication");
	}


	@Test
	public void testProtectedPageNotLoggedin() {
		driver.get(deploymentUrl + "protected/a.jsp");
		
		// Not logged-in thus should not be accessible.
		assertFalse(driver.getPageSource().contains("This is page A."));
	}
	
	@Test
	public void testProtectedPageLoggedin() {
		driver.get(deploymentUrl + "protected/a.jsp?doLogin=true");
		
		// Now has to be logged-in so page is accessible
		assertTrue(driver.getPageSource().contains("This is page A."));
	}
	
}