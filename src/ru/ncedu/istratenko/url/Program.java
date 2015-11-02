package ru.ncedu.istratenko.url;


import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by denis on 28.10.15.
 */
public class Program {
    public static void main(String[] args){
        RunProgram rp=null;
        switch (args.length){
            case 1:
                rp = new RunProgram(args[0]); //Только URL
                break;
            case 2:
                 rp = new RunProgram(args[0], args[1]); //URL и dir
                break;
            case 3:
                rp = new RunProgram(args[0], args[1], args[2]); //URL, dir, open/true
        }
        rp.executeProgram();
    }
}
