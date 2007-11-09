package com.dneero.constants;

import java.util.TreeSet;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 12:34:09 PM
 */
public class Incomes {

    public static TreeSet<String> get(){
        TreeSet ts = new TreeSet();
        ts.add("0 - 10000");
        ts.add("10001 - 20000");
        ts.add("20001 - 30000");
        ts.add("30001 - 40000");
        ts.add("40001 - 50000");
        ts.add("50001 - 60000");
        ts.add("60001 - 70000");
        ts.add("70001 - 80000");
        ts.add("80001 - 90000");
        ts.add("90001 - 100000");
        ts.add("100001 - 120000");
        ts.add("120001 - 150000");
        ts.add("150001 - 200000");
        ts.add("200000 and Above");
        return ts;
    }

}
