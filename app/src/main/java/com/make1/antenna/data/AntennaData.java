package com.make1.antenna.data;

/**
 * Created by Vangelis on 2016/5/24.
 * <p/>
 * 天线协议数据
 */
public class AntennaData {

    /**
     * 天线消息帧组成部分
     * ↓↓↓↓↓↓↓↓↓↓↓
     */

    public static final int CMD_START = 0x3A;
    public static final int CR = 0x0D;
    public static final int LF = 0x0A;
    public static final int TO_ADDRESS = 0x10;
    public static final int ADDRESS_COM = 0x20;


    /**
     * 天线功能代码
     * ↓↓↓↓↓↓↓↓↓↓↓
     */

    //0x11	 主机端广播信息
    public static final int FUNCTION_CODE_HOST_BRODCAST = 0x11;
    //0x12	 复位、使能、初始化(针对伺服)
    public static final int FUNCTION_CODE_SERVO_CONTROL = 0x12;
    //0x13	 天线测试(测试专用，针对伺服)
    public static final int FUNCTION_CODE_SERVO_TEST = 0x13;
    //0x15	 天线程序升级
    public static final int FUNCTION_CODE_ANTENNA_UPDATE = 0x15;
    //0x16	 配置文件读取、更新
    public static final int FUNCTION_CODE_SET_CONFIG = 0x16;
    //0x39	 功放控制与状态查询 amplifier
    public static final int FUNCTION_CODE_AMPLIFIER_CONTROL = 0x39;
    //0x3A	 LNA控制与状态查询
    public static final int FUNCTION_CODE_LNA_CONTROL = 0x3A;
    //0x43	 版本信息查询与写入
    public static final int FUNCTION_CODE_VERSION_CONTROL = 0x43;
    //0x50	 目标卫星配置
    public static final int FUNCTION_CODE_TARGET_SATELLITE_CONTROL = 0x50;
    //0x59	 手动控制步长、速度、目标位置配置与查询
    public static final int FUNCTION_CODE_MANUAL_CONTROL = 0x59;
    //0x5A	 手动控制三轴命令
    public static final int FUNCTION_CODE_AXIS_CONTROL = 0x5A;
    //0x55   天线开关
    public static final int FUNCTION_CODE_ANTENNA_SWITCH = 0x55;


    /**
     * 复位 Data
     */
    public static final int[] DATA_ANTENNA_RESET = {0x01};

    /**
     * 天线测试子功能码 方位顺时针 0x11
     */
    public static final int DATA_FUNCCODE_ROTATION_CW_TEST = 17;

    /**
     * 天线测试子功能码 方位逆时针 0x21
     */
    public static final int DATA_FUNCODE_ROTATION_ACW_TEST = 33;

    /**
     * 天线测试子功能码 俯仰顺时针 0x12
     */
    public static final int DATA_FUNCODE_ANGLE_CW_TEST = 18;

    /**
     * 天线测试子功能码 俯仰逆时针 0x22
     */
    public static final int DATA_FUNCODE_ANGLE_ACW_TEST = 34;


    /**
     * 天线状态 初始化 0x10
     */
    public static final int DATA_ANTENNA_STATUS_INIT = 0x10;

    /**
     * 天线状态 准备搜索 0x20
     */
    public static final int DATA_ANTENNA_STATUS_PREPARE_SEARCH = 0x20;

    /**
     * 天线状态 搜索 0x30
     */
    public static final int DATA_ANTENNA_STATUS_SEARCH = 0x30;

    /**
     * 天线状态 跟踪 0x40
     */
    public static final int DATA_ANTENNA_STATUS_TAIL = 0x40;

    /**
     * 天线状态 保护 0x50
     */
    public static final int DATA_ANTENNA_STATUS_PROTECT = 0x50;

    /**
     * 天线状态 手动 0x60
     */
    public static final int DATA_ANTENNA_STATUS_HM = 0x60;


