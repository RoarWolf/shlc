package com.hedong.hedongwx.web.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.FileUtil;
import com.hedong.hedongwx.utils.SSHUtil;

@Controller
@RequestMapping("/fileDispose")
public class FileDisposeController {
	
	/**
	 * 文件上传类
	 * 文件会自动绑定到MultipartFile中
	 * @param request 获取请求信息
	 * @param description 文件描述
	 * @param file 上传的文件
	 * @return 上传成功或失败结果
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@PostMapping("/upload")
	@ResponseBody
	public String upload(HttpServletRequest request, 
//			@RequestParam("description") String description,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		
		// 获取文件描述参数 description，纯粹测试使用
//		System.out.println("description = " + description);
		
		// 测试MultipartFile接口的各个方法
		System.out.println("文件类型ContentType=" + file.getContentType());
		System.out.println("文件组件名称Name=" + file.getName());
		System.out.println("文件原名称OriginalFileName=" + file.getOriginalFilename());
		System.out.println("文件大小Size=" + file.getSize()/1024 + "KB");
		
		// 如果文件不为空，写入上传路径，进行文件上传
		if (!file.isEmpty()) {
			
			// 构建上传文件的存放路径
			String path = "/usr/local/uploadfile/";
			System.out.println("path = " + path);
			
			// 获取上传的文件名称，并结合存放路径，构建新的文件名称
			String filename = "HD7KW.bin";
			File filepath = new File(path, filename);
			
			// 判断路径是否存在，不存在则新创建一个
			if (!filepath.getParentFile().exists()) {
				filepath.getParentFile().mkdirs();
			}
			
			// 将上传文件保存到目标文件目录
			file.transferTo(new File(path + File.separator + filename));
			return "success";
		} else {
			return "error";
		}
	}
	
	@RequestMapping("exportVehicleInfo")
    public void exportVehicleInfo(HttpServletRequest req, HttpServletResponse resp) {
        String filename = req.getParameter("filename");
        DataInputStream in = null;
        OutputStream out = null;
        try{
            resp.reset();// 清空输出流
            
            String resultFileName = filename + System.currentTimeMillis() + ".txt";
            resultFileName = URLEncoder.encode(resultFileName,"UTF-8");  
            resp.setCharacterEncoding("UTF-8");  
            resp.setHeader("Content-disposition", "attachment; filename=" + resultFileName);// 设定输出文件头
            resp.setContentType("application/msexcel");// 定义输出类型
            //输入流：本地文件路径
            in = new DataInputStream(
                    new FileInputStream(new File("/log2alipay_log_1590744213997.txt")));  
            //输出流
            out = resp.getOutputStream();
            //输出文件
            int bytes = 0;
            byte[] bufferOut = new byte[1024];  
            while ((bytes = in.read(bufferOut)) != -1) {  
                out.write(bufferOut, 0, bytes);  
            }
        } catch(Exception e){
            e.printStackTrace();
            resp.reset();
            try {
                OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");  
                String data = "<script language='javascript'>alert(\"\\u64cd\\u4f5c\\u5f02\\u5e38\\uff01\");</script>";
                writer.write(data); 
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
	
	@RequestMapping("/downloadfile")
	@ResponseBody
	public Map<String,Object> downloadFileNet(String code, String date) {
		System.out.println("date===" + date);
		try {
			SSHUtil.copyFile("/wolfdevicelogs/device" + code + "-" + date + ".log", "D:");
			return CommUtil.responseBuildInfo(200, "下载成功", null);
		} catch (Exception e) {
			return CommUtil.responseBuildInfo(500, "下载失败", null);
		}
	}
	
	/**
     * 平台实现更新包下载
     *
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/download")
    @ResponseBody
    public Object downloadFile(HttpServletRequest request,
                          HttpServletResponse response, String fileFullName) throws UnsupportedEncodingException {
        String rootPath = "/wolfdevicelogs/";//这里是我在配置文件里面配置的根路径，各位可以更换成自己的路径之后再使用（例如：D：/test）
        String FullPath = rootPath + fileFullName;//将文件的统一储存路径和文件名拼接得到文件全路径
        File packetFile = new File(FullPath);
        String fileName = packetFile.getName(); //下载的文件名
        File file = new File(FullPath);
        // 如果文件名存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download the song successfully!");
            } catch (Exception e) {
                System.out.println("Download the song failed!");
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
//        	backwrite(response, "下载成功");
//        	FileUtil.download(response, rootPath, fileName);
//          return CommUtil.responseBuildInfo(200, "下载成功", null);
      } else {//对应文件不存在
      	backwrite(response, "对应文件不存在");
//              return R.error("-1","对应文件不存在");
//      	return CommUtil.responseBuildInfo(500, "下载失败，对应文件不存在", null);
      }
      return null;
  }
  
  public void backwrite(HttpServletResponse response, String downinfo) {
  	try {
          //设置响应的数据类型是html文本，并且告知浏览器，使用UTF-8 来编码。
          response.setContentType("text/html;charset=UTF-8");

          //String这个类里面， getBytes()方法使用的码表，是UTF-8，  跟tomcat的默认码表没关系。 tomcat iso-8859-1
          String csn = Charset.defaultCharset().name();

          System.out.println("默认的String里面的getBytes方法使用的码表是： " + csn);

          //1. 指定浏览器看这份数据使用的码表
          response.setHeader("Content-Type", "text/html;charset=UTF-8");
          OutputStream os = response.getOutputStream();

          os.write(downinfo.getBytes());
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}
