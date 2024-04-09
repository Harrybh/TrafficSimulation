package config;

import controller.HelloController;

import com.jfinal.config.*;
import com.jfinal.template.Engine;

public class DemoConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants constants) {
        //开启开发模式
        constants.setDevMode(true);
    }

    @Override
    public void configRoute(Routes routes) {
        routes.add("/hello", HelloController.class);
    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}