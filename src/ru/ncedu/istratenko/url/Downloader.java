package ru.ncedu.istratenko.url;

import sun.net.ftp.FtpProtocolException;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by denis on 28.10.15.
 */

public class Downloader {
    private File file=null;
    private Checker check =null;
    private File newGeneralFile=null;

    /**
     * Читает данные с url байтовым потоком, создает файл и записывает эти данные в файл.
     * @param url web странциы/файла, которую необходимо сохарнить
     * @param path путь на компьютере пользователя, по которому сохранится ресурс
     * @return file, которые получится в результате скачивания с url
     */
    public File downloadFile(URL url, String path) {
        OutputStream out = null;
        InputStream in = null;
        URLConnection myURLConnection = null;
        try {
            boolean login = true;
            check = new Checker(url);
            while (true) {
                if (!login) {
                    url = check.getURLForFTP();
                }
                myURLConnection = url.openConnection();
                myURLConnection.setConnectTimeout(2 * 1000);
                try {
                    myURLConnection.connect();
                    in = myURLConnection.getInputStream();
                    break;
                } catch (IOException e) {
                    if (e.getMessage().contains("FtpProtocolException")) {
                        login = false;
                    } else {
                        if (e.getMessage().contains("Invalid username/password")) {
                            System.out.println("Invalid username/password. try again");
                        } else {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }
            if (in==null)
                return null;

            file = new File(path + check.giveNameOfFile());
            if (file.exists() && !path.contains("_files")) {
                String fileName = check.changeFileName();
                file = new File(path + fileName);
            }
            File dir = new File(path);
            try {
                if (dir.exists()) {
                    out = new FileOutputStream(file);
                } else {
                    out = new FileOutputStream(getFileForNotExistingPath(url, path, check));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[64 * 1024];
            int c = 0;
            while ((c = in.read(buffer)) > 0) {
                out.write(buffer, 0, c);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return file;
    }

    /**
     * Открывает файл
     */
    public void openFile(){
        Desktop desktop=null;
        if (Desktop.isDesktopSupported()){
            desktop=Desktop.getDesktop();
        }
        try {
            if (newGeneralFile != null) {
                desktop.open(newGeneralFile);
            } else{
            desktop.open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFileForNotExistingPath(URL url, String path, Checker c) {
        String defaultPath = new File("").getAbsolutePath();
        StringBuilder fileNameNoExistingPath = null;
        if (url.getPath().contains(".")) {
            fileNameNoExistingPath = new StringBuilder(path.replace("/", " ")).append(c.getExtendOfFile());
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

    List<File> allRsc = new ArrayList<File>();
    List<String> allPathsRsc = new ArrayList<String>();

    /**
     * создает поддиректорию со всеми файлами, которые используются в тегах link и img html файла
     */
    public void downloadRsc(){
        String text=null;

        FileInputStream inFile=null;
        File generalFile=file;
        try {
            inFile = new FileInputStream(generalFile);
            byte[] str = new byte[inFile.available()];
            inFile.read(str);
            text = new String(str);
            String defaultName=generalFile.getName().substring(0,generalFile.getName().indexOf("."));
            String defaultPath = generalFile.getAbsolutePath().substring(0, generalFile.getPath().indexOf(".")-defaultName.length());
            File dir=new File(defaultPath+"/"+defaultName+"_files/");
            getURLRsc(text, dir);
            newGeneralFile=replacePathRscinGeneralFile(allRsc, text, allPathsRsc, generalFile); //создается только в том случае, если в файле пути к img меняются на локальные
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if (inFile!=null){
                try {
                    inFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File replacePathRscinGeneralFile(List<File> imgs, String text, List<String> paths, File generalFile){
        String newText=null;
        FileOutputStream outFile=null;
        try {
            generalFile.delete();
            generalFile.createNewFile();
            newText=text;
            for (int i = 0; i < imgs.size(); i++) {
                newText=newText.replace(paths.get(i), imgs.get(i).getAbsolutePath());
            }
            byte[] contentInBytes = newText.getBytes();
            outFile = new FileOutputStream(generalFile);
            outFile.write(contentInBytes);
            outFile.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return generalFile;
    }

    private void getURLRsc(String text, File dir) throws MalformedURLException {
        Pattern p = Pattern.compile("(<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>)");
        Matcher m = p.matcher(text);
        while (m.find()){
            Pattern pattern = Pattern.compile("(((http[s]?)|ftp)://.+\\.((jpg)|(png)|(gif)))");
            Matcher matcher = pattern.matcher(m.group(1));
            if (matcher.find()) {
                if (!dir.exists()){
                    try {
                        dir.mkdir();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                allPathsRsc.add(matcher.group(1));
                allRsc.add(downloadFile(new URL(matcher.group(1)), dir.getAbsolutePath()));
            }
        }

        p = Pattern.compile("(<link[^>]+href\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>)");
        m = p.matcher(text);
        while (m.find()){
            Pattern pattern = Pattern.compile("(((http[s]?)|ftp)://.+\\.(.)+(\"))");
            Matcher matcher = pattern.matcher(m.group(1));
            if (matcher.find()) {
                if (!dir.exists()){
                    try {
                        dir.mkdir();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                allPathsRsc.add(matcher.group(1).substring(0,matcher.group(1).length()-1));
                allRsc.add(downloadFile(new URL(matcher.group(1).substring(0,matcher.group(1).length()-1)), dir.getAbsolutePath()));
            }
        }
    }

}
