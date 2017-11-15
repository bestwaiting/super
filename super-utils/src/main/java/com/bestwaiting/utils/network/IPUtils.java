package com.bestwaiting.utils.network;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by bestwaiting on 17/8/14.
 */
@Slf4j
public final class IPUtils {

    static String ip = null;

    private IPUtils() {
    }

    /**
     * 获取本地有效IP
     *
     * @return
     */
    public static String getLocalIP() {
        if (ip != null) {
            return ip;
        } else {
            Enumeration<NetworkInterface> netInterfaces;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface netInterface = netInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address.isSiteLocalAddress() && !address.isLoopbackAddress()) {
                            if (address instanceof Inet4Address) {
                                ip = address.getHostAddress();
                                return ip;
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                log.error("IPUtils 获取IP地址失败", e.toString());
            }
            return null;
        }
    }
}
