//package com.restocktime.monitor.helper;
//
//import org.apache.commons.codec.digest.DigestUtils;
//
//public class EndMiner {
//    public void mine(){
//
//    }
//
//    public String hash(String t){
//        long[] value = { 1518500249, 1859775393, 2400959708L, 3395469782L };
//        int visRecords = t.length()/4 + 2;
//        int n = (int)Math.ceil(visRecords / 16);
//        long[][] result = new long[n][n];
//        for(int i = 0 ; i < n; i++) {
//            for (int j = 0; j < 16; j++) {
//                result[i][j] = Character.codePointAt(t, 64 * i + 4 * j) << 24 |
//                        Character.codePointAt(t, 64 * i + 4 * j + 1) << 16 | Character.codePointAt(t, 64 * i + 4 * j + 2) << 8 | Character.codePointAt(t, 64 * i + 4 * j + 3)
//            }
//        }
//        result[n - 1][14] = 8 * (t.length() - 1) / (long)Math.pow(2, 32);
//        result[n - 1][14] = (long)Math.floor(result[n - 1][14]);
//        result[n - 1][15] = 8 * (t.length() - 1) & 4294967295L;
//
//        long a;
//        long b;
//        long c;
//        long d;
//        long e;
//        /** @type {number} */
//        long H0 = 1732584193;
//        /** @type {number} */
//        long H1 = 4023233417L;
//        /** @type {number} */
//        long H2 = 2562383102L;
//        /** @type {number} */
//        long H3 = 271733878;
//        /** @type {number} */
//        long s = 3285377520L;
//        /** @type {!Array} */
//        long[] sprites = new long[80];
//        int i =0;
//        for (; n > i; i++) {
//            /** @type {number} */
//            int j = 0;
//            for (; 16 > j; j++) {
//                sprites[j] = result[i][j];
//            }
//            /** @type {number} */
//            j = 16;
//            for (; 80 > j; j++) {
//                sprites[j] = rotl(sprites[j - 3] ^ sprites[j - 8] ^ sprites[j - 14] ^ sprites[j - 16], 1);
//            }
//            /** @type {number} */
//            a = H0;
//            /** @type {number} */
//            b = H1;
//            /** @type {number} */
//            c = H2;
//            /** @type {number} */
//            d = H3;
//            /** @type {number} */
//            e = s;
//            /** @type {number} */
//            j = 0;
//            for (; 80 > j; j++) {
//                /** @type {number} */
//                int s1 = (int)Math.floor(j / 20);
//                /** @type {number} */
//                long nativeObjectObject = rotl(a, 5) + f(s1, b, c, d) + e + value[s1] + sprites[j] & 4294967295;
//                e = d;
//                d = c;
//                c = rotl(b, 30);
//                /** @type {number} */
//                b = a;
//                /** @type {number} */
//                a = nativeObjectObject;
//            }
//            /** @type {number} */
//            H0 = H0 + a & 4294967295L;
//            /** @type {number} */
//            H1 = H1 + b & 4294967295L;
//            /** @type {number} */
//            H2 = H2 + c & 4294967295L;
//            /** @type {number} */
//            H3 = H3 + d & 4294967295L;
//            /** @type {number} */
//            s = s + e & 4294967295L;
//
//        }
//        return toHexStr(H0) + toHexStr(H1) + toHexStr(H2) + toHexStr(H3) + toHexStr(s);
//    }
//
//    private long rotl(long x, long n) {
//        return x << n | x >>> 32 - n;
//    }
//
//    private long f(long underscore, long t, long a, long b) {
//        switch ((int) underscore) {
//            case 0:
//                return t & a ^ ~t & b;
//            case 1:
//                return t ^ a ^ b;
//            case 2:
//                return t & a ^ t & b ^ a & b;
//            case 3:
//                return t ^ a ^ b;
//        }
//    }
//
//    private String toHexStr(long val) {
//        long default_favicon;
//        /** @type {string} */
//        String s = "";
//        /** @type {number} */
//        int b = 7;
//        for (; b >= 0; b--) {
//            /** @type {number} */
//            default_favicon = val >>> 4 * b & 15;
//            /** @type {string} */
//            s = s + Long.toHexString(default_favicon);
//        }
//        return s;
//    }
//}