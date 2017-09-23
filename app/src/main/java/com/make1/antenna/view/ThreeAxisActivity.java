package com.make1.antenna.view;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.orhanobut.logger.Logger;

/**
 * Created by Vange on 2017/9/22.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 */

public class ThreeAxisActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar mActionBar;

    private ListPreference mThreeAxisFunc;
    private ListPreference mThreeAxisType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.three_axis_xml);

        initActionBar();
        initPreference();
    }

    private void initPreference() {
        mThreeAxisFunc = (ListPreference) findPreference("three_axis_func");
        mThreeAxisType = (ListPreference) findPreference("three_axis_type");
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.setTitle("三轴控制");
        mActionBar.setIcon(R.mipmap.ic_launcher_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setThreeAxisFuncSummary();
        setThreeAxisTypeSummary();
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
                Logger.i("手动控制三轴命令...:" + AntennaCommand.sendMessageToAntenna(AntennaData.FUNCTION_CODE_AXIS_CONTROL
                        , AntennaData.TO_ADDRESS, AntennaDataCombineUtil
                                .combineManualControlData(DataFormatUtil.objectToOneHex(getThreeAxisFuncValue())
                                        , DataFormatUtil.objectToOneHex(getThreeAxisTypeValue()))));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "three_axis_func":
                setThreeAxisFuncSummary();
                break;
            case "three_axis_type":
                setThreeAxisTypeSummary();
                break;
        }
    }

    /**
     * 设置Three Axis Func的Summary
     */
    private void setThreeAxisFuncSummary() {
        if (mThreeAxisFunc.getSummary() != null) {
            if (mThreeAxisFunc.getSummary().equals("")) {
                mThreeAxisFunc.setSummary("请设置功能码");
            } else {
                mThreeAxisFunc.setSummary(mThreeAxisFunc.getEntry());
            }
        }else{
            mThreeAxisFunc.setSummary(mThreeAxisFunc.getEntry());
        }
    }

    /**
     * 获取Three Axis Func的值
     *
     * @return Int
     */
    private int getThreeAxisFuncValue() {
        if (mThreeAxisFunc.getValue().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(mThreeAxisFunc.getValue());
        }
    }

    /**
     * 设置Three Axis Type的Summary
     */
    private void setThreeAxisTypeSummary() {
        if (mThreeAxisType.getSummary() != null) {
            if (mThreeAxisType.getSummary().equals("")) {
                mThreeAxisType.setSummary("请设置操作类型");
            } else {
                mThreeAxisType.setSummary(mThreeAxisType.getEntry());
            }
        }else{
            mThreeAxisType.setSummary(mThreeAxisType.getEntry());
        }
    }

    /**
     * 获取Three Axis Type的值
     *
     * @return Int
     */
    private int getThreeAxisTypeValue() {
        if (mThreeAxisType.getValue().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(mThreeAxisType.getValue());
        }
    }
}
