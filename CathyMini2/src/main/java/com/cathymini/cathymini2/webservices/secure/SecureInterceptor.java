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

@Secure
@Interceptor
public class SecureInterceptor {
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();

    public SecureInterceptor() {
    }

    @AroundInvoke
    public Object secure(InvocationContext invocationContext)
            throws Exception {
        Role role = getRole(invocationContext);
        HttpServletRequest req = getHttpRequest(invocationContext);
        HttpServletResponse res = getHttpResponse(invocationContext);

        if (req == null) {
            res.sendRedirect("/");
            return null;
        }
        
        if (role.equals(Role.ANONYM)) {
            System.out.println(sessionSecuring.isConnected(req));
            if (!sessionSecuring.isConnected(req)) {
                return invocationContext.proceed();
            }
        } else if (role.equals(Role.MEMBER)) {
            if (sessionSecuring.isConnected(req)) {
                return invocationContext.proceed();
            }
        } else if (role.equals(Role.ADMIN)) {

        }

        return null;
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
