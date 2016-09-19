package com.yunbei.shorturl.core.base.utils;

public class FeistelUtil {

    public static double round(long val) {
        return ((131239 * val + 15534) % 714025) / 714025.0;
    }

    public static long permuteId(long id) {
        long l1 = (id >> 16) & 65535;
        long r1 = id & 65535;

        for (int i = 0; i < 2; i++) {
            long l2 = r1;
            long r2 = l1 ^ (long) (round(r1) * 65535);

            l1 = l2;
            r1 = r2;
        }

        return ((r1 << 16) + l1);
    }

    public static String getIndex(long i) {
        return Base62Util.encode(permuteId(i));
    }

    public static void main(String[] args) {
        System.out.println(permuteId(1));
        System.out.println(Base62Util.encode(permuteId(1)));
        System.out.println("49Ikv{18768184438}".hashCode());
        System.out.println(Base62Util.encode(Math.abs(-283718765)));
    }

}
