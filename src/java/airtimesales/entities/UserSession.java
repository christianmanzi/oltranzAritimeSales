/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package airtimesales.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aimable
 */
@Entity
@Table(name = "user_session")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserSession.findAll", query = "SELECT u FROM UserSession u"),
    @NamedQuery(name = "UserSession.findByMsisdn", query = "SELECT u FROM UserSession u WHERE u.msisdn = :msisdn"),
    @NamedQuery(name = "UserSession.findByCreationDatetime", query = "SELECT u FROM UserSession u WHERE u.creationDatetime = :creationDatetime")})
public class UserSession implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
    @Id
    @Column(name = "sessionId")
    private String sessionId;
    @Size(min = 1, max = 12)
    
    @Column(name = "creation_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDatetime;
    
    @Column(name = "msisdn")
    private String msisdn;
    
    @Column(name = "step1")
    private String step1;
    @Column(name = "step2")
    private String step2;
    @Column(name = "step3")
    private String step3;
    @Column(name = "step4")
    private String step4;
    @Column(name = "step5")
    private String step5;
    @Column(name = "step6")
    private String step6;
    @Column(name = "step7")
    private String step7;
    @Column(name = "step8")
    private String step8;
    
    
    public UserSession() {
    }
    
    public UserSession(String msisdn) {
        this.msisdn = msisdn;
    }
    
    public UserSession(String msisdn, Date creationDatetime) {
        this.msisdn = msisdn;
        this.creationDatetime = creationDatetime;
    }
    
    public String getMsisdn() {
        return msisdn;
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    
    public Date getCreationDatetime() {
        return creationDatetime;
    }
    
    public void setCreationDatetime(Date creationDatetime) {
        this.creationDatetime = creationDatetime;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (msisdn != null ? msisdn.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserSession)) {
            return false;
        }
        UserSession other = (UserSession) object;
        if ((this.msisdn == null && other.msisdn != null) || (this.msisdn != null && !this.msisdn.equals(other.msisdn))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.oltranz.khenzussd.beans.UserSession[ msisdn=" + msisdn + " ]";
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the step1
     */
    public String getStep1() {
        return step1;
    }

    /**
     * @param step1 the step1 to set
     */
    public void setStep1(String step1) {
        this.step1 = step1;
    }

    /**
     * @return the step2
     */
    public String getStep2() {
        return step2;
    }

    /**
     * @param step2 the step2 to set
     */
    public void setStep2(String step2) {
        this.step2 = step2;
    }

    /**
     * @return the step3
     */
    public String getStep3() {
        return step3;
    }

    /**
     * @param step3 the step3 to set
     */
    public void setStep3(String step3) {
        this.step3 = step3;
    }

    /**
     * @return the step4
     */
    public String getStep4() {
        return step4;
    }

    /**
     * @param step4 the step4 to set
     */
    public void setStep4(String step4) {
        this.step4 = step4;
    }

    /**
     * @return the step5
     */
    public String getStep5() {
        return step5;
    }

    /**
     * @param step5 the step5 to set
     */
    public void setStep5(String step5) {
        this.step5 = step5;
    }

    /**
     * @return the step6
     */
    public String getStep6() {
        return step6;
    }

    /**
     * @param step6 the step6 to set
     */
    public void setStep6(String step6) {
        this.step6 = step6;
    }

    /**
     * @return the step7
     */
    public String getStep7() {
        return step7;
    }

    /**
     * @param step7 the step7 to set
     */
    public void setStep7(String step7) {
        this.step7 = step7;
    }

    /**
     * @return the step8
     */
    public String getStep8() {
        return step8;
    }

    /**
     * @param step8 the step8 to set
     */
    public void setStep8(String step8) {
        this.step8 = step8;
    }
    
}
