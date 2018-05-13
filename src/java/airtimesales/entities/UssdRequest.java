/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.entities;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aimable
 */
@XmlRootElement(name = "COMMAND")
@XmlAccessorType(XmlAccessType.FIELD)
public class UssdRequest {

    @XmlElement(name = "MSISDN")
    private String msisdn;

    @XmlElement(name = "SESSIONID")
    private BigInteger sessionId;

    @XmlElement(name = "SPID")
    private Integer spId;

    @XmlElement(name = "INPUT")
    private String input;

    @XmlElement(name = "NEWREQUEST")
    private int newRequest;

    public UssdRequest() {
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public BigInteger getSessionId() {
        return sessionId;
    }

    public void setSessionId(BigInteger sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getSpId() {
        return spId;
    }

    public void setSpId(Integer spId) {
        this.spId = spId;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public int getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(int newRequest) {
        this.newRequest = newRequest;
    }

}
