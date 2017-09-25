package com.make1.antenna.view;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.make1.antenna.R;
import com.make1.antenna.data.AntennaData;
import com.make1.antenna.util.AntennaCommand;
import com.make1.antenna.util.AntennaDataCombineUtil;
import com.make1.antenna.util.DataFormatUtil;
import com.make1.antenna.util.ToastUtil;
import com.orhanobut.logger.Logger;

import static com.make1.antenna.util.DataFormatUtil.checkValueFormat;

/**
 * Created by Vange on 2017/9/22.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 */

public class AntennaTestActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar mActionBar;

    private ListPreference mRotationType;
    private EditTextPreference mAngleValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.antenna_test_xml);

        mActionBar = getActionBar();
        mActionBar.setTitle("天线测试");
        mActionBar.setIcon(R.mipmap.ic_launcher_settings);

        initPreference();
    }

    private void initPreference() {
        mRotationType = (ListPreference) findPreference("rotation_type");
        mAngleValue = (EditTextPreference) findPreference("angle_value");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setRotationTypeSummary();
        setAngleValueSummary();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mian, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                if (getAngleValue() instanceof Boolean) {
                    Logger.e("无法发送消息帧");
                    ToastUtil.showShort(this, "转动角度范围输入数据有误！");
                } else {
                    ToastUtil.showShort(this,"发送成功！");
                    Logger.i("天线测试:" + AntennaCommand.sendMessageToAntenna(
                            AntennaData.FUNCTION_CODE_SERVO_TEST
                            , AntennaData.TO_ADDRESS
                            , AntennaDataCombineUtil.combineAntennaTestInfoData(DataFormatUtil.objectToOneHex(getRotationTypeValue())
                                    , DataFormatUtil.testAngleToHex(getAngleValue()))));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "rotation_type":
                setRotationTypeSummary();
                break;
            case "angle_value":
                setAngleValueSummary();
                break;
        }
    }

    /**
     * 设置Rotation Type的Summary
     */
    private void setRotationTypeSummary() {
        if (mRotationType.getSummary() != null) {
            if (mRotationType.getSummary().equals("")) {
                mRotationType.setSummary("请设置转动类型");
            } else {
                mRotationType.setSummary(mRotationType.getEntry());
            }
        } else {
            mRotationType.setSummary(mRotationType.getEntry());
        }
    }

    /**
     * 设置Angle Value的Summary
     */
    private void setAngleValueSummary() {
        if (mAngleValue.getText() != null) {
            if (mAngleValue.getText().trim().isEmpty()) {
                mAngleValue.setSummary("负角度表示逆时针，正角度表示顺时针(范围:-2880°~+2880°，精度为0.1)");
            } else {
                mAngleValue.setSummary(mAngleValue.getText() + "(范围:-2880°~+2880°，精度为0.1)");
            }
        }
    }

    /**
     * 获取Rotation Type的值
     *
     * @return Int
     */
    private int getRotationTypeValue() {
        if (mRotationType.getValue().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(mRotationType.getValue());
        }
    }

    /**
     * 获取Angle Value的值
     *
     * @return Int
     */
    private Object getAngleValue() {
        String result = mAngleValue.getText();
        return checkValueFormat(this, result, 2880, -2880, 1);
    }
}
