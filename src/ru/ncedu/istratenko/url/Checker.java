package ru.ncedu.istratenko.url;

import sun.net.ftp.FtpClient;

import java.io.Console;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by denis on 30.10.15.
 */
public class Checker { //придумать другое название классу
    private URL url=null;
    private StringBuilder fullNameOfFile = new StringBuilder();
    private String extendOfFile=null;

    public Checker(URL url){
        this.url=url;
    }

    public String giveNameOfFile(){
        String nameFile=null;
        StringBuilder sb= new StringBuilder();
        String[] urls=url.getPath().split("/");
        if (url.getProtocol().startsWith("ftp")){
            nameFile=sb.append("/").append(urls[urls.length-1]).toString();
            int i=urls[urls.length-1].indexOf(".");
            extendOfFile=urls[urls.length-1].substring(i,urls[urls.length-1].length());
        } else {
            if (url.getPath().isEmpty()) {
                nameFile = "/index.html";
            }
            if (url.getPath().contains("/")) {
                if (urls[urls.length - 1].isEmpty()) {
                    nameFile=sb.append("/").append(urls[urls.length - 2]).append(".html").toString();
                } else if (urls[urls.length - 1].endsWith("html")) {
                    nameFile = sb.append("/").append(urls[urls.length - 1]).toString();
                } else {
                    nameFile=sb.append("/").append(urls[urls.length - 1]).append(".html").toString();
                }
            }
        }
        return nameFile;
    }

    public String getExtendOfFile() {
        return extendOfFile;
    }

    public String changeFileName(){
        String fileName=null;
        String yn=null;
        System.out.println("Do you want to change the file name? Enter y, if you want change, \nelse-n and existing file replace with a new");
        while (true){
            Scanner sc = new Scanner(System.in);
            yn = sc.nextLine();
            if (yn.compareTo("y") == 0 || yn.compareTo("Y") == 0) {
                System.out.println("Enter new file name:");
                fileName = sc.nextLine();
                if (url.getProtocol().startsWith("ftp")){
                    fileName=fullNameOfFile.append("/").append(fileName).append(getExtendOfFile()).toString();
                } else {
                    fileName = fullNameOfFile.append("/").append(fileName).append(".html").toString();
                }
                break;
            }
            if (yn.compareTo("n") == 0 || yn.compareTo("N") == 0) {
                fileName = giveNameOfFile();
                break;
            } else {
                System.out.println("Error input. Try again");
            }
        }
        return fileName;
    }


    public URL getURLForFTP(){
        String ftpurl= "ftp://%s:%s@%s";
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Enter login:");
            String login = sc.nextLine();
            System.out.println("Enter password:");
            String pass = sc.nextLine();
            if (pass.compareTo("")==0){
                pass=" ";
            }
            String hostAndPath = url.getHost()+url.getPath();
            url=new URL(String.format(ftpurl, login, pass,hostAndPath));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }
}
