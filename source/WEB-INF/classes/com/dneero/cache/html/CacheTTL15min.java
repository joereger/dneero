package com.dneero.cache.html;

/**
 * User: Joe Reger Jr
 * Date: Oct 26, 2008
 * Time: 12:00:03 PM
 */
public class CacheTTL15min implements CacheTTL {

    private static int hrs = 0;
    private static int min = 15;


    public int getMinutesTL(){
        return (hrs*60)+(min);
    }

}