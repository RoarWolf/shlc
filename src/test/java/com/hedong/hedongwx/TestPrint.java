package com.hedong.hedongwx;

import java.io.IOException;

import org.junit.Test;

public class TestPrint {
	
	@Test
	public void generateQRcode(){
//		String str = "http://www.he360.com.cn/oauth2Portpay?/oauth2Portpay=";
//		int n = 300000;
//		int m = 300099;
//		//300000-300099
//		for(int i=n; i<=m; i++){
//			for(Integer j=1; j<=10; j++){
//				Integer s = j;
//				String num = s.toString();
////				String qrcode = str + i + num(num);
//				String qrcode = i + num(num);
//				System.out.println(qrcode);
//			}
//		}
	}
	
//	public String num(String number){
//		StringBuilder mark = new StringBuilder("00");
//		int marknum = mark.length();
//		int num = number.length();
//		String numerical = mark.replace(marknum - num, marknum, number).toString();
//		return numerical;
//	}
	
	@Test
	public void test(){
//		ArrayList<Commodity> cmd_list = new ArrayList<Commodity>();
//		cmd_list.add(new Commodity("土豆", "5元", "2", "10"));
//		cmd_list.add(new Commodity("黄瓜", "5元", "2", "10"));
//		cmd_list.add(new Commodity("茄子", "5元", "2", "10"));
//		// 打印结算单
//		CheckOutTicket salesTicket = new CheckOutTicket(cmd_list,"000001", "3", "30", "30", "余额支付");
//		PrintCheckOut pintSale = new PrintCheckOut(salesTicket);
//		pintSale.PrintSale();
//		
//		//打印点餐单
//		OrderDishesTicket oft = new OrderDishesTicket(cmd_list, "000001","30", "30", "余额支付");
//		PrintOrderDishes printOrderFood = new PrintOrderDishes(oft);
//		printOrderFood.PrintSale();
	}
	
	@Test
	public void printtest(){
//
//		Object result = null;
//   	 	System.out.println("*****1    开始打印测试      ");
//		try {
//		    //通俗理解就是书、文档   
//		    Book book = new Book();  
//		    System.out.println("*****2	 文档book 	"+book);
//		    
//		    //设置成竖打   
//		    PageFormat pf = new PageFormat();  // 自定义页面设置
//		    System.out.println("*****3    设置成竖打	"+pf);  
//	         
//		    pf.setOrientation(PageFormat.PORTRAIT); //设置页面横纵向 
//		    System.out.println("*****4  测试4		"+pf);
//		    
//		    //通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。  
//		    Paper p = new Paper(); 
//		    System.out.println("*****  测试5		"+p); 
//		    
//		    p.setSize(590,840);//纸张大小   
//		    System.out.println("*****  测试6		"+p);
//		    
//		    p.setImageableArea(10,10, 590,840);//A4(595 X 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72 
//		    System.out.println("*****  测试7		"+p); 
//		    
//		    pf.setPaper(p); //设置页面符合设定要求 
//		    System.out.println("*****  测试8		"+pf); 
//		    
//		    // 把 PageFormat 和 Printable 添加到书中，组成一个页面    
//		    book.append(new PrintTest0(), pf); 
//		    System.out.println("*****  测试9		"+pf);
//		    
//		     //获取打印服务对象  
//		     PrinterJob job = PrinterJob.getPrinterJob();  
//			 System.out.println("*****  测试10		"+job);  
//			 
//		     // 设置打印类   
//		     job.setPageable(book);  
//			 System.out.println("*****  测试11		"+job);
//			 
//		     try {  
//		         //可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印  
//		         boolean a=job.printDialog();  
//				 System.out.println("*****  测试12		"+a);  
//				 
//		         if(a){         
//				   	 	System.out.println("结束打印测试printTest AAA     ");   
//		             job.print();   
//		         }else{
//				   	 	System.out.println("结束打印测试printTest BB     ");   
//		             job.cancel();
//		         }
//		         
//		     } catch (PrinterException e) {  
//		         e.printStackTrace();  
//		     }  
//		   	 	System.out.println("结束打印测试printTest      ");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
	}
	

	@Test
	public void fileData() throws IOException{
//		FileOutputStream(file);
//		FileOutputStream(file,true);
//		String str="锄禾日当午，旱地皆下土";
//		byte[] b=str.getBytes();
//		outputStream.write(b);
//		outputStream.close();
	}

	@Test
	public void fileTestData() throws IOException{
		/*
		 * 路径：
		 * 绝对路径：包括盘符在内的完整文件路径
		 * 相对路径：在当前文件目录下的文件路径
		 * File中的方法仅涉及到文件的创建，删除，重命名等等，只要设计文件内容的File无能为力，只能由IO流来完成
		 * 凡是与输入、输出相关的类，接口java.util.io包下
		 * File类的对象常作为IO流的具体类的构造器的形参
		 */
	}
	
	@Test
	public void fileOutTestData() throws IOException{
//		FileOutputStream out = new FileOutputStream("demo/out.txt");
//		out.write(67);
	}
	
	
	
}
