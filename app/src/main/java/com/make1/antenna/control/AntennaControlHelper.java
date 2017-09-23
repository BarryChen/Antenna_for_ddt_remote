package com.make1.antenna.control;

import com.orhanobut.logger.Logger;

/**
 * Created by Vange on 2017/9/20.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 * <p>
 * 天线控制帮助类
 */

public class AntennaControlHelper {

    private static AntennaControlHelper instance = null;
    private AntennaWriteReadThread mAntennaRead = null;


    private AntennaControlHelper() {
    }

    public static AntennaControlHelper getInstance() {
        if (instance == null) {
            return new AntennaControlHelper();
        }
        return instance;
    }

    /**
     * 开启天线操作线程
     */
    public void startAntennaThread() {
        if (mAntennaRead == null) {
            mAntennaRead = new AntennaWriteReadThread();
            Logger.i("线程已开启");
            mAntennaRead.start();
        }
    }

    /**
     * 停止线程
     */
    public void stopAntennaThread() {
        if (mAntennaRead != null && mAntennaRead.isAlive()) {
            Logger.i("线程已停止");
            mAntennaRead.interrupt();
            mAntennaRead = null;
        }
    }

    /**
     * 向节点中写值
     *
     * @param nodeValue Value
     */
    public void writeValueToNode(String nodeValue) {
        if (mAntennaRead != null) {
            mAntennaRead.writeNode(nodeValue);
        }
    }

    /**
     * 判断读写线程是否正在运行
     * @return
     */
    public boolean antennaThreadIsAlive() {
        return mAntennaRead != null && mAntennaRead.isAlive();
    }

    /**
     * 向通讯串口中写值
     *
     * @param value Value
     */
    public void writeValueToPort(String value) {
        if (mAntennaRead != null) {
            mAntennaRead.writeData(value);
        }
    }
}
