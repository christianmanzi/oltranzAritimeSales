/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.models.retails;

/**
 *{"code":"200","description":"DONE SUCCESSFULLY","body":{"shortCode":"72725"}}
 * 
 * 
 * @author manzi
 */
public class RetailerByMsisdnResponse {
    private String code;
    private String description;
    private RetailerByMsisdnResponseBody body;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the body
     */
    public RetailerByMsisdnResponseBody getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(RetailerByMsisdnResponseBody body) {
        this.body = body;
    }
}
