package org.omnifaces.jaspictest.install;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.omnifaces.jaspictest.common.BaseServletContextListener;
import org.omnifaces.jaspictest.common.JaspicUtils;
import org.omnifaces.jaspictest.sam.TestLifecycleAuthModule;

@WebListener
public class SamAutoRegistrationListener extends BaseServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		System.out.println("\n\n**** contextInitialized ****\n\n");
		
		JaspicUtils.registerSAM(new TestLifecycleAuthModule());
	}
	
}