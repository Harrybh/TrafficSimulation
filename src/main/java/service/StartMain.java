package service;

import com.jfinal.core.JFinal;

public class StartMain {

    public static void main(String[] args) {
        JFinal.start("src/main/webapp",8081,"/",0);
    }
}