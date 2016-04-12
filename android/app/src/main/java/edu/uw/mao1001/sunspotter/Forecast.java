package edu.uw.mao1001.sunspotter;

import java.util.Date;

/**
 * Created by Nick on 4/11/2016.
 */
public class Forecast {
    private Double temperature;
    private Date date;
    private boolean sunStatus;

    public Forecast(String rawDate, String sunStatus, Double temperature) {
        long dateValue = Long.valueOf(rawDate) * 1000;
        Date date = new Date(dateValue);
        this.date = date;

        this.temperature = temperature;

        this.sunStatus = (sunStatus.equals("clear") || sunStatus.equals("few clouds"));
    }

    public boolean getSunStatus() {
        return sunStatus;
    }

    public Date getDate() {
        return date;
    }

    public Double getTemperature() {
        return temperature;
    }
}
