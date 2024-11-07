package com.yft.user;

import static com.yft.user.router.UserRouter.ACTIVITY_SET;
import static com.yft.zbase.router.ZbaseRouter.ACTIVITY_WEB;
import static com.yft.zbase.utils.Logger.LOGE;
import static com.yft.zbase.utils.Utils.getUserLevel;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chenenyu.router.annotation.Route;
import com.gongjiebin.latticeview.BaseLatticeView;
import com.gongjiebin.latticeview.LatticeView;
import com.yft.user.databinding.FragmentUserLayoutBinding;
import com.yft.user.model.UserViewModel;
import com.yft.user.router.UserRouter;
import com.yft.zbase.server.UserLevelType;
import com.yft.zbase.base.BaseFragment;
import com.yft.zbase.bean.ServiceBean;
import com.yft.zbase.bean.UserInfo;
import com.yft.zbase.router.RouterFactory;
import com.yft.zbase.utils.ImageGlideLoader;
import com.yft.zbase.utils.ToastUtils;

@Route(UserRouter.FRAGMENT_USER)
public class UserFragment extends BaseFragment<FragmentUserLayoutBinding, UserViewModel> {

    @Override
    public void initView() {
        // 我的团队
        setStatusBar(getContext());
        GradientDrawable grad = new GradientDrawable(//渐变色
                GradientDrawable.Orientation.BOTTOM_TOP,
                mColors);
        mDataBing.clMain.setBackground(grad);

        LatticeView.ImageTextParams imageTextParams1 = new LatticeView.ImageTextParams();

        // 未被选中图片
        imageTextParams1.images = new Integer[] {
                0,
                0,
                0,
                0};

        imageTextParams1.text = new String[]{viewModel.getString("a"), viewModel.getString("b"), viewModel.getString("c"), viewModel.getString("d")};
        imageTextParams1.maxLine = 4; // 每一行显示的个数
        imageTextParams1.imageHigh = 25;
        imageTextParams1.imageLoader = new ImageGlideLoader();
        imageTextParams1.imageWidth = 25;
        imageTextParams1.textSize = 12; // text的字体大小
        imageTextParams1.textPaddingTop = LatticeView.dip2px(getContext(), 8); // 字体向上给一个padding
        imageTextParams1.selectIndex = 0; // 默认第一个被选中
        imageTextParams1.textColor = com.yft.zbase.R.color.theme_text_color; // 字体默认颜色
        imageTextParams1.imageType = ImageView.ScaleType.FIT_XY;
        imageTextParams1.bg_color = "#00000000";
        imageTextParams1.textSelectColor = com.yft.zbase.R.color.theme_text_color; // 字体被选中的颜色
        mDataBing.llViewTwo.setImageTextParams(imageTextParams1);
        mDataBing.llViewTwo.startView(); // 开始加载布局

    }

    protected void setStatusBar(Context context) {
        ViewGroup.LayoutParams marginLayoutParams = mDataBing.vTop.getLayoutParams();
        marginLayoutParams.height = viewModel.getStatusBarHeight();
        mDataBing.vTop.setLayoutParams(marginLayoutParams);
    }

    @Override
    public void initListener() {

        mDataBing.rlC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewModel.getUserInfo().setAmountComing(93232878237.00d);
//                mDataBing.setUser(viewModel.getUserInfo());
            }
        });

        mDataBing.rlB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mDataBing.rlA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ACTIVITY_FAVORITESLISTACTIVITY
            }
        });

        mDataBing.ivKf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kfStr = viewModel.getString("在线客服");
