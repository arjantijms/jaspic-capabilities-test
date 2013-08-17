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
package org.omnifaces.jaspictest.sam;

import static javax.security.auth.message.AuthStatus.SEND_SUCCESS;
import static javax.security.auth.message.AuthStatus.SUCCESS;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.jaspictest.servlet.TestHttpServletRequestWrapper;
import org.omnifaces.jaspictest.servlet.TestHttpServletResponseWrapper;

public class TestWrappingServerAuthModule implements ServerAuthModule {

	private CallbackHandler handler;
	private Class<?>[] supportedMessageTypes = new Class[] { HttpServletRequest.class, HttpServletResponse.class };

	@Override
	public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, @SuppressWarnings("rawtypes") Map options) throws AuthException {
		this.handler = handler;
	}

	@Override
	public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
		
		try {
			handler.handle(new Callback[] { 
				new CallerPrincipalCallback(clientSubject, "test"),
				new GroupPrincipalCallback(clientSubject, new String[] { "architect" }) 
			});
		} catch (IOException | UnsupportedCallbackException e) {
			throw (AuthException) new AuthException().initCause(e);
		}
	
		
		// Wrap the request - the resource to be invoked should get to see this
		messageInfo.setRequestMessage(
			new TestHttpServletRequestWrapper((HttpServletRequest) messageInfo.getRequestMessage())
        );
		
		// Wrap the response - the resource to be invoked should get to see this
		messageInfo.setResponseMessage(
			new TestHttpServletResponseWrapper((HttpServletResponse) messageInfo.getResponseMessage())
		);

		return SUCCESS;
	}

	@Override
	public Class<?>[] getSupportedMessageTypes() {
		return supportedMessageTypes;
	}

	@Override
	public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
		
		HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
		
		// Unwrap the request
		if (request instanceof TestHttpServletRequestWrapper) {
			messageInfo.setRequestMessage(((TestHttpServletRequestWrapper) request).getRequest());
		}
		
		HttpServletResponse response = (HttpServletResponse) messageInfo.getResponseMessage();
		
		if (response instanceof TestHttpServletResponseWrapper) {
			messageInfo.setResponseMessage(((TestHttpServletResponseWrapper) response).getResponse());
		}
		
		return SEND_SUCCESS;
	}

	@Override
	public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {

	}
}