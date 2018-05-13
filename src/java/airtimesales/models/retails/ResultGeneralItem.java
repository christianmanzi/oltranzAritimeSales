/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.models.retails;

/**
 *
 * @author manzi
 */
public class ResultGeneralItem {
    private Integer balance;
    private String shortCode;
    private Integer id;
    private String subscriberNumber;
    private String telephoneNumber;
    private Integer amount;
    private String recordDate;
    private String status;
    private String retTelephoneNumber;

    /**
     * @return the balance
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
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
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
     * @return the recordDate
     */
    public String getRecordDate() {
        return recordDate;
    }

    /**
     * @param recordDate the recordDate to set
     */
    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
