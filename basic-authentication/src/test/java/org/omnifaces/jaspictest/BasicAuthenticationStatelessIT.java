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

@RunWith(Arquillian.class)
public class BasicAuthenticationStatelessIT extends ArquillianBase {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return fromWar("basic-authentication");
	}
	
	/**
	 * Tests that access to a protected page does not depend on the authenticated identity
	 * that was established in a previous request.
	 */
	@Test
	public void testProtectedAccessIsStateless() {
		
		// -------------------- Request 1 ---------------------------
		
		System.out.println("Accessing protected page without login");
		driver.get(deploymentUrl + "protected/a.jsp");
		
		// Not logged-in thus should not be accessible.
		assertFalse(driver.getPageSource().contains("This is page A."));
		
		
		// -------------------- Request 2 ---------------------------
		
		// JASPIC is stateless and login (re-authenticate) has to happen for every request
		//
		// If the following fails the but "testProtectedPageLoggedin" has succeeded,
		// the container has probably remembered the "unauthenticated identity"
		System.out.println("Accessing protected page with login");
		
		driver.get(deploymentUrl + "protected/a.jsp?doLogin=true");
		
		// Now has to be logged-in so page is accessible
		assertTrue(
			"Could not access protected page, but should be able to. " +
			"Did the container remember the previously set 'unauthenticated identity'?",  
			driver.getPageSource().contains("This is page A.")
		);
		
		
		// -------------------- Request 3 ---------------------------
		
		// JASPIC is stateless and login (re-authenticate) has to happen for every request
		//
		// In the following method we do a call without logging in after one where we did login.
		// The container should not remember this login and had to deny access.
		System.out.println("Accessing protected page without login");
		driver.get(deploymentUrl + "protected/a.jsp");
		
		// Not logged-in thus should not be accessible.
		assertFalse(
			"Could access protected page, but should not be able to. " +
			"Did the container remember the authenticated identity that was set in previous request?", 	
			driver.getPageSource().contains("This is page A.")
		);
	}
	
	/**
	 * Tests that access to a protected page does not depend on the authenticated identity
	 * that was established in a previous request, but use a different request order than
	 * the previous test.
	 */
	@Test
	public void testProtectedAccessIsStateless2() {
		
		
		// -------------------- Request 1 ---------------------------
		
		// Start with doing a login
		System.out.println("Accessing protected page with login");
		driver.get(deploymentUrl + "protected/a.jsp?doLogin=true");
		
		
		// -------------------- Request 2 ---------------------------
		
		// JASPIC is stateless and login (re-authenticate) has to happen for every request
		//
		// In the following method we do a call without logging in after one where we did login.
		// The container should not remember this login and has to deny access.
		System.out.println("Accessing protected page without login");
		driver.get(deploymentUrl + "protected/a.jsp");
		
		// Not logged-in thus should not be accessible.
		assertFalse(
			"Could access protected page, but should not be able to. " +
			"Did the container remember the authenticated identity that was set in previous request?", 	
			driver.getPageSource().contains("This is page A.")
		);
	}
	
	
	/**
	 * Tests independently from being able to access a protected resource if any details of
	 * a previously established authenticated identity are remembered
	 */
	@Test
	public void testUserIdentityIsStateless() {
		
		// -------------------- Request 1 ---------------------------
		
		System.out.println("Accessing protected page with login");
		driver.get(deploymentUrl + "protected/a.jsp?doLogin=true");
		
		
		// -------------------- Request 2 ---------------------------
		System.out.println("Accessing public page without login");
		
		driver.get(deploymentUrl + "index.jsp");
		
		// No details should linger around
		assertFalse(
			"User principal was 'test', but it should be null here. " + 
		    "The container seemed to have remembered it from the previous request.",
			driver.getPageSource().contains("UserPrincipal: test")
		);
		assertTrue(
			"User principal was not null, but it should be null here. ", 
			driver.getPageSource().contains("UserPrincipal: null")
		);
		assertTrue(
			"The unauthenticated user has the role 'architect', which should not be the case. " + 
			"The container seemed to have remembered it from the previous request.",
			driver.getPageSource().contains("User has role \"architect\": false")
		);
	}
	
	
}