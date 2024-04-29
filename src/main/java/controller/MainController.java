package controller;

import bean.Goods;
import bean.Poi;
import bean.Road;
import bean.Vehicle;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import java.util.*;



public class MainController extends Controller{
    static int last_time = 0;
    static final int POI_COUNT = 2;
    private static int randomPoi() {
        return (int) (Math.random() * POI_COUNT + 1);
    }
    public void getAjaxData(){
        int current_time = (int)Math.round(Double.parseDouble(getPara("timeInterval")));
        int step_time = current_time - last_time;
        last_time = current_time;
        List<Vehicle> vehicles = Vehicle.dao.find("select * from vehicle");
        List<Goods> goods = Goods.dao.find("select * from goods");
        for(Goods good:goods) {
            if(good.getInt("limit") < current_time){
                good.delete();
            }
        }
        goods = Goods.dao.find("select * from goods");
        if(goods.size() < 30) {
            GoodsController.createGoods(current_time, POI_COUNT);
        }
        System.out.println("current_time: " + current_time);
        for(Vehicle vehicle:vehicles) {
            VehicleController.calculateVehicleStatus(vehicle, step_time, current_time);
        }
        for(Vehicle vehicle:vehicles) {
            if(vehicle.getInt("poi_id") == 0){
                Road road = Road.dao.findById(vehicle.getInt("road_id"));
                Poi poi_st = Poi.dao.findById(road.getInt("st_id"));
                Poi poi_ed = Poi.dao.findById(road.getInt("ed_id"));
                double X1 = poi_st.getDouble("x");
                double Y1 = poi_st.getDouble("y");
                double X2 = poi_ed.getDouble("x");
                double Y2 = poi_ed.getDouble("y");
                int currentTime = vehicle.getInt("current_time");
                int statusTime = vehicle.getInt("status_time");
                double currentX = X1 + (X2 - X1) * (currentTime - statusTime) / statusTime;
                double currentY = Y1 + (Y2 - Y1) * (currentTime - statusTime) / statusTime;
                vehicle.set("x", currentX);
                vehicle.set("y", currentY);
                vehicle.update();
            }
            else
            {
                Poi poi = Poi.dao.findById(vehicle.getInt("poi_id"));
                double currentX = poi.getDouble("x");
                double currentY = poi.getDouble("y");
                vehicle.set("x", currentX);
                vehicle.set("y", currentY);
                vehicle.update();
            }
            System.out.println(vehicle);
        }
        renderJson("message", vehicles);
    }
    public void getAjaxData2(){
        List<Poi> Pois = Poi.dao.find("select * from poi");
        System.out.println("zheli"+Pois);
        renderJson("message", Pois);
    }
    public void index(){
        List<Goods> goods = Goods.dao.find("select * from goods");
        for(Goods good:goods) {
            good.delete();
        }
        List<Vehicle> vehicles = Vehicle.dao.find("select * from vehicle");
        for(Vehicle vehicle:vehicles) {
            vehicle.set("current_time", 0);
            vehicle.set("status", 0);
            vehicle.set("status_time", 1);
            vehicle.set("goods_id", null);
            vehicle.set("road_id", null);
            vehicle.set("poi_id", randomPoi());
            vehicle.update();
        }
        render("/index.jsp");
    }
}
