package com.durbinlabs.usbcamera;

/**
 * Created by Sifat Ullah Chowdhury on 7/20/2019.
 * Durbin Labs Ltd
 * sif.sifat24@gmail.com
 */
public class DeviceInfo {
    private int ProductID ;
    private int DeviceClass;
    private int Subclass ;
    private int protocol;
    public DeviceInfo(int productID, int deviceClass, int subclass, int protocol) {
        ProductID = productID;
        DeviceClass = deviceClass;
        Subclass = subclass;
        this.protocol = protocol;
    }
    public int getProductID() {
        return ProductID;
    }

    public int getSubclass() {
        return Subclass;
    }

    public int getProtocol() {
        return protocol;
    }
    public int getDeviceClass() {
        return DeviceClass;
    }
}
