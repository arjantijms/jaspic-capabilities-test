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
package org.omnifaces.jaspictest.common;

import static java.lang.System.getProperty;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;


@RunWith(Arquillian.class)
public class ArquillianBase {
	
	static final Logger logger = Logger.getLogger("ArquillianBase");
	
	@Drone
    protected WebDriver driver;
	
	@ArquillianResource
	protected URL deploymentUrl;
	
	public static WebArchive fromWar(String warName) {

		String jbossWebXmlVersion = null;
		try {
			jbossWebXmlVersion = getProperty("jbossWebXmlVersion");
			logger.info("jbossWebXmlVersion:"+jbossWebXmlVersion);
		} catch (Exception e) {
			logger.info("jbossWebXmlVersion not set");
		}

		try {
			WebArchive war = ShrinkWrap
					.create(ZipImporter.class, warName + ".war")
					.importFrom(new File("target/jaspic-capabilities-test-" + warName + "-1.0.war"))
					.as(WebArchive.class);
			if (jbossWebXmlVersion != null) {
				war.addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-web"+ jbossWebXmlVersion + ".xml"), "jboss-web.xml");
			}
			return war;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return null;

	}
	
	@Rule
	public TestRule watcher = new TestWatcher() {
	   protected void starting(Description description) {
	      System.out.println("Starting test: " + description.getMethodName());
	   }
	};
	
	@After
	public void after() {
		System.out.println(driver.getPageSource());
		driver.manage().deleteAllCookies();
	}
	
	public void ifJaspic11() {
		assumeTrue(Boolean.valueOf(getProperty("testJaspic11")));
	}

}
