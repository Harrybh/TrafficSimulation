package controller;
import bean.Road;

public class RoadController {
    public static Road getRoad(int st_id, int ed_id){
        return Road.dao.findFirst("select * from road where st_id = ? and ed_id = ?", st_id, ed_id);
    }
}
