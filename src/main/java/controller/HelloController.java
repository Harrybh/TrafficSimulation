package controller;

import com.jfinal.core.Controller;



public classHelloController extends Controller {

    public void index() {
        renderText("Hello World! jFinal");
    }
}