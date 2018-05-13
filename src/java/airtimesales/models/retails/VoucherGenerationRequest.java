/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.models.retails;

/**
 *
 * @author manzi
 * {
    "subscriberTelephoneNumber":"",
"amount":
}

 */
public class VoucherGenerationRequest {
    private String  subscriberTelephoneNumber;
    private Integer amount;

    /**
     * @return the subscriberTelephoneNumber
     */
    public String getSubscriberTelephoneNumber() {
        return subscriberTelephoneNumber;
    }

    /**
     * @param subscriberTelephoneNumber the subscriberTelephoneNumber to set
     */
    public void setSubscriberTelephoneNumber(String subscriberTelephoneNumber) {
        this.subscriberTelephoneNumber = subscriberTelephoneNumber;
    }

    /**
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
