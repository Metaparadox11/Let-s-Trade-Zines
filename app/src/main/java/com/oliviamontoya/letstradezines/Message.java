package com.oliviamontoya.letstradezines;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Olivia on 6/25/17.
 */

public class Message {
    private long timeStamp;
    private String messageText;
    private String to;
    private String from;

    public Message(String messageText, String to, String from)
    {
        timeStamp = new Date().getTime();
        this.messageText = messageText;
        this.to = to;
        this.from = from;
    }

    public Message(){

    }

    public String getMessageText() {
        return this.messageText;
    }

    public void setMessage(String messageText) {
        this.messageText = messageText;
    }

    public String getTo() { return this.to; }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }
}
