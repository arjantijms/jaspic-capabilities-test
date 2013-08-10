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

import static java.lang.Boolean.TRUE;
import static javax.security.auth.message.AuthStatus.SUCCESS;

import java.io.IOException;
import java.security.Principal;
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

public class TestServerAuthModule implements ServerAuthModule {

	private CallbackHandler handler;
	private Class<?>[] supportedMessageTypes = new Class[] { HttpServletRequest.class, HttpServletResponse.class };

	@Override
	public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, @SuppressWarnings("rawtypes") Map options) throws AuthException {
		this.handler = handler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {

		HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();
		Principal userPrincipal = request.getUserPrincipal();

		if (userPrincipal != null) {

			try {
				// Execute protocol to signal container remember authentication session be used.
				handler.handle(new Callback[] { new CallerPrincipalCallback(clientSubject, userPrincipal) });
				return SUCCESS;
			} catch (IOException | UnsupportedCallbackException e) {
				throw (AuthException) new AuthException().initCause(e);
			}
		}

		CallerPrincipalCallback callerPrincipalCallback = new CallerPrincipalCallback(clientSubject, "test");
		GroupPrincipalCallback groupPrincipalCallback = new GroupPrincipalCallback(clientSubject, new String[] { "architect" });

		try {
			handler.handle(new Callback[] { callerPrincipalCallback, groupPrincipalCallback });

			// Tell container to register an authentication session.
			messageInfo.getMap().put("javax.servlet.http.registerSession", TRUE.toString());
			// Legacy fall back for GlassFish 3.1
			messageInfo.getMap().put("com.sun.web.RealmAdapter.register", TRUE.toString());

		} catch (IOException | UnsupportedCallbackException e) {
			throw (AuthException) new AuthException().initCause(e);
		}

		return SUCCESS;
	}

	@Override
	public Class<?>[] getSupportedMessageTypes() {
		return supportedMessageTypes;
	}

	@Override
	public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
		return AuthStatus.SEND_SUCCESS;
	}

	@Override
	public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {

	}
}