//                if (BuildConfig.DEBUG) {
////                    Bundle bundle = new Bundle();
////                    bundle.putString("title", kfStr);
////                    bundle.putString("url", "file:///android_asset/h5/index.html");
////                    RouterFactory.startRouterBundleActivity(requireContext(), RouterFactory.ACTIVITY_WEB, bundle);
//
//                    return;
//                }

                if (viewModel.getUserInfo() != null && !TextUtils.isEmpty(viewModel.getUserServer().getServiceUrl().getServiceLinkUrl())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", kfStr);
                    bundle.putBoolean("isWebTitle", false);
                    bundle.putString("url", viewModel.getUserServer().getServiceUrl().getServiceLinkUrl());
//                    RouterFactory.startRouterBundleActivity(requireContext(), RouterFactory.ACTIVITY_WEB, bundle);
                    RouterFactory.getInstance().startRouterBundleActivity(requireContext(), RouterFactory.getInstance().getPage("WebYftActivity"), bundle);
                } else {
                    ToastUtils.toast(viewModel.getString("未匹配到客服!"));
                }
            }
        });

        mDataBing.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouterFactory.getInstance().startRouterActivity(getContext(), RouterFactory.getInstance().getPage("SetActivity"));
            }
        });

        mDataBing.llViewTwo.setOnPageItemOnClickListener(new BaseLatticeView.OnPageItemOnClickListener() {
            @Override
            public void onClick(View v, Object[] urls, int position) {
                if (position == 0) {
                }  else if (position == 1) {
                } else if (position == 2) {
                } else if(position == 3) {
                    RouterFactory.getInstance().startRouterActivity(getContext(), RouterFactory.getInstance().getPage("InviteFriends2Activity"));
                }
            }
        });


        //    RouterFactory.startRouterActivity(getContext(), RouterFactory.ACTIVITY_SET);
        mDataBing.rlD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouterFactory.getInstance().startRouterActivity(getContext(), RouterFactory.getInstance().getPage("SetActivity"));
            }
        });
        mDataBing.ivIcon.setOnClickListener(mGoToUserInfoClick);
        mDataBing.llLogoState.setOnClickListener(mGoToUserInfoClick);
        mDataBing.rlInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewModel.getErrorMutableLiveData().observe(this, this::onError);
        viewModel.getMutableUserLiveData().observe(this, this::onUser);
        mDataBing.llAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mDataBing.llSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DividedDesActivity
            }
        });
    }

    private void onError(String tag) {

    }

    private void onUser(UserInfo userInfo) {
        mDataBing.setUser(userInfo);
        mDataBing.tvLoginState.setText("UID:" + viewModel.getUserInfo().getRecommendCode());
        UserLevelType levelType = getUserLevel(viewModel.getUserInfo().getLevel());
        if (levelType != null) {
            mDataBing.ivLevel.setVisibility(View.VISIBLE);
            mDataBing.ivLevel.setImageResource(levelType.getImageId());
        } else {
            mDataBing.ivLevel.setVisibility(View.GONE);
        }
    }


    private void setTips(int position, int number) {

    }

    private View.OnClickListener mGoToUserInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //ActivityUserInformation
            RouterFactory.getInstance().startRouterActivity(UserFragment.this.requireContext(), RouterFactory.getInstance().getPage("ActivityUserInformation"));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LOGE("UserFragment", "=====> onResume");
        viewModel.requestMyInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LOGE("UserFragment", "=====> hidden=> " + hidden);
        if (!hidden) {
            viewModel.requestMyInfo();
        }
    }


    @Override
    protected void handleServiceAddress(ServiceBean serviceBean) {
        super.handleServiceAddress(serviceBean);
        LOGE("======serviceBean=user>>>>>" + serviceBean.getAssignUri());
    }


    @Override
    public void initData() {
        mDataBing.setUser(viewModel.getUserInfo());
        if (viewModel.getUserInfo() == null) {
            return;
        }
        UserLevelType levelType = getUserLevel(viewModel.getUserInfo().getLevel());
        if (levelType != null) {
            mDataBing.ivLevel.setVisibility(View.VISIBLE);
            mDataBing.ivLevel.setImageResource(levelType.getImageId());
        } else {
            mDataBing.ivLevel.setVisibility(View.GONE);
        }
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_user_layout;
    }
}
