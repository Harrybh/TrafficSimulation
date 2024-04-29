package controller;

import bean.Poi;
import bean.Road;
import bean.Vehicle;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import java.util.List;



public class MainController extends Controller{
    private final static int step_time = 1;
    public void index(){
        int current_time = 0;
        List<Vehicle> vehicles = Vehicle.dao.find("select * from vehicle");
        List<Poi> Pois = Poi.dao.find("select * from poi");
        setAttr("pois", Pois);
        while(true)
        {
            for(int i = 0; i < 4; i++) GoodsController.createGoods(current_time);
            for(Vehicle vehicle:vehicles) {
                VehicleController.calculateVehicleStatus(vehicle, step_time, current_time);
            }
            current_time += step_time;
            for(Vehicle vehicle:vehicles) {
                if(vehicle.getInt("poi_id") == 0){
                    Road road = Road.dao.findById(vehicle.getInt("current_poi"));
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

                    //setAttr("currentX", currentX);
                    //setAttr("currentY", currentY);
                }
                else
                {
                    Poi poi = Poi.dao.findById(vehicle.getInt("poi_id"));
                    double currentX = poi.getDouble("x");
                    double currentY = poi.getDouble("y");
                    vehicle.set("x", currentX);
                    vehicle.set("y", currentY);

                    //setAttr("currentX", currentX);
                    //setAttr("currentY", currentY);
                }
            }
            setAttr("vehicles", vehicles);
        }

    }
}
