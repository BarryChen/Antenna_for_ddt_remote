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
import com.orhanobut.logger.Logger;

import static com.make1.antenna.util.DataFormatUtil.checkValueFormat;

/**
 * Created by Vange on 2017/9/22.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 */

public class VersionActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar mActionBar;

    private EditTextPreference mVersionData;
    private ListPreference mVersionType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.version_xml);

        initActionBar();
        initPreference();
    }

    private void initPreference() {
        mVersionData = (EditTextPreference) findPreference("version_data");
        mVersionType = (ListPreference) findPreference("version_func_type");
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.setTitle("版本信息");
        mActionBar.setIcon(R.mipmap.ic_launcher_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setVersionDataSummary();
        setVersionTypeSummary();
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
                Logger.d("Senddddddddddddddd");

                //只有功能为写入时，才需要判断写入的数据是否正确
                if (mVersionType.getValue().equals("49")) {
                    if (getVersionData() instanceof Boolean) {
                        Logger.d("无法发送消息帧");
                    } else {
                        Logger.i("版本信息:" + AntennaCommand.sendMessageToAntenna(AntennaData.FUNCTION_CODE_VERSION_CONTROL
                                , AntennaData.TO_ADDRESS, AntennaDataCombineUtil
                                        .combineVersionInfoData(DataFormatUtil
                                                        .objectToOneHex(getVersionTypeValue())
                                                , DataFormatUtil.objectToTwoHex(getVersionData()))));
                    }
                } else {
                    Logger.i("版本信息:" + AntennaCommand.sendMessageToAntenna(AntennaData.FUNCTION_CODE_VERSION_CONTROL
                            , AntennaData.TO_ADDRESS, AntennaDataCombineUtil
                                    .combineVersionInfoData(DataFormatUtil
                                            .objectToOneHex(getVersionTypeValue()), "")));
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "version_data":
                Logger.d("onSharedPreferenceChanged --- version_data");
                setVersionDataSummary();
                break;
            case "version_func_type":
                Logger.d("onSharedPreferenceChanged --- version_func_type");
                setVersionTypeSummary();
                break;
        }
    }

    /**
     * 设置Version Type的Summary
     */
    private void setVersionTypeSummary() {
        if (mVersionType.getSummary() != null) {
            if (mVersionType.getSummary().equals("")) {
                mVersionType.setSummary("请设置功能类型");
            } else {
                mVersionType.setSummary(mVersionType.getEntry());
            }
        } else {
            mVersionType.setSummary(mVersionType.getEntry());
        }
    }

    /**
     * 获取Version Type的值
     *
     * @return Int
     */
    private int getVersionTypeValue() {
        if (mVersionType.getValue().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(mVersionType.getValue());
        }
    }


    /**
     * 设置Version Value的Summary
     */
    private void setVersionDataSummary() {
        if (mVersionData.getText() != null) {
            if (mVersionData.getText().trim().isEmpty()) {
                mVersionData.setSummary("版本号：有效值0~4095");
            } else {
                mVersionData.setSummary(mVersionData.getText());
            }
        }
    }

    /**
     * 获取Version Value的值
     *
     * @return Int
     */
    private Object getVersionData() {
        String result = mVersionData.getText();
        return checkValueFormat(this, result, 4095, 0, 2);
    }

}
