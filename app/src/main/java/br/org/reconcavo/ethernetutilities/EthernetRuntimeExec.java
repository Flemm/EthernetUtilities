package br.org.reconcavo.ethernetutilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by felipe on 23/08/16.
 */
public class EthernetRuntimeExec {
    private static final String ifconfig="/system/bin/ifconfig";
    private static final String netcfg="/system/bin/netcfg";

    public static boolean ethernetInterfaceExists() {
        List<String> list = getListOfNetworkInterfaces();

        return list.contains("eth0");
    }

    public static List<String> getListOfNetworkInterfaces() {

        List<String> list = new ArrayList<String>();

        Enumeration<NetworkInterface> nets;

        try {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {

            e.printStackTrace();
            return null;
        }

        for (NetworkInterface netint : Collections.list(nets)) {

            list.add(netint.getName());
        }

        return list;
    }

    public static boolean isEthOn() throws IOException{
            String line;
            boolean r = false;

            Process p = Runtime.getRuntime().exec(netcfg);

            BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if(line.contains("eth0")){
                    if(line.contains("UP")){
                        r=true;
                    }
                    else{
                        r=false;
                    }
                }
            }
            input.close();

            System.out.println("EthernetRuntimeExec status: "+ (r ? "on" : "off"));
            return r;
    }

    public static void turnEthOn(boolean turnOn) {
        boolean eth0_isUp;
        try {
            eth0_isUp = isEthOn();
        }catch(IOException e){
            e.printStackTrace();
            return;
        }
        try {
            if(!turnOn){
                if(eth0_isUp){
                Runtime.getRuntime().exec(ifconfig + " eth0 down");
                }else{
                    System.out.println("Eth0 is already OFF!");
                }
            }
            else{
                if(!eth0_isUp) {
                    Runtime.getRuntime().exec(ifconfig + " eth0 up");
                }else{
                    System.out.println("Eth0 is already ON!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean connect(ConnectionInfo conn) {
        try {
            Runtime.getRuntime().exec(ifconfig + " eth0 "+conn.getIp()+" netmask "+conn.getNetmask()+" gw "+conn.getGateway());
            System.out.println("Connecting using static info...");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean connect(){
        try {
            Runtime.getRuntime().exec(ifconfig + " eth0 dhcp start");
            System.out.println("Connecting using DHCP...");
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
