/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.models.subscriber;

/**
 *
 * @author manzi
 * 
 * {
 * "subscriberNumber":"12113",
 * "telephoneNumber":"8990800",
 * "amount":1000,
 * "shortCode":"12322",
 * "retTelephoneNumber":"22324"
 * }
 * 
 */
public class SubAIrtimeVendIniRequest {
    private String subscriberNumber;
    private String telephoneNumber;
    private Integer amount;
    private String shortCode;
    private String retTelephoneNumber;

    /**
     * @return the subscriberNumber
     */
    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    /**
     * @param subscriberNumber the subscriberNumber to set
     */
    public void setSubscriberNumber(String subscriberNumber) {
        this.subscriberNumber = subscriberNumber;
    }

    /**
     * @return the telephoneNumber
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * @param telephoneNumber the telephoneNumber to set
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
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

    /**
     * @return the shortCode
     */
    public String getShortCode() {
        return shortCode;
    }

    /**
     * @param shortCode the shortCode to set
     */
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    /**
     * @return the retTelephoneNumber
     */
    public String getRetTelephoneNumber() {
        return retTelephoneNumber;
    }

    /**
     * @param retTelephoneNumber the retTelephoneNumber to set
     */
    public void setRetTelephoneNumber(String retTelephoneNumber) {
        this.retTelephoneNumber = retTelephoneNumber;
    }
}
