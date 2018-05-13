/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtimesales.models.retails;

import java.util.List;

/**
 *"vouchers": [
            "291391688106420"
        ],
        "balance": 464100
 * @author manzi
 */
public class VoucherGenerationResponseBody {
    private List<String> vouchers;
    private Integer balance;

    /**
     * @return the vouchers
     */
    public List<String> getVouchers() {
        return vouchers;
    }

    /**
     * @param vouchers the vouchers to set
     */
    public void setVouchers(List<String> vouchers) {
        this.vouchers = vouchers;
    }

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
}
