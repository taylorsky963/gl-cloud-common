package com.gl.myparadise.common.tools.http;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: IpAddressUtil
 * @ProjectName: gl-common
 * @Date 2023-10-09 - 15:23
 */
@Slf4j
public class IpAddressUtil {

    private IpAddressUtil(){}

    @Deprecated
    public static String getClinetIpByHttpServletRequest(HttpServletRequest request) {
        // 获取客户端ip地址
        String clientIp = request.getHeader("x-forwarded-for");

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        /*
         * 对于获取到多ip的情况下，找到公网ip.
         */
        String sIP = null;
        if (clientIp != null && !clientIp.contains("unknown") && clientIp.indexOf(",") > 0) {
            String[] ipsz = clientIp.split(",");
            for (String anIpsz : ipsz) {
                if (!isInnerIP(anIpsz.trim())) {
                    sIP = anIpsz.trim();
                    break;
                }
            }
            /*
             * 如果多ip都是内网ip，则取第一个ip.
             */
            if (null == sIP) {
                sIP = ipsz[0].trim();
            }
            clientIp = sIP;
        }
        if (clientIp != null && clientIp.contains("unknown")) {
            clientIp = clientIp.replaceAll("unknown,", "");
            clientIp = clientIp.trim();
        }
        if ("".equals(clientIp) || null == clientIp) {
            clientIp = "127.0.0.1";
        }
        return clientIp;
    }

    /**
     * 判断IP是否是内网地址
     *
     * @param ipAddress ip地址
     * @return 是否是内网地址
     */
    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp;
        long ipNum = getIpNum(ipAddress);
        /**
         私有IP：A类  10.0.0.0-10.255.255.255
         B类  172.16.0.0-172.31.255.255
         C类  192.168.0.0-192.168.255.255
         当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");

        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");

        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    public static String getRealIP(HttpServletRequest request) {

        String realIp = "127.0.0.1";

        String xForwardedFor = request.getHeader("X-Forwarded-For");

        String xRealIp = request.getHeader("X-Real-IP");

        log.debug("getRealIP X-Forwarded-For: {}", xForwardedFor);

        log.debug("getRealIP X-Real-IP: {}", xRealIp);

        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {

            String[] ipList = xForwardedFor.split(",");

            if (isComefromCDN(request) && ipList.length >= 2) {

                realIp = ipList[ipList.length - 2].trim();

                log.debug("CDN detected, use CDN-preceding IP: {}", realIp);

                return realIp;
            }

            for (String ip : ipList) {

                ip = ip.trim();

                if (!"unknown".equalsIgnoreCase(ip) && !ip.isEmpty()) {

                    realIp = ip;

                    log.debug("First valid IP from X-Forwarded-For: {}", realIp);

                    return realIp;
                }
            }
        }

        // fallback to X-Real-IP
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {

            realIp = xRealIp.trim();

            log.debug("Fallback to X-Real-IP: {}", realIp);

            return realIp;

        }

        // final fallback
        realIp = request.getRemoteAddr();

        log.debug("Fallback to request.getRemoteAddr(): {}", realIp);

        return realIp;
    }

    private static boolean isComefromCDN(HttpServletRequest request) {

        String via = request.getHeader("Via");
        String cdnSrcIp = request.getHeader("CDN-Src-IP");
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String userAgent = request.getHeader("User-Agent");

        log.debug("CDN check - Via: {}", via);

        log.debug("CDN check - CDN-Src-IP: {}", cdnSrcIp);

        log.debug("CDN check - X-Forwarded-For: {}", xForwardedFor);

        log.debug("CDN check - User-Agent: {}", userAgent);

        // 判断是否存在CDN特征
        if ((via != null && via.toLowerCase().contains("cdn")) ||
                (cdnSrcIp != null && !cdnSrcIp.isEmpty()) ||
                (userAgent != null && userAgent.toLowerCase().contains("cdn")) ||
                (xForwardedFor != null && xForwardedFor.split(",").length > 1)) {

            return true;

        }

        return false;

    }
}