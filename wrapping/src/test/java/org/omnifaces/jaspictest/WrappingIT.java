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
 * This tests that the wrapped request and response a SAM puts into the MessageInfo structure reaches the Servlet
 * that's invoked.
 * 
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class WrappingIT extends ArquillianBase {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return fromWar("wrapping");
	}


	@Test
	public void testRequestWrapping() {
		driver.get(deploymentUrl + "protected/servlet");
		
		// The SAM wrapped a request so that it always contains the request attribute "isWrapped" with value true.
		assertTrue("Request wrapped by SAM did not arrive in Servlet.", driver.getPageSource().contains("request isWrapped: true"));
	}
	
	@Test
	public void testResponseWrapping() {
		driver.get(deploymentUrl + "protected/servlet");
		
		// The SAM wrapped a response so that it always contains the header "isWrapped" with value true.
		assertTrue("Response wrapped by SAM did not arrive in Servlet.", driver.getPageSource().contains("response isWrapped: true"));
	}
	
}