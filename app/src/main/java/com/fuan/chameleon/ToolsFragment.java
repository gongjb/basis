package com.fuan.chameleon;

import com.chenenyu.router.annotation.Route;
import com.fuan.chameleon.databinding.FragmentToolsLayoutBinding;
import com.fuan.chameleon.router.AppRouter;
import com.yft.zbase.base.BaseFragment;
import com.yft.zbase.base.BaseViewModel;

/**
 * 与ChameleonHomeFragment一样只是工具入口， 具体能力包装到其它model
 */
@Route(AppRouter.FRAGMENT_CHAMELEON_TOOLS)
public class ToolsFragment extends BaseFragment<FragmentToolsLayoutBinding, BaseViewModel> {

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_tools_layout;
    }
}
