package com.hedong.hedongwx.utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;

public class AESUtil {

	public static void main(String[] args) throws Exception {
//		String content = "密码1993";
//		String password = "加密密码";
//		System.out.println("需要加密的内容：" + content);
//		byte[] encrypt = encrypt(content, password);
//		System.out.println("加密后的2进制密文：" + new String(encrypt));
//		String hexStr = parseByte2HexStr(encrypt);
//		System.out.println("加密后的16进制密文:" + hexStr);
//		byte[] byte2 = parseHexStr2Byte(hexStr);
//		System.out.println("加密后的2进制密文：" + new String(byte2));
//		byte[] decrypt = decrypt(byte2, password);
//		System.out.println("解密后的内容：" + new String(decrypt, "utf-8"));
//		int i1 = 59;
//		int i2 = 12;
//		String i1Hex = Integer.toHexString(i1);
//		System.out.println("i1Hex===" + i1Hex);
//		String i2Hex = Integer.toHexString(i2);
//		System.out.println("i2Hex===" + i2Hex);
//		String str = i1Hex + i2Hex;
//		System.out.println("str===" + str);
//		byte[] str2cbcd = String_BCD(str);
//		System.out.println("length===" + str2cbcd.length);
//		for (byte b : str2cbcd) {
//			System.out.print("b=" + b);
//		}
//		System.out.println();
//		String bcd_String = BCD_String(str2cbcd);
//		System.out.println(bcd_String);
		
		String str = "www.tengfuchong.com.cn";
		byte[] bytes = str.getBytes();
		for (byte b : bytes) {
			System.out.println("b===" + b);
		}
				
	}

	/**
	 * AES加密字符串
	 * 
	 * @param content
	 *            需要被加密的字符串
	 * @param password
	 *            加密需要的密码
	 * @return 密文
	 */
	public static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
			kgen.init(128, new SecureRandom(password.getBytes()));// 利用用户密码作为随机数初始化出
			// 加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
			SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
			byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
			byte[] result = cipher.doFinal(byteContent);// 加密
			return result;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密AES加密过的字符串
	 * 
	 * @param content
	 *            AES加密过过的内容
	 * @param password
	 *            加密时的密码
	 * @return 明文
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
			byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
			byte[] result = cipher.doFinal(content);
			return result; // 明文
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 将十进制字符串转换为 BCD编码
	 * @param str
	 * @return
	 */
	public static byte[] String_BCD(String str) {
		byte[] s = str.getBytes();
		byte[] b = new byte[s.length/2];
		for(int i=0;i<b.length;i++) {
			b[i] = (byte) (s[2*i] << 4 | (s[2*i+1] & 0xf));
		}
		return b;
	}
	/**
	 * 将BCD编码的byte数组转换为String
	 * @param bcd
	 * @return
	 */
	public static String BCD_String(byte[] bcd) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<bcd.length;i++) {
			sb.append(bcd[i]>>4&0xf)
			.append(bcd[i]&0xf);
		}
		return sb.toString();
	}
	
	/**
	 * 将BCD编码的byte转换为String
	 * @param bcd
	 * @return
	 */
	public static String byte_BCD_String(byte bcd) {
		StringBuilder sb = new StringBuilder();
			sb.append(bcd>>4&0xf).append(bcd&0xf);
		return sb.toString();
	}

}