package com.hedong.hedongwx.utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Properties;
import java.awt.font.FontRenderContext;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;
import java.io.*;

public class PrintTest extends JFrame implements ActionListener, Printable {
	private JButton printTextButton = new JButton("打印文本");// 实例化打印文本按钮
    private JButton previewButton = new JButton("打印预览");
	private JButton printText2Button = new JButton("打印文本2");// 实例化打印文本按钮
	private JButton printFileButton = new JButton("打印文件");
    private JButton printFrameButton = new JButton("打印框架");
    private JButton exitButton = new JButton("退出");
    private JLabel tipLabel = new JLabel("");
    private JTextArea area = new JTextArea();// 显示纯文本的多行区域。它作为一个轻量级组件
    private JScrollPane scroll = new JScrollPane(area);// 轻量级组件的 scrollable 视图
    private JPanel buttonPanel = new JPanel();// 一般轻量级容器,存放页面按钮等组件

    private int PAGES = 0;// 打印总页
    private String printStr;
    
    /**
     * 用于接收操作事件的侦听器接口
     */
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == printTextButton)
            printTextAction();
        else if (src == previewButton)
            previewAction();
        else if (src == printText2Button)
            printText2Action();
        else if (src == printFileButton)
            printFileAction();
        else if (src == printFrameButton)
            printFrameAction();
        else if (src == exitButton)
            exitApp();
    }
    
    /**
     * 覆写实现Printable接口的print方法
     */
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        Graphics2D g2 = (Graphics2D)g;//这两个类都是抽象类，声明的只是引用类型，为何可以调用方法
        g2.setPaint(Color.black);// 设置颜色
	    if (page >= PAGES)
	        return Printable.NO_SUCH_PAGE;// 表示页面不存在，结束打印
        g2.translate(pf.getImageableX(), pf.getImageableY());// 将 Graphics2D 上下文的原点平移到当前坐标系中的点 (x, y)
        drawCurrentPageText(g2, pf, page);
        return Printable.PAGE_EXISTS;
	}
    
    public PrintTest() {
        this.setTitle("打印测试");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds((int)((SystemProperties.SCREEN_WIDTH - 800) / 2), (int)((SystemProperties.SCREEN_HEIGHT - 600) / 2), 800, 600);
        initLayout();
    }

    private void initLayout() { // 初始化界面组件
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(scroll, BorderLayout.CENTER);
        printTextButton.setMnemonic('P');//设置当前模型上的键盘助记知符。助记符是某种键，它与外观的无鼠标修饰符（通常是 Alt）组合时（如果焦点被包含在此按钮祖先窗口中的道某个地方）将激活此按钮。
        printTextButton.addActionListener(this);
        buttonPanel.add(printTextButton);
        
        previewButton.setMnemonic('v');
        previewButton.addActionListener(this);
        buttonPanel.add(previewButton);
        
        printText2Button.setMnemonic('e');
        printText2Button.addActionListener(this);
        buttonPanel.add(printText2Button);
        
        printFileButton.setMnemonic('i');
        printFileButton.addActionListener(this);
        buttonPanel.add(printFileButton);
        
        printFrameButton.setMnemonic('F');
        printFrameButton.addActionListener(this);
        buttonPanel.add(printFrameButton);
        
        exitButton.setMnemonic('x');
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);
        
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
     
    /**
     * 绘制字符串内容
     * @param g2
     * @param pf
     * @param page
     */
    private void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page) {
        Font f = area.getFont();// 从系统属性列表返回一个 Font 对象
        String s = getDrawText(printStr)[page];// 获取打印值
        String drawText;
        float ascent = 16;
        int k, i = f.getSize(), lines = 0;
        while(s.length() > 0 && lines < 54) {
            k = s.indexOf('\n');
            if (k != -1) {
                lines += 1;
                drawText = s.substring(0, k);
                g2.drawString(drawText, 0, ascent);
                if (s.substring(k + 1).length() > 0) {
                    s = s.substring(k + 1);
                    ascent += i;
                }
            } else {
                lines += 1;
                drawText = s;
                g2.drawString(drawText, 0, ascent);
                s = "";
            }
        }
	}
    
    /**
     * 获取需要绘制的文本字符串数组
     * @param s
     * @return
     */
    public String[] getDrawText(String s) {
        String[] drawText = new String[PAGES];// 声明一个长度为总页数的的字符串数组
        for (int i = 0; i < PAGES; i++)
            drawText[i] = "";
        int k, suffix = 0, lines = 0;
        while(s.length() > 0) {
            if(lines < 54) {
                k = s.indexOf('\n');// 记录是否换行
                if (k != -1) {
                    lines += 1;
                    drawText[suffix] = drawText[suffix] + s.substring(0, k + 1);//拼接字符串
                    if (s.substring(k + 1).length() > 0)
                        s = s.substring(k + 1);
                } else {
                    lines += 1;
                    drawText[suffix] = drawText[suffix] + s;
                    s = "";
                }
            } else {
                lines = 0;
                suffix++;
            }
        }
        return drawText;
    }
    
    /**
     * 计算需要打印的页数
     * @param printStr
     * @return 打印页数
     */
    public int getPagesCount(String curStr) {
        int page = 0;
        int position, count = 0;
        String str = curStr;
	    while(str.length() > 0) {
	        position = str.indexOf('\n');
            count += 1;// 记录行数
	        if (position != -1) {
                str = str.substring(position + 1);
	    	} else {
	            str = "";// 结束循环
	    	}
	    }
//	    if (count > 0 && count <= 54) {
//            page = 1;
//        } else {
//            page = count / 54 + 1;// 规定54行为一页，计算打印的页数
//        }
	    if (count > 0)
	        page = count / 54 + 1;
        return page;
	}
    
    /**
     * 打印文本
     */
    private void printTextAction() {
        printStr = area.getText().trim();
        if (printStr != null && printStr.length() > 0) {
            PAGES = getPagesCount(printStr);
            PrinterJob myPrtJob = PrinterJob.getPrinterJob();// 创建并返回初始化时与默认打印机关联的PrinterJob
            PageFormat pageFormat = myPrtJob.defaultPage();// 创建新的 PageFormat实例，并将它设置为默认大小和方向
            myPrtJob.setPrintable(this, pageFormat);// 调用 painter，用指定的 format呈现该页面
            if (myPrtJob.printDialog()) {
                try {
                    myPrtJob.print();
                } catch (PrinterException pe) {
                    pe.printStackTrace();
                }
            }
        } else {
            JOptionPane.showConfirmDialog(null, "Sorry, Printer Job is Empty, Print Cancelled!", 
            		"Empty", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }

    private void previewAction() {
        printStr = area.getText().trim();
        PAGES = getPagesCount(printStr);
        (new PrintPreviewDialog(this, "Print Preview", true, this, printStr)).setVisible(true);
    }

    private void printText2Action() {
        printStr = area.getText().trim();
        if (printStr != null && printStr.length() > 0) {
            PAGES = getPagesCount(printStr);
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob job = printService.createPrintJob();
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocAttributeSet das = new HashDocAttributeSet();
            Doc doc = new SimpleDoc(this, flavor, das);
            try {
                job.print(doc, pras);
            } catch(PrintException pe) {
                pe.printStackTrace();
            }
        } else {
            JOptionPane.showConfirmDialog(null, "Sorry, Printer Job is Empty, Print Cancelled!", 
            		"Empty", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /* 打印指定的文件 */ 
    private void printFileAction() {
    	// 构造一个文件选择器，默认为当前目录
        JFileChooser fileChooser = new JFileChooser(SystemProperties.USER_DIR);
//        fileChooser.setFileFilter(new com.hedong.hedongwx.file.JavaFilter());
        fileChooser.setFileFilter(null);
        int state = fileChooser.showOpenDialog(this);// 弹出文件选择对话框
        if (state == fileChooser.APPROVE_OPTION){// 如果用户选定了文件
            File file = fileChooser.getSelectedFile();// 获取选择的文件
            // 构建打印请求属性集
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            // 设置打印格式，因为未确定文件类型，这里选择 AUTOSENSE 
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            // 查找所有的可用打印服务
            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            // 定位默认的打印服务
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            // 显示打印对话框
            PrintService service = ServiceUI.printDialog(null, 200, 200, printService
                                                        , defaultService, flavor, pras);
            if (service != null) {
                try {
                    DocPrintJob job = service.createPrintJob();// 创建打印作业
                    FileInputStream fis = new FileInputStream(file);// 构造待打印的文件流
                    DocAttributeSet das = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc(fis, flavor, das);// 建立打印文件格式
                    job.print(doc, pras);// 进行文件的打印
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /* 打印指定的窗体及其包含的组件 */ 
    private void printFrameAction() {
        Toolkit kit = Toolkit.getDefaultToolkit();// 获取工具箱
        Properties props = new Properties();
        props.put("awt.print.printer", "durango");// 设置打印属性
        props.put("awt.print.numCopies", "2");
        if(kit != null) {
            PrintJob printJob = kit.getPrintJob(this, "Print Frame", props);// 获取工具箱自带的打印对象
            if(printJob != null) {
                Graphics pg = printJob.getGraphics();// 获取打印对象的图形环境
                if(pg != null) {
					try {
                        this.printAll(pg);// 打印该窗体及其所有的组件
                    } finally {
                        pg.dispose();// 注销图形环境
                    }
                }
                printJob.end();// 结束打印作业
            }
        }
    }

    private void exitApp() {
        this.setVisible(false);
        this.dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        (new PrintTest()).setVisible(true);
    }
}

