package com.durbinlabs.usbcamera.view;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;
import com.durbinlabs.com.usbcamera.R;
import com.durbinlabs.usbcamera.DeviceInfo;
import com.durbinlabs.usbcamera.UVCCameraHelper;
import com.durbinlabs.usbcamera.utils.FileUtils;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.widget.CameraViewInterface;

import java.util.ArrayList;

/**
 * Created by Sifat Ullah Chowdhury on 7/20/2019.
 * Durbin Labs Ltd
 * sif.sifat24@gmail.com
 */

public class USBCameraActivity extends AppCompatActivity implements CameraDialog.CameraDialogParent, CameraViewInterface.Callback {
    private static final String TAG = "Debug";


    private UVCCameraHelper mCameraHelper;
    private AlertDialog mDialog;

    private boolean isRequest;
    private boolean isPreview;

    private UVCCameraHelper.OnMyDevConnectListener listener = new UVCCameraHelper.OnMyDevConnectListener() {
        private  ArrayList<DeviceInfo> getSupportedDevices(){

            ArrayList<DeviceInfo> deviceInfos= new ArrayList<>();
            deviceInfos.add(new DeviceInfo(9229,0,1,0));
            return deviceInfos;
        }
        @Override
        public void onAttachDev(UsbDevice device) {
            // request open permission
            if (!isRequest) {
                isRequest = true;
                if (mCameraHelper != null) {

                    for (DeviceInfo supportedDev : getSupportedDevices()){
                        if (device.getProductId()==supportedDev.getProductID())
                        {
                            mCameraHelper.requestPermission(0);
                            showShortMsg("connecting");
                            return;
                        }

                        else if (device.getDeviceClass()==supportedDev.getDeviceClass() && device.getDeviceSubclass()==supportedDev.getSubclass())
                        {
                            mCameraHelper.requestPermission(0);
                            showShortMsg("connecting");
                            return;
                        }
                    }

                    //do something if the connected device is not supported
                    showShortMsg("Connected device is not supported");

                }
            }
        }

        @Override
        public void onDettachDev(UsbDevice device) {
            // close camera
            if (isRequest) {
                isRequest = false;
                mCameraHelper.closeCamera();
                showShortMsg(device.getDeviceName() + " is out");
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            if (!isConnected) {
                showShortMsg("fail to connect,please check resolution params");
                isPreview = false;
            } else {
                isPreview = true;
                showShortMsg("connected device is supported");
            }
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            showShortMsg("disconnecting");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usbcamera);
        //ButterKnife.bind(this);


        // step.1 initialize UVCCameraHelper
     /*   mUVCCameraView = (CameraViewInterface) mTextureView;
        mUVCCameraView.setCallback(this);*/
        mCameraHelper = UVCCameraHelper.getInstance();
     //   mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.initUSBMonitor(this, null, listener);

    }



    @Override
    protected void onStart() {
        super.onStart();
        // step.2 register USB event broadcast
        if (mCameraHelper != null) {
            mCameraHelper.registerUSB();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // step.3 unregister USB event broadcast
        if (mCameraHelper != null) {
            mCameraHelper.unregisterUSB();
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.releaseFile();
        // step.4 release uvc camera resources
        if (mCameraHelper != null) {
            mCameraHelper.release();
        }
    }

    private void showShortMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public USBMonitor getUSBMonitor() {
        return mCameraHelper.getUSBMonitor();
    }

    @Override
    public void onDialogResult(boolean canceled) {
        if (canceled) {
            showShortMsg("取消操作");
        }
    }

    public boolean isCameraOpened() {
        return mCameraHelper.isCameraOpened();
    }

    @Override
    public void onSurfaceCreated(CameraViewInterface view, Surface surface) {

    }

    @Override
    public void onSurfaceChanged(CameraViewInterface view, Surface surface, int width, int height) {

    }

    @Override
    public void onSurfaceDestroy(CameraViewInterface view, Surface surface) {

    }
}