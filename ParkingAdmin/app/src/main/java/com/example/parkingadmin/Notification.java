package com.example.parkingadmin;

public class Notification {

    private String notificationName;
    private String notificationData;

    public Notification(String notificationName, String notificationData) {
        this.notificationName = notificationName;
        this.notificationData = notificationData;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(String notificationData) {
        this.notificationData = notificationData;
    }
}
