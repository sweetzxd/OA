package com.oa.core.util;

import com.oa.core.bean.system.FormCustomMade;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class NewPageTempUtil {
    private FormCustomMade formMade;

    @Test
    public void test1() {
        FormCustomMade formCustomMade = new FormCustomMade();
        formCustomMade.setEditPage("");
        newPage(null, formCustomMade, "form", true);
    }

    public void newPage(HttpServletRequest request, FormCustomMade formCustomMade) {
        newPage(request, formCustomMade, "form", false);
    }

    public void newPage(HttpServletRequest request, FormCustomMade formCustomMade, String type, boolean exist) {
        formMade = formCustomMade;
        ConfParseUtil cp = new ConfParseUtil();
        String realPath = cp.getProperty("produce_file");
        String path = "";
        if (type != null && type.equals("form")) {
            path = "config/Form.jsp";
        } else {
            path = "config/Flow.jsp";
        }
        LogUtil.sysLog("========path============>" + type + "<===========" + path);
        String infilePath = this.getClass().getClassLoader().getResource("").getPath() + path;
        LogUtil.sysLog("infilePath:" + infilePath);
        File infile = new File(infilePath);
        File oufile = new File(realPath, formCustomMade.getEditPage() + ".jsp");
        File fileParent = oufile.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        //判断文件是否存在
        if (exist) {
            String newFilePath = realPath + formCustomMade.getEditPage() + "new.jsp";
            fileUpdate(infilePath, realPath + formCustomMade.getEditPage() + ".jsp", newFilePath);
        } else {
            fileChannelCopy(infile, oufile);
        }
        /*try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(infile));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(uofile));
            byte[] b = new byte[1024];
            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = bis.read()) != -1) {
                bos.write(bys, 0, len);
            }
            bis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //根据模板文件修改已存在的jsp文件
    public void fileUpdate(String modelFilePath, String oldFilePath, String newFilePath) {
        try {
            //模板文件
            File modelFile = new File(modelFilePath);
            //旧的.jsp文件
            File oldFile = new File(oldFilePath);
            //新的.jsp文件
            File newFile = new File(newFilePath);
            FileReader modelReader = new FileReader(modelFile);
            FileReader oldReader = new FileReader(oldFile);
            FileWriter newWriter = new FileWriter(newFile, true);
            BufferedReader modelBr = new BufferedReader(modelReader);
            BufferedReader oldBr = new BufferedReader(oldReader);
            BufferedWriter newBw = new BufferedWriter(newWriter);
            boolean flag1 = true;
            boolean flag2 = false;
            String oldLine = "";
            String modelLine = "";
            List<String> modelList = new ArrayList<>();
            LinkedMap<String, List<String>> oldMap = new LinkedMap<>();

            //遍历模板文件,取出标签外的内容
            while ((modelLine = modelBr.readLine()) != null) {
                if (flag1) {
                    modelList.add(modelLine);
                }
                if (modelLine.indexOf("<%--") != -1) {
                    String info = modelLine.trim();
                    if (info.equals("<%--jspBegin--%>") || info.equals("<%--scriptBegin--%>") || info.equals("<%--functionBegin--%>") || info.equals("<%--jsBegin--%>") || info.equals("<%--aBegin--%>") || info.equals("<%--formBegin--%>") || info.equals("<%--divBegin--%>") || info.equals("<%--modiaBegin--%>") || info.equals("<%--modiFormBegin--%>")) {
                        flag1 = false;
                    }

                    if (info.equals("<%--jspEnd--%>") || info.equals("<%--scriptEnd--%>") || info.equals("<%--functionEnd--%>") || info.equals("<%--jsEnd--%>") || info.equals("<%--aEnd--%>") || info.equals("<%--formEnd--%>") || info.equals("<%--divEnd--%>") || info.equals("<%--modiaEnd--%>") || info.equals("<%--modiFormEnd--%>")) {
                        modelList.add(modelLine);
                        flag1 = true;
                    }
                }
            }
            //遍历旧文件，取出标签内的内容
            List<String> jspList = new ArrayList<>();
            List<String> scriptList = new ArrayList<>();
            List<String> functionList = new ArrayList<>();
            List<String> jsList = new ArrayList<>();
            List<String> aList = new ArrayList<>();
            List<String> formList = new ArrayList<>();
            List<String> divList = new ArrayList<>();
            List<String> modiaList = new ArrayList<>();
            List<String> modiFormList = new ArrayList<>();
            String type = "";
            while ((oldLine = oldBr.readLine()) != null) {
                if (oldLine.indexOf("<%--") != -1) {
                    String info = oldLine.trim();
                    if (info.equals("<%--jspBegin--%>")) {
                        type = "jsp";
                        flag2 = true;
                    } else if (info.equals("<%--scriptBegin--%>")) {
                        type = "script";
                        flag2 = true;
                    } else if (info.equals("<%--functionBegin--%>")) {
                        type = "function";
                        flag2 = true;
                    } else if (info.equals("<%--jsBegin--%>")) {
                        type = "js";
                        flag2 = true;
                    } else if (info.equals("<%--aBegin--%>")) {
                        type = "a";
                        flag2 = true;
                    } else if (info.equals("<%--formBegin--%>")) {
                        type = "form";
                        flag2 = true;
                    } else if (info.equals("<%--divBegin--%>")) {
                        type = "div";
                        flag2 = true;
                    } else if (info.equals("<%--modiaBegin--%>")) {
                        type = "modia";
                        flag2 = true;
                    } else if (info.equals("<%--modiaFormBegin--%>")) {
                        type = "modiaForm";
                        flag2 = true;
                    }

                    if (info.equals("<%--jspEnd--%>")) {
                        oldMap.put("jsp", jspList);
                        flag2 = false;
                    } else if (info.equals("<%--scriptEnd--%>")) {
                        oldMap.put("script", scriptList);
                        flag2 = false;
                    } else if (info.equals("<%--functionEnd--%>")) {
                        oldMap.put("function", functionList);
                        flag2 = false;
                    } else if (info.equals("<%--jsEnd--%>")) {
                        oldMap.put("js", jsList);
                        flag2 = false;
                    } else if (info.equals("<%--aEnd--%>")) {
                        oldMap.put("a", aList);
                        flag2 = false;
                    } else if (info.equals("<%--formEnd--%>")) {
                        oldMap.put("form", formList);
                        flag2 = false;
                    } else if (info.equals("<%--divEnd--%>")) {
                        oldMap.put("div", divList);
                        flag2 = false;
                    } else if (info.equals("<%--modiaEnd--%>")) {
                        oldMap.put("modia", modiaList);
                        flag2 = false;
                    } else if (info.equals("<%--modiFormEnd--%>")) {
                        oldMap.put("modiForm", modiFormList);
                        flag2 = false;
                    }
                }

                if (flag2) {
                    if (type.equals("jsp") && !oldLine.trim().equals("<%--jspBegin--%>")) {
                        jspList.add(oldLine);
                    } else if (type.equals("script") && !oldLine.trim().equals("<%--scriptBegin--%>")) {
                        scriptList.add(oldLine);
                    } else if (type.equals("function") && !oldLine.trim().equals("<%--functionBegin--%>")) {
                        functionList.add(oldLine);
                    } else if (type.equals("js") && !oldLine.trim().equals("<%--jsBegin--%>")) {
                        jsList.add(oldLine);
                    } else if (type.equals("a") && !oldLine.trim().equals("<%--aBegin--%>")) {
                        aList.add(oldLine);
                    } else if (type.equals("form") && !oldLine.trim().equals("<%--formBegin--%>")) {
                        formList.add(oldLine);
                    } else if (type.equals("div") && !oldLine.trim().equals("<%--divBegin--%>")) {
                        divList.add(oldLine);
                    } else if (type.equals("modia") && !oldLine.trim().equals("<%--modiaBegin--%>")) {
                        modiaList.add(oldLine);
                    } else if (type.equals("modiForm") && !oldLine.trim().equals("<%--modiFormBegin--%>")) {
                        modiFormList.add(oldLine);
                    }
                }
            }

            for (String str : modelList) {
                if (str == null) {
                    str = "";
                }
                newBw.write(str);
                newBw.newLine();
                newBw.flush();
                if (str.indexOf("Begin--%>") != -1) {
                    if (str.trim().equals("<%--jspBegin--%>")) {
                        List<String> list1 = oldMap.get("jsp");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    } else if (str.trim().equals("<%--scriptBegin--%>")) {
                        List<String> list1 = oldMap.get("script");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    } else if (str.trim().equals("<%--functionBegin--%>")) {
                        List<String> list1 = oldMap.get("function");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    } else if (str.trim().equals("<%--jsBegin--%>")) {
                        List<String> list1 = oldMap.get("js");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    } else if (str.trim().equals("<%--aBegin--%>")) {
                        List<String> list1 = oldMap.get("a");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    } else if (str.trim().equals("<%--formBegin--%>")) {
                        List<String> list1 = oldMap.get("form");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    } else if (str.trim().equals("<%--divBegin--%>")) {
                        List<String> list1 = oldMap.get("div");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    } else if (str.trim().equals("<%--modiaBegin--%>")) {
                        List<String> list1 = oldMap.get("modia");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    } else if (str.trim().equals("<%--modiFormBegin--%>")) {
                        List<String> list1 = oldMap.get("modiForm");
                        if (list1 != null) {
                            for (String str1 : list1) {
                                newBw.write(str1);
                                newBw.newLine();
                                newBw.flush();
                            }
                        }
                    }
                }
            }
            modelReader.close();
            oldReader.close();
            newWriter.close();
            //删除旧的.jsp文件
            File file = new File(oldFilePath);
            file.delete();
            //将新生成的.jsp文件重命名为旧文件名
            new File(newFilePath).renameTo(new File(oldFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            if (formMade != null) {
                newPage(null, formMade, "form", false);
            }
        }
    }

    @Test
    public void test() {
        try {
            LogUtil.sysLog(NewPageTempUtil.class.getResource("").getPath());
            InputStream inStream = NewPageTempUtil.class.getClassLoader().getResourceAsStream("config/Form.temp");
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            byte[] filebt = outSteam.toByteArray();
            LogUtil.sysLog(filebt);

            File directory = new File("WEB-INF\\classes\\config\\Form.temp");// 参数为空
            String courseFile = directory.getCanonicalPath();
            LogUtil.sysLog(courseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
