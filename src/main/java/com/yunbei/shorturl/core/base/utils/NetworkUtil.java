package com.yunbei.shorturl.core.base.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkUtil {
    private final static Logger log = LoggerFactory.getLogger(NetworkUtil.class);

    // private static String machineName = getMachineName();

    // public static String getMachineName() {
    // if (StringUtils.isNotEmpty(machineName)) {
    // log.debug("getMachineName = {} ", machineName);
    // return machineName;
    // }
    // try {
    // machineName = InetAddress.getLocalHost().getHostName();
    // log.warn("getMachineName = {} ", machineName);
    // return machineName;
    // } catch (UnknownHostException e) {
    // e.printStackTrace();
    // }
    // if (StringUtils.isEmpty(machineName)) {
    // System.out.println("ERROR:INIT MACHINE NAME ERROR");
    // System.exit(1);
    // }
    // return null;
    // }

    public static String getRemoteIPForNginx(HttpServletRequest req) {
        String header = req.getHeader("X-Real-IP");

        if (org.apache.commons.lang3.StringUtils.isBlank(header)) {
            return header;
        }

        header = req.getHeader("x-real-ip");

        if (StringUtils.isBlank(header)) {
            return header;
        }

        return req.getRemoteAddr();

    }

    public static String getRemoteIPForNginxWebSocket(HttpServletRequest req) {

        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = req.getHeader(key);
            map.put(key, value);
            log.warn("key:{},value:{}", key, value);
        }

        String ip = req.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("x-real-ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }

        return ip;
    }

    public static long getIpNum(HttpServletRequest req) {
        return getIpNum(getRemoteIPForNginx(req));
    }

    public static long getIpNum(String ipAddress) {
        try {
            String[] ip = ipAddress.split("\\.");
            long a = Integer.parseInt(ip[0]);
            long b = Integer.parseInt(ip[1]);
            long c = Integer.parseInt(ip[2]);
            long d = Integer.parseInt(ip[3]);

            long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
            return ipNum;
        } catch (Exception e) {
            NumberUtil.log.warn(e.getMessage());
            return 0;
        }
    }

    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp = false;
        long ipNum = getIpNum(ipAddress);
        /**
         * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
         * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1") || ipNum <= 0;
        return isInnerIp;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }
}
