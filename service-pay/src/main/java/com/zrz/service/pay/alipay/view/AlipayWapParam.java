package com.zrz.service.pay.alipay.view;


import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;


/**
 * //TODO 支付宝WAP支付参数
 * @author dengyingxiang
 */

@Getter
@Setter
public class AlipayWapParam implements Serializable {
    private static final long serialVersionUID = 100001L;
	
	private String out_trade_no;//商户网站唯一订单号
	private String total_fee;//交易金额
	private String subject;//商品名称
	private String body;//商品描述
	private String extra_common_param;//公用回传参数
	private String show_url="#";//商品展示网址
	private String notify_url ;//异步回调地址
    private String return_url ;//同步回调地址

}
