package com.make1.antenna.util;

import com.orhanobut.logger.Logger;

/**
 * Created by Vange on 2017/9/21.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 * <p>
 * 天线不同功能组合数据的工具类
 */

public class AntennaDataCombineUtil {

    /**
     * 组合 0x11 功能的数据体
     * 保留 3位
     *
     * @param snr       信噪比（2位） -10 ~ 100 精度为0.01
     * @param status    地理位置解调状态（1位）
     * @param longitude 经度信息（2位） ±180° 精度为0.01
     * @param latitude  纬度信息（2位） ±90° 精度为0.01
     * @param type      接收机类型（1位）
     * @param frequency 信号频率（4位）
     * @param rsl       接收信号强度（2位） -50~-150dBm 精度为0.01
     */
    public static int[] combineBroadcastInfoData(String snr, String status
            , String longitude, String latitude
            , String type, String frequency, String rsl) {

        if (snr == null || snr.equals("")) {
            snr = "0000";
        }
        if (status == null || status.equals("")) {
            status = "00";
        }
        if (longitude == null || longitude.equals("")) {
            longitude = "0000";
        }
        if (latitude == null || latitude.equals("")) {
            latitude = "0000";
        }
        if (type == null || type.equals("")) {
            type = "00";
        }
        if (frequency == null || frequency.equals("")) {
            frequency = "00000000";
        }
        if (rsl == null || rsl.equals("")) {
            rsl = "0000";
        }

        String result = snr + status + longitude
                + latitude + type + frequency
                + rsl + "000000";
        System.out.print("result:" + result + "\n");

        String[] strs = new String[result.length() - 1];
        int[] ints = new int[result.length() / 2 + 1];
        int j = 0;

        for (int i = 0; i < result.length() - 1; i++) {
            if (i % 2 == 0) {
                strs[i] = result.substring(i, i + 2);
                ints[j] = Integer.valueOf(strs[i], 16) & 0xff;
                j++;
//                Logger.i("ints" + j + ":" + ints[j] + "\n");
            }
        }
        return ints;
    }


    /**
     * 组合 0x13 功能的数据体
     *
     * @param funcCode 子功能码
     * @param angle    要求转动的角度值 -2880° ~ +2880°
     */
    public static int[] combineAntennaTestInfoData(String funcCode, String angle) {
        if (funcCode == null || "".equals(funcCode)) {
            funcCode = "00";
        }

        if (angle == null || "".equals(angle)) {
            angle = "0000";
        }
        String result = funcCode + angle + "000000000000";

        String[] strs = new String[result.length() - 1];
        int[] ints = new int[result.length() / 2 + 1];
        int j = 0;

        for (int i = 0; i < result.length() - 1; i++) {
            if (i % 2 == 0) {
                strs[i] = result.substring(i, i + 2);
                ints[j] = Integer.valueOf(strs[i], 16) & 0xff;
                j++;
            }
        }
        return ints;
    }


    /**
     * 组合 0x43 功能的数据体
     *
     * @param type    版本类型
     * @param version 版本信息数据
     * @return
     */
    public static int[] combineVersionInfoData(String type, String version) {
        if (type == null || "".equals(type)) {
            type = "00";
        }

        if (version == null || "".equals(version)) {
            version = "0000";
        }

        String result = type + version;
        String[] strs = new String[result.length() - 1];
        int[] ints = new int[result.length() / 2];
        int j = 0;

        for (int i = 0; i < result.length() - 1; i++) {
            if (i % 2 == 0) {
                strs[i] = result.substring(i, i + 2);
                Logger.d("ints j:" + j);
                ints[j] = Integer.valueOf(strs[i], 16) & 0xff;
                j++;
            }
        }
        Logger.d("ints size:" + ints.length);
        return ints;
    }


    /**
     * 组合 0x50 功能的数据体
     *
     * @param func       功能
     * @param longitude  卫星经度
     * @param type       接收机类型
     * @param polarity   极化方式
     * @param frequency  频率设定
     * @param symbolRate 符号率
     * @param threshold  门限
     * @return
     */
    public static int[] combineSatelliteConfigData(String func, String longitude,
                                                   String type, String polarity, String frequency
            , String symbolRate, String threshold) {
        if (func == null || "".equals(func)) {
            func = "00";
        }

        if (longitude == null || "".equals(longitude)) {
            longitude = "0000";
        }

        if (type == null || "".equals(type)) {
            type = "00";
        }

        if (polarity == null || "".equals(polarity)) {
            polarity = "00";
        }

        if (frequency == null || "".equals(frequency)) {
            frequency = "00000000";
        }

        if (symbolRate == null || "".equals(symbolRate)) {
            symbolRate = "0000";
        }

        if (threshold == null || "".equals(threshold)) {
            threshold = "00";
        }
//        Logger.i("func:" + func);
//        Logger.i("longitude:" + longitude);
//        Logger.i("type:" + type);
//        Logger.i("polarity:" + polarity);
//        Logger.i("frequency:" + frequency);
//        Logger.i("symbolRate:" + symbolRate);
//        Logger.i("threshold:" + threshold);

        String result = func + longitude + type + polarity + frequency + symbolRate + threshold + "00";
        Logger.i("result:" + result);
        Logger.i("result size:" + result.length());
        String[] strs = new String[result.length() - 1];
        int[] ints = new int[result.length() / 2 + 1];
        int j = 0;

        for (int i = 0; i < result.length() - 1; i++) {
            if (i % 2 == 0) {
                strs[i] = result.substring(i, i + 2);
                ints[j] = Integer.valueOf(strs[i], 16) & 0xff;
                j++;
            }
        }
        Logger.i("ints size:" + ints.length);
        return ints;
    }


    /**
     * 组合 0x59 功能的数据体
     *
     * @param func 功能
     * @param data 数据
     * @return
     */
    public static int[] combineManualConfigData(String func, String data) {
        if (func == null || "".equals(func)) {
            func = "00";
        }

        if (data == null || "".equals(data)) {
            data = "0000";
        }


        String result = func + data + "000000000000";

        String[] strs = new String[result.length() - 1];
        int[] ints = new int[result.length() / 2];
        int j = 0;

        for (int i = 0; i < result.length() - 1; i++) {
            if (i % 2 == 0) {
                strs[i] = result.substring(i, i + 2);
                ints[j] = Integer.valueOf(strs[i], 16) & 0xff;
                j++;
            }
        }
        return ints;
    }

    /**
     * 组合 0x5A 功能的数据体
     *
     * @param func 功能
     * @param data 数据
     * @return
     */
    public static int[] combineManualControlData(String func, String data) {
        if (func == null || "".equals(func)) {
            func = "00";
        }

        if (data == null || "".equals(data)) {
            data = "00";
        }


        String result = func + data + "00000000000000";
        Logger.d("result size:" + result.length());
        String[] strs = new String[result.length() - 1];
        int[] ints = new int[result.length() / 2];
        int j = 0;

        for (int i = 0; i < result.length() - 1; i++) {
            if (i % 2 == 0) {
                strs[i] = result.substring(i, i + 2);
                Logger.d("Ints j:" + j);
                ints[j] = Integer.valueOf(strs[i], 16) & 0xff;
                j++;
            }
        }
        Logger.d("Ints size:" + ints.length);
        return ints;
    }

}
