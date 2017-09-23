package com.make1.antenna.control;

import android.util.Log;

import com.make1.antenna.util.CommandUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android_serialport_api.SerialPort;

/**
 * 天线串口通信工具类
 */
public class AntennaWriteReadThread extends Thread {

    private String str;

    /**
     * 卫星通讯串口
     */
    private final static String BEI_DOU_DATA_NODE = "/dev/tty37";// TODO: 2017/9/23 需要根据具体的节点而修改

    /**
     * 节点位置
     */
    private final static String MODEL_CHANGE_NODE = "/sys/class/AS433SET/AS433SET/as433set/as433set_status";

    private FileOutputStream mFileOutputStream;
    private FileInputStream mFileInputStream;
    private SerialPort sp;

    public AntennaWriteReadThread() {
        try {
            // TODO: 2017/9/23 需要根据通讯参数来设置波特率
            sp = new SerialPort(new File(BEI_DOU_DATA_NODE), 115200, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mFileOutputStream = (FileOutputStream) sp.getOutputStream();
        mFileInputStream = (FileInputStream) sp.getInputStream();
    }

    @Override
    public void run() {
        super.run();
        byte[] buffer = new byte[1024];
        int[] a = new int[1024];
        int len = 0;
        int index = 0;
        String mString = "";
        String str1 = "";
        final String End = "0d0a";

        try {
            len = mFileInputStream.read(buffer);
            if (len != -1) {
//					 str = new String(buffer, 0, len); 
//					 //Logs.e("AntennaWriteReadThread run: str = ----" + str);
                     /*for (int i = 0; i < len; i++) {
                         Logs.e("AntennaWriteReadThread run: buffer = " + buffer[i]);
					 }*/

                for (int i = 0; i < len; i++) {
                    //Logs.e("AntennaWriteReadThread run: len = ----" + len);
                    //Logs.e("AntennaWriteReadThread run: buffer = ----" + buffer[i]);
                    a[i] = buffer[i];
                    if (buffer[i] < 0) {
                        str = CommandUtil.bytesToHexString(buffer[i]);
                        Log.e("lihang1", "bytesToHexString(-1);" + CommandUtil.bytesToHexString(buffer[i]));

                    } else {
                        str = Integer.toHexString(a[i]);
                        if (str.length() == 1) {
                            str = "0" + str;
                        }
                    }
                    //Logs.e("AntennaWriteReadThread run: a = " + a[i]);
                    mString = mString + str;
                    //Logs.e("AntennaWriteReadThread run: str = ----" + str);
                    //Logs.e("AntennaWriteReadThread run: mString = " + mString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 往节点写值
     *
     * @param writeStr 写入内容
     */
    public void writeNode(String writeStr) {
        Logger.e("writeNode ->value111:+" + writeStr);
        try {
            if (!isFileExists(MODEL_CHANGE_NODE)) {
                Logger.e("writeNode ->value222:+" + writeStr);
                return;
            }
            FileOutputStream fout = new FileOutputStream(MODEL_CHANGE_NODE);
            byte[] bytes = writeStr.getBytes();
            fout.write(bytes);
            fout.close();
            Logger.e("writeNode ->value333:+" + writeStr);
        } catch (FileNotFoundException e) {
            android.util.Log.i("lee", "FileNotFoundException-lee = " + e.toString());
        } catch (IOException e) {
            android.util.Log.i("lee", "IOException-lee = " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向串口写入数据
     *
     * @param writestr
     */
    public void writeData(String writestr) {
        try {
            int[] a = new int[200];
            int[] b = new int[200];
            int i = 0;
            int k = 0;

            for (i = 0; i < writestr.length(); i++) {
                String mString = writestr.substring(i, i + 1);
                if ("a".equals(mString)) {
                    a[i] = 10;
                } else if ("b".equals(mString)) {
                    a[i] = 11;
                } else if ("c".equals(mString)) {
                    a[i] = 12;
                } else if ("d".equals(mString)) {
                    a[i] = 13;
                } else if ("e".equals(mString)) {
                    a[i] = 14;
                } else if ("f".equals(mString)) {
                    a[i] = 15;
                } else {
                    a[i] = Integer.parseInt(mString);
                }
//                Logger.e("writeData: a[i] = " + a[i]);
            }

            for (k = 0; k < i / 2; k++) {
                b[k] = a[2 * k] * 16 + a[2 * k + 1];
//                Logger.e("writeData: k = " + k);
//                Logger.e("writeData: b[k] = " + b[k]);
                mFileOutputStream.write(b[k]);
                // mFileOutputStream.close();
            }

            Logger.e("Write success ->Data:+" + "value:" + writestr);
        } catch (Exception e) {
            Logger.e("IOException-lee = " + e.toString());
            e.printStackTrace();
        }
    }
}
