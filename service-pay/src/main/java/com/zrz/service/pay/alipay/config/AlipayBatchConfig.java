package com.zrz.service.pay.alipay.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class AlipayBatchConfig {
    
    @Value("${alipay_batch.batch_key}")
    private String batch_key;
    
    @Value("${alipay_batch.batch_notify_url}")
    private String batch_notify_url;
    
    @Value("${alipay_batch.batch_partner}")
    private String batch_partner;
    
    @Value("${alipay_batch.batch_seller_email}")
    private String batch_seller_email;
    
}
