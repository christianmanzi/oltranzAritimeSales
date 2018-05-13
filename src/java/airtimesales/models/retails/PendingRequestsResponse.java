/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.models.retails;

import java.util.List;

/**
 *
 * @author manzi
 */


/*
{"code":"200",
"description":"DONE SUCCESSFULLY",
"body":[{"id":1,"subscriberNumber":"19166","telephoneNumber":"250734483825","amount":100.0,"shortCode":"72725","recordDate":"2018-05-12 06:52:50","status":"PENDING","retTelephoneNumber":"250786003635"}]}"
*/
public class PendingRequestsResponse {
    private String code;
    private String description;
    private List<ResultGeneralItem> body;

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
    public List<ResultGeneralItem> getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(List<ResultGeneralItem> body) {
        this.body = body;
    }

  
}


