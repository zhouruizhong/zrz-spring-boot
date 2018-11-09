package com.zrz.service.pay.alipay.view;


import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;


/**
 * //TODO 支付宝退订入参
 * @author dengyingxiang
 */

@Getter
@Setter
public class AlipayTradeRefundParams implements Serializable {
    private static final long serialVersionUID = 100001L;
	
	private String outTradeNo;  //订单支付时传入的商户订单号,不能和 trade_no同时为空
	private String tradeNo;     //支付宝交易号，和商户订单号不能同时为空
	private String refundAmount ;   //需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
	private String refundReason = "";   //退款的原因说明
	private String outRequestNo = "";   //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
	private String operatorId = "";   //商户的操作员编号
	private String storeId = "";   //商户的门店编号
	private String terminalId = "";   //商户的终端编号

}
