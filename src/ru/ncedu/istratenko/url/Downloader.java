package ru.ncedu.istratenko.url;

import sun.net.ftp.FtpProtocolException;

import java.awt.*;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by denis on 28.10.15.
 */

public class Downloader {
    private Checker c =null;
    private File file=null;

    public void downloadHTMLFile(URL url, String path) {
        URLConnection myURLConnection =null;
        InputStreamReader in=null;
        OutputStreamWriter out = null;
        BufferedReader br = null;
        BufferedWriter bw=null;
        try {
            c=new Checker(url);
                myURLConnection = url.openConnection();
                myURLConnection.setConnectTimeout(2 * 1000);
                myURLConnection.connect();
                try {
                    in = new InputStreamReader(myURLConnection.getInputStream(), getEncodingFile(myURLConnection));
                    br=new BufferedReader(in);
                } catch (IOException e){
                    e.printStackTrace();
                }
                file = new File(path + c.giveNameOfFile());
                if (file.exists()){
                    String fileName = c.changeFileName();
                    file = new File(path+fileName); 
                }
                File dir = new File(path);
                try {
                    if (dir.exists()) {
                        out=new OutputStreamWriter(new FileOutputStream(file),getEncodingFile(myURLConnection));
                    } else {
                        out=new OutputStreamWriter(new FileOutputStream(getFileForNotExistingPath(url, path)),getEncodingFile(myURLConnection));
                    }
                    bw = new BufferedWriter(out);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                    char[] buffer = new char[64 * 1024];
                    int c = 0;
                    while ((c = br.read(buffer)) != -1) {
                        bw.write(buffer, 0 ,c);
                    }
            bw.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (out!=null){
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void downloadFTPFile(URL url, String path) {
        OutputStream out = null;
        InputStream in =null;
        URLConnection myURLConnection =null;
        try {
            boolean login=true;
            c=new Checker(url);
            while (true) {
                if (!login){
                    url=c.getURLForFTP();
                }
                myURLConnection = url.openConnection();
                myURLConnection.setConnectTimeout(2 * 1000);
                myURLConnection.connect();
                try {
                    in = myURLConnection.getInputStream();
                    break;
                } catch (IOException e){
                    if (e.getMessage().contains("FtpProtocolException")) {
                        login = false;
                    } else
                        e.printStackTrace();
                }
            }

            file = new File(path + c.giveNameOfFile());
            if (file.exists()){
                String fileName = c.changeFileName();
                file = new File(path+fileName);
            }
            File dir = new File(path);
            try {
                if (dir.exists()) {
                    out = new FileOutputStream(file);
                } else {
                    out = new FileOutputStream(getFileForNotExistingPath(url, path));
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[64 * 1024];
            int c = 0;
            while ((c = in.read(buffer)) > 0) {
                out.write(buffer, 0, c);
            }
            out.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (out!=null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void openFile(){
        Desktop desktop=null;
        if (Desktop.isDesktopSupported()){
            desktop=Desktop.getDesktop();
        }
        try{
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFileForNotExistingPath(URL url, String path) {
        String defaultPath = new File("").getAbsolutePath();
        StringBuilder fileNameNoExistingPath = null;
        if (url.getProtocol().startsWith("ftp")) {
            fileNameNoExistingPath = new StringBuilder(path.replace("/", " ")).append(".").append(c.getExtendOfFile());
        } else {
            fileNameNoExistingPath = new StringBuilder(path.replace("/", " ")).append(".html");
        }
        file = new File(defaultPath + "/" + fileNameNoExistingPath.toString());
        if (file.exists()) {
            String fileName = c.changeFileName();
            file = new File(defaultPath + fileName);
        }
        return file;
    }

    private String getEncodingFile(URLConnection myConnection){
        int i=myConnection.getContentType().indexOf("=");
        String encoding = myConnection.getContentType().substring(i+1,myConnection.getContentType().length());
        return encoding;
    }
}
