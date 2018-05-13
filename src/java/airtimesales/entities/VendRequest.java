/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.entities;

import airtimesales.entities.utils.CommonLibrary;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author manzi
 */
@Entity
@Table(name = "vend_requests")
public class VendRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "transactionId",length=50,nullable = false)
    private String transactionId;
    
    @Column(name = "retailerShortCode", length=50,nullable = false)
    private String retailerShortCode;
    
    @Column(name = "subscriberTelephoneNumber",length=50,nullable = false)
    private String subscriberTelephoneNumber;
    
    @Column(name = "amount")
    private Integer amount;
    
    @Column(name = "resultCode",length=50,nullable = true)
    private String resultCode;
    
    @Column(name = "descr",length=500,nullable = true)
    private String descr;
    
    @Column(name = "requestTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestTime;
    
    public VendRequest() {
        this.transactionId= CommonLibrary.generateRandomLong(10)+"";
    }
    
    public String getId() {
        return getTransactionId();
    }
    
    public void setId(String id) {
        this.setTransactionId(id);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getTransactionId() != null ? getTransactionId().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VendRequest)) {
            return false;
        }
        VendRequest other = (VendRequest) object;
        if ((this.getTransactionId() == null && other.getTransactionId() != null) || (this.getTransactionId() != null && !this.transactionId.equals(other.transactionId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "airtimesales.entities.VendRequest[ transactionId=" + getTransactionId() + " ]";
    }
    
    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
    
    /**
     * @return the retailerShortCode
     */
    public String getRetailerShortCode() {
        return retailerShortCode;
    }
    
    /**
     * @param retailerShortCode the retailerShortCode to set
     */
    public void setRetailerShortCode(String retailerShortCode) {
        this.retailerShortCode = retailerShortCode;
    }
    
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
    
    /**
     * @return the requestTime
     */
    public Date getRequestTime() {
        return requestTime;
    }
    
    /**
     * @param requestTime the requestTime to set
     */
    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * @return the resultCode
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @return the descr
     */
    public String getDescr() {
        return descr;
    }

    /**
     * @param descr the descr to set
     */
    public void setDescr(String descr) {
        this.descr = descr;
    }
    
}