    /**
     * 版本信息查询与写入 主程序版本 0x01
     */
    public static final int DATA_ANTENNA_MAIN_VERSION = 01;
    /**
     * 版本信息查询与写入 配置版本 0x02
     */
    public static final int DATA_ANTENNA_CONFIG_VERSION = 02;
    /**
     * 版本信息查询与写入 硬件版本 0x10
     */
    public static final int DATA_ANTENNA_HARDWARE_VERSION = 16;
    /**
     * 版本信息查询与写入 机械结构版本 0x20
     */
    public static final int DATA_ANTENNA_MACHINE_VERSION = 32;
    /**
     * 版本信息查询与写入 产品编号查询 0x30
     */
    public static final int DATA_ANTENNA_PRODUCT_VERSION = 48;
    /**
     * 版本信息查询与写入 产品编号写入 0x31
     */
    public static final int DATA_ANTENNA_WRITE_PRODUCT_VERSION = 49;

    /**
     * 目标卫星 查询目标星参数 0x01
     */
    public static final int DATA_ANTENNA_SATELLITE_CONFIG_SELECT = 1;
    /**
     * 目标卫星 设定目标星参数 0x11
     */
    public static final int DATA_ANTENNA_SATELLITE_CONFIG_SET = 17;


    /**
     * 手动控制步长、速度、目标位置配置与查询 方位步长设置 0x11
     */
    public static final int DATA_ANTENNA_DIRECTION_STEP = 17;

    /**
     * 手动控制步长、速度、目标位置配置与查询 方位速度设置 0x21
     */
    public static final int DATA_ANTENNA_DIRECTION_SPEED = 33;

    /**
     * 手动控制步长、速度、目标位置配置与查询 方位步长查询 0x41
     */
    public static final int DATA_ANTENNA_DIRECTION_STEP_SELECT = 65;

    /**
     * 手动控制步长、速度、目标位置配置与查询 方位速度查询 0x51
     */
    public static final int DATA_ANTENNA_DIRECTION_SPEED_SELECT = 81;

    /**
     * 手动控制步长、速度、目标位置配置与查询 俯仰步长设置 0x12
     */
    public static final int DATA_ANTENNA_PITCH_STEP = 18;

    /**
     * 手动控制步长、速度、目标位置配置与查询 俯仰速度设置 0x22
     */
    public static final int DATA_ANTENNA_PITCH_SPEED = 34;

    /**
     * 手动控制步长、速度、目标位置配置与查询 俯仰步长查询 0x42
     */
    public static final int DATA_ANTENNA_PITCH_STEP_SELECT = 66;

    /**
     * 手动控制步长、速度、目标位置配置与查询 俯仰速度查询 0x52
     */
    public static final int DATA_ANTENNA_PITCH_SPEED_SELECT = 82;

    /**
     * 手动控制步长、速度、目标位置配置与查询 极化步长设置 0x13
     */
    public static final int DATA_ANTENNA_POLARITY_STEP = 19;

    /**
     * 手动控制步长、速度、目标位置配置与查询 极化速度设置 0x23
     */
    public static final int DATA_ANTENNA_POLARITY_SPEED = 35;

    /**
     * 手动控制步长、速度、目标位置配置与查询 极化步长查询 0x43
     */
    public static final int DATA_ANTENNA_POLARITY_STEP_SELECT = 67;

    /**
     * 手动控制步长、速度、目标位置配置与查询 极化速度查询 0x53
     */
    public static final int DATA_ANTENNA_POLARITY_SPEED_SELECT = 83;


    /**
     * 手动控制三轴命令 子功能码 进入手动
     */
    public static final int DATA_ANTENNA_INTO_MANUAL = 0;

    /**
     * 手动控制三轴命令 子功能码 退出手动
     */
    public static final int DATA_ANTENNA_EXIT_MANUAL = 1;

    /**
     * 手动控制三轴命令 子功能码 手动调整方位
     */
    public static final int DATA_ANTENNA_MANUAL_DIRECTION = 2;

    /**
     * 手动控制三轴命令 子功能码 手动调整俯仰
     */
    public static final int DATA_ANTENNA_MANUAL_PITCH = 3;

    /**
     * 手动控制三轴命令 数据 增加（开始）
     */
    public static final int DATA_ANTENNA_MANUAL_ADD = 1;

    /**
     * 手动控制三轴命令 数据 减少一步
     */
    public static final int DATA_ANTENNA_MANUAL_REDUCE = -1;


}
