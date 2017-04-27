package com.rentit.priyath.rentitlayout.models;


public class RequestBody {

    private String deviceName;
    private String deviceId;
    private String registrationId;
    private String userId;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
    public void setUserId(String userId){this.userId = userId; }
}
