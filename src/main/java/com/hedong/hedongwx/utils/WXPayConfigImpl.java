package com.hedong.hedongwx.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;

public class WXPayConfigImpl extends WXPayConfig{

    private byte[] certData;
    private static WXPayConfigImpl INSTANCE;

    private WXPayConfigImpl() throws Exception{
//        String certPath = "D://tengfuchong/apiclient_cert.p12";
//        String certPath = "D://hedongteyue/apiclient_cert.p12";
//        String certPath = "D://hetengserver/apiclient_cert.p12";
//        String certPath = "D://he360/apiclient_cert.p12";
        String certPath = "/usr/local/cert1/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static WXPayConfigImpl getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

    public String getAppID() {
        return WeiXinConfigParam.FUWUAPPID;
    }

    public String getMchID() {
        return WeiXinConfigParam.MCHID;
    }

    public String getKey() {
        return WeiXinConfigParam.KEY;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    public IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum() {
        return 1;
    }

    @Override
    public int getReportBatchSize() {
        return 2;
    }
}
