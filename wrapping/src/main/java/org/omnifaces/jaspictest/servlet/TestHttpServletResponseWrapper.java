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
package org.omnifaces.jaspictest.servlet;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class TestHttpServletResponseWrapper extends HttpServletResponseWrapper {
	
	public TestHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
	}
	
	@Override
	public String getHeader(String name) {
		
		if ("isWrapped".equals(name)) {
			return "true";
		}
		
		return super.getHeader(name);
	}

}
