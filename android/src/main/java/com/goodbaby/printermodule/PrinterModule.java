package com.goodbaby.printermodule;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.goodbaby.printermodule.entity.OrderEntity;
import com.goodbaby.printermodule.utils.PrintUtil;
import com.goodbaby.printermodule.utils.Utils;
import com.printsdk.usbsdk.UsbDriver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by limingguang on 2018/8/22.
 */

public class PrinterModule extends ReactContextBaseJavaModule {

    private static final String TAG = "PrinterModule";

    private ReactApplicationContext reactContext;
    private ArrayList<UsbDevice> deviceList;
    private static UsbDriver mUsbDriver;
    private static UsbDevice mUSBDevice;
    private UsbManager manager;
    public static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";

    public PrinterModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        init();
    }

    @Override
    public String getName() {
        return "PrinterModule";
    }

    @ReactMethod
    public void print(ReadableMap json) {
        if(json == null){
            Toast.makeText(reactContext,"打印内容为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(json.getInt("code") != 0){
            Toast.makeText(reactContext,"打印报文错误!", Toast.LENGTH_SHORT).show();
            return;
        }
        OrderEntity orderEntity = Utils.serializeOrder(json);
        // 根据不同的模板调用不同的打印方法
        usbAutoConn(manager);
        if(!PrintUtil.isUsbPrinter(mUSBDevice)){
            return;
        }
        // 1.第一步打印小票
        PrintUtil.printTicket(mUSBDevice, mUsbDriver, orderEntity);
        // 2.第二步打印优惠信息
        PrintUtil.printDeduct(mUSBDevice, mUsbDriver, orderEntity);
        // 3.第三步打印提货存根联
        PrintUtil.printPickUpStub(mUSBDevice, mUsbDriver, orderEntity);
        // 4.第四步打印提货单
        for (int i =0; i< orderEntity.getContainerItemList().size(); i++){
            PrintUtil.printPickUpBill(mUSBDevice, mUsbDriver, orderEntity, orderEntity.getContainerItemList().get(i));
        }
        // 5. 切纸
        PrintUtil.setFeedCut(0, mUSBDevice, mUsbDriver);
    }

    private void init(){
        manager = (UsbManager) reactContext.getSystemService(Context.USB_SERVICE);
    }

    /**
     * 打开自动连接打印机
     *
     * @param manager
     */
    public void usbAutoConn(UsbManager manager) {
        doDiscovery(manager);
        if (deviceList.isEmpty()) {
//            Looper.prepare();
            Toast.makeText(reactContext, "USB打印机没有连接", Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return;
        }
        mUSBDevice = deviceList.get(0);
        if (mUSBDevice == null) {
            String connectFaile = "连接失败";
//            Looper.prepare();
            Toast.makeText(reactContext, connectFaile, Toast.LENGTH_SHORT).show();
//            Looper.loop();
            return;
        }
        // MS
        mUsbDriver = new UsbDriver(manager, reactContext);
        UsbManager mUsbManager = (UsbManager) reactContext.getSystemService(Context.USB_SERVICE);
        try {
            if (mUsbManager != null) {
                if (mUsbManager.hasPermission(mUSBDevice)) {
                    openUsbConnect();
                } else {
                    // 没有权限询问用户是否授予权限
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(reactContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                    filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                    filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                    reactContext.registerReceiver(mUsbReceiver, filter);
                    mUsbManager.requestPermission(mUSBDevice, pendingIntent); // 该代码执行后，系统弹出一个对话框
                }
            } else {
                String msg = "USBManager空指针";
                Log.d(TAG, "usbAutoConn: " + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "USB-Service空指针";
            Log.d(TAG, "usbAutoConn: " + msg);
        }
    }

    /**
     * 打开USB打印机
     */
    private void openUsbConnect() {
        mUsbDriver.usbAttached(mUSBDevice);
        boolean b = mUsbDriver.openUsbDevice(mUSBDevice);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    reactContext.unregisterReceiver(mUsbReceiver);
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            && mUSBDevice.equals(device)) {
                        openUsbConnect();
                    } else {
                    }
                }
            }
        }
    };

    /**
     * 没有则自动获取设备
     *
     * @param manager
     */
    private void doDiscovery(UsbManager manager) {
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        deviceList = new ArrayList<UsbDevice>();
        for (UsbDevice device : devices.values()) {
            if (PrintUtil.isUsbPrinter(device)) {
                Log.d(TAG, "doDiscovery: " + device.getDeviceName());
                deviceList.add(device);
            }
//            if (USBPort.isUsbPrinter(device)) {
//                deviceList.add(device);
//            }
        }
    }


}
