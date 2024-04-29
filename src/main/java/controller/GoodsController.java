package controller;
import bean.Goods;



public class GoodsController {
    private static int id = 0;
    private static double randomPrice() {
        return 1000000 + Math.random() * 2000000;
    }
    private static int randomPoi(final int POI_COUNT) {
        return (int) (Math.random() * POI_COUNT + 1);
    }
    private static int randomTime() {
        return (int) (Math.random() * 100);
    }
    public static void createGoods(int current_time,final int POI_COUNT) {
        Goods goods = new Goods();
        id++;
        goods.set("id", id);
        goods.set("name", "goods" + id);
        goods.set("weight", 5 + Math.random() * 30);
        goods.set("is_delivered", false);
        goods.set("value", randomPrice());
        int st = randomPoi(POI_COUNT);
        int ed = randomPoi(POI_COUNT);
        while(st == ed) {
            ed = randomPoi(POI_COUNT);
        }
        goods.set("st_id", st);
        goods.set("ed_id", ed);
        goods.set("limit", current_time + randomTime());
        System.out.println(goods);
        goods.save();
    }
}
