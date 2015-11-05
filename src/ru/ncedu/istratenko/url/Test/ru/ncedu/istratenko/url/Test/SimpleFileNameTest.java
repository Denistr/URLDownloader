package ru.ncedu.istratenko.url.Test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.ncedu.istratenko.url.Checker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleFileNameTest {
    protected  Object check;
    protected static Method method = null;
    protected static Constructor c=null;


    @BeforeClass
    public static void setBeforeClass() throws Exception{
        Class<?> clazz=null;
        try{
            clazz = Class.forName("ru.ncedu.istratenko.url.Checker");
        } catch (Exception e){
            fail("Class not found");
        }

        try{
            method=clazz.getMethod("giveNameOfFile", null);
        } catch(Exception e){
            fail("method not found");
        }
        try{
            c=clazz.getConstructor(URL.class);
        } catch(Exception e){
            fail("Constructor couldn't find");
        }
    }

    @Test
    public void testGiveNameOfFileFirst() throws Exception {


        try{
            check= c.newInstance(new Object[]{new URL("https://docs.oracle.com/javase/7/docs/api/java/lang/reflect/Field.html#get(java.lang.Object)")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name  1 mistake", "/Field.html", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

    @Test
    public void testGiveNameOfFileSecond() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("http://www.iphones.ru")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 2 mistake", "/index.html", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

    @Test
    public void testGiveNameOfFileThird() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("http://www.iphones.ru/wp-content/uploads/2015/11/wa1-345x195.jpg")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 3 mistake", "/wa1-345x195.jpg", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

    @Test
    public void testGiveNameOfFileFourth() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("https://tjournal.ru/p/elbrus-cellphone-lifesaver")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 4 mistake", "/elbrus-cellphone-lifesaver.html", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

    @Test
    public void testGiveNameOfFileFive() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("https://vk.com/im?_smt=feed%3A1")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 5 mistake", "/im.html", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

    @Test
    public void testGiveNameOfFileSixth() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("http://www.blog-fiesta.com/users/sign_in/?direct=true")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 6 mistake", "/sign_in.html", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

    
    @Test
    public void testGiveNameOfFileSeventh() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("ftp://chip.local/list.txt")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 7 mistake", "/list.txt", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

    @Test
    public void testGiveNameOfFile8() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("ftp://chip.local/list.txt")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 8 mistake", "/list.txt", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

    @Test
    public void testGiveNameOfFile9() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("ftp://shepard.local/Soft/keys_windows_xp_vl_sp3.txt")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 9 mistake", "/keys_windows_xp_vl_sp3.txt", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }


    @Test
    public void testGiveNameOfFile10() throws Exception {
        try{
            check= c.newInstance(new Object[]{new URL("ftp://anonymous@speedtest.tele2.net/1KB.zip")});
        } catch(Exception e){
            fail("New object could not created");
        }
        try{
            String name=(String)method.invoke(check, null);
            assertEquals("Name 9 mistake", "/1KB.zip", name);
        } catch(Exception e){
            fail("Error in getName");
        }
    }

}