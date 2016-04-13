package edu.uw.mao1001.sunspotter;

import java.util.Date;

/**
 * Created by Nick on 4/11/2016.
 */
public class Forecast {
    private Double temperature;
    private Date date;
    private String sunStatus;

    public Forecast(String rawDate, String sunStatus, Double temperature) {
        long dateValue = Long.valueOf(rawDate) * 1000;
        Date date = new Date(dateValue);
        this.date = date;

        this.temperature = temperature;

        if (sunStatus.equals("clear") || sunStatus.equals("few clouds")) {
            this.sunStatus = "Sun";
        } else {
            this.sunStatus = "No sun";
        }
    }

    public String getSunStatus() {
        return sunStatus;
    }

    public Date getDate() {
        return date;
    }

    public Double getTemperature() {
        return temperature;
    }

    public String toString() {
        return sunStatus + " " + date.toString();
    }
}
