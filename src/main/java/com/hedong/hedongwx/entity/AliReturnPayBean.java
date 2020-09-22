package com.hedong.hedongwx.entity;

import java.io.Serializable;

public class AliReturnPayBean implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 开发者的app_id
     */
    private String app_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 签名
     */
    private String sign;

    /**
     * 交易状态
     */
    private String trade_status;

    /**
     *  支付宝交易号
     */
    private String trade_no;

    /**
     * 交易的金额
     */
    private String total_amount;

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    @Override
    public String toString() {
        return "AliReturnPayBean [app_id=" + app_id + ", out_trade_no=" + out_trade_no + ", sign=" + sign
                + ", trade_status=" + trade_status + ", trade_no=" + trade_no + "]";
    }
}
