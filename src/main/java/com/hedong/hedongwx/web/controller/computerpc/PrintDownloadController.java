package com.hedong.hedongwx.web.controller.computerpc;

import java.io.File;
import java.io.FileInputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFileChooser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hedong.hedongwx.utils.PrintMethod;
import com.hedong.hedongwx.utils.PrintTest;



@Controller
@RequestMapping(value = "/pcoperate")
public class PrintDownloadController {
	

	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping("/printOperate")
	public Object printOperate(HttpServletRequest request, HttpServletResponse response, Model model){
		PrintMethod.PrintFile();
		
		
		return model;
	}


	@RequestMapping("/downloadOperate")
	public Object downloadOperate(HttpServletRequest request, HttpServletResponse response, Model model){
		
		
		
		
		return model;
	}
	
	
	
	
	
	
}
