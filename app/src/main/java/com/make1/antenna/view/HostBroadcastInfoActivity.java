package com.make1.antenna.view;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
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

public class HostBroadcastInfoActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar mActionBar;

    private EditTextPreference mSnr;
    private SwitchPreference mPositionStatus;
    private EditTextPreference mLongitude;
    private EditTextPreference mLatitude;
    private ListPreference mReceiverType;
    private EditTextPreference mRsl;
    private EditTextPreference mFreq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.host_broadcast_info_xml);

        initActionBar();
        initPreference();
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.setTitle("主机端信息设置");
        mActionBar.setIcon(R.mipmap.ic_launcher_settings);
    }

    private void initPreference() {

        mSnr = (EditTextPreference) findPreference("host_snr");
        mLongitude = (EditTextPreference) findPreference("host_longitude");
        mLatitude = (EditTextPreference) findPreference("host_latitude");
        mRsl = (EditTextPreference) findPreference("host_rsl");
        mFreq = (EditTextPreference) findPreference("host_freq");

        mPositionStatus = (SwitchPreference) findPreference("host_position_status");

        mReceiverType = (ListPreference) findPreference("host_receiver_type");
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
                    Logger.i("主机端信息：" + AntennaCommand.sendMessageToAntenna(AntennaData.FUNCTION_CODE_HOST_BRODCAST
                            , AntennaData.TO_ADDRESS
                            , AntennaDataCombineUtil.combineBroadcastInfoData(DataFormatUtil
                                            .objectToTwoHex(getHostSnrValue())
                                    , DataFormatUtil.objectToOneHex(getPositionStatusValue())
                                    , DataFormatUtil.objectToTwoHex(getLongitudeValue())
                                    , DataFormatUtil.objectToTwoHex(getLatitudeValue())
                                    , DataFormatUtil.objectToOneHex(getReceiverTypeValue())
                                    , DataFormatUtil.freqToFourHex(getHostFreqValue())
                                    , DataFormatUtil.objectToTwoHex(getRslValue()))));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 提示用户具体哪一个参数值有问题
     */
    private boolean hintErrorByValue() {
        if (getHostSnrValue() instanceof Boolean) {
            ToastUtil.showShort(this, "信噪比设置有误!");
            return false;
        } else if (getLongitudeValue() instanceof Boolean) {
            ToastUtil.showShort(this, "经度信息设置有误!");
            return false;
        } else if (getLatitudeValue() instanceof Boolean) {
            ToastUtil.showShort(this, "纬度信息设置有误!");
            return false;
        } else if (getRslValue() instanceof Boolean) {
            ToastUtil.showShort(this, "接收信号强度信息设置有误!");
            return false;
        } else if (getHostFreqValue() instanceof Boolean) {
            ToastUtil.showShort(this, "信号频率设置有误!");
            return false;
        } else {
            Logger.d("参数没有问题");
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setHostSnrSummary();
        setLongitudeSummary();
        setLatitudeSummary();
        setRslSummary();
        setPositionStatusSummary();
        setReceiverTypeSummary();
        setHostFreqSummary();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "host_snr":
                setHostSnrSummary();
                break;
            case "host_longitude":
                setLongitudeSummary();
                break;
            case "host_latitude":
                setLatitudeSummary();
                break;
            case "host_rsl":
                setRslSummary();
                break;
            case "host_position_status":
                setPositionStatusSummary();
                break;
            case "host_receiver_type":
                setReceiverTypeSummary();
                break;
            case "host_freq":
                setHostFreqSummary();
                break;

        }
    }

    /**
     * 设置Host Snr的Summary
     */
    private void setHostSnrSummary() {
        if (mSnr.getText() != null) {
            if (mSnr.getText().trim().isEmpty()) {
                mSnr.setSummary("请设置信噪比");
            } else {
                mSnr.setSummary(mSnr.getText());
            }
        }
    }

    /**
     * 获取Host Snr的值
     *
     * @return Int
     */
    private Object getHostSnrValue() {
        String result = mSnr.getText();
        return checkValueFormat(this, result, 100, -10, 2);
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
     * 设置Latitude的Summary
     */
    private void setLatitudeSummary() {
        if (mLatitude.getText() != null) {
            if (mLatitude.getText().trim().isEmpty()) {
                mLatitude.setSummary("请设置纬度信息");
            } else {
                mLatitude.setSummary(mLatitude.getText());
            }
        }
    }

    /**
     * 获取Latitude的值
     *
     * @return Int
     */
    private Object getLatitudeValue() {
        String result = mLatitude.getText();
        return checkValueFormat(this, result, 90, -90, 2);
    }

    /**
     * 设置Rsl的Summary
     */
    private void setRslSummary() {
        if (mRsl.getText() != null) {
            if (mRsl.getText().trim().isEmpty()) {
                mRsl.setSummary("请设置接受信号强度");
            } else {
                mRsl.setSummary(mRsl.getText());
            }
        }
    }

    /**
     * 获取Rsl的值
     *
     * @return Int
     */
    private Object getRslValue() {
        String result = mRsl.getText();
        return checkValueFormat(this, result, -50, -150, 2);
    }

    /**
     * 设置Position Status的Summary
     */
    private void setPositionStatusSummary() {
        mPositionStatus.setChecked(mPositionStatus.isChecked());
    }

    /**
     * 获取 Position Status 的值
     *
     * @return
     */
    private int getPositionStatusValue() {
        if (mPositionStatus.isChecked()) {
            return 0;
        } else {
            return -1;
        }
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
        } else {
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
     * 设置Freq Snr的Summary
     */
    private void setHostFreqSummary() {
        if (mFreq.getText() != null) {
            if (mFreq.getText().trim().isEmpty()) {
                mFreq.setSummary("请设置信号频率");
            } else {
                mFreq.setSummary(mFreq.getText());
            }
        }
    }

    /**
     * 获取Freq Snr的值
     *
     * @return Int
     */
    private Object getHostFreqValue() {
        String result = mFreq.getText();
        return checkValueFormat(this, result, 1000, -1000, 1);
    }
}
