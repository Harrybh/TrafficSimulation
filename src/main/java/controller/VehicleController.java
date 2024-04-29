package controller;

import bean.Road;
import bean.Vehicle;
import bean.Goods;

import com.jfinal.plugin.activerecord.Model;
import java.util.List;
//0状态：休息
//1状态：寻路
//2状态：运货
//3状态：养护，加油
//4状态：调整货



public class VehicleController {
    private static final int[][] MARKOV = {
            {20, 50, 0, 30, 0},
            {0, 0, 0, 0, 100},
            {50, 20, 0, 30, 0},
            {10, 90 , 0, 0, 0},
            {0, 0, 100, 0, 0}
    };
    private static final double fee = 5;
    private static int randomTime() {
        return (int) (Math.random() * 5 + 1);
    }
    public static Goods getPurposeGoods(Vehicle vehicle, int worldTime ,int current_poi)
    {
        List<Goods> goods = Goods.dao.find("select * from goods");
        System.out.println(goods);
        Goods purpose = null;
        double maxvalue = 0;
        for(Goods good : goods)
        {
            if(good.getBoolean("is_delivered"))
            {
                continue;
            }
            if(good.getInt("limit") < worldTime)
            {
                continue;
            }
            if(vehicle.getDouble("capacity") < good.getDouble("weight"))
            {
                continue;
            }

            Road road1 = RoadController.getRoad(current_poi, good.getInt("st_id"));
            Road road2 = RoadController.getRoad(good.getInt("st_id"), good.getInt("ed_id"));

            double D = road1.getDouble("length") + road2.getDouble("length");
            double W = good.getDouble("weight") ;
            double V = good.getDouble("value");
            double ALL = V / W - fee * D;
            System.out.println("____________________________ALL: "+ALL);
            if(ALL > maxvalue)
            {
                maxvalue = ALL;
                purpose = good;
            }
        }
        return purpose;
    }

    public static void calculateVehicleStatus(Vehicle vehicle, int addTime, int worldTime) {
        System.out.println(vehicle);
        int status = vehicle.getInt("status");
        int statusTime = vehicle.getInt("status_time");
        int currentTime = vehicle.getInt("current_time");
        System.out.println("___________________________"+addTime + " "+currentTime);
        if(currentTime + addTime < statusTime) {
            currentTime = currentTime + addTime;
           // System.out.println("___________________________"+addTime + " "+currentTime);
            vehicle.set("current_time", currentTime);
           // System.out.println(vehicle);
           // System.out.println("haha");
        }
        else {
            if(status == 2) {
                Goods nowGoods = Goods.dao.findById(vehicle.getInt("goods_id"));
                nowGoods.delete();
                vehicle.set("goods_id", 0);
                vehicle.set("poi_id", nowGoods.getInt("ed_id"));
            }
            int random = (int) (Math.random() * 100);
            int up = 0;
            int nextStatus = 0;
            for(int i = 0; i < 5; i++) {
                if(MARKOV[status][i] != 0) {
                    up += MARKOV[status][i];
                    if(random < up) {
                        nextStatus = i;
                        break;
                    }
                }
            }
           // System.out.println("current: "+status+" next:"+nextStatus);
            if(nextStatus == 0) {
                vehicle.set("status", 0);
                vehicle.set("status_time", randomTime());
                vehicle.set("current_time", 0);
            }
            else if(nextStatus == 1) {
                Goods purpose = getPurposeGoods(vehicle, worldTime, vehicle.getInt("poi_id"));
                if(purpose == null) {
                    vehicle.set("status", 0);
                    vehicle.set("status_time", randomTime());
                    vehicle.set("current_time", 0);
                }
                else {
                    Road road1 = RoadController.getRoad(vehicle.getInt("poi_id"), purpose.getInt("st_id"));
                    int road_id = road1.getInt("id");
                    vehicle.set("status", 1);
                    vehicle.set("status_time", (int)(road1.getDouble("length")/vehicle.getDouble("speed")));
                    vehicle.set("current_time", 0);
                    vehicle.set("road_id", road_id);
                    vehicle.set("goods_id", purpose.getInt("id"));
                    vehicle.set("poi_id", 0);
                }
            }
            else if(nextStatus == 2) {
                Goods nowGoods = Goods.dao.findById(vehicle.getInt("goods_id"));
                Road road = Road.dao.findById(vehicle.getInt("road_id"));
                if(nowGoods == null) {
                    vehicle.set("status", 0);
                    vehicle.set("status_time", randomTime());
                    vehicle.set("current_time", 0);
                    vehicle.set("goods_id", null);
                    vehicle.set("poi_id", road.getInt("ed_id") );
                    vehicle.set("road_id", null);
                    return;
                }
                if(!nowGoods.getBoolean("is_delivered")) {
                    Road road1 = RoadController.getRoad(nowGoods.getInt("st_id"), nowGoods.getInt("ed_id"));
                    int road_id = road1.getInt("id");
                    vehicle.set("status", 2);
                    vehicle.set("status_time", (int)(road1.getDouble("length")/vehicle.getDouble("speed")));
                    vehicle.set("current_time", 0);
                    vehicle.set("road_id", road_id);
                    vehicle.set("poi_id", 0);
                    nowGoods.set("is_delivered", true);
                }
                else {
                    vehicle.set("status", 0);
                    vehicle.set("status_time", randomTime());
                    vehicle.set("current_time", 0);
                    vehicle.set("poi_id", road.getInt("ed_id") );
                    vehicle.set("rood_id", null);
                    vehicle.set("goods_id", null);
                }
            }
            else if(nextStatus == 3) {
                vehicle.set("status", 3);
                vehicle.set("status_time", randomTime());
                vehicle.set("current_time", 0);
            }
            else if(nextStatus == 4) {
                Road road = Road.dao.findById(vehicle.getInt("road_id"));
                vehicle.set("status", 4);
                vehicle.set("status_time", randomTime());
                vehicle.set("poi_id", road.get("ed_id"));
                vehicle.set("road_id", 0);
                vehicle.set("current_time", 0);
            }
        }
    }
}
