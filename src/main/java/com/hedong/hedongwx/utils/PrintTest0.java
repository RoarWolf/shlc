package com.hedong.hedongwx.utils;

import java.awt.print.Printable;
import java.awt.Graphics;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.*;

/**
 * @Description： ORIGIN打印
 * @author  origin  创建时间：   2020年4月21日 上午10:10:13
 */
public class PrintTest0 implements Printable{
	
	
	
	/** 
	 
	   * @param Graphic指明打印的图形环境 
	 
	   * @param PageFormat指明打印页格式（页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点） 
	 
	   * @param pageIndex指明页号 
	 
	   **/  
	  
	   public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {  
	       
	       System.out.println("pageIndex="+pageIndex);   
	       Component c = null;  
	       
	      //print string    
	      String str = "中华民族是勤劳、勇敢和富有智慧的伟大民族。";  
	      System.out.println("str=  "+str);
	      //转换成Graphics2D  
	      Graphics2D g2 = (Graphics2D) gra;  
	  
	      //设置打印颜色为黑色  
	      g2.setColor(Color.black);  
	  
	      //打印起点坐标  
	      double x = pf.getImageableX();  
	      double y = pf.getImageableY();  
	      
	      System.out.println("x  "+x);
	      System.out.println("y  "+y);
	      System.out.println("pageIndex  "+pageIndex);
	   
	      switch(pageIndex){  
	         case 0:  
	           //设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）  
	           //Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput  
	           Font font = new Font("新宋体", Font.PLAIN, 9);  
	           g2.setFont(font);//设置字体  
	  
	           //BasicStroke bs_3=new BasicStroke(0.5f);     
	  
	           float[] dash1 = {2.0f};   
	  
	           //设置打印线的属性。   
	           //1.线宽 2、3、不知道，4、空白的宽度，5、虚线的宽度，6、偏移量  
	           g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));    
	           //g2.setStroke(bs_3);//设置线宽 
	           float heigth = font.getSize2D();//字体高度   
	           System.out.println("x="+x);  
	           // -1- 用Graphics2D直接输出   
	           //首字符的基线(右下部)位于用户空间中的 (x, y) 位置处   
	           //g2.drawLine(10,10,200,300);   
	               
	           Image src = Toolkit.getDefaultToolkit().getImage("F:\\workspace\\QQ1.JPG");  
	           g2.drawImage(src,(int)x,(int)y,c);    
	           int img_Height=src.getHeight(c);  
	           int img_width=src.getWidth(c);  
	           //System.out.println("img_Height="+img_Height+"img_width="+img_width) ;  
	  
	           g2.drawString(str, (float)x, (float)y+1*heigth+img_Height);  
	           g2.drawLine((int)x,(int)(y+1*heigth+img_Height+10),(int)x+200,(int)(y+1*heigth+img_Height+10));             
	           g2.drawImage(src,(int)x,(int)(y+1*heigth+img_Height+11),c);  
	  
	                return PAGE_EXISTS;  
	  
	         default:  
	  
	             return NO_SUCH_PAGE;  
	  
	      }  
	   }      
	
}
