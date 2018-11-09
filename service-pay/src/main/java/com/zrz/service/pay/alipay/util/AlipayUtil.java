package com.zrz.service.pay.alipay.util;

import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.huilv.payment.alipay.constants.AlipayConstant;
import com.huilv.payment.alipay.view.AlipayAppParam;
import com.huilv.payment.alipay.view.AlipayTradeQueryData;
import com.huilv.payment.alipay.view.AlipayTradeQueryParams;

import me.erz.core.exception.HuilvException;
import me.erz.core.util.DateUtils;


public class AlipayUtil {
    
    private static final Logger log = LoggerFactory.getLogger(AlipayUtil.class);
    
    /**
     * bean转map
     * 只支持类型字段（int,integer,string,double,float,date,boolean,short）
     * 其中date 型格式化为（yyyyMMddHHmmss）
    * @Title: 
    * @Description: TODO
    * @param bean
    * @return
    * 
    *
     */
    public static Map<String,String> beanToMap(Object bean) {
        if(bean == null) {
            return null;
        }
        Map<String,String> map = new HashMap<String, String>();
        try {
            Field[] fields = bean.getClass().getDeclaredFields();
            String fieldtype,fieldname;
            Object val;
            String types = "int,integer,string,double,float,date,boolean,short";
            for (Field field : fields) {
                fieldname=field.getName();
                fieldtype = field.getType().toString().toLowerCase();
                fieldtype=fieldtype.substring(fieldtype.lastIndexOf(".")+1);
                if(types.indexOf(fieldtype)!=-1){
                    field.setAccessible(true);
                    val = field.get(bean);
                    if(val == null) {
                        continue;
                    }
                    if(fieldtype.indexOf("date")!=-1){
                        map.put(fieldname, DateUtils.date2String((Date)val, "yyyyMMddHHmmss"));
                    }else{
                        map.put(fieldname, String.valueOf(val));
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("bean转map异常", e);
        }
        return map.isEmpty()?null:map;
    }
    
    /**
     * //TODO web支付参数验证
     * @param params
     * @param key
     * @return
     * @throws HuilvException
     */
    public static Map<String,String> checkPayWebParam(Map<String,String> sParaTemp){
        Map<String, String> result = new HashMap<String, String>();
        result.put("isPass", "true");  //是否验证通过
        
        if(StringUtils.isBlank(sParaTemp.get("partner"))){
            result.put("isPass", "false");
            result.put("msg","合作者身份ID(partner)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("notify_url"))){
            result.put("isPass", "false");
            result.put("msg","服务器异步通知页面路径(notify_url)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("notify_url"))){
            result.put("isPass", "false");
            result.put("msg","服务器异步通知页面路径(notify_url)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("return_url"))){
            result.put("isPass", "false");
            result.put("msg","页面跳转同步通知页面路径(return_url)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("out_trade_no"))){
            result.put("isPass", "false");
            result.put("msg","商户网站唯一订单号(out_trade_no)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("total_fee"))){
            result.put("isPass", "false");
            result.put("msg","交易金额(total_fee)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("seller_email"))){
            result.put("isPass", "false");
            result.put("msg","卖家支付宝账号(seller_email)不能为空");
            return result;
        }
        return result;
    }
    
    /**
     * //TODO wap支付参数验证
     * @param params
     * @param key
     * @return
     * @throws HuilvException
     */
    public static Map<String,String> checkPayWapParam(Map<String,String> sParaTemp){
        Map<String, String> result = new HashMap<String, String>();
        result.put("isPass", "true");  //是否验证通过
        
        if(StringUtils.isBlank(sParaTemp.get("partner"))){
            result.put("isPass", "false");
            result.put("msg","合作者身份ID(partner)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("notify_url"))){
            result.put("isPass", "false");
            result.put("msg","服务器异步通知页面路径(notify_url)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("notify_url"))){
            result.put("isPass", "false");
            result.put("msg","服务器异步通知页面路径(notify_url)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("return_url"))){
            result.put("isPass", "false");
            result.put("msg","页面跳转同步通知页面路径(return_url)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("out_trade_no"))){
            result.put("isPass", "false");
            result.put("msg","商户网站唯一订单号(out_trade_no)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("total_fee"))){
            result.put("isPass", "false");
            result.put("msg","交易金额(total_fee)不能为空");
            return result;
        }
        if(StringUtils.isBlank(sParaTemp.get("seller_id"))){
            result.put("isPass", "false");
            result.put("msg","卖家支付宝账号(seller_id)不能为空");
            return result;
        }
        return result;
    }
    
    public static Map<String,String> checkTradeQueryParam(AlipayTradeQueryParams alipaytradeQueryParams) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("isPass", "true");  //是否验证通过
        
        if(StringUtils.isBlank(alipaytradeQueryParams.getOutTradeNo()) && StringUtils.isBlank(alipaytradeQueryParams.getTradeNo())){
            result.put("isPass", "false");
            result.put("msg","订单支付时传入的商户订单号,和支付宝交易号不能同时为空。");
            return result;
        }
        
        return result;
    }
    
    //获取支付宝POST过来反馈信息
    @SuppressWarnings("rawtypes")
    public static Map<String,String> getNotifyParams(HttpServletRequest request){
        
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        } 
        return params;
    }
    
    
    /**
     * create the order info. 创建APP订单信息
     * 
     */
    public static String getOrderInfo(String partner,String seller,AlipayAppParam alipayAppParam) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + partner + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + seller + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + alipayAppParam.getOut_trade_no() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + alipayAppParam.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + alipayAppParam.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + alipayAppParam.getTotal_fee() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + alipayAppParam.getNotify_url() + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\""+AlipayConstant.SERVICE_APP+"\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
    
    
    /**
     * 输出流
     * 
     */
    public static void writeStream(HttpServletResponse response,String shtmltext )  throws Exception  {

        response.setContentType("text/Html");
        ServletOutputStream out = response.getOutputStream();
        OutputStreamWriter ow = new OutputStreamWriter(out,"UTF-8");
        ow.write("<!DOCTYPE HTML>");
        ow.write("<HTML>");
        ow.write("  <HEAD><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" ><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><TITLE>支付中</TITLE></HEAD>");
        ow.write("  <BODY >");
        ow.write(shtmltext);
        ow.write("  </BODY>");
        ow.write("</HTML>");
        ow.flush();
        ow.close();
    }

    public static AlipayTradeQueryData getTradeInfo(AlipayTradeQueryResponse response, AlipayTradeQueryRequest request, String key) throws HuilvException {
        String code = response.getCode();
        String msg = response.getMsg();
        //通信失败
        if(!response.isSuccess()){
            throw new HuilvException("通信异常：code="+code+" msg="+msg);
        }
        // 非业务调用错误
        if(!StringUtils.equals("10000", code)) {
            String subCode = response.getSubCode();
            String subMsg = response.getSubMsg();
            throw new HuilvException("交易查询失败：code="+code+" subCode="+subCode+" subMsg="+subMsg);
        }
        
        //对象转换
//        Map<String,String> paramMap = AlipayUtil.beanToMap(request);
        
        //校验签名
//        String sign = AlipayUtil.sign(paramMap, key);
//        if(!StringUtils.equals(sign, response.ge)){//签名正确
//            throw new HuilvException("交易查询失败：返回签名校验不正确");
//        }
        
        AlipayTradeQueryData alipayTradeQueryData = new AlipayTradeQueryData();
        BeanUtils.copyProperties(response, alipayTradeQueryData);
        alipayTradeQueryData.setSendPayDate(DateUtils.date2String(response.getSendPayDate(), DateUtils.YYYYMMDDHHMMSS));
        return alipayTradeQueryData;
    }

    public static String sign(Map<String,String> paramMap, String key) {
        return null;
    }
}
