package com.jeremie.dynamic;

import org.apache.commons.lang.time.DateUtils;

import java.util.*;

/**
 * @author guanhong 2019-06-19.
 */
public class Test2 {
    /**
     * 获取n天前\后的那天0点
     *
     * @param whiteSpan 为正表示当前时间的几天后，为负表示几天前
     * @return
     */
    public static Date getTimeAddDays(int whiteSpan) {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.set(Calendar.YEAR, 1970);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static void main(String[] args) {
        //System.out.println(getTimeAddDays(0));
        //System.out.println(DateUtils.addMilliseconds(getTimeAddDays(2+1), -1));
        /*System.out.println(getTimeAddDays(0).getTime());
        System.out.println(new Date(0).getTime());
        System.out.println(new Date(0));*/
        Map<Integer,String> test = new HashMap<>();
        test.put(null,"test");
        System.out.println(test.get(null));
    }
}
