package com.spirited.support;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }
}
