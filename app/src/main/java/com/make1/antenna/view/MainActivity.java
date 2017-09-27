package com.make1.antenna.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.make1.antenna.R;
import com.make1.antenna.control.AntennaControlHelper;
import com.make1.antenna.data.AntennaData;
import com.make1.antenna.util.AntennaCommand;
import com.make1.antenna.util.AntennaPreferences;
import com.make1.antenna.util.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {


    @BindView(R.id.btn_switch)
    TextView btnSwitch;
    @BindView(R.id.main_item_layout)
    LinearLayout mMainItemLayout;
    @BindView(R.id.mImgSettings)
    ImageView mImgSettings;
    @BindView(R.id.main_text)
    TextView mainText;

    private AntennaControlHelper mAntennaHelper = AntennaControlHelper.getInstance();

    private boolean isRunning = false;
    private String mPortData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Logger.i("读写线程是否在运行：" + mAntennaHelper.antennaThreadIsAlive());
        mMainItemLayout.setVisibility(View.INVISIBLE);
        // TODO: 2017/9/23 这里要根据是否打开通讯节点来控制视图的开关

        mPortData = AntennaPreferences.getInstance().getPortData();
        if (mPortData != null) {
            mainText.setText("串口位置为：" + mPortData);
        } else {
            mainText.setText("天线控制程序V1.0");
        }
    }

    @OnClick({R.id.layout_main_info, R.id.layout_reset, R.id.layout_antenna_test
            , R.id.layout_version_control, R.id.layout_satellite_config
            , R.id.layout_step_speed_config, R.id.layout_three_axes
            , R.id.layout_tdb_1, R.id.layout_tdb_2, R.id.btn_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_main_info://主机端广播信息
//                mAntennaHelper.writeValueToPort("123123123");
                startActivity(new Intent(this, HostBroadcastInfoActivity.class));
                break;
            case R.id.layout_reset://复位
                Logger.i("复位：" + AntennaCommand.sendMessageToAntenna(
                        AntennaData.FUNCTION_CODE_SERVO_CONTROL,
                        AntennaData.TO_ADDRESS,
                        AntennaData.DATA_ANTENNA_RESET));
                break;
            case R.id.layout_antenna_test://天线测试
                startActivity(new Intent(this, AntennaTestActivity.class));
                break;
            case R.id.layout_version_control://版本信息查询与写入
                startActivity(new Intent(this, VersionActivity.class));
                break;
            case R.id.layout_satellite_config://目标卫星配置
                startActivity(new Intent(this, TargetSatelliteActivity.class));
                break;
            case R.id.layout_step_speed_config://手动控制步长、速度、目标位置配置与查询
                startActivity(new Intent(this, ManualControlActivity.class));
                break;
            case R.id.layout_three_axes://手动控制三轴命令
                startActivity(new Intent(this, ThreeAxisActivity.class));
                break;
            case R.id.layout_tdb_1://预留界面
                break;
            case R.id.layout_tdb_2://预留界面
                break;
            case R.id.btn_switch://打开和关闭天线读写线程
                if (mPortData != null) {
                    if (!isRunning) {
                        try {
                            mAntennaHelper.startAntennaThread();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.showShort(this, "串口无法被打开");
                            return;
                        }

                        btnSwitch.setText("关闭线程");
                        btnSwitch.setBackgroundColor(getResources().getColor(R.color.call_refuse_color));
                        mMainItemLayout.setVisibility(View.VISIBLE);
                        isRunning = true;
                    } else {
                        try {
                            mAntennaHelper.stopAntennaThread();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtil.showShort(this, "串口无法被关闭");
                            return;
                        }

                        btnSwitch.setText("开启线程");
                        btnSwitch.setBackgroundColor(getResources().getColor(R.color.call_answer_color));
                        mMainItemLayout.setVisibility(View.INVISIBLE);
                        isRunning = false;
                    }
                } else {
                    ToastUtil.showShort(this, "串口位置不正确，请设置串口位置再进行操作");
                }
                break;
        }
    }

    @OnClick(R.id.mImgSettings)
    public void onViewClicked() {
        startActivity(new Intent(this, PortSettingActivity.class));
    }
}
