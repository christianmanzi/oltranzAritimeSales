/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.models.retails;

/**
 *{
    "code": "200",
    "description": "DONE SUCCESSFULLY",
    "body": {
        "vouchers": [
            "291391688106420"
        ],
        "balance": 464100
    }
}

 * @author manzi
 */
public class VoucherGenerationResponse {
    private String code;
    private String description;
    private VoucherGenerationResponseBody body;

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
    public VoucherGenerationResponseBody getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(VoucherGenerationResponseBody body) {
        this.body = body;
    }
}
