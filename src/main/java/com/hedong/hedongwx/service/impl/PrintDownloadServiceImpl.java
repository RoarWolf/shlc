//package com.hedong.hedongwx.service.impl;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.Toolkit;
//import java.awt.print.PageFormat;
//import java.awt.print.Printable;
//import java.awt.print.PrinterException;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import com.hedong.hedongwx.entity.Commodity;
//import com.hedong.hedongwx.service.PrintDownloadService;
//
////public class PrintDownloadServiceImpl extends Printable implements PrintDownloadService{
//public class PrintDownloadServiceImpl  implements PrintDownloadService, Printable{
//
////	@Override
////	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//		// TODO Auto-generated method stub
////		return 0;
////	}
//	
//	
//	
//	
//	private ArrayList<Commodity> list;
//	//订单编号
//	private String orders;
//	//商品总数
//	private String sale_num;
//	//总金额
//	private String sale_sum;
//	//支付金额
//	private String practical;
//	//支付方式
//	private String payMode;
//	//字体
//	private Font font;
// 
//	// 构造函数
//	public PrintDownloadServiceImpl(ArrayList<Commodity> list,String orders, String sale_num, String sale_sum,
//			String practical,String payMode) {
//		this.list = list;
//		this.orders = orders;
//		this.sale_num = sale_num;
//		this.sale_sum = sale_sum;
//		this.practical = practical;
//		this.payMode = payMode;
//	}
// 
//	/**
//	* 打印方法
//	* graphics - 用来绘制页面的上下文，即打印的图形
//	* pageFormat - 将绘制页面的大小和方向，即设置打印格式，如页面大小一点为计量单位（以1/72 英寸为单位，1英寸为25.4毫米。A4纸大致为595 × 842点）
//	* 小票纸宽度一般为58mm，大概为165点
//	* pageIndex - 要绘制的页面从 0 开始的索引 ，即页号
//	**/
//	@Override
//	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
//			throws PrinterException {
//		Component c = null;
//		//此 Graphics2D 类扩展 Graphics 类，以提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制。
//		//它是用于在 Java(tm) 平台上呈现二维形状、文本和图像的基础类。
//		//拿到画笔
//		Graphics2D g2 = (Graphics2D) graphics;
//		// 设置打印颜色为黑色
//		g2.setColor(Color.black);
// 
//		// 打印起点坐标
//		double x = pageFormat.getImageableX(); //返回与此 PageFormat相关的 Paper对象的可成像区域左上方点的 x坐标。
//		double y = pageFormat.getImageableY(); //返回与此 PageFormat相关的 Paper对象的可成像区域左上方点的 y坐标。
// 
//		// 虚线
//		float[] dash1 = { 4.0f };
//		// width - 此 BasicStroke 的宽度。此宽度必须大于或等于 0.0f。如果将宽度设置为
//		// 0.0f，则将笔划呈现为可用于目标设备和抗锯齿提示设置的最细线条。
//		// cap - BasicStroke 端点的装饰
//		// join - 应用在路径线段交汇处的装饰
//		// miterlimit - 斜接处的剪裁限制。miterlimit 必须大于或等于 1.0f。
//		// dash - 表示虚线模式的数组
//		// dash_phase - 开始虚线模式的偏移量
// 
//		// 设置画虚线
//		g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4.0f, dash1, 0.0f));
// 
//		// 设置打印字体（字体名称、样式和磅值大小）（字体名称可以是物理或者逻辑名称）
//		//Font.PLAIN:普通样式常量   Font.ITALIC:斜体样式常量   Font.BOLD:粗体样式常量。
//		font = new Font("宋体", Font.PLAIN, 16);
//		g2.setFont(font);				//设置字体
//		float heigth = font.getSize2D();//字体高度
//		float line = heigth;//下一行开始打印的高度
//		//设置小票标题
//		g2.drawString("攸之源(横岗店)", (float)x+40, (float)y+line);
// 
//		
//		font = new Font("宋体",Font.BOLD,16);
//		g2.setFont(font);
//		heigth = font.getSize2D();//字体高度
//		line +=heigth;
//		//设置小票副标题
//		g2.drawString("结账单", (float)x+65, (float)y + line);
//		
//		font = new Font("宋体",Font.PLAIN,16);
//		g2.setFont(font);
//		line =line+heigth*2;
//		//设置小票副标题
//		g2.drawString("桌号: 01号", (float)x, (float)y + line);
//		
//		font = new Font("宋体", Font.PLAIN, 10);
//		g2.setFont(font);		  // 设置字体
//		heigth = font.getSize2D();// 字体高度
//		
//		line +=heigth;
//		// 显示订单号
//		g2.drawString("订单编号:" + orders, (float) x , (float) y + line);
//		
//		line+=heigth;
//		// 显示收银员
//		g2.drawString("下单时间:" + Calendar.getInstance().getTime().toLocaleString(), (float) x, (float) y + line);
//		
//		line+=heigth;
//		g2.drawString("支付方式:" + payMode, (float) x , (float) y + line);
//		
//		line+=heigth;
//		g2.drawString("支付金额:" + practical, (float) x , (float) y + line);
//		
//		line += heigth;	
//		//绘制虚线: (在此图形上下文的坐标系中使用当前颜色在点 (x1, y1) 和 (x2, y2) 之间画一条线。)
//		g2.drawLine((int) x, (int) (y + line), (int) x + 180, (int) (y + line));
//		
//		line += heigth;	
//		//显示标题
//		g2.drawString("品名", (float) x , (float) y + line+5);
//		g2.drawString("单价", (float) x + 60, (float) y + line+5);
//		g2.drawString("数量", (float) x + 100, (float) y + line+5);
//		g2.drawString("金额", (float) x + 150, (float) y + line+5);
//		
//		line = line+heigth*2;
//		// 显示内容
//		for (int i = 0; i < list.size(); i++) {
//			Commodity commodity = list.get(i);
//			g2.drawString(commodity.getName(), (float) x, (float) y + line);
//			g2.drawString(commodity.getUnit_price(), (float) x + 60, (float) y + line);
//			g2.drawString(commodity.getNum(), (float) x + 100, (float) y + line);
//			g2.drawString(commodity.getSum(), (float) x + 150, (float) y + line);
//			line += heigth;
//		}
//		
//		line += heigth;		
//		//绘制虚线
//		g2.drawLine((int) x, (int) (y + line), (int) x + 180, (int) (y + line));
//		
//		line = line+heigth*2;
//		g2.drawString("消费总金额:" + "￥" + sale_sum , (float) x+80, (float) y + line);
//		
//		line = line+heigth*7;//空7行
//		g2.drawString("攸之源,健康美味之源!", (float) x + 40, (float) y + line);
//		line += heigth;
//		g2.drawString("横岗银信荣恒2楼", (float) x +50, (float) y + line);
//		line += heigth;
//		g2.drawString("0755-28912757", (float) x +50, (float) y +line);
//		
//		
//		switch (pageIndex) {
//		case 0:
//			return PAGE_EXISTS;//0
//		default:
//			return NO_SUCH_PAGE;//1
// 
//		}
//	}
//	
//	
//	
////	/** 
////	 
////	   * @param Graphic指明打印的图形环境 
////	 
////	   * @param PageFormat指明打印页格式（页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点） 
////	 
////	   * @param pageIndex指明页号 
////	 
////	   **/  
////	@Override
////	public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {
////        System.out.println("pageIndex="+pageIndex);   
////        Component c = null;  
////        //print string    
////        String str = "中华民族是勤劳、勇敢和富有智慧的伟大民族。";  
////        //转换成Graphics2D  
////        Graphics2D g2 = (Graphics2D) gra;  
////        
////        //设置打印颜色为黑色  
////        g2.setColor(Color.black);  
////  
////        //打印起点坐标  
////        double x = pf.getImageableX();  
////        double y = pf.getImageableY();  
////        
////        switch(pageIndex){
////        	
////         	case 0:  
////            //设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）  
////            //Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput  
////            Font font = new Font("新宋体", Font.PLAIN, 9);  
////            g2.setFont(font);//设置字体  
////            
////            //BasicStroke bs_3=new BasicStroke(0.5f);     
////            
////            float[] dash1 = {2.0f};   
////  
////            //设置打印线的属性。   
////            //1.线宽 2、3、不知道，4、空白的宽度，5、虚线的宽度，6、偏移量  
////            g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));    
////            //g2.setStroke(bs_3);//设置线宽 
////            float heigth = font.getSize2D();//字体高度   
////            System.out.println("x="+x);  
////            // -1- 用Graphics2D直接输出   
////            //首字符的基线(右下部)位于用户空间中的 (x, y) 位置处   
////            //g2.drawLine(10,10,200,300);   
////               
////            Image src = Toolkit.getDefaultToolkit().getImage("F:\\workspace\\QQ.png");  
////            g2.drawImage(src,(int)x,(int)y,c);    
////            int img_Height=src.getHeight(c);  
////            int img_width=src.getWidth(c);  
////            //System.out.println("img_Height="+img_Height+"img_width="+img_width) ;  
////            
////            g2.drawString(str, (float)x, (float)y+1*heigth+img_Height);  
////            g2.drawLine((int)x,(int)(y+1*heigth+img_Height+10),(int)x+200,(int)(y+1*heigth+img_Height+10));             
////            g2.drawImage(src,(int)x,(int)(y+1*heigth+img_Height+11),c);  
////            
////            return PAGE_EXISTS;  
////            
////         	default:  
////         	
////            return NO_SUCH_PAGE;  
////      }  
////         
////   }  
//
//	
//	
//	
//	
//	
//	
//	
//	
//
//}
