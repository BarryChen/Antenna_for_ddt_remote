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

public class ManualControlActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar mActionBar;

    private ListPreference mManualFunc;
    private EditTextPreference mManualData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.manual_control_xml);

        initActionBar();
        initPreference();
    }

    private void initPreference() {
        mManualFunc = (ListPreference) findPreference("manual_type");
        mManualData = (EditTextPreference) findPreference("manual_data");
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.setTitle("手动控制");
        mActionBar.setIcon(R.mipmap.ic_launcher_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setManualFuncSummary();
        setManualDataSummary();
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
                if (hintErrorByValue()) {
                    ToastUtil.showShort(this,"发送成功！");
                    Logger.i("手动控制步长...:" + AntennaCommand.sendMessageToAntenna(AntennaData.FUNCTION_CODE_MANUAL_CONTROL
                            , AntennaData.TO_ADDRESS
                            , AntennaDataCombineUtil
                                    .combineManualConfigData(DataFormatUtil.objectToOneHex(getManualFuncValue())
                                            , DataFormatUtil.objectToTwoHex(getManualDataValue()))));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hintErrorByValue() {
        if (getManualDataValue() instanceof Boolean) {
            ToastUtil.showShort(this, "数据输入有误！");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "manual_type":
                setManualFuncSummary();
                break;
            case "manual_data":
                setManualDataSummary();
                break;
        }
    }


    /**
     * 设置Manual Data的Summary
     */
    private void setManualDataSummary() {
        if (mManualData.getText() != null) {
            if (mManualData.getText().trim().isEmpty()) {
                mManualData.setSummary("请设置写入数据");
            } else {
                mManualData.setSummary(mManualData.getText());
            }
        }
    }

    /**
     * 获取Manual Data的值
     *
     * @return Int
     */
    private Object getManualDataValue() {
        String result = mManualData.getText();
        return checkValueFormat(this, result, 1000, -1000, 2);
    }

    /**
     * 设置Manual Func的Summary
     */
    private void setManualFuncSummary() {
        if (mManualFunc.getSummary() != null) {
            if (mManualFunc.getSummary().equals("")) {
                mManualFunc.setSummary("请设置功能码");
            } else {
                mManualFunc.setSummary(mManualFunc.getEntry());
            }
        } else {
            mManualFunc.setSummary(mManualFunc.getEntry());
        }
    }

    /**
     * 获取Manual Func的值
     *
     * @return Int
     */
    private int getManualFuncValue() {
        if (mManualFunc.getValue().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(mManualFunc.getValue());
        }
    }
}
