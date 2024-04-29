package config;

import controller.MainController;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

import com.jfinal.config.*;
import com.jfinal.template.Engine;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;

import bean.Vehicle;
import bean.Road;
import bean.Poi;
import bean.Goods;

public class DemoConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants constants) {
        //开启开发模式
        constants.setDevMode(true);

    }

    @Override
    public void configRoute(Routes routes) {
        routes.add("/", MainController.class);
        routes.add("/MainController/getAjaxData", MainController.class, "getAjaxData");
        routes.add("/MainController/getAjaxData2", MainController.class, "getAjaxData2");
    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {
        DruidPlugin dp = new DruidPlugin("jdbc:postgresql://localhost:5432/postgres","postgres", "2022091201017","org.postgresql.Driver");
        plugins.add(dp);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setShowSql(true);
        arp.addMapping("poi","id", Poi.class);
        arp.addMapping("goods","id", Goods.class);
        arp.addMapping("road","id", Road.class);
        arp.addMapping("vehicle","id", Vehicle.class);
        plugins.add(arp);
        arp.setDialect(new PostgreSqlDialect());
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}