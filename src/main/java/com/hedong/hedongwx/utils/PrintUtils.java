package com.hedong.hedongwx.utils;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @Description： 
 * @author  origin  创建时间：   2020年4月25日 下午4:48:54  
 */
public class PrintUtils extends JFrame implements Printable, ActionListener {
	
	/**
     * 实现功能：调打印机打印文本内容
     */
    private static final long serialVersionUID = 1L;// 序列化
    private JButton printTextButton = new JButton("打印文本");// 实例化打印文本按钮
    private JPanel buttonPanel = new JPanel();// 一般轻量级容器,存放页面按钮等组件
    private JTextArea area = new JTextArea();// 显示纯文本的多行区域。它作为一个轻量级组件
    private JScrollPane scroll = new JScrollPane(area);// 轻量级组件的 scrollable 视图

    private int PAGES = 0;// 打印总页
    private String printStr;
	
	 /**
     * 用于接收操作事件的侦听器接口
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == printTextButton)
            printTextAction();
    }

    /**
     * 覆写实现Printable接口的print方法
     */
    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        Graphics2D g2 = (Graphics2D) g;//这两个类都是抽象类，声明的只是引用类型，为何可以调用方法
        g2.setPaint(Color.black);// 设置颜色
        if (page >= PAGES)
            return Printable.NO_SUCH_PAGE;// 表示页面不存在，结束打印
        g2.translate(pf.getImageableX(), pf.getImageableY());// 将 Graphics2D 上下文的原点平移到当前坐标系中的点 (x, y)
        drawCurrentPageText(g2, pf, page);
        return Printable.PAGE_EXISTS;
    }

    

    public PrintUtils() {// 初始化显示窗口 SystemProperties
        JFrame.setDefaultLookAndFeelDecorated(true);// 新创建的窗体设置一个默认外观显示
//        this.setSize(SystemProperties.SCREEN_WIDTH, SystemProperties.SCREEN_HEIGHT);// 設置窗口大小
        this.setSize(600, 500);// 設置窗口大小
//        this.setSize(SysProperties.SCREEN_WIDTH, SysProperties.SCREEN_HEIGHT);// 設置窗口大小
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);// 设置点击关闭时退出
        initLayout();// 界面上添加需要的組件
        this.setLocationRelativeTo(null);// 组件当前未显示，或者 c 为 null，则此窗口将置于屏幕的中央
        this.setVisible(true);
    }

    private void initLayout() {// 初始化界面组件
        printTextButton.addActionListener(this);
        printTextButton.setMnemonic(KeyEvent.VK_P);// 设置键盘上p键跟按钮绑定，Alt+p进行触发
        buttonPanel.add(printTextButton);// 将按钮添加到JPanel这个中间容器
        this.getContentPane().setLayout(new BorderLayout());// 设置内容面板为边框布局
        this.getContentPane().add(scroll, BorderLayout.CENTER);// 添加scroll组件到JFrame内容面板的中位置
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);// 添加buttonPanel按钮这个中间容器到JFrame内容面板的南位置
    }

    /**
     * 打印文本
     */
    private void printTextAction() {
        printStr = area.getText().trim();
        if (printStr != null && printStr.length() > 0) {
            PAGES = getPagesCount(printStr);
            PrinterJob prtJob = PrinterJob.getPrinterJob();// 创建并返回初始化时与默认打印机关联的PrinterJob
            PageFormat pageFormat = prtJob.defaultPage();// 创建新的 PageFormat实例，并将它设置为默认大小和方向
            prtJob.setPrintable(this, pageFormat);// 调用 painter，用指定的 format呈现该页面
            if (prtJob.printDialog()) {
                try {
                    prtJob.print();
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showConfirmDialog(null, "Sorry, Printer Job is Empty, Print Cancelled!",
                        "Empty", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * 计算需要打印的页数
     * @param printStr
     * @return 打印页数
     */
    private int getPagesCount(String curStr) {
        int count = 0, page = 0, position = 0;
        while (curStr.length() > 0) {
            position = curStr.indexOf("\n");
            count++;// 记录行数
            if (position != -1) {
                curStr = curStr.substring(position + 1);
            } else {
                curStr = "";// 结束循环
            }
        }
        if (count > 0 && count <= 10) {
            page = 1;
        } else {
            page = count / 10 + 1;// 规定10行为一页，计算打印的页数
        }
        return page;
    }

    public static void main(String[] args) {
//    	 (new PrintTest()).setVisible(true);
        new PrintUtils();
    }

    /**
     * 绘制字符串内容
     * 
     * @param g2
     * @param pf
     * @param page
     */
    private void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page) {
        Font f = area.getFont();// 从系统属性列表返回一个 Font 对象
        String s = getDrawText(printStr)[page];// 获取打印值		获取当前页的待打印文本内容
        String drawText;
        float ascent = 16;	// 给定字符点阵
        int k, i = f.getSize(), lines = 0;
        while (s.length() > 0 && lines < 10) {// 每页限定在10 行以内
            k = s.indexOf('\n');// 获取每一个回车符的位置
            if (k != -1) { // 存在回车符
                lines++; // 计算行数
                drawText = s.substring(0, k); // 获取每一行文本
                g2.drawString(drawText, 0, ascent); // 具体打印每一行文本，同时走纸移位
                if (s.substring(k + 1).length() > 0) {
                    s = s.substring(k + 1); // 截取尚未打印的文本
                    ascent += i;
                }
            } else { // 不存在回车符
                lines += 1; // 计算行数
                drawText = s; // 获取每一行文本
                g2.drawString(drawText, 0, ascent); // 具体打印每一行文本，同时走纸移位
                s = ""; // 文本已结束
            }
        }

    }

    /**
     * 获取需要绘制的文本字符串数组
     * @param s
     * @return
     */
    private String[] getDrawText(String s) {
        String[] drawText = new String[PAGES];// 声明一个长度为总页数的的字符串数组
        for (int i = 0; i < PAGES; i++)
            drawText[i] = "";// 这段是拿来干嘛的？？
        int k, suffix = 0, lines = 0;
        while (s.length() > 0) {
            if (lines < 10) {
                k = s.indexOf('\n');// 记录是否换行
                if (k != -1) {
                    ++lines;
                    drawText[suffix] = drawText[suffix] + s.substring(0, k + 1);//拼接字符串
                    if (s.substring(k + 1).length() > 0) {
                        s = s.substring(k + 1);
                    }
                } else {
                    ++lines;
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

	   

}
