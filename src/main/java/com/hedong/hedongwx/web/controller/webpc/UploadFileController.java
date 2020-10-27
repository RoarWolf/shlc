package com.hedong.hedongwx.web.controller.webpc;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 上传文件Controller
 */
@RestController
@RequestMapping("/upload")
public class UploadFileController {
	
//	private static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";// <endpoint, http://oss-cn-hangzhou.aliyuncs.com>
//	private static String accessKeyId = "LTAIzIBoI7eqyrG9";// <accessKeyId>
//	private static String accessKeySecret = "mFMFWHWVV77CHv37hIcKS5ofJ1M1Wi";// <accessKeySecret>
//	private static String bucketName = "qiaoxiaoxiannv0209";// <bucketName>需要修改  桶名

//	private static String key = "";// <key>

	private static String endpoint = "https://oss-cn-shanghai.aliyuncs.com";// <endpoint, http://oss-cn-hangzhou.aliyuncs.com>
	private static String accessKeyId = "LTAI4GHLWGjGJhsLCAbZ33xo";// <accessKeyId>
	private static String accessKeySecret = "cG8EqF7azmGvrhkU6AmqAd3Z5Wo6bw";// <accessKeySecret>
	private static String bucketName = "cdzimages";// <bucketName>需要修改  桶名
	
	/**
	 * 上传文件
	 * @param uploadFile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@PostMapping("/uploadFile")
	public Object handleFileUpload(@RequestParam("file") MultipartFile uploadFile) throws Exception {
//		long size = uploadFile.getSize();
//			if(size>2*1024*1024) {
//				respBean = GuoRespBeanUtil.setCustomContentRespBean("图片不允许超过2M");
//				return respBean;
//			}
		// 上传
		long startTime = System.currentTimeMillis();// 开始时间
		String key = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/"
				+ UUID.randomUUID().toString();// oss文件名（Object Name）
//				+ UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);// oss文件名（Object Name）
		/*
		 * Constructs a client instance with your account for accessing OSS
		 */
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		try {
			// new对象元信息
			ObjectMetadata meta = new ObjectMetadata();
			// 设置contentType
			meta.setContentType(uploadFile.getContentType());
			// 上传文件
			PutObjectResult putObject = ossClient.putObject(bucketName, key, uploadFile.getInputStream(), meta);
		} catch (OSSException oe) {
			System.out.println("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
		} catch (ClientException ce) {
			System.out.println("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
		} finally {
			/*
			 * Do not forget to shut down the client finally to release all allocated resources.
			 */
			ossClient.shutdown();
		}
		long endTime = System.currentTimeMillis();// 结束时间
		// 出参
		String baseFilePath = "https://cdzimages.oss-cn-shanghai.aliyuncs.com/";//前半段修改成桶名
		Map<String, Object> respMap = new HashMap<String, Object>();
		respMap.put("uploadFileName", uploadFile.getOriginalFilename());// 文件名称
		respMap.put("uploadFilePath", baseFilePath + key);// 访问路径
		
		System.err.println("\n上传耗时：" + (endTime - startTime)/1000.0 + "s");
		System.err.println("访问路径：" + respMap.get("uploadFilePath"));
		return JSON.toJSON(respMap);
	}


}
