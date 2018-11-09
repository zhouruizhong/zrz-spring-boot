package com.zrz.service.pay.alipay.view;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class AlipayBatchParam {
	
	String account_name; //= "案例maomao";
	String batch_no; // = sTemp.get("batch_no"); 
	String batch_fee; // = sTemp.get("batch_fee");
	String batch_num ; //= sTemp.get("batch_num");
	String detail_data ; //= sTemp.get("detail_data");
	String extend_param; //= sTemp.get("extend_param");
	
	
	
}
