package com.hkbyte.ad.adstart;

import static com.yft.zbase.utils.Logger.LOGE;

import android.app.Activity;
import android.os.Bundle;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot;
import com.yft.zbase.utils.UIUtils;

/**
 * 激励视频
 */
public class AdRewardStart implements IRewardAdAdapter {
    private static final String TAG = "AdRewardStart";
    private TTRewardVideoAd mTTRewardVideoAd; // 插全屏广告对象
    private TTAdNative.RewardVideoAdListener mRewardVideoListener; // 广告加载监听器
    private TTRewardVideoAd.RewardAdInteractionListener mRewardVideoAdInteractionListener; // 广告展示监听器
    private TTRewardVideoAd.RewardAdInteractionListener mRewardVideoAdPlayAgainInteractionListener; // 再看一个广告展示监听器
    private volatile boolean isOpenReward;
    private static IRewardAdAdapter mInstance;

    private AdRewardStart() {};

    public static synchronized <T extends IAdAdapter> T getInstance() {
        if (mInstance == null) {
            synchronized (AdSplashStart.class) {
                if (mInstance == null) {
                    mInstance = new AdRewardStart();
                }
            }
        }
        return (T) mInstance;
    }

    @Override
    public boolean startRewardAd(Activity activity, String adId,
                                 int rewardAmount, String rewardName, IRewardViewListener iRewardViewListener) {

        if (activity == null) {
            LOGE(TAG, "activity is null");
            return false;
        }

        if (activity.isFinishing()) {
            LOGE(TAG, "activity is isFinishing");
            return false;
        }

        if (iRewardViewListener == null) {
            LOGE(TAG, "iRewardViewListener is null");
            return false;
        }

        if (isOpenReward) {
            LOGE(TAG, "isOpenReward is true");
            return false;
        }

        isOpenReward = true;
        /** 1、创建AdSlot对象 */
        AdSlot adslot = new AdSlot.Builder()
                .setCodeId(adId)
                .setRewardAmount(1)
                .setRewardName("个月VIP")
                .setOrientation(TTAdConstant.ORIENTATION_VERTICAL)
                .build();

        /** 2、创建TTAdNative对象 */
        TTAdNative adNativeLoader = TTAdSdk.getAdManager().createAdNative(activity);

        /** 3、创建加载、展示监听器 */
        initListeners(activity, iRewardViewListener);

        /** 4、加载广告 */
        adNativeLoader.loadRewardVideoAd(adslot, mRewardVideoListener);
        return true;
    }



    private void initListeners(final Activity activity, final IRewardViewListener iRewardViewListener) {
        // 广告加载监听器
        this.mRewardVideoListener = new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                isOpenReward = false;
                iRewardViewListener.onError(i, s);
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                mTTRewardVideoAd = ttRewardVideoAd;

                if (mTTRewardVideoAd == null) {
                    return;
                }
                /** 5、设置展示监听器，展示广告 */
                mTTRewardVideoAd.setRewardAdInteractionListener(mRewardVideoAdInteractionListener);

                mTTRewardVideoAd.showRewardVideoAd(activity);
            }

            @Override
            public void onRewardVideoCached() {

            }

            @Override
            public void onRewardVideoCached(TTRewardVideoAd ttRewardVideoAd) {
                mTTRewardVideoAd = ttRewardVideoAd;
            }
        };

        // 广告展示监听器
        this.mRewardVideoAdInteractionListener = new TTRewardVideoAd.RewardAdInteractionListener() {
            @Override
            public void onAdShow() {
                iRewardViewListener.onAdShow();
            }

            @Override
            public void onAdVideoBarClick() {
                iRewardViewListener.onAdVideoBarClick();
            }

            @Override
            public void onAdClose() {
                isOpenReward = false;
                iRewardViewListener.onAdClose();
            }

            @Override
            public void onVideoComplete() {
                isOpenReward = false;
                iRewardViewListener.onVideoComplete();
            }

            @Override
            public void onVideoError() {
                isOpenReward = false;
                iRewardViewListener.onVideoError();
            }

            @Override
            public void onRewardVerify(boolean b, int i, String s, int i1, String s1) {
            }

            @Override
            public void onRewardArrived(boolean isRewardValid, int i, Bundle bundle) {
                isOpenReward = false;
                RewardBundleModel rewardBundleModel = new RewardBundleModel(bundle);
                LOGE(TAG, "Callback --> rewardVideoAd has onRewardArrived " +
                        "\n奖励是否有效：" + isRewardValid +
                       // "\n奖励类型：" + rewardType +
                        "\n奖励名称：" + rewardBundleModel.getRewardName() +
                        "\n奖励数量：" + rewardBundleModel.getRewardAmount() +
                        "\n建议奖励百分比：" + rewardBundleModel.getRewardPropose());

//                if (!isRewardValid) {
//                    Log.d(TAG, "发送奖励失败 code：" + rewardBundleModel.getServerErrorCode() +
//                            "\n msg：" + rewardBundleModel.getServerErrorMsg());
//                }
                iRewardViewListener.onRewardArrived(isRewardValid, rewardBundleModel);
            }

            @Override
            public void onSkippedVideo() {
                iRewardViewListener.onSkippedVideo();
            }
        };
    }

    @Override
    public void onClean() {
        if (mTTRewardVideoAd != null && mTTRewardVideoAd.getMediationManager() != null) {
            mTTRewardVideoAd.getMediationManager().destroy();
        }
    }

    public void test(Activity activity) {
        IRewardAdAdapter mRewardAdAdapter = AdFactory.getInstance().createRewardAd(AdType.CSJ_REWARD);
        mRewardAdAdapter.startRewardAd(activity, "103183279", 1, "个月VIP",  new IRewardAdAdapter.IRewardViewListener() {
            @Override
            public void onRewardArrived(boolean isRewardValid, RewardBundleModel rewardBundleModel) {
                if (isRewardValid) {
                    // 获得奖励
                    UIUtils.showToast("恭喜获得奖励" + rewardBundleModel.getRewardAmount() + "--" + rewardBundleModel.getRewardName());
                } else {
                    UIUtils.showToast("未获得奖励");
                }
            }

            @Override
            public void onError(int code, String msg) {
                UIUtils.showToast("code" +code+ "===>msg：" + msg);
            }

            @Override
            public void onVideoError() {
                // 视频出错
                UIUtils.showToast("视频1， 播放出错！！！");
            }
        });
    }
}
