package com.zrz.service.pay.alipay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zhouruizhong
 * @title: AlipayConfig
 * @project zrz-spring-boot
 * @description TODO
 * @date 2018/11/9 10:33
 */

@Service
@Getter
@Setter
public class AlipayConfig {

    @Value("${alipay.app_id}")
    private String app_id;

    @Value("${alipay.partner}")
    private String partner;

    @Value("${alipay.seller}")
    private String seller;

    @Value("${alipay.seller_email}")
    private String seller_email;

    @Value("${alipay.rsa2_private}")
    private String rsa2_private;

    @Value("${alipay.rsa2_public}")
    private String rsa2_public;

    @Value("${alipay.rsa_private}")
    private String rsa_private;

    @Value("${alipay.rsa_public}")
    private String rsa_public;

    @Value("${alipay.app_order_pay_notify_url}")
    private String app_order_pay_notify_url;

    @Value("${alipay.web_order_pay_notify_url}")
    private String web_order_pay_notify_url;

    @Value("${alipay.web_order_pay_return_url}")
    private String web_order_pay_return_url;

    @Value("${alipay.wap_order_pay_notify_url}")
    private String wap_order_pay_notify_url;

    @Value("${alipay.wap_order_pay_return_url}")
    private String wap_order_pay_return_url;
}
