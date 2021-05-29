package com.oliviamontoya.letstradezines;

import java.util.Date;

/**
 * Created by Olivia on 6/29/17.
 */

public class Zine {
    private long timeStamp;
    private String descriptionText;
    private String user;
    private String zineName;
    private String numPages;
    private String zineSize;
    private String tags;

    public Zine(String user, String zineName, String numPages, String zineSize, String descriptionText, String tags)
    {
        timeStamp = new Date().getTime();
        this.user = user;
        this.zineName = zineName;
        this.numPages = numPages;
        this.zineSize = zineSize;
        this.descriptionText = descriptionText;
        this.tags = tags;
    }

    public Zine() {

    }

    public String getDescriptionText() {
        return this.descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getUser() { return this.user; }

    public void setUser(String user) {
        this.user = user;
    }

    public String getZineName() { return this.zineName; }

    public void setZineName(String zineName) {
        this.zineName = zineName;
    }

    public String getNumPages() { return this.numPages; }

    public void setNumPages(String numPages) {
        this.numPages = numPages;
    }

    public String getZineSize() { return this.zineSize; }

    public void setZineSize(String zineSize) {
        this.zineSize = zineSize;
    }

    public String getTags() { return this.tags; }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }
}
