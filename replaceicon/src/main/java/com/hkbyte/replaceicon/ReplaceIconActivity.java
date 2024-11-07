package com.hkbyte.replaceicon;

import static com.hkbyte.cnbase.util.Constant.TO_CALCULATOR_STYLE_1;
import static com.hkbyte.cnbase.util.Constant.TO_CALCULATOR_STYLE_2;
import static com.hkbyte.cnbase.util.Constant.TO_MAIL_STYLE;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.chenenyu.router.annotation.Route;
import com.hkbyte.replaceicon.adapter.ViewPageAdapter;
import com.hkbyte.replaceicon.databinding.ActivityReplaceIconLayoutBinding;
import com.hkbyte.replaceicon.router.ReplaceRouter;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@Route(ReplaceRouter.REPLACE_ICON_ACTIVITY)
public class ReplaceIconActivity extends BaseActivity<ActivityReplaceIconLayoutBinding, ReplaceViewModel> {

    private int mPosition;

    @Override
    public void initView() {
        mDataBing.tlt.setTitle("更换图标");
        mDataBing.tlt.setLeftBackImage();
        mDataBing.tlt.setBackgroundColor(getResources().getColor(com.hkbyte.cnbase.R.color.chameleon_theme_color));
        //viewpage切换视图
        List<Fragment> list = new ArrayList<>();
        list.add(new StyleToMainFragment());
        list.add(new CalculatorFragment());
        list.add(new Calculator2Fragment());
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle(), list);
        mDataBing.vp2.setAdapter(adapter);
        mDataBing.vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                ReplaceIconActivity.this.mPosition = position;
            }
        });
    }

    @Override
    public void initListener() {
        mDataBing.btnClick.setOnClickListener(v -> {
            switch (ReplaceIconActivity.this.mPosition) {
                case TO_MAIL_STYLE:
                    // 恢复
                    startStyleMainAliasActivity();
                    break;
                case TO_CALCULATOR_STYLE_1:
                    // style 1
                    startStyle1AliasActivity();
                    break;
                case TO_CALCULATOR_STYLE_2:
                    // style 2
                    startStyle2AliasActivity();
                    break;
                default:{
                    //
                }
            }

            mViewModel.saveConfigStyle(ReplaceIconActivity.this.mPosition);
        });
    }

    private void startStyle1AliasActivity() {
        //启动样式1
        PackageManager pm = getPackageManager();
        ComponentName componentName = new ComponentName(this, "com.hkbyte.CalculatorActivityAlias");
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);


        ComponentName componentName2 = new ComponentName(this, "com.hkbyte.CalculatorActivity2Alias");
        pm.setComponentEnabledSetting(componentName2,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        ComponentName mainActivity = new ComponentName(this, "com.fuan.chameleon.WelcomeActivity");
        pm.setComponentEnabledSetting(mainActivity,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        // 重启
        ToastUtils.toast("设置完成");
    }


    private void startStyle2AliasActivity() {
        //启动样式1
        PackageManager pm = getPackageManager();
        ComponentName componentName = new ComponentName(this, "com.hkbyte.CalculatorActivity2Alias");
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);


        ComponentName componentName2 = new ComponentName(this, "com.hkbyte.CalculatorActivityAlias");
        pm.setComponentEnabledSetting(componentName2,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        ComponentName mainActivity = new ComponentName(this, "com.fuan.chameleon.WelcomeActivity");
        pm.setComponentEnabledSetting(mainActivity,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        // 重启
        ToastUtils.toast("设置完成");
    }


    private void startStyleMainAliasActivity() {
        //启动样式1
        PackageManager pm = getPackageManager();
        ComponentName componentName = new ComponentName(this, "com.fuan.chameleon.WelcomeActivity");
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);


        ComponentName componentName2 = new ComponentName(this, "com.hkbyte.CalculatorActivityAlias");
        pm.setComponentEnabledSetting(componentName2,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        ComponentName mainActivity = new ComponentName(this, "com.hkbyte.CalculatorActivity2Alias");
        pm.setComponentEnabledSetting(mainActivity,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);


        // 重启
        ToastUtils.toast("设置完成");
    }


    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_replace_icon_layout;
    }
}
