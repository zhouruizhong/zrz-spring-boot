package com.zrz.service.pay.alipay.view;

import java.io.Serializable;

import lombok.Data;

public @Data class AlipayTradeQueryData implements Serializable {

    /** 
    * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
    */ 
    private static final long serialVersionUID = 1L;

    /**
     * 网关返回码 10000 接口调用成功
     */
    private String code;
    /**
     * 网关返回码描述 Success 接口调用成功
     */
    private String msg;
    /**
     * 支付宝交易号 2013112011001004330000121536
     */
    private String tradeNo;
    /**
     * 商家订单号 6823789339978248
     */
    private String outTradeNo;
    /**
     * 交易状态：
     * WAIT_BUYER_PAY（交易创建，等待买家付款）
     * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
     * TRADE_SUCCESS（交易支付成功）
     * TRADE_FINISHED（交易结束，不可退款）
     */
    private String tradeStatus;
    /**
     * 交易的订单金额，单位为元，两位小数。该参数的值为支付时传入的total_amount 示例值 88.88
     */
    private String totalAmount;
    /**
     * 本次交易打款给卖家的时间 示例值 2014-11-27 15:45:57
     */
    private String sendPayDate;
    /**
     * 买家支付宝账号 159****5620
     */
    private String buyerLogonId;
    /**
     * 收款账号
     */
    private String incomeAccount;
}
