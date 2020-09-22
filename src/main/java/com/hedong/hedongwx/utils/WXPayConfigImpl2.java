package com.hedong.hedongwx.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;

public class WXPayConfigImpl2 extends WXPayConfig{

    private byte[] certData;
    private static WXPayConfigImpl2 INSTANCE;

    private WXPayConfigImpl2() throws Exception{
//        String certPath = "D://tengfuchong/apiclient_cert.p12";
//        String certPath = "D://hedongteyue/apiclient_cert.p12";
//        String certPath = "D://hetengserver/apiclient_cert.p12";
//        String certPath = "D://he360/apiclient_cert.p12";
        //String certPath = "/usr/local/cert1/apiclient_cert.p12";
    	String pathZZ = "/usr/local/elk/apiclient_cert.p12";
        File file = new File(pathZZ);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static WXPayConfigImpl2 getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl2.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl2();
                }
            }
        }
        return INSTANCE;
    }

    public String getAppID() {
        return WeiXinConfigParam.FUWUAPPID;
    }

    public String getMchID() {
        return "1525089101";
    }

    public String getKey() {
        return "51b29nvuphzf81a9oo8yihdh0unp8w1q";
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
        return "fraud.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "fraud.mch.weixin.qq.com";
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
