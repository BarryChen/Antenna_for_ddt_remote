package com.make1.antenna.view;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.make1.antenna.R;
import com.make1.antenna.util.AntennaPreferences;
import com.make1.antenna.util.ToastUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by Vange on 2017/9/22.
 * <p>
 * Email:Vangelis.wang@make1.cn
 * Company:Make1
 */

public class PortSettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActionBar mActionBar;

    private EditTextPreference mPortSettingData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.port_setting_xml);

        initActionBar();
        initPreference();
    }

    private void initPreference() {
        mPortSettingData = (EditTextPreference) findPreference("port_data");
    }

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.setTitle("通讯串口设置");
        mActionBar.setIcon(R.mipmap.ic_launcher_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        setPortDataSummary();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.port_setting_mian, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                //只有功能为写入时，才需要判断写入的数据是否正确
                String result = getPortData();
                if("".equals(result)){
                    ToastUtil.showShort(this,"串口值为无效字符!");
                }else{
                    AntennaPreferences.getInstance().savePortData(result);
                    this.finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "port_data":
                Logger.d("onSharedPreferenceChanged --- port_data");
                setPortDataSummary();
                break;
        }
    }

    /**
     * 设置Version Value的Summary
     */
    private void setPortDataSummary() {
        if (mPortSettingData.getText() != null) {
            if (mPortSettingData.getText().trim().isEmpty()) {
                mPortSettingData.setSummary("设置通讯的串口");
            } else {
                mPortSettingData.setSummary(mPortSettingData.getText());
            }
        }
    }

    /**
     * 获取Version Value的值
     *
     * @return Int
     */
    private String getPortData() {
        String result = mPortSettingData.getText();
        if(TextUtils.isEmpty(result)){
            return "";
        }else{
            return result;
        }

    }

}
