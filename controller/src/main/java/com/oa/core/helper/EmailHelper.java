package com.oa.core.helper;

import com.oa.core.bean.module.Employees;
import com.oa.core.service.module.EmployeesService;
import com.oa.core.util.LogUtil;
import com.oa.core.util.SpringContextUtil;
import org.junit.Test;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @ClassName:EmailHelper
 * @author:zxd
 * @Date:2018/10/26
 * @Time:上午 11:32
 * @Version V1.0
 * @Explain 邮件发送工具类
 */
public class EmailHelper {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("config.oa");
    private static final String adress = bundle.getString("mail_adress");
    private static final String sendFrom = bundle.getString("mail_form");
    private static final String username = bundle.getString("mail_username");
    private static final String password = bundle.getString("mail_password");

    public static void sendEmail(String someone, String subject, String content){
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth","true");
        props.setProperty("mail.smtp.host",adress);

        Authenticator authenticator = new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        };
        Session session = Session.getDefaultInstance(props, authenticator);
        session.setDebug(true);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(sendFrom));
            message.setRecipients(RecipientType.TO,InternetAddress.parse(someone));
            try {
                message.setSubject(subject);
                message.setContent(content,"text/html;charset=UTF-8");
                Transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void email(String userId,String title,String msg){
        try {
            String remind = bundle.getString("mail_remind");
            if(false) {
                EmployeesService e = (EmployeesService) SpringContextUtil.getBean("employeesService");
                Employees employees = new Employees();
                employees.setUserName(userId);
                Employees emp = e.selectTerms(employees);
                String mail = null;
                if (emp != null) {
                    mail = emp.getMailbox();
                }
                if(mail == null || mail.equals("")){
                    String ht = sendFrom.substring(sendFrom.indexOf("@"));
                    mail = userId + ht;
                }
                String msghtml = msg;
                sendEmail(mail, title, msghtml);
            }
        }catch (Exception e){
            LogUtil.sysLog("Exception:"+e);
            e.printStackTrace();
        }
    }

    @Test
    public  void  tsetemail(){
        String content ="哈喽你好！";
        EmailHelper.sendEmail("zhenxudong@sjzhtkj.com", "提示", content);

        String a = bundle.getString("mail_remind");
        System.out.println("========================>"+a+"<======================");
    }
}

