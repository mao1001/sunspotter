package edu.uw.mao1001.sunspotter;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nick on 4/11/2016.
 */
public class Forecast {
    private Double temperature;
    private Date date;
    private boolean sunStatus;

    //-----------------------------//
    //   C O N S T R U C T O R S   //
    //-----------------------------//

    /**
     * Constructor to build an object representing a single forecast.
     * @param rawDate : Date in unix UTC form.
     * @param sunStatus : Descriptor of the sun status
     * @param temperature : Temperature at the time listed.
     */
    public Forecast(String rawDate, String sunStatus, Double temperature) {
        long dateValue = Long.valueOf(rawDate) * 1000;
        Date date = new Date(dateValue);
        this.date = date;
        this.temperature = temperature;
        this.sunStatus = (sunStatus.equals("clear") || sunStatus.equals("few clouds"));
    }

    //-----------------------------------//
    //   G E T T E R S / S E T T E R S   //
    //-----------------------------------//

    /**
     * Gets the sun status for this forecast.
     * @return boolean: Representing the status of the sun for this forecast.
     */
    public boolean getSunStatus() {
        return sunStatus;
    }

    /**
     * Takes the date and returns a meaningful string.
     * Example:
     * 'Wed, Apr 13, 2:00AM, PDT'
     * @return String: Meaningful representation of the date.
     */
    public String getDate() {
        return new SimpleDateFormat("EEE, MMM d, h:mma, z").format(this.date);
    }

    /**
     * Gets the temperature for this forecast.
     * 'Wed, Apr 13, 2:00AM, PDT'
     * @return Double: Temperature
     */
    public Double getTemperature() {
        return temperature;
    }
}
