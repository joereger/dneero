package com.dneero.iptrack;

import com.dneero.util.Num;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Dec 3, 2008
 * Time: 8:13:34 PM
 */
public class IpAddressParser {

    private String ipaddress;
    private int octet1;
    private int octet2;
    private int octet3;
    private int octet4;

    public IpAddressParser(String ipaddress){
        this.ipaddress =  ipaddress;
        parse();
    }

    public IpAddressParser(HttpServletRequest request){
        this.ipaddress = request.getRemoteAddr();
        parse();
    }

    private void parse(){
        if (ipaddress!=null){
            String[] split = ipaddress.split("\\.");
            if (split.length>=4){
                if (Num.isinteger(split[0])){
                    octet1 = Integer.parseInt(split[0]);
                }
                if (Num.isinteger(split[1])){
                    octet2 = Integer.parseInt(split[1]);
                }
                if (Num.isinteger(split[2])){
                    octet3 = Integer.parseInt(split[2]);
                }
                if (Num.isinteger(split[3])){
                    octet4 = Integer.parseInt(split[3]);
                }
            }
        }
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress=ipaddress;
    }

    public int getOctet1() {
        return octet1;
    }

    public void setOctet1(int octet1) {
        this.octet1=octet1;
    }

    public int getOctet2() {
        return octet2;
    }

    public void setOctet2(int octet2) {
        this.octet2=octet2;
    }

    public int getOctet3() {
        return octet3;
    }

    public void setOctet3(int octet3) {
        this.octet3=octet3;
    }

    public int getOctet4() {
        return octet4;
    }

    public void setOctet4(int octet4) {
        this.octet4=octet4;
    }
}
