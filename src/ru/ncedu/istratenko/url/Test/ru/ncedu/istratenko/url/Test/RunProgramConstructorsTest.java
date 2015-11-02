package ru.ncedu.istratenko.url.Test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RunProgramConstructorsTest {
    protected String  url;
    protected String path;
    protected  boolean open;

    protected Object o;
    protected static Method method = null;
    protected static Constructor cUrl, cUrlP, cUrlPO=null;
    protected static Class<?> clazz=null;
    static List<String> listurl=null;
    static List<String> listPaths=null;

    @BeforeClass
    public static void setBeforeClass() throws Exception{

        try{
            clazz = Class.forName("ru.ncedu.istratenko.url.RunProgram");
        } catch (Exception e){
            fail("Class not found");
        }
        try{
            cUrl=clazz.getConstructor(String.class);
        } catch(Exception e){
            fail("Constructor with URL couldn't find");
        }
        try {
            cUrlP=clazz.getConstructor(String.class, String.class);
        } catch (Exception e){
            fail("Constructor with URL and path not found");
        }
        try {
            cUrlPO=clazz.getConstructor(String.class, String.class, String.class);
        } catch (Exception e){
            fail("Constructor with URL, path and Open not found");
        }

        listurl = new ArrayList<String>();
        listurl.add("https://mail.ru");
        listurl.add("https://vk.com/feed");
        listurl.add("https://docs.oracle.com/javase/7/docs/api/java/lang/reflect/Field.html#get(java.lang.Object)");
        listurl.add("http://edu-netcracker.com:8080/user/default.xhtml");
        listurl.add("http://www.iphones.ru");
        listurl.add("ftp://shepard.local/Soft/keys_windows_xp_vl_sp3.txt");
        listurl.add("ftp://shepard.local/Soft/175_7550.JPG");
        listurl.add("ftp://anonymous@speedtest.tele2.net/1KB.zip");
        listurl.add("http://www.blog-fiesta.com/users/sign_in/?direct=true");
        listurl.add("https://tjournal.ru/p/emirates-flight-upgrade?from=relap");
        listurl.add("http://habrahabr.ru/company/golovachcourses/blog/215275/");
        listurl.add("http://kinozal.tv");
    }

    @Test
    public void onlyURLTest(){
        Field url=null;
        Field path=null;
        Field open = null;

        for (int i=0;i<listurl.size();i++) {
            try {
                o = cUrl.newInstance(new Object[]{listurl.get(i)});
            } catch (Exception e) {
                fail(Integer.toString(i)+" object not created");
            }
            try{
                url = clazz.getDeclaredField("url");
                path = clazz.getDeclaredField("path");
                open = clazz.getDeclaredField("openFile");
                url.setAccessible(true);
                path.setAccessible(true);
                open.setAccessible(true);
            } catch (Exception e) {
                fail("filed "+ Integer.toString(i) + " url not found");
            }
            try {
                assertEquals("Mistake in "+Integer.toString(i)+" URL", new URL(listurl.get(i)), url.get(o));
                assertEquals("Mistake in " + Integer.toString(i) + " path", new File("").getAbsolutePath(), path.get(o));
                assertEquals("Mistake in " + Integer.toString(i) + " open", false, open.get(o));
            } catch (Exception e) {
                fail("Object not found in test");
            }
        }
    }
}