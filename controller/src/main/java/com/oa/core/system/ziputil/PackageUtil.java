package com.oa.core.system.ziputil;

import com.oa.core.helper.DateHelper;
import com.oa.core.util.ConfParseUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:TextHelper
 * @author:zxd
 * @Date:2018/11/15
 * @Time:下午 5:08
 * @Version V1.0
 * @Explain 生成txt文件并插入SQL语句
 */
public class PackageUtil {

    private static String path = getFile();
    private static String filenameTemp;
    private static String strPatientImageNewPath;
    private static String folder;

    public static String getFile(){
        ConfParseUtil cp = new ConfParseUtil();
        String file = cp.getProperty("upload_file");
        return file;
    }

    /**
     * 创建文件
     *
     * @throws IOException
     */
    public boolean creatTxtFile(StringBuffer newStr) {
        return creatTxtFile("", newStr);
    }

    public boolean creatTxtFile(String name, StringBuffer newStr) {
        name = DateHelper.getYearMonth()+"-"+name;
        folder = name;
        boolean flag = false;
        filenameTemp = path + "upload/temp/" + DateHelper.getYearMonth() + "/" + name + "/" + name + "SQL更新文件.sql";
        strPatientImageNewPath = path + "upload/temp/" + DateHelper.getYearMonth() + "/" + name + "/";
        File fileDir = new File(path + "upload/temp/" + DateHelper.getYearMonth() + "/" + name);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File filename = new File(filenameTemp);
        if (!filename.exists()) {
            try {
                filename.createNewFile();
                flag = writeTxtFile(newStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 写文件
     *
     * @param newStr 新内容
     * @throws IOException
     */
    private boolean writeTxtFile(StringBuffer newStr) throws IOException {
        // 先读取原有文件内容，然后进行写入操作
        boolean flag = false;
        String encoding = "UTF-8";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            // 文件路径
            File file = new File(filenameTemp);
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            fos = new FileOutputStream(file);
            OutputStreamWriter outstream = new OutputStreamWriter(fos, encoding);
            pw = new PrintWriter(outstream);
            pw.write(newStr.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }

    public void copyFile(String strOldpath, String strNewPath) {
        try {

            File fOldFile = new File(strOldpath);
            if (fOldFile.exists()) {
                int byteread = 0;
                InputStream inputStream = new FileInputStream(fOldFile);
                FileOutputStream fileOutputStream = new FileOutputStream(strNewPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, byteread);
                }
                inputStream.close();
                fileOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("复制单个文件出错");
            e.printStackTrace();
        }
    }

    public void copyFolder(List<String> page) {
        String p = page.get(0);
        p = p.substring(0,p.lastIndexOf("/")+1  );
        String strPatientImageOldPath = path + "WEB-INF/views/";
        try {
            File fNewFolder = new File(strPatientImageNewPath + "WEB-INF/views/"+p);
            if (!fNewFolder.exists()) {
                fNewFolder.mkdirs();//不存在就创建一个文件夹
            }
            for (int i = 0; i < page.size(); i++) {
                //从原来的路径拷贝到现在的路径，拷贝一个文件
                copyFile(strPatientImageOldPath + page.get(i)+".jsp", strPatientImageNewPath + "WEB-INF/views/" + page.get(i)+".jsp");
            }
            new ZipCipherUtil().encryptZip(strPatientImageNewPath, strPatientImageNewPath+folder+".zip", "zxc123");
        } catch (Exception e) {
        }
    }

    public void copyFolder(File srcFile, File destFile) throws IOException {
        if(srcFile.isDirectory()){
            File newFolder=new File(destFile,srcFile.getName());
            newFolder.mkdirs();
            File[] fileArray=srcFile.listFiles();

            for(File file:fileArray){
                copyFolder(file, newFolder);
            }

        }else{
            File newFile=new File(destFile,srcFile.getName());
            copyUpdateFile(srcFile,newFile);
        }

    }

    private void copyUpdateFile(File srcFile, File newFile) throws IOException{
        BufferedInputStream bis=new BufferedInputStream(new FileInputStream(srcFile));
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(newFile));

        byte[] bys=new byte[1024];
        int len=0;
        while((len=bis.read(bys))!=-1){
            bos.write(bys,0,len);
        }
        bos.close();
        bis.close();

    }

    public String download(HttpServletResponse response) {
        response.setContentType("application/x-download");
        String filedownload = strPatientImageNewPath+folder+".zip";
        String filedisplay = folder+".zip";
        try {
            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filedisplay);
        OutputStream outp = null;
        FileInputStream in = null;
        try {
            outp = response.getOutputStream();
            in = new FileInputStream(filedownload);
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) > 0) {
                outp.write(b, 0, i);
            }
            outp.flush();
        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (outp != null) {
                try {
                    outp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                outp = null;
            }
        }
        return null;

    }

    public String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

}