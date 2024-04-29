package controller;
import bean.Goods;



public class GoodsController {
    private static int id = 0;
    private static final int POI_COUNT = 10;
    private static double randomPrice() {
        return 1000 + Math.random() * 2000;
    }
    private static int randomPoi() {
        return (int) (Math.random() * POI_COUNT + 1);
    }
    private static int randomTime() {
        return (int) (Math.random() * 100);
    }
    public static void createGoods(int current_time) {
        Goods goods = new Goods();
        id++;
        goods.set("id", id);
        goods.set("name", "goods" + id);
        goods.set("weight", 100 + Math.random() * 200);
        goods.set("is_delivered", false);
        goods.set("value", randomPrice());
        int st = randomPoi();
        int ed = randomPoi();
        while(st == ed) {
            ed = randomPoi();
        }
        goods.set("st_id", st);
        goods.set("ed_id", ed);
        goods.set("limit", current_time + randomTime());
        goods.save();
    }
}
