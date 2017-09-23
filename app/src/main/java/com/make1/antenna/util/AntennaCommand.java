package com.make1.antenna.util;


import com.make1.antenna.data.AntennaData;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 * <p/>
 *
 * 天线数据组合工具类
 */
public class AntennaCommand {


    /**
     * 若数据长度为1，则格式化为01
     *
     * @param i 源数据
     * @return 格式化的数据
     */
    private static String addZero(int i) {
        String str = Integer.toHexString(i);
        if (str.length() == 1) {
            return "0" + str;
        }
        return str;
    }

    /**
     * 计算LRC
     *
     * @param item 数据项
     * @return 十六进制的和
     */
    private static String getLRC(List<Integer> item) {
        int sum = 0;
        for (int i = 0; i < item.size(); i++) {
            sum = item.get(i) + sum;
        }
        String result = Integer.toHexString(sum);
//        Logger.e("Format LRC Before:" + result);
        if (result.length() > 2) {
            Logger.e("Format LRC After:" + result.substring(result.length() - 2, result.length()));
            return result.substring(result.length() - 2, result.length());
        } else {
            return Integer.toHexString(sum);
        }
    }

    /**
     * 转义发送消息帧
     *
     * @param pbuf 源消息消息帧
     * @param nLen 长度
     * @return 转义后的发送消息帧
     */
    private static String sendTransMean(String[] pbuf, int nLen) {
        String temp = "";
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < (nLen - 2); i++) {
            if (pbuf[i].equals("0d")) {//0d ---> 0e and 2d
                list.add("0e");
                list.add("2d");
            } else if ((pbuf[i].equals("0e"))) {//0e ---> 0e and 2e
                list.add("0e");
                list.add("2e");
            } else {
                list.add(pbuf[i]);
            }
        }
        list.add("0d");
        list.add("0a");

