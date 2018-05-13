/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.models.subscriber;


import airtimesales.models.retails.ResultGeneralItem;
/**
 *
 * @author manzi
 */

/*
"{"code":"200",
"description":"DONE SUCCESSFULLY",
"body":
{"id":3,"subscriberNumber":"6845","telephoneNumber":"250785534672","amount":100.0,"shortCode":"72725","recordDate":null,"status":"PENDING","retTelephoneNumber":"0786003635"}}

*/
public class SubAirtimeVendInitResponse {
    private String code;
    private String description;
    private ResultGeneralItem body;
    
    

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
    public ResultGeneralItem getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(ResultGeneralItem body) {
        this.body = body;
    }

       
}
