package com.spirited.support.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.appframe.library.application.AFApplication;
import com.appframe.utils.app.AppRuntimeUtil;
import com.appframe.utils.logger.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 获取设备相关数据
 */

public class DeviceInfo {

    public static String getIp() {
        String ip = "";
        ConnectivityManager conMann = (ConnectivityManager)
                AppRuntimeUtil.getInstance().getCurrentActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMann != null) {
            NetworkInfo mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mobileNetworkInfo.isConnected()) {
                ip = getLocalIpAddress();
                Logger.getLogger().d("移动网络环境下，本地IP:" + ip);
            } else if (wifiNetworkInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) AFApplication.applicationContext.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    ip = intToIp(ipAddress);
                    Logger.getLogger().d("WIFI环境下，本地IP为：" + ip);
                }
            }
        }

        return ip;
    }

    private static String getLocalIpAddress() {
        try {
            String ipv4;
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nilist) {
                ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address : ialist) {
                    ipv4 = address.getHostAddress();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return ipv4;
                    }
                }

            }

        } catch (SocketException ex) {
            Logger.getLogger().e("获取本地IP失败：" + ex.toString());
        }
        return null;
    }

    private static String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }
}
