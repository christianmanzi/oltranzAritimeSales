/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.models.subscriber;

import airtimesales.entities.utils.CommonLibrary;

/**
 *Example: {
  "voucher":"",
    "telephoneNumber":"",
    "transactionId":""
}

 * @author manzi
 */
public class VoucherRedeemRequest {
    private String voucher;
    private String telephoneNumber;
    private String transactionId;

    public VoucherRedeemRequest() {
         this.transactionId= CommonLibrary.generateRandomLong(10)+"";
    }

    /**
     * @return the voucher
     */
    public String getVoucher() {
        return voucher;
    }

    /**
     * @param voucher the voucher to set
     */
    public void setVoucher(String voucher) {
        this.voucher = voucher;
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
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    
}
