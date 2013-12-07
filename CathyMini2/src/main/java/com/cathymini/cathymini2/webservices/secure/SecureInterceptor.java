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
/**
 * The class {@link SecureInterceptor} implements a JEE Interceptor to manage security access to CathyMini ressources
 */
public class SecureInterceptor {
    private static final ConsumerSessionSecuring sessionSecuring = ConsumerSessionSecuring.getInstance();

    public SecureInterceptor() {
    }

    @AroundInvoke
    /**
     * Worker called when a ressource is accessed. 
     * The worker check if the premission level is high enough to access the ressource
     */
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
            System.out.println(sessionSecuring.isConnected(req));
            if (sessionSecuring.isConnected(req)) {
                return invocationContext.proceed();
            }
        } else if (role.equals(Role.ADMIN)) {

        }

        return null;
    }

    /**
     * Return the {@link Role} associate to the annotation of the {@link InvocationContext}
     * @param invocationContext Context of the invocation
     * @return The {@link Role} associate to the annotation of the {@link InvocationContext}
     */
    private static Role getRole(final InvocationContext invocationContext) {
        final Secure annotation = invocationContext.getMethod().getAnnotation(Secure.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

    /**
     * Return the HTTP request of the {@link InvocationContext}
     * @param invocationContext Context of the invocation
     * @return The HTTP request of the {@link InvocationContext}
     */
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

    /**
     * Return the HTTP response of the {@link InvocationContext}
     * @param invocationContext Context of the invocation
     * @return The HTTP response of the {@link InvocationContext}
     */
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
