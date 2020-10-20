package bean;

import org.litepal.crud.LitePalSupport;

import java.util.HashMap;
import java.util.Map;

public class Fortune extends LitePalSupport {
    Map<String,Object> year = new HashMap<>();
    Map<String,Object> week = new HashMap<>();
    Map<String,Object> today = new HashMap<>();
    Map<String,Object> tomorrow = new HashMap<>();
    Map<String,Object> month = new HashMap<>();

    public Fortune() {
    }

    public Fortune(Map<String, Object> year, Map<String, Object> week, Map<String, Object> today, Map<String, Object> tomorrow, Map<String, Object> month) {
        this.year = year;
        this.week = week;
        this.today = today;
        this.tomorrow = tomorrow;
        this.month = month;
    }

    public Map<String, Object> getYear() {
        return year;
    }

    public void setYear(Map<String, Object> year) {
        this.year = year;
    }

    public Map<String, Object> getWeek() {
        return week;
    }

    public void setWeek(Map<String, Object> week) {
        this.week = week;
    }

    public Map<String, Object> getToday() {
        return today;
    }

    public void setToday(Map<String, Object> today) {
        this.today = today;
    }

    public Map<String, Object> getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(Map<String, Object> tomorrow) {
        this.tomorrow = tomorrow;
    }

    public Map<String, Object> getMonth() {
        return month;
    }

    public void setMonth(Map<String, Object> month) {
        this.month = month;
    }
}
