package com.zrz.service.pay.alipay.view;


import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;


/**
 * //TODO 支付宝退订vo
 * @author dengyingxiang
 */

@Getter
@Setter
public class AlipayWebRefundVo implements Serializable {
    private static final long serialVersionUID = 100001L;
	
	private String tradeNo = "";  //支付宝交易号
	private String refundFee;     //退订金额
	private String remark = "";   //退款理由

}
