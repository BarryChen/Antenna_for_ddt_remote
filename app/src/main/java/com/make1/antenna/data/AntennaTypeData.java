package com.make1.antenna.data;

/**
 * Created by Vange on 2017/9/21.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 * <p>
 * 天线数据标志
 */

public class AntennaTypeData {

    /**
     * 0x11	主机端广播信息
     */
    /*
    发送端
     */
    public static final String ANTENNA_TYPE_SNR = "antenna_type_snr";
    public static final String ANTENNA_TYPE_LONGITUDE = "antenna_type_longitude";
    public static final String ANTENNA_TYPE_LATITUDE = "antenna_type_latitude";
    public static final String ANTENNA_TYPE_RSL = "antenna_type_rsl";

    /*
    接受端
     */
    public static final String ANTENNA_TYPE_ANGLE_ORIENTATION = "antenna_type_angle_orientation";
    public static final String ANTENNA_TYPE_ANGLE_PITCH = "antenna_type_angle_pitch";
}
