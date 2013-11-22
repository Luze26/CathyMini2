/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices.model;

/**
 *
 * @author Kraiss
 */
public class JSonErrorMsg extends Exception {
    String title;
    String message;

    public JSonErrorMsg(String title, String message) {
        this.title = title;
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        String error = "\"title\":"+title;
        error += ",\"message\":"+message;
        
        return "{"+error+"}";
    }
}
