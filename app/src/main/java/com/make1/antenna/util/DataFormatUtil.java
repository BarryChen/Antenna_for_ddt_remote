package com.make1.antenna.util;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

/**
 * Created by Vange on 2017/9/21.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 * <p>
 * 数据转换工具类
 */

public class DataFormatUtil {


    /**
     * 转为一位十六进制数
     *
     * @param value
     * @return
     */
    public static String objectToOneHex(Object value) {
        String result;
        if (value instanceof Integer) {
            result = Integer.toHexString((Integer) value);
        } else if (value instanceof Double) {
            result = Integer.toHexString(Double.valueOf((Double) value * 10).intValue());
        } else {
            result = "";
        }

        Logger.d("Result-----------:" + result);

        if (result.length() < 2) {
            switch (result.length()) {
                case 1:
                    return "0" + result;
            }
        } else {
            return result.substring(result.length() - 2);
        }

        return "";
    }

    /**
     * 将精度为0.01或者整型转换为两位十六进制数
     *
     * @param value
     * @return
     */
    public static String objectToTwoHex(Object value) {

        String result;

        if (value instanceof Double) {
            result = Integer.toHexString(Double.valueOf((Double) value * 100).intValue());
        } else if (value instanceof Integer) {
            result = Integer.toHexString((Integer) value);
        } else {
            result = "";
        }

        if (result.length() < 4) {
            switch (result.length()) {
                case 1:
                    return "000" + result;
                case 2:
                    return "00" + result;
                case 3:
                    return "0" + result;
            }
        } else {
            return result.substring(4);
        }
        return "";
    }

    /**
     * 将精度为0.1或者整型转换为两位十六进制数
     *
     * @param value
     * @return
     */
    public static String testAngleToHex(Object value) {
        String result;

        if (value instanceof Double) {
            result = Integer.toHexString(Double.valueOf((Double) value * 10).intValue());
        } else if (value instanceof Integer) {
            result = Integer.toHexString((Integer) value);
        } else {
            result = "";
        }

        if (result.length() < 4) {
            switch (result.length()) {
                case 1:
                    return "000" + result;
                case 2:
                    return "00" + result;
                case 3:
                    return "0" + result;
            }
        } else {
            return result.substring(4);
        }
        return "";
    }

    /**
     * 检查数据格式是否正确
     *
     * @param result    元数据
     * @param max       最大范围
     * @param min       最小范围
     * @param precision 精度
     * @return Object -- Int or Double
     */
    public static Object checkValueFormat(Context context, String result, int max, int min, int precision) {
        Logger.d("max:" + max + ",min:" + min);
        try {
            if (result.contains(".")) {
                Double doubleResult = Double.valueOf(result);
                if (doubleResult > min && doubleResult < max) {
                    String[] split = result.split("\\.");
                    Logger.d("Split[0]:" + split[0]);
                    Logger.d("Split[1]:" + split[1]);
                    if (split[1].length() > precision) {
                        //精度不对
                        Logger.e("精度不对");
                        Toast.makeText(context, "数据精度不对", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        Logger.i("正确");
                        return doubleResult;
                    }
                } else {
                    //不符合范围
                    Logger.e("不符合范围");
                    Toast.makeText(context, "数据不符合范围", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                int intResult = Integer.valueOf(result);
                Logger.d("intResult:" + intResult);
                if (intResult > min && intResult < max) {
                    Logger.i("正确");
                    return intResult;
                } else {
                    //不符合范围
                    Logger.e("不符合范围");
                    Toast.makeText(context, "数据不符合范围", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            Logger.d("数据输入有误");
            Toast.makeText(context, "数据输入有误", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
