package com.make1.antenna.app;

import android.app.Application;

import com.make1.antenna.util.AntennaPreferences;
import com.make1.antenna.util.ToastUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by Vange on 2017/9/20.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Log工具的初始化
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(1)         // (Optional) How many method line to show. Default 2
                .methodOffset(0)// (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("Antenna")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        //保存在文件中
        FormatStrategy diskFormatStrategy = CsvFormatStrategy.newBuilder()
                .tag("MakeOneC")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(diskFormatStrategy));

        ToastUtil.controlShow(true);

        AntennaPreferences.getInstance().init(this);
    }
}
