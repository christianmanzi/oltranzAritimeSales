/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.models.validations;

/**
 *
 * @author manzi
 */

public class ValidationResponseModel {
    private String statusCode;
    private String statusDescr;
    private String message;

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the statusDescr
     */
    public String getStatusDescr() {
        return statusDescr;
    }

    /**
     * @param statusDescr the statusDescr to set
     */
    public void setStatusDescr(String statusDescr) {
        this.statusDescr = statusDescr;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
