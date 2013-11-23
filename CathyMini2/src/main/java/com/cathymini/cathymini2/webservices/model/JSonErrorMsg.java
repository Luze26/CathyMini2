/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cathymini.cathymini2.webservices.model;

/**
 * The class {@link JSonErrorMsg} is thrown when an EJB session bean throws an exception
 * This exception will be used by the facade to return a JSon object representing the error
 * @author Kraiss
 */
public class JSonErrorMsg extends Exception {
    /** Error title */
    String title;
    
    /** Error message */
    String message;

    /**
     * Construct a new {@link JSonErrorMsg} exception
     * @param title Error title
     * @param message Error message
     */
    public JSonErrorMsg(String title, String message) {
        this.title = title;
        this.message = message;
    }
    
    @Override
    /**
     * The message return is a JSon Object 
     */
    public String getMessage() {
        String error = "\"title\":"+title;
        error += ",\"message\":"+message;
        
        return "{"+error+"}";
    }
}
