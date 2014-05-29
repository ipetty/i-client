/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core.util;

import net.ipetty.android.core.mail.MultiMailsender;

/**
 *
 * @author yneos
 */
public class MailUtils {

    private static final String host = "smtp.ipetty.net";
    private static final String port = "587";
    private static final Boolean isValidate = true;
    private static final String userName = "service@ipetty.net";
    private static final String password = "ipetty888";

    //发给单个人
    public static void sendTextMail(String fromAddress, String toAddress, String subject, String textBody) {
        //设置邮件
        MultiMailsender.MultiMailSenderInfo mailInfo = new MultiMailsender.MultiMailSenderInfo();
        mailInfo.setMailServerHost(host);
        mailInfo.setMailServerPort(port);
        mailInfo.setValidate(isValidate);
        mailInfo.setUserName(userName);
        mailInfo.setPassword(password);
        mailInfo.setFromAddress(fromAddress);
        mailInfo.setToAddress(toAddress);
        mailInfo.setSubject(subject);
        mailInfo.setContent(textBody);
        //这个类主要来发送邮件 
        MultiMailsender sms = new MultiMailsender();
        sms.sendTextMail(mailInfo);//发送文体格式 
    }

    //发给多个接收人
    public static void sendTextMail(String fromAddress, String toAddress, String subject, String textBody, String receivers[]) {
        //设置邮件
        MultiMailsender.MultiMailSenderInfo mailInfo = new MultiMailsender.MultiMailSenderInfo();
        mailInfo.setMailServerHost(host);
        mailInfo.setMailServerPort(port);
        mailInfo.setValidate(isValidate);
        mailInfo.setUserName(userName);
        mailInfo.setPassword(password);
        mailInfo.setFromAddress(fromAddress);
        mailInfo.setToAddress(toAddress);
        mailInfo.setSubject(subject);
        mailInfo.setContent(textBody);
        mailInfo.setReceivers(receivers);
        //这个类主要来发送邮件 
        MultiMailsender sms = new MultiMailsender();
        sms.sendTextMail(mailInfo);//发送文体格式 
    }

    //发给多个接收人，并带有抄送
    public static void sendTextMail(String fromAddress, String toAddress, String subject, String textBody, String receivers[], String cc[]) {
        //设置邮件
        MultiMailsender.MultiMailSenderInfo mailInfo = new MultiMailsender.MultiMailSenderInfo();
        mailInfo.setMailServerHost(host);
        mailInfo.setMailServerPort(port);
        mailInfo.setValidate(isValidate);
        mailInfo.setUserName(userName);
        mailInfo.setPassword(password);
        mailInfo.setFromAddress(fromAddress);
        mailInfo.setToAddress(toAddress);
        mailInfo.setSubject(subject);
        mailInfo.setContent(textBody);
        mailInfo.setReceivers(receivers);
        mailInfo.setCcs(cc);
        //这个类主要来发送邮件 
        MultiMailsender sms = new MultiMailsender();
        sms.sendTextMail(mailInfo);//发送文体格式 
    }
}
