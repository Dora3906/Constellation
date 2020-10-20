package bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Weather {
    private Map<String, String> result;
    private List<Map<String, String>> index;
    private Map<String, String> aqi;
    private List<Map<String, String>> daily;
    private List<Map<String, String>> hourly;

    public Weather() {
    }

    public Weather(Map result, List<Map<String, String>> index, Map<String, String> aqi, List<Map<String, String>> daily, List<Map<String, String>> hourly) {
        this.result = result;
        this.index = index;
        this.aqi = aqi;
        this.daily = daily;
        this.hourly = hourly;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }

    public List<Map<String, String>> getIndex() {
        return index;
    }

    public void setIndex(List<Map<String, String>> index) {
        this.index = index;
    }

    public Map<String, String> getAqi() {
        return aqi;
    }

    public void setAqi(Map<String, String> aqi) {
        this.aqi = aqi;
    }

    public List<Map<String, String>> getDaily() {
        return daily;
    }

    public void setDaily(List<Map<String, String>> daily) {
        this.daily = daily;
    }

    public List<Map<String, String>> getHourly() {
        return hourly;
    }

    public void setHourly(List<Map<String, String>> hourly) {
        this.hourly = hourly;
    }
}
