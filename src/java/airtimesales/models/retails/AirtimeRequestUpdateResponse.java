/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.models.retails;

/**
 *
 * "code": "200",
 * "description": "DONE SUCCESSFULLY",
 * "body": {
 * "id": 1,
 * "status": "CANCELLED"
 * }
 * }
 * 
 * @author manzi
 */
public class AirtimeRequestUpdateResponse {
    private String code;
    private String description;
    private AirtimeRequestUpdateResponseBody body;
    
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
    public AirtimeRequestUpdateResponseBody getBody() {
        return body;
    }
    
    /**
     * @param body the body to set
     */
    public void setBody(AirtimeRequestUpdateResponseBody body) {
        this.body = body;
    }
}
