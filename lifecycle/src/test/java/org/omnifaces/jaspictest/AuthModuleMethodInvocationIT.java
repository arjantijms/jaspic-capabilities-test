package org.omnifaces.jaspictest;

import static org.junit.Assert.assertTrue;

import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnifaces.jaspictest.common.ArquillianBase;

/**
 * This tests that the two main methods of a SAM, {@link ServerAuthModule#validateRequest}
 * and {@link ServerAuthModule#secureResponse} are called at the right
 * time, which is resp. before and after the resource (e.g. a Servlet) is invoked.
 * 
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class AuthModuleMethodInvocationIT extends ArquillianBase {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return fromWar("lifecycle");
	}

	/**
	 * Test that the main SAM methods are called and are called in the correct order.
	 * 
	 * The rule seems simple:
	 * <ul>
	 * <li> First call validateRequest() in the SAM. 
	 * <li> Then invoke the requested resource (e.g. a Servlet or JSP page)
	 * <li> Finally call secureResponse() in the SAM
	 * </ul>
	 */
	@Test
	public void testBasicSAMMethodsCalled() {
		
		driver.get(deploymentUrl + "protected/servlet");
		
		// First test if individual methods are called
		assertTrue("SAM method validateRequest not called, but should have been.", driver.getPageSource().contains("validateRequest invoked"));
		assertTrue("Resource (Servlet) not invoked, but should have been.", driver.getPageSource().contains("Resource invoked"));
		
		// The previous two methods are rare to not be called, but secureResponse is more likely to fail.  
		assertTrue("SAM method secureResponse not called, but should have been.", driver.getPageSource().contains("secureResponse invoked"));
		
		// Finally the order should be correct. More than a few implementations call secureResponse before the resource is invoked.
		assertTrue("SAM methods called in wrong order", driver.getPageSource().contains("validateRequest invoked\nResource invoked\nsecureResponse invoked\n"));
	}
	
	/**
	 * Test that the SAM's cleanSubject method is called following a call to {@link HttpServletRequest#logout()}.
	 * <p>
	 * Although occasionally a JASPIC 1.0 implementation indeed does this, it's only mandated that this happens in JASPIC 1.1
	 */
	@Test
	public void testLogout() {
		ifJaspic11();
		
		driver.get(deploymentUrl + "protected/servlet?doLogout=true");
		
		assertTrue("SAM method cleanSubject not called, but should have been.", driver.getPageSource().contains("cleanSubject invoked"));
	}
	
}