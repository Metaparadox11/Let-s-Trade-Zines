package com.oliviamontoya.letstradezines;

import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Olivia on 7/4/17.
 */

public class Trade {
    private long timeStamp;
    private String tradeInitiator;
    private String tradeAccepter;
    private String tradeInitiatorZines;
    private String tradeAccepterZines;
    private Boolean tradeInitiatorCompleted;
    private Boolean tradeInitiatorCancelled;
    private Boolean tradeAccepterAccepted;
    private Boolean tradeAccepterCompleted;
    private Boolean tradeAccepterCancelled;

    public Trade(String tradeInitiator, String tradeAccepter, String tradeInitiatorZines, String tradeAccepterZines)
    {
        timeStamp = new Date().getTime();
        this.tradeInitiator = tradeInitiator;
        this.tradeAccepter = tradeAccepter;
        this.tradeInitiatorZines = tradeInitiatorZines;
        this.tradeAccepterZines = tradeAccepterZines;
        tradeInitiatorCompleted = false;
        tradeInitiatorCancelled = false;
        tradeAccepterAccepted = false;
        tradeAccepterCompleted = false;
        tradeAccepterCancelled = false;
    }

    public Trade() {

    }

    public String getTradeInitiator() {
        return this.tradeInitiator;
    }

    public void setTradeInitiator(String tradeInitiator) {
        this.tradeInitiator = tradeInitiator;
    }

    public String getTradeAccepter() { return this.tradeAccepter; }

    public void setTradeAccepter(String tradeAccepter) {
        this.tradeAccepter = tradeAccepter;
    }

    public String getTradeInitiatorZines() { return this.tradeInitiatorZines; }

    public void setTradeInitiatorZines(String tradeInitiatorZines) {
        this.tradeInitiatorZines = tradeInitiatorZines;
    }

    public String getTradeAccepterZines() { return this.tradeAccepterZines; }

    public void setTradeAccepterZines(String tradeAccepterZines) {
        this.tradeAccepterZines = tradeAccepterZines;
    }

    public Boolean getTradeInitiatorCompleted() { return this.tradeInitiatorCompleted; }

    public void setTradeInitiatorCompleted(Boolean tradeInitiatorCompleted) {
        this.tradeInitiatorCompleted = tradeInitiatorCompleted;
    }

    public Boolean getTradeInitiatorCancelled() { return this.tradeInitiatorCancelled; }

    public void setTradeInitiatorCancelled(Boolean tradeInitiatorCancelled) {
        this.tradeInitiatorCancelled = tradeInitiatorCancelled;
    }

    public Boolean getTradeAccepterAccepted() { return this.tradeAccepterAccepted; }

    public void setTradeAccepterAccepted(Boolean tradeAccepterAccepted) {
        this.tradeAccepterAccepted = tradeAccepterAccepted;
    }

    public Boolean getTradeAccepterCompleted() { return this.tradeAccepterCompleted; }

    public void setTradeAccepterCompleted(Boolean tradeAccepterCompleted) {
        this.tradeAccepterCompleted = tradeAccepterCompleted;
    }

    public Boolean getTradeAccepterCancelled() { return this.tradeAccepterCancelled; }

    public void setTradeAccepterCancelled(Boolean tradeAccepterCancelled) {
        this.tradeAccepterCancelled = tradeAccepterCancelled;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }

    public Boolean timePassed() {
        long currentTime = new Date().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStamp);
        cal.add(Calendar.DAY_OF_MONTH, 30);
        long ts = cal.getTime().getTime();
        if (currentTime >= ts) {
            return true;
        } else {
            return false;
        }
    }
}
