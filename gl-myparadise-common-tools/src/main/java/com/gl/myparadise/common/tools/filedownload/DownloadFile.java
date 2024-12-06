package com.gl.myparadise.common.tools.filedownload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DownloadFile
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 0:27
 */
public class DownloadFile {
	
	//写文件缓冲大小
	final private static int BUFSIZE = 8196;

	private static final Logger logger = LoggerFactory.getLogger(DownloadFile.class);

	/**
	 * 下载文件主方法
	 * @param req
	 * @param resp
	 * @param filename
	 * @param
	 * @throws IOException
	 */
    public static void doDownload( HttpServletRequest req, HttpServletResponse resp,
                             String path, String filename ){
        
    	File f = new File(path + filename);
        int  length = 0;
        ServletOutputStream op = null ;
        DataInputStream in = null ;
		try {
			op = resp.getOutputStream();
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/octet-stream" );
	        resp.setContentLength((int)f.length());
	        resp.setHeader("Content-Disposition", "attachment; filename=\"" + new   String(filename.getBytes("GB2312"), "ISO8859_1") + "\"");
	        
	        //写流缓冲
	        byte[] bbuf = new byte[BUFSIZE];
	        in = new DataInputStream(new FileInputStream(f));
	        
	        while ((in != null) && ((length = in.read(bbuf)) != -1))
	        {
	            op.write(bbuf,0,length);
	        }
		} catch (IOException e) {
			logger.error("下载["+filename+"] 文件时异常:"+e.getMessage());
		} finally{
			try{
			if(in != null)
				in.close();
			if(op != null){
		        op.flush();
		        op.close();
			}
			} catch(IOException e){
				logger.error("下载["+filename+"] 文件时，关闭文件流异常:"+e.getMessage());
			}
		}
    }
	
}
