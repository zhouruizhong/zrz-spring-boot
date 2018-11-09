package com.zrz.service.pay.alipay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zrz.service.pay.alipay.config.AlipayBatchConfig;
import com.zrz.service.pay.alipay.config.AlipayConfig;
import com.zrz.service.pay.alipay.constants.AlipayConstant;
import com.zrz.service.pay.alipay.service.AlipayService;
import com.zrz.service.pay.alipay.util.*;
import com.zrz.service.pay.alipay.view.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouruizhong
 * @title: AliPayServiceImpl
 * @project zrz-spring-boot
 * @description TODO
 * @date 2018/11/9 11:00
 */
@Service
public class AlipayServiceImpl implements AlipayService {

    private static Log log = LogFactory.getLog(AlipayServiceImpl.class);

    private static final String ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";
    private static final String CHARSET = "UTF-8";
    //    private static final String SIGN_TYPE_RSA = "RSA";
    private static final String SIGN_TYPE_RSA2 = "RSA2";

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private AlipayBatchConfig alipayBatchConfig;

    @Override
    public void alipayWap(HttpServletRequest httpRequest,
                          HttpServletResponse httpResponse , AlipayWapParam alipayWapParam)  throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(ALIPAY_GATEWAY, alipayConfig.getApp_id(), alipayConfig.getRsa2_private(), "json", CHARSET, alipayConfig.getRsa2_public(), SIGN_TYPE_RSA2); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(alipayWapParam.getReturn_url());
        alipayRequest.setNotifyUrl(alipayWapParam.getNotify_url());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+alipayWapParam.getOut_trade_no()+"\"," +
                "    \"product_code\":\"QUICK_WAP_PAY\"," +
                "    \"total_amount\":\""+alipayWapParam.getTotal_fee()+"\"," +
                "    \"subject\":\""+alipayWapParam.getSubject()+"\"," +
                "    \"body\":\""+alipayWapParam.getBody()+"\"," +
                "    \"passback_params\":\""+alipayWapParam.getExtra_common_param()+"\"" +
                "  }");//填充业务参数
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        log.info("--alipayWeb form--\r" + form);
        AlipayUtil.writeStream(httpResponse, form);
    }


    @Override
    public void alipayWeb(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AlipayWebParam alipayWebParam) throws Exception {

        AlipayClient alipayClient = new DefaultAlipayClient(ALIPAY_GATEWAY,alipayConfig.getApp_id(),alipayConfig.getRsa2_private(),"json",CHARSET,alipayConfig.getRsa2_public(),SIGN_TYPE_RSA2);
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();// 创建API对应的request
        alipayRequest.setReturnUrl(alipayWebParam.getReturn_url());
        alipayRequest.setNotifyUrl(alipayWebParam.getNotify_url());// 在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+alipayWebParam.getOut_trade_no()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":\""+alipayWebParam.getTotal_fee()+"\"," +
                "    \"subject\":\""+alipayWebParam.getSubject()+"\"," +
                "    \"body\":\""+alipayWebParam.getBody()+"\"," +
                "    \"passback_params\":\""+alipayWebParam.getExtra_common_param()+"\"" +
                "  }");//填充业务参数

        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        log.info("--alipayWeb form--\r"+form);
        AlipayUtil.writeStream(httpResponse, form);
    }


    /**
     * 批量支付接口
     */
    @Override
    public String batch_trans_notify(Map<String, String> sTemp){
        //String batch_no,String batch_fee,String batch_num,String detail_data){
        //服务器异步通知页面路径
        String notify_url = alipayBatchConfig.getBatch_notify_url();	//param
        //需http://格式的完整路径，不允许加?id=123这类自定义参数
        //付款账号
        String email = alipayBatchConfig.getBatch_seller_email();
        //必填
        //付款账户名
        String account_name = "广州慧旅信息科技有限公司";
        //必填，个人支付宝账号是真实姓名公司支付宝账号是公司名称
        //付款当天日期
        String pay_date = UtilDate.getDate();
        //必填，格式：年[4位]月[2位]日[2位]，如：20100801
        //批次号
        String batch_no = sTemp.get("batch_no"); //"201507130000019";
        //必填，格式：当天日期[8位]+序列号[3至16位]，如：201008010000001

        //付款总金额
        String batch_fee = sTemp.get("batch_fee");//"1.00";
        //必填，即参数detail_data的值中所有金额的总和

        //付款笔数
        String batch_num = sTemp.get("batch_num");// "1";
        //必填，即参数detail_data的值中，“|”字符出现的数量加1，最大支持1000笔（即“|”字符出现的数量999个）

        //付款详细数据
        String detail_data = sTemp.get("detail_data"); //"20150713012^gzchongdong@163.com^广州虫洞网络科技有限公司^1.00^慧旅转虫洞账户接口调试";
        //必填，格式：流水号1^收款方帐号1^真实姓名^付款金额1^备注说明1|流水号2^收款方帐号2^真实姓名^付款金额2^备注说明2....
        //扩展字段
        String extend_param= sTemp.get("extend_param");//"agent^123456|aa^123123;

        try {//处理传输数据乱码，导致支付页面报错问题
            extend_param = new String(extend_param.getBytes("UTF-8"),"UTF-8");
        } catch (Exception e) {
            log.info("extend_param参数编码转换："+extend_param);
        }

        Map<String, String> sParaTemp=new HashMap<String, String>();
        //把请求参数打包成数组
        sParaTemp.put("service", AlipayConstant.SERVICE_BATCH);
        sParaTemp.put("partner", alipayBatchConfig.getBatch_partner());
        sParaTemp.put("_input_charset", AlipayConstant.input_charset);
        sParaTemp.put("notify_url", notify_url);
        sParaTemp.put("email", email);
        sParaTemp.put("account_name", account_name);
        sParaTemp.put("pay_date", pay_date);
        sParaTemp.put("batch_no", batch_no);
        sParaTemp.put("batch_fee", batch_fee);
        sParaTemp.put("batch_num", batch_num);
        sParaTemp.put("detail_data", detail_data);
        sParaTemp.put("extend_param", extend_param);
        String result=null;
        try {
            result =  AlipaySubmit.buildRequest(sParaTemp,"get","确认",alipayBatchConfig.getBatch_key());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }


    /**
     * //获取20150713格式的日期
     * @return
     */
    public static String GetNowDate_8(){
        String temp_str="";
        Date dt = new Date();
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        temp_str=sdf.format(dt);
        return temp_str;
    }


    /**
     * 支付宝APP支付接口
     * @param alipayAppParam 请求参数集合
     * @return 表单提交HTML信息
     */
    @Override
    public  Map<String,String> alipayApp(AlipayAppParam alipayAppParam){

        //构造参数
        String orderInfo = AlipayUtil.getOrderInfo( alipayConfig.getPartner(), alipayConfig.getSeller(), alipayAppParam);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = RSA.sign(orderInfo, alipayConfig.getRsa_private());
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
        Map<String,String> map = new HashMap<String, String>();
        map.put("payInfo", payInfo);
        return map;
    }

    @Override
    public Map<String,String> getAlipayNotifyParams(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String,String> params = AlipayUtil.getNotifyParams(request);
        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

        params.put("out_trade_no", out_trade_no);
        params.put("trade_no", trade_no);
        params.put("trade_status", trade_status);
        return params;

    }

    @Override
    public boolean checkAlipayParams(String payment,Map<String,String> params){
        //交易状态
        String trade_status = params.get("trade_status");

        String partner = "";
        String key = "";

        if("ALIPAY_APP".equals(payment)){
            partner = alipayConfig.getPartner();
            key = alipayConfig.getRsa_public();
        }

        if("batch_trans_notify".equals(payment)){
            partner = alipayBatchConfig.getBatch_partner();
            key = alipayBatchConfig.getBatch_key();

            if(AlipayNotify.verify(params,key, partner)){//由于批量付款，并没有TRADE_FINISHED或TRADE_SUCCESS字段返回，因此在这里单独定义
                return true;
            }else{
                return false;
            }
        }


        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        if(AlipayNotify.verify(params,key, partner)){//验证成功
            if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
                return true;
            }
        }
        return false;
    }

    @Override
    public  boolean rsaCheckV1(Map<String,String> params){
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getRsa2_public(), CHARSET, SIGN_TYPE_RSA2);//调用SDK验证签名
        } catch (AlipayApiException e) {
            log.error("", e);
        }
        return signVerified;
    }

    @Override
    public  boolean rsaCheckV2(Map<String,String> params){
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getRsa2_public(), CHARSET, SIGN_TYPE_RSA2);//调用SDK验证签名
        } catch (AlipayApiException e) {
            log.error("", e);
        }
        return signVerified;
    }

    @Override
    public boolean checkAlipayWebNotifyParams(Map<String,String> params){
        //交易状态
        String app_id = params.get("app_id");
        String seller_id = params.get("seller_id");
        if(StringUtils.isNotBlank(app_id) && app_id.equals(alipayConfig.getApp_id())
                && StringUtils.isNotBlank(seller_id) && seller_id.equals(alipayConfig.getPartner())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean checkAlipayWapNotifyParams(Map<String,String> params){
        //交易状态
        String app_id = params.get("app_id");
        String seller_email = params.get("seller_email");
        if(StringUtils.isNotBlank(app_id) && app_id.equals(alipayConfig.getApp_id())
                && StringUtils.isNotBlank(seller_email) && seller_email.equals(alipayConfig.getSeller_email())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public AlipayTradeRefundResponse alipayTradeRefund(AlipayTradeRefundParams alipayTradeRefundParams) throws AlipayApiException{

        AlipayClient alipayClient = new DefaultAlipayClient(ALIPAY_GATEWAY,alipayConfig.getApp_id(),alipayConfig.getRsa2_private(),"json",CHARSET,alipayConfig.getRsa2_public(),"RSA2");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+ alipayTradeRefundParams.getOutTradeNo() +"\"," +
                "    \"trade_no\":\""+ alipayTradeRefundParams.getTradeNo() +"\"," +
                "    \"refund_amount\":\""+ alipayTradeRefundParams.getRefundAmount() +"\"," +
                "    \"refund_reason\":\""+ alipayTradeRefundParams.getRefundReason() +"\"," +
                "    \"out_request_no\":\""+ alipayTradeRefundParams.getOutRequestNo() +"\"," +
                "    \"operator_id\":\""+ alipayTradeRefundParams.getOperatorId() +"\"," +
                "    \"store_id\":\""+ alipayTradeRefundParams.getStoreId() +"\"," +
                "    \"terminal_id\":\""+ alipayTradeRefundParams.getTerminalId() +"\"" +
                "  }");
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            log.info("调用成功");
        } else {
            log.info("调用失败");
        }
        return response;
    }


    @Override
    public AlipayTradeQueryData tradeQuery(AlipayTradeQueryParams alipaytradeQueryParams) throws Exception {
        // 参数验证
        Map<String, String> checkResult = AlipayUtil.checkTradeQueryParam(alipaytradeQueryParams);
        if ("false".equals(checkResult.get("isPass"))) {
            throw new Exception("支付宝交易查询参数错误:" + checkResult.get("msg"));
        }

        AlipayClient alipayClient = new DefaultAlipayClient(ALIPAY_GATEWAY, alipayConfig.getApp_id(), alipayConfig.getRsa2_private(), "json", CHARSET,
                alipayConfig.getRsa2_public(), SIGN_TYPE_RSA2);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" + "\"out_trade_no\":\"" + alipaytradeQueryParams.getOutTradeNo() + "\"," + "\"trade_no\":\"\"" + "  }");
        log.info("支付宝查询参数：" + JsonUtils.toJson(request));

        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            log.info("调用成功");
        } else {
            log.info("调用失败");
        }
        AlipayTradeQueryData alipayTradeQueryData = AlipayUtil.getTradeInfo(response, request, alipayConfig.getRsa2_private());
        alipayTradeQueryData.setIncomeAccount(alipayConfig.getSeller());
        return alipayTradeQueryData;
    }
}
