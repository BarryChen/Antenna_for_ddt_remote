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

public class TargetSatelliteActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar mActionBar;

    private ListPreference mType;
    private EditTextPreference mLongitude;
    private ListPreference mReceiverType;
    private ListPreference mPolarity;
    private EditTextPreference mFreq;
    private EditTextPreference mSignFreq;
    private EditTextPreference mThreshold;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.target_satellite_xml);

        initActionBar();
        initPreference();
    }

    private void initPreference() {
        mType = (ListPreference) findPreference("target_type");
        mLongitude = (EditTextPreference) findPreference("target_longitude");
        mReceiverType = (ListPreference) findPreference("target_receiver_type");
        mPolarity = (ListPreference) findPreference("target_polarity");
        mFreq = (EditTextPreference) findPreference("target_freq");
        mSignFreq = (EditTextPreference) findPreference("target_sign_freq");
        mThreshold = (EditTextPreference) findPreference("target_threshold");
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.setTitle("目标卫星配置");
        mActionBar.setIcon(R.mipmap.ic_launcher_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setTargetTypeSummary();
        setLongitudeSummary();
        setReceiverTypeSummary();
        setPolarityTypeSummary();
        setFreqSummary();
        setSignFreqSummary();
        setThresholdSummary();
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
                if (getLongitudeValue() instanceof Boolean || getFreqValue() instanceof Boolean
                        || getSignFreqValue() instanceof Boolean || getThresholdValue() instanceof Boolean) {
                    Logger.d("无法发送消息帧");
                } else {
                    Logger.i("目标卫星配置:" + AntennaCommand.sendMessageToAntenna(AntennaData.FUNCTION_CODE_TARGET_SATELLITE_CONTROL
                            , AntennaData.TO_ADDRESS, AntennaDataCombineUtil
                                    .combineSatelliteConfigData(DataFormatUtil
                                                    .objectToOneHex(getTargetTypeValue())
                                            , DataFormatUtil.objectToTwoHex(getLongitudeValue())
                                            , DataFormatUtil.objectToOneHex(getReceiverTypeValue())
                                            , DataFormatUtil.objectToOneHex(getPolarityTypeValue())
                                            , DataFormatUtil.freqToFourHex(getFreqValue())
                                            , DataFormatUtil.objectToOneHex(getSignFreqValue())
                                            , DataFormatUtil.objectToOneHex(getThresholdValue()))));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "target_type":
                setTargetTypeSummary();
                break;
            case "target_longitude":
                setLongitudeSummary();
                break;
            case "target_receiver_type":
                setReceiverTypeSummary();
                break;
            case "target_polarity":
                setPolarityTypeSummary();
                break;
            case "target_freq":
                setFreqSummary();
                break;
            case "target_sign_freq":
                setSignFreqSummary();
                break;
            case "target_threshold":
                setThresholdSummary();
                break;
        }
    }

    /**
     * 设置Target Type的Summary
     */
    private void setTargetTypeSummary() {
        if (mType.getSummary() != null) {
            if (mType.getSummary().equals("")) {
                mType.setSummary("请设置目标参数类型");
            } else {
                mType.setSummary(mType.getEntry());
            }
        }else{
            mType.setSummary(mType.getEntry());
        }
    }

    /**
     * 获取Target Type的值
     *
     * @return Int
     */
    private int getTargetTypeValue() {
        if (mType.getValue().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(mType.getValue());
        }
    }

    /**
     * 设置Longitude的Summary
     */
    private void setLongitudeSummary() {
        if (mLongitude.getText() != null) {
            if (mLongitude.getText().trim().isEmpty()) {
                mLongitude.setSummary("请设置经度信息");
            } else {
                mLongitude.setSummary(mLongitude.getText());
            }
        }
    }

    /**
     * 获取Longitude的值
     *
     * @return Int
     */
    private Object getLongitudeValue() {
        String result = mLongitude.getText();
        return checkValueFormat(this, result, 180, -180, 2);
    }

    /**
     * 设置Receiver Type的Summary
     */
    private void setReceiverTypeSummary() {
        if (mReceiverType.getSummary() != null) {
            if (mReceiverType.getSummary().equals("")) {
                mReceiverType.setSummary("请设置接收机类型");
            } else {
                mReceiverType.setSummary(mReceiverType.getEntry());
            }
        }else{
            mReceiverType.setSummary(mReceiverType.getEntry());
        }
    }

    /**
     * 获取Receiver Type的值
     *
     * @return Int
     */
    private int getReceiverTypeValue() {
        if (mReceiverType.getValue().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(mReceiverType.getValue());
        }
    }

    /**
     * 设置Polarity Type的Summary
     */
    private void setPolarityTypeSummary() {
        if (mPolarity.getSummary() != null) {
            if (mPolarity.getSummary().equals("")) {
                mPolarity.setSummary("请设置极化类型");
            } else {
                mPolarity.setSummary(mPolarity.getEntry());
            }
        }else{
            mPolarity.setSummary(mPolarity.getEntry());
        }
    }

    /**
     * 获取Polarity Type的值
     *
     * @return Int
     */
    private int getPolarityTypeValue() {
        if (mPolarity.getValue().equals("")) {
            return 0;
        } else {
            return Integer.valueOf(mPolarity.getValue());
        }
    }

    /**
     * 获取Freq的值
     *
     * @return Int
     */
    private Object getFreqValue() {
        String result = mFreq.getText();
        return checkValueFormat(this, result, 1000, -1000, 1);
    }

    /**
     * 设置Freq的Summary
     */
    private void setFreqSummary() {
        if (mFreq.getText() != null) {
            if (mFreq.getText().trim().isEmpty()) {
                mFreq.setSummary("请设置频率");
            } else {
                mFreq.setSummary(mFreq.getText());
            }
        }
    }

    /**
     * 获取SignFreq的值
     *
     * @return Int
     */
    private Object getSignFreqValue() {
        String result = mSignFreq.getText();
        return checkValueFormat(this, result, 1000, -1000, 2);
    }

    /**
     * 设置SignFreq的Summary
     */
    private void setSignFreqSummary() {
        if (mSignFreq.getText() != null) {
            if (mSignFreq.getText().trim().isEmpty()) {
                mSignFreq.setSummary("请设置符号率");
            } else {
                mSignFreq.setSummary(mSignFreq.getText());
            }
        }
    }

    /**
     * 获取SignFreq的值
     *
     * @return Int
     */
    private Object getThresholdValue() {
        String result = mThreshold.getText();
        return checkValueFormat(this, result, 1000, -1000, 1);
    }

    /**
     * 设置SignFreq的Summary
     */
    private void setThresholdSummary() {
        if (mThreshold.getText() != null) {
            if (mThreshold.getText().trim().isEmpty()) {
                mThreshold.setSummary("请设置门限");
            } else {
                mThreshold.setSummary(mThreshold.getText());
            }
        }
    }
}
