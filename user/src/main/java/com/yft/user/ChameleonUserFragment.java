package com.yft.user;


import com.chenenyu.router.annotation.Route;
import com.yft.user.databinding.FragmentUserLayoutBinding;
import com.yft.user.model.UserViewModel;
import com.yft.user.router.UserRouter;
import com.yft.zbase.base.BaseFragment;

@Route(UserRouter.FRAGMENT_CHAMELEON_USER)
public class ChameleonUserFragment extends BaseFragment<FragmentUserLayoutBinding, UserViewModel> {
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
        return R.layout.fragment_user_layout;
    }
}
