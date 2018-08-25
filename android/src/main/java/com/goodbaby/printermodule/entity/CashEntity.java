package com.goodbaby.printermodule.entity;

/**
 * Created by limingguang on 2018/8/24.
 */

public class CashEntity {
    /**
     * 付款方式
     */
    private String payType;
    /**
     * 付款金额
     */
    private double payAmount;

    private boolean returnCashSign;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public boolean isReturnCashSign() {
        return returnCashSign;
    }

    public void setReturnCashSign(boolean returnCashSign) {
        this.returnCashSign = returnCashSign;
    }
}
