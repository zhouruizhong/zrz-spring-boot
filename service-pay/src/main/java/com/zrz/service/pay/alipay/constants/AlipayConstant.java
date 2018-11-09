package com.zrz.service.pay.alipay.constants;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConstant {

	// 签名方式
	public static String sign_type = "MD5";
	
	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "C:\\";
		
	// 字符编码格式 目前支持utf-8
	public static String input_charset = "UTF-8";
		
	// 支付类型 ，无需修改
	public static String payment_type = "1";
		
	// web支付调用的接口名，无需修改
	public static String SERVICE_WEB = "create_direct_pay_by_user";
	
	//有密退订接口
	public static String SERVICE_REFUND = "refund_fastpay_by_platform_pwd";
	
	// batch批量支付调用的接口名，无需修改
    public static String SERVICE_BATCH = "batch_trans_notify";

    // wap支付调用的接口名，无需修改
    public static String SERVICE_WAP = "alipay.wap.create.direct.pay.by.user";
    
    // app支付调用的接口名，无需修改
    public static String SERVICE_APP = "mobile.securitypay.pay";
    
 
    
    // 支付宝的公钥，无需修改该值
    public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

//    //支付宝批量付款参数
//    public static String batch_partner = "2088811743213253";
//    public static String batch_key = "xkck44bqrossxpnrawqpnxfz429hyo4b";
//    public static String batch_seller_email = "gzhuilv@gototw.com.cn";
//    public static String batch_notify_url = "localhost:8883";
}

