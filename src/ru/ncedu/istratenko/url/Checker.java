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
    private String[] parseUrl = null;

    public Checker(URL url){
        this.url=url;
    }

    /**
     * Позволяет получить из URL имя файла
     * если URL содержит лишь имя домена, то имя файла - index.html; 
     * если URL содержит параметры, то именем файла будут символы, которые стоят последними в пути файла перед расширением</br>
     * Если расширение отсутствует, то именем файла будут символы в конце пути после последнего слеша (или между последним и предпоследним слешем)
     * @return имя файла с расширением
     */
    public String giveNameOfFile(){
        String nameFile=null;
        StringBuilder sb= new StringBuilder();
        parseUrl=url.getPath().split("/");
        if (url.getPath().isEmpty()) {
            nameFile = "/index.html";
        }
        if (url.getPath().contains("/")){
            if (parseUrl[parseUrl.length - 1].isEmpty()) {
                nameFile=sb.append("/").append(parseUrl[parseUrl.length - 2]).append(".html").toString();
            } else if (parseUrl[parseUrl.length - 1].contains(".")) {
                nameFile = sb.append("/").append(parseUrl[parseUrl.length - 1]).toString();
            } else {
                nameFile=sb.append("/").append(parseUrl[parseUrl.length - 1]).append(".html").toString();
            }
        }
        return nameFile;
    }

    /**
     * Определяет расширение файла, который указан в url.
     * @return расширение файла с точкой (Например: .zip, .jpg)
     */
    public String getExtendOfFile() {
        int i=parseUrl[parseUrl.length-1].indexOf(".");
        extendOfFile=parseUrl[parseUrl.length-1].substring(i,parseUrl[parseUrl.length-1].length());
        return extendOfFile;
    }

    /**
     * Меняет имя файла на то, которое будет введено с клавиатуры, если при запросе ответить [y].
     * @return имя файла после изменения
     */
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
                if (url.getProtocol().startsWith("ftp") || (url.getPath().contains(".") && !url.getPath().endsWith("html"))){
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


    /**
     * Запрашивает логин и пароль для авторизации на ftp сервере.
     * Преобразует введенные строки в url вида ftp://user:password@host:port/path
     * @return url для ftp сервера
     */
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
