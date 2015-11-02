package ru.ncedu.istratenko.url;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by denis on 28.10.15.
 */
public class RunProgram {

    private URL url=null;
    private String path = null;
    private boolean openFile=false;

    public RunProgram(String url){
        try {
            this.url=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        path=new File("").getAbsolutePath();
        this.openFile=false;
    }

    public RunProgram(String url, String pathOrOpen){
        try {
            this.url=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Pattern p = Pattern.compile("([Tt]rue)|([Oo]pen)");
        Matcher m = p.matcher(pathOrOpen);
        if (m.matches()){
            this.openFile=true;
            path=new File("").getAbsolutePath();
        } else {
            this.path=pathOrOpen;
            this.openFile=false;
        }
    }

    public RunProgram(String url, String path, String openFile){
        try {
            this.url=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.path=path;
        Pattern p = Pattern.compile("([Tt]rue)|([Oo]pen)");
        Matcher m = p.matcher(openFile);
        if (m.matches()){
            this.openFile=true;
        }

    }

    public void executeProgram(){
        Downloader d = new Downloader();
        if (url.getProtocol().startsWith("ftp")) {
            d.downloadFTPFile(url, path);
        } else {
            d.downloadHTMLFile(url, path);
        }
        if (openFile) {
            d.openFile();
        }
    }
}
