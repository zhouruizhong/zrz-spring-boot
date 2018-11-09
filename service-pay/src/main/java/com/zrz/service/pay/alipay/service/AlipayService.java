package com.zrz.service.pay.alipay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zrz.service.pay.alipay.view.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhouruizhong
 * @title: AlipayService
 * @project zrz-spring-boot
 * @description TODO
 * @date 2018/11/9 10:58
 */
public interface AlipayService {

    /**
     * @Description: 手机网站支付
     * @param httpRequest
     * @param httpResponse
     * @param alipayWapParam
     * @throws Exception
     * @Author:dengyingxiang 2018年1月25日下午9:09:19
     *
     */
    public void alipayWap(HttpServletRequest httpRequest, HttpServletResponse httpResponse , AlipayWapParam alipayWapParam)  throws Exception;

    /**
     * @Description:电脑网站支付
     * @param httpRequest
     * @param httpResponse
     * @param alipayWebParam
     * @throws Exception
     * @Author:dengyingxiang 2017年11月10日上午11:26:49
     *
     */
    public void alipayWeb(HttpServletRequest httpRequest, HttpServletResponse httpResponse, AlipayWebParam alipayWebParam) throws Exception;

    /**
     * 支付宝APP支付接口
     * @param alipayAppParam 请求参数集合
     * @return 表单提交HTML信息
     */
    public Map<String,String> alipayApp(AlipayAppParam alipayAppParam);


    /**
     * 获取支付宝同步回调参数
     * @param request 请求参数集合
     * @return 表单提交HTML信息
     */
    public Map<String,String> getAlipayNotifyParams(HttpServletRequest request,HttpServletResponse response) throws Exception;

    /**
     * //TODO 验证支付宝回调参数
     * @param params
     * @return
     */
    public boolean checkAlipayParams(String payment,Map<String,String> params);

    /**
     * @Description:验签
     * @param params
     * @return
     * @Author:dengyingxiang 2017年11月10日下午2:34:38
     *
     */
    public  boolean rsaCheckV1(Map<String,String> params);

    /**
     * @Description:验签
     * @param params
     * @return
     * @Author:dengyingxiang 2017年11月10日下午2:34:38
     *
     */
    public  boolean rsaCheckV2(Map<String,String> params);

    /**
     * @Description:验证支付宝电脑网站支付异步回调参数
     * @param params
     * @return
     * @Author:dengyingxiang 2017年11月10日下午2:34:38
     *
     */
    public boolean checkAlipayWebNotifyParams(Map<String,String> params);

    /**
     * @Description:验证支付宝手机网站支付异步回调参数
     * @param params
     * @return
     * @Author:dengyingxiang 2017年11月10日下午2:34:38
     *
     */
    public boolean checkAlipayWapNotifyParams(Map<String,String> params);

    /**
     * //TODO 统一收单交易退款接口
     * @param alipayTradeRefundParams
     * @return AlipayTradeRefundResponse
     */
    public AlipayTradeRefundResponse alipayTradeRefund(AlipayTradeRefundParams alipayTradeRefundParams) throws AlipayApiException, AlipayApiException;

    /**
     * //TODO 批量付款接口
     * @param sTemp
     * @return
     */
    public String batch_trans_notify(Map<String, String> sTemp);

    /**
     *
     * @description: 支付宝交易查询
     * @author: zhou rui zhong
     * @date:   2018年3月28日下午6:01:42
     * @return  AlipayTradeQueryData
     * @throws
     */
    public AlipayTradeQueryData tradeQuery(AlipayTradeQueryParams alipayTradeQueryParams) throws Exception ;
}
