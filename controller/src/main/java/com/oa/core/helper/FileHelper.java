package com.oa.core.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import com.oa.core.util.Const;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class FileHelper {

	public static String save(MultipartFile file, String savePath) {
		String srcFileName = file.getOriginalFilename();
		String prefix = System.currentTimeMillis() + "_";
		File saveFile = new File(savePath, prefix + srcFileName);
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
			return prefix + srcFileName;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String fileLib = null;

	public static String getFileRoot(HttpServletRequest request) {
		if (fileLib == null) {
			fileLib = request.getSession().getServletContext().getRealPath("/upload");
		}
		return fileLib;
	}
	
	/**
	 * 获得工程指定物理路径
	 *
	 * @param request
 	 * @param path
	 */
	public static String getFileRoot(HttpServletRequest request,String path) {
		if (StringHelper.isNotEmpty(path)) {
			return request.getSession().getServletContext().getRealPath("/"+path);
		}
		return request.getSession().getServletContext().getRealPath("/");
	}

	/**
	 * 删除一个文件。
	 *
	 * @param filename
	 * @throws IOException
	 */
	public static void deleteFile(String filename) throws IOException {
		File file = new File(filename);
		if (file.isDirectory()) {
			throw new IOException(
					"IOException -> BadInputException: not a file.");
		}
		if (file.exists() == false) {
			throw new IOException(
					"IOException -> BadInputException: file is not exist.");
		}
		if (file.delete() == false) {
			throw new IOException("Cannot delete file. filename = " + filename);
		}
	}

	/**
	 * 删除文件夹及其下面的子文件夹
	 *
	 * @param dir
	 * @throws IOException
	 */
	public static void deleteDir(File dir) throws IOException {
		if (dir.isFile()) {
			throw new IOException("IOException -> BadInputException: not a directory.");
		}
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
		}
		dir.delete();
	}

	/**
	 * 获得properties对象
	 * @param filePath
	 * @return
	 */
	public static Properties getProperties(String filePath) {
		// 生成文件对象
		File pf = new File(filePath);
		FileInputStream inpf = null;
		Properties properties = new Properties();
		try {
			// 生成文件输入流
			inpf = new FileInputStream(pf);
			// 生成properties对象
			properties.load(inpf);
		} catch (Exception e) {
				try {
					if (null != inpf) {
						inpf.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			e.printStackTrace();
		} finally {
			try {
				if (null != inpf) {
					inpf.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return properties;
	}

	/**
	 *  * 多附件上传
	 * @param request
	 * @param savePath 文件服务器路径
	 * @param modulePath 模块路径(子文件夹)
	 * @return 上传后的路径，以分号（;）做分割
	 * @throws IllegalStateException
	 * @throws IOException
	 *
	 */
	public static String upload(HttpServletRequest request,String savePath,String modulePath) throws IllegalStateException, IOException {
		String path = "";
		String src = "";
		//创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//判断 request 是否有文件上传,即多部分请求
		if(multipartResolver.isMultipart(request)){
			//转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			//取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			if(modulePath!=null&&modulePath.length()>0){
				savePath+="/"+modulePath;
			}
			while(iter.hasNext()){
				//取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if(file != null){
					//取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					//如果名称不为空,说明该文件存在，否则说明该文件不存在
					if(myFileName.trim() !=""){
						//重命名上传后的文件名
						String prefix = System.currentTimeMillis() + "_";
						String fileName=prefix+ myFileName;
						//拼接多文件名，以分号做分隔符
						path+=modulePath+"/"+fileName+ Const.SEPARATE;
						//上传到服务器
						File saveFile = new File(savePath, fileName);
						FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
					}
				}
			}
		}
		if(path.length()>0){
			src= StringHelper.subEndSeparate(path, ";");
		}
		return src;
	}


	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 * @param filePath 路径
	 * @param fileName 文件名
	 * @param conent
	 */
	public static void writerFile(String filePath,String fileName, String conent) {
		BufferedWriter out = null;
		try {
			File f = new File(filePath);
			if(!f.exists()){
				f.mkdirs();
			}
			fileName=filePath+"/"+fileName;
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true)));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null!=out){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 单个文件拷贝。
	 *
	 * @param srcFile
	 * @param destFile
	 * @param overwrite 是否覆盖目的文件
	 * @param delSrc 是否删除源文件
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile, boolean overwrite,boolean delSrc)
			throws IOException {
		// 首先判断源文件是否存在
		if (!srcFile.exists()) {
			return;
		}
		// 判断源文件是否可读
		if (!srcFile.canRead()) {
			return;
		}
		if (overwrite == false) {
			// 目标文件存在就不覆盖
			if (destFile.exists()){
				return;
			}
		} else {
			// 如果要覆盖已经存在的目标文件，首先判断是否目标文件可写。
			if (destFile.exists()) {
				if (!destFile.canWrite()) {
					return;
				}
			} else {
				File parent=new File(destFile.getParent());
				if(!parent.exists()){
					parent.mkdirs();
				}
				// 不存在就创建一个新的空文件。
				if (!destFile.createNewFile()) {
					return;
				}
			}
		}
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		byte[] block = new byte[1024];
		try {
			inputStream = new BufferedInputStream(new FileInputStream(srcFile));
			outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
			while (true) {
				int readLength = inputStream.read(block);
				if (readLength == -1) {
					break;// end of file
				}
				outputStream.write(block, 0, readLength);
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if(delSrc){
				srcFile.delete();
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 拷贝文件
	 * @param realPath 文件上传路径 G:/apache-tomcat-7.0.65/webapps/Cnrgs/upload，为绝对路径
	 * @param src 文件临时目录  temp/a.jpg（单附件）或者temp/a.jpg;temp/b.jpg;temp/a.jpg（多附件）
	 * @param target 目标路径 如：bug、web、job
	 * @param overwrite 加入目标文件夹里有相同的文件名，是否覆盖文件
	 * @param delSrc 是否删除源文件
	 * @return 返回相对路径地址，如：bug/a.jpg或者bug/a.jpg;bug/b.jpg;bug/a.jpg（多附件）
	 */
	public static String copyFile(String realPath,String src, String target, boolean overwrite,boolean delSrc){
		String fileLib="";
		if(StringHelper.isNotEmpty(src)){
			String[] srcs=src.split(";");
			for(int i=0;i<srcs.length;i++){
				String tem=srcs[i];
				try {
					FileHelper.copyFile(new File(realPath+"/"+tem),new File(realPath+"/"+tem.replace("temp",target)), overwrite, delSrc);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fileLib=src.replace("temp",target);
		}
		return fileLib;
	}


	/**
	 * 一次性压缩多个文件，文件存放至一个文件夹中
	 * @param filePath 文件夹路径，里面不能有文件夹，必须全部为单独的文件
	 * @param zipPath 压缩后的保存文件，如：c:/file.zip
	 */
	public static void ZipMultiFile(String filePath ,String zipPath) {
		try {
			File file = new File(filePath);// 要被压缩的文件夹
			File zipFile = new File(zipPath);
			File parentFile=zipFile.getParentFile();
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}
			InputStream input = null;
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			if(file.isDirectory()){
				File[] files = file.listFiles();
				for(int i = 0; i < files.length; ++i){
					input = new FileInputStream(files[i]);
					zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + files[i].getName()));
					int temp = 0;
					while((temp = input.read()) != -1){
						zipOut.write(temp);
					}
					input.close();
				}
			}
			zipOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 /**
	  * 向文件写内容
	  * @param fileName 文件名
	  * @param content 内容
	  * @param isAppend 追加(true)或覆盖(false)
	  */
    public static void writeData(String fileName, String content,boolean isAppend) {
        File file =new File(fileName);
    	File parent =file.getParentFile();
    	//文件夹不存在，则创建文件夹
    	if(!parent.exists()){
    		parent.mkdirs();
    	}
		BufferedWriter out = null;
        try {
        	//文件不存在，则创建文件
        	if(!file.exists()){
				file.createNewFile();
        	}
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, isAppend),"utf-8"));
			out.write(content);
			out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
    		if(null!=out ){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取文件大小
	 * @param size 文件长度
	 * @param
	 */
	public static String getPrintSize(long size) {
		// 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
		double value = (double) size;
		if (value < 1024) {
			return String.valueOf(value) + "B";
		} else {
			value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		}
		// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
		// 因为还没有到达要使用另一个单位的时候
		// 接下去以此类推
		if (value < 1024) {
			return String.valueOf(value) + "KB";
		} else {
			value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		}
		if (value < 1024) {
			return String.valueOf(value) + "MB";
		} else {
			// 否则如果要以GB为单位的，先除于1024再作同样的处理
			value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
			return String.valueOf(value) + "GB";
		}
	}
}