        //组合成一个字符串
        for (int k = 0; k < list.size(); k++) {
            temp += list.get(k);
        }
        Logger.d("最终的要发送的字符串为："+temp);
        return temp;
    }

    /**
     * 转义接收消息帧 (需要校验LRC是否正确)
     *
     * @param pbuf 源消息消息帧
     * @param nLen 长度
     * @return 转义后的发送消息帧
     */
    private static String recTransMean(String[] pbuf, int nLen) {
        String temp = "";
        List<String> list = new ArrayList<String>();
        int lrcSum = 0;//LRC值

        for (int i = 0; i < (nLen - 2); i++) {
            if (pbuf[i].equals("0e")) {//0e and 2d ---> 0d
                if (pbuf[i + 1].equals("2d")) {
                    list.add("0d");
                    i++;//skip "2d"
                } else if (pbuf[i + 1].equals("2e")) { //0e and 2e ---> 0e;
                    list.add("0e");
                    i++;//skip "2e"
                }
            } else {
                list.add(pbuf[i]);
            }
        }
        list.add("0d");
        list.add("0a");

        for (int k = 0; k < list.size(); k++) {
            //Logger.e("Message Item[" + k + "] ===== " + list.get(k));
            if (k > 0 && k < list.size() - 3) {
                lrcSum += Integer.valueOf(list.get(k), 16);
            }
            temp += list.get(k);
        }

        Logger.e("Filter :" + temp);

        String lrc = Integer.toHexString(lrcSum);
        Logger.e("count lrc === " + lrc);
        if (lrc.length() > 2) {
            lrc = lrc.substring(lrc.length() - 2, lrc.length());
            Logger.e("lrc.length()>2 --> count lrc === " + lrc);
        }


        //如果计算的LRC和原数据帧相同则返回消息帧
        Logger.e("Lrc in Message === " + list.get(list.size() - 3));
        if (lrc.equals(list.get(list.size() - 3))) {
            Logger.e("LRC Success!");
            return temp;
        } else {
            Logger.e("LRC Fail!");
            return "error";
        }
    }


    /**
     * 发送消息帧
     *
     * @param funCode 功能代码
     * @param data    数据源
     */
    public static String sendMessageToAntenna(int funCode, int address, int[] data) {
        int dataLen = data.length;
        int cr = AntennaData.CR;
        int lf = AntennaData.LF;
        //计算LRC
        List<Integer> lrcData = new ArrayList<Integer>();
        lrcData.add(address);
        lrcData.add(funCode);
        lrcData.add(dataLen);
        for (int aData : data) {
            lrcData.add(aData);
        }
        String lrc = getLRC(lrcData);

        String[] message = new String[7 + dataLen];
        message[0] = addZero(AntennaData.CMD_START);
        message[1] = addZero(address);//address
        message[2] = addZero(funCode);//funCode
        message[3] = addZero(dataLen);//dataLen

        for (int i = 0; i < dataLen; i++) {
//            Logger.d("收取的data"+i+":"+data[i]);
            message[i+4] = addZero(data[i]);
        }

        message[dataLen + 4] = lrc;
        message[dataLen + 5] = addZero(cr);
        message[dataLen + 6] = addZero(lf);

        for (int i = 0; i < message.length; i++) {
//            Logger.e("Send Message " + i + "======" + message[i]);
        }

        return sendTransMean(message, message.length);
    }

    /**
     * 解析接收的消息帧
     *
     * @param response 默认为字符串类型
     *                 <p/>
     *                 如果返回-1,返回帧里的-1是怎么表示的   ff表示-1
     */
    public static void analyMessageForAntenna(String response) {
        int stringleth = response.length();
        int dataleth = stringleth / 2;
        String[] str = new String[dataleth];
        for (int n = 0; n < dataleth; n++) {
            str[n] = response.substring(2 * n, (n + 1) * 2);
        }
        String after = recTransMean(str, str.length);
        Logger.e("Received Message after:" + after);
        //解析具体的Funcode和数据
        if ("error".equals(after)) {
            //错误
            Logger.e("Error");
        } else {
            //去除冒号和结束符
            String subString = after.substring(2, after.length() - 6);
            int address = Integer.parseInt(subString.substring(0, 2), 16);
            int funCode = Integer.parseInt(subString.substring(2, 4), 16);
            int dataLen = Integer.parseInt(subString.substring(4, 6), 16);

            //根据功能代码来解析相关的数据
            switch (funCode) {
                //0x11	 主机端广播信息
                case AntennaData.FUNCTION_CODE_HOST_BRODCAST:
                    Logger.e("Host Broadcast Message");
                    //存储由便携发送的数据
                    break;

                //0x12	 复位、使能、初始化(针对伺服)
                case AntennaData.FUNCTION_CODE_SERVO_CONTROL:
                    Logger.e("FUNCTION_CODE_HOST_BROADCAST");

                    //0x13	 天线测试(测试专用，针对伺服)
                case AntennaData.FUNCTION_CODE_SERVO_TEST:
                    Logger.e("FUNCTION_CODE_SERVO_TEST");

                    break;
                //0x39	 功放控制与状态查询 amplifier
                case AntennaData.FUNCTION_CODE_AMPLIFIER_CONTROL:
                    Logger.e("FUNCTION_CODE_AMPLIFIER_CONTROL");
                    break;

                //0x3A	 LNA控制与状态查询
                case AntennaData.FUNCTION_CODE_LNA_CONTROL:
                    Logger.e("FUNCTION_CODE_LNA_CONTROL");

                    break;
                //0x50	 目标卫星配置
                case AntennaData.FUNCTION_CODE_TARGET_SATELLITE_CONTROL:
                    Logger.e("FUNCTION_CODE_TARGET_SATELLITE_CONTROL");

                    break;
                //0x59	 手动控制步长、速度、目标位置配置与查询
                case AntennaData.FUNCTION_CODE_MANUAL_CONTROL:
                    Logger.e("FUNCTION_CODE_MANUAL_CONTROL");

                    break;
                //0x5A	 手动控制三轴命令
                case AntennaData.FUNCTION_CODE_AXIS_CONTROL:
                    Logger.e("FUNCTION_CODE_AXIS_CONTROL");

                    break;
            }
        }
    }
}
