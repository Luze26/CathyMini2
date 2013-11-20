/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cathymini.cathymini2.webservices.secure;

import java.util.Arrays;
import java.util.List;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Secure
@Interceptor
public class SecureInterceptor {

    private static final String USER_ATTR = "_USER_ATTR";

    public SecureInterceptor() {
    }

    @AroundInvoke
    public Object secure(InvocationContext invocationContext)
            throws Exception {
        Role role = getRole(invocationContext);
        HttpServletRequest req = getHttpRequest(invocationContext);
        HttpServletResponse res = getHttpResponse(invocationContext);

        if (req != null) {
            HttpSession session = req.getSession();
            if (Role.ANONYM.equals(role) && isAnonym(session)) { //The requester must be anonym
                    invocationContext.proceed();
            } else if (role == Role.MEMBER || role == Role.ADMIN) {
                //TODO ADMIN AND REAL AUTHENTIFICATION
                if (!isAnonym(session)) {
                    invocationContext.proceed();
                } else {
                    if (res != null) {
                        res.sendRedirect("/");
                    }
                }
            }
        }
        return null;
        
    }

    private static boolean isAnonym(HttpSession session) {
        if (session == null || session.getAttribute(USER_ATTR) == null) {
            return true;
        } else {
            return false;
        }
    }

    private static Role getRole(final InvocationContext invocationContext) {
        final Secure annotation = invocationContext.getMethod().getAnnotation(Secure.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

    private static HttpServletRequest getHttpRequest(final InvocationContext invocationContext) {
        final List<Class<?>> paramsTypes = Arrays.asList(invocationContext.getMethod().getParameterTypes());

        int index = 0;
        for (Class<?> type : paramsTypes) {
            if (type.equals(HttpServletRequest.class)) {
                return (HttpServletRequest) invocationContext.getParameters()[index];
            }
            index++;
        }
        return null;
    }

    private static HttpServletResponse getHttpResponse(final InvocationContext invocationContext) {
        final List<Class<?>> paramsTypes = Arrays.asList(invocationContext.getMethod().getParameterTypes());

        int index = 0;
        for (Class<?> type : paramsTypes) {
            if (type.equals(HttpServletResponse.class)) {
                return (HttpServletResponse) invocationContext.getParameters()[index];
            }
            index++;
        }
        return null;
    }
}
