package ru.ncedu.istratenko.url;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

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

    /**
     *
     * @param url типа String
     */
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

    /**
     * скачивает файл с url и сохраняет его по заданному пути.
     * Если файл html, то в отдельную поддиректорию сохраняются все ресурсы в тегах link и img
     * Если в качестве параметра указана опция открытия файла после скачивания, то файл откроется
     */
    public void executeProgram(){
        Downloader d = new Downloader();
        if(d.downloadFile(url, path).getAbsolutePath().endsWith("html")) {
            d.downloadRsc();
        }
        if (openFile) {
            d.openFile();
        }
    }
}
