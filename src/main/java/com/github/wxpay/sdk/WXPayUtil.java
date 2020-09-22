package com.github.wxpay.sdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.wxpay.sdk.WXPayConstants.SignType;

public class WXPayUtil {
	
    // 指定填充模式 
    private static final String CIPHERALGORITHM = "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
	private static final int KEYLENGTH = 2048;
	private static final int RESERVESIZE = 11;
	// 和腾的公钥名称
	private static String PUBLIC_KEY_FILE_NAME = "9951publicPKCS8.pem";
    
	private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            WXPayUtil.getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }

    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        org.w3c.dom.Document document = WXPayXmlUtil.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }


    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key) throws Exception {
        return generateSignedXml(data, key, SignType.MD5);
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名类型
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key, SignType signType) throws Exception {
        String sign = generateSignature(data, key, signType);
        data.put(WXPayConstants.FIELD_SIGN, sign);
        return mapToXml(data);
    }


    /**
     * 判断签名是否正确
     *
     * @param xmlStr XML格式数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
        Map<String, String> data = xmlToMap(xmlStr);
        if (!data.containsKey(WXPayConstants.FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(WXPayConstants.FIELD_SIGN);
        return generateSignature(data, key).equals(sign);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        return isSignatureValid(data, key, SignType.MD5);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) throws Exception {
        if (!data.containsKey(WXPayConstants.FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(WXPayConstants.FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        return generateSignature(data, key, SignType.MD5);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXPayConstants.FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        }
        else if (SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }


    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }


    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据
     * @param key 密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
   /*
	 * 获取公钥
	 * @return {@link String}
	 * @throws Exception
	 *//*
	public static String getPublicKey(){
		SortedMap<String, String> paraMap = new TreeMap<>();
		// 商户号
		paraMap.put("mchid", "1526869951");
		// 随机字符串
		paraMap.put("nonce_str", HttpRequest.getRandomStringByLength(30));
		// 签名
		String sign = HttpRequest.createSign("UTF-8", paraMap);
		// 签名类型	
		paraMap.put("sign_type", "MD5");
		paraMap.put("sign", sign);
		// 获取公钥的地址
		String url = "https://fraud.mch.weixin.qq.com/risk/getpublickey";
		String publicKey = null;
		try {
			// 证书
			WXPay wxPay = new WXPay(WXPayConfigImpl.getInstance());
			// 发送证书的post请求
			publicKey = wxPay.requestWithCert(url, paraMap, 3000, 3000);
			// 将XML数据转换成Map
			Map<String, String> map = XMLUtil.doXMLParse(publicKey);
			// 请求成功获取微信返回的信息
			if(map.get("result_code").toString().equalsIgnoreCase("SUCCESS")){
				publicKey = map.get("pub_key");
				savePubKeyToLocal(publicKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publicKey;
	}
	
	 
     * 将公钥保存到本地
     *
     * @param pubKey
    private static void savePubKeyToLocal(String pubKey) {
        //String classPath = WXPayUtil.class.getClass().getResource("/").getPath();
        String classPath = "/usr/local/elk/";
    	File file = new File(classPath + PUBLIC_KEY_FILE_NAME);
        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);
        Path path = get(absolutePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(pubKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    /**
     * 读取本地公钥
     * 将公钥放入spring容器，这样就不必每次用的时候都要读取
     *
     * @return
     * @throws IOException
     */
    public static String readLocalPubKey() throws IOException {
    	//String classPath = WXPayUtil.class.getClass().getResource("/").getPath();
    	String classPath = "/usr/local/elk/";
        File file = new File(classPath + PUBLIC_KEY_FILE_NAME);
        String absolutePath = file.getAbsolutePath();
        List<String> lines = Files.readAllLines(Paths.get(absolutePath), StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (line.charAt(0) == '-') {
                continue;
            } else {
                sb.append(line);
                sb.append('\r');
            }
        }
        return sb.toString();
    }
	/**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param plainBytes 需加密数据的byte数据
     * @param publicKey  公钥
     * @return 加密后的byte型数据
     */
    public static String encrypt(byte[] plainBytes, PublicKey publicKey) throws Exception {
        int keyByteSize = KEYLENGTH / 8;
        int encryptBlockSize = keyByteSize - RESERVESIZE;
        int nBlock = plainBytes.length / encryptBlockSize;
        if ((plainBytes.length % encryptBlockSize) != 0) {
            nBlock += 1;
        }
        ByteArrayOutputStream outbuf = null;
        try {
            Cipher cipher = Cipher.getInstance(CIPHERALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            outbuf = new ByteArrayOutputStream(nBlock * keyByteSize);
            for (int offset = 0; offset < plainBytes.length; offset += encryptBlockSize) {
                int inputLen = plainBytes.length - offset;
                if (inputLen > encryptBlockSize) {
                    inputLen = encryptBlockSize;
                }
                byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);
                outbuf.write(encryptedBlock);
            }
            outbuf.flush();
            byte[] encryptedData = outbuf.toByteArray();
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e) {
            throw new Exception("ENCRYPT ERROR:", e);
        } finally {
            try {
                if (outbuf != null) {
                    outbuf.close();
                }
            } catch (Exception e) {
                throw new Exception("CLOSE ByteArrayOutputStream ERROR:", e);
            }
        }
    }
    
    /**
     * 根据银行卡名称获取编码(ZZ)
     * @param bankName 银行卡名称
     * @return {@link String}
     */
    public static String getBankCode(String bankName){
		if(bankName == null || "".equals(bankName)){
			return null;
		}
		if(bankName.contains("工商银行")){
			return "1002";
		}else if(bankName.contains("农业银行")){
			return "1005";
		}else if(bankName.contains("建设银行")){
			return "1003";
		}else if(bankName.contains("中国银行")){
			return "1026";
		}else if(bankName.contains("交通银行")){
			return "1020";
		}else if(bankName.contains("招商银行")){
			return "1001";
		}else if(bankName.contains("邮储银行")){
			return "1066";
		}else if(bankName.contains("民生银行")){
			return "1006";
		}else if(bankName.contains("平安银行")){
			return "1010";
		}else if(bankName.contains("中信银行")){
			return "1021";
		}else if(bankName.contains("浦发银行")){
			return "1004";
		}else if(bankName.contains("兴业银行")){
			return "1009";
		}else if(bankName.contains("光大银行")){
			return "1022";
		}else if(bankName.contains("广发银行")){
			return "1027";
		}else if(bankName.contains("华夏银行")){
			return "1025";
		}else if(bankName.contains("宁波银行")){
			return "1056";
		}else if(bankName.contains("北京银行")){
			return "4836";
		}else if(bankName.contains("上海银行")){
			return "1024";
		}else if(bankName.contains("南京银行")){
			return "1054";
		}else if(bankName.contains("长子县融汇村镇银行")){
			return "4755";
		}else if(bankName.contains("长沙银行")){
			return "4216";
		}else if(bankName.contains("浙江泰隆商业银行")){
			return "4051";
		}else if(bankName.contains("中原银行")){
			return "4753";
		}else if(bankName.contains("企业银行(中国)")){
			return "4761";
		}else if(bankName.contains("顺德农商银行")){
			return "4036";
		}else if(bankName.contains("衡水银行")){
			return "4752";
		}else if(bankName.contains("长治银行")){
			return "4756";
		}else if(bankName.contains("大同银行")){
			return "4767";
		}else if(bankName.contains("河南省农村信用社")){
			return "4115";
		}else if(bankName.contains("宁夏黄河农村商业银行")){
			return "4150";
		}else if(bankName.contains("山西省农村信用社")){
			return "4156";
		}else if(bankName.contains("安徽省农村信用社")){
			return "4166";
		}else if(bankName.contains("甘肃省农村信用社")){
			return "4157";
		}else if(bankName.contains("天津农村商业银行")){
			return "4153";
		}else if(bankName.contains("广西壮族自治区农村信用社")){
			return "4113";
		}else if(bankName.contains("陕西省农村信用社")){
			return "4108";
		}else if(bankName.contains("深圳农村商业银行")){
			return "4076";
		}else if(bankName.contains("宁波鄞州农村商业银行")){
			return "4052";
		}else if(bankName.contains("浙江省农村信用社联合社")){
			return "4764";
		}else if(bankName.contains("江苏省农村信用社联合社")){
			return "4217";
		}else if(bankName.contains("江苏紫金农村商业银行股份有限公司")){
			return "4072";
		}else if(bankName.contains("北京中关村银行股份有限公司")){
			return "4769";
		}else if(bankName.contains("星展银行(中国)有限公司")){
			return "4778";
		}else if(bankName.contains("枣庄银行股份有限公司")){
			return "4766";
		}else if(bankName.contains("海口联合农村商业银行股份有限公司")){
			return "4758";
		}else if(bankName.contains("南洋商业银行(中国)有限公司")){
			return "4763";
		}else{
			return null;
		}
	}
    /**
     * 日志
     * @return
     */
    public static Logger getLogger() {
        Logger logger = LoggerFactory.getLogger("wxpay java sdk");
        return logger;
    }

    /**
     * 获取当前时间戳，单位秒
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }
}
