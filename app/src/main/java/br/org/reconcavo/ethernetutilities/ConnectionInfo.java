package br.org.reconcavo.ethernetutilities;

/**
 * Created by felipe on 23/08/16.
 */
public class ConnectionInfo {
    private String ip;
    private String netmask;
    private String gateway;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
}
