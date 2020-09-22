package com.hedong.hedongwx.utils;

import java.awt.event.*;
import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import java.awt.geom.*;

public class PrintPreviewDialog extends JDialog
implements ActionListener
{
    private JButton nextButton = new JButton("Next");
    private JButton previousButton = new JButton("Previous");
    private JButton closeButton = new JButton("Close");
    private JPanel buttonPanel = new JPanel();
    private PreviewCanvas canvas;

    public PrintPreviewDialog(Frame parent, String title, boolean modal, PrintTest pt, String str) {
        super(parent, title, modal);
        canvas = new PreviewCanvas(pt, str);
        setLayout();
    }

    private void setLayout() {
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(canvas, BorderLayout.CENTER);

        nextButton.setMnemonic('N');
        nextButton.addActionListener(this);
        buttonPanel.add(nextButton);
        previousButton.setMnemonic('N');
        previousButton.addActionListener(this);
        buttonPanel.add(previousButton);
        closeButton.setMnemonic('N');
        closeButton.addActionListener(this);
        buttonPanel.add(closeButton);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.setBounds((int)((SystemProperties.SCREEN_WIDTH - 400) / 2), (int)((SystemProperties.SCREEN_HEIGHT - 400) / 2), 400, 400);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == nextButton)
            nextAction();
        else if (src == previousButton)
            previousAction();
        else if (src == closeButton)
            closeAction();
    }

    private void closeAction() {
        this.setVisible(false);
        this.dispose();
    }

    private void nextAction() {
        canvas.viewPage(1);
    }

    private void previousAction() {
        canvas.viewPage(-1);
    }

    class PreviewCanvas extends JPanel {
        private String printStr;
        private int currentPage = 0;
        private PrintTest preview;

        public PreviewCanvas(PrintTest pt, String str) {
            printStr = str;
            preview = pt;
        }

        /* 将待打印内容按比例绘制到屏幕 */ 
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();// 获取页面格式

            double xoff;// 在屏幕上页面初始位置的水平偏移
            double yoff;// 在屏幕上页面初始位置的垂直偏移
            double scale;// 在屏幕上适合页面的比例
            double px = pf.getWidth();// 页面宽度
            double py = pf.getHeight();// 页面高度
            double sx = getWidth() - 1;
            double sy = getHeight() - 1;
            if (px / py < sx / sy) {
                scale = sy / py;// 计算比例
                xoff = 0.5 * (sx - scale * px);// 水平偏移量
                yoff = 0;
            } else {
                scale = sx / px;// 计算比例
                xoff = 0;
                yoff = 0.5 * (sy - scale * py);// 垂直偏移量
            }
            g2.translate((float)xoff, (float)yoff);// 转换坐标
            g2.scale((float)scale, (float)scale);

            Rectangle2D page = new Rectangle2D.Double(0, 0, px, py);// 绘制页面矩形
            g2.setPaint(Color.white);// 设置页面背景为白色
            g2.fill(page);
            g2.setPaint(Color.black);// 设置页面文字为黑色
            g2.draw(page);

            try {
                preview.print(g2, pf, currentPage);// 显示指定的预览页面
            } catch(PrinterException pe) {
                g2.draw(new Line2D.Double(0, 0, px, py));
                g2.draw(new Line2D.Double(0, px, 0, py));
            }
        }
        
        /* 预览指定的页面 */ 
        public void viewPage(int pos) {
            int newPage = currentPage + pos;
            // 指定页面在实际的范围内
            if (0 <= newPage && newPage < preview.getPagesCount(printStr)) {
                currentPage = newPage;
                repaint();
            }
        }
        
//        这样，在按下"Next"按钮时，只需要调用 canvas.viewPage(1)；而在按下"Preview"按钮时，
//                   只需要调用 canvas.viewPage(-1) 即可实现预览的前后翻页。
    }
}