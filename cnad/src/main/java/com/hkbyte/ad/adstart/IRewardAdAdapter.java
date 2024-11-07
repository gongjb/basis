package com.hkbyte.ad.adstart;

import android.app.Activity;

/**
 * 激励视频接口
 */
public interface IRewardAdAdapter extends IAdAdapter {

    /**
     *
     * @param activity
     * @param adId 广告id
     * @param rewardAmount 奖励数量
     * @param rewardName 奖励名称
     * @param iRewardViewListener 监听器
     * @return
     */
    boolean startRewardAd(Activity activity, String adId, int rewardAmount, String rewardName, IRewardViewListener iRewardViewListener);

    interface IRewardViewListener {
        /**
         * 奖励结果
         * @param isRewardValid 是否获得奖励
         * @param rewardBundleModel
         */
        void onRewardArrived(boolean isRewardValid, RewardBundleModel rewardBundleModel);

        void onError(int code, String msg);

        /**
         * 跳过广告
         */
        default void onSkippedVideo(){};

        default void onAdShow() {} // 开始

        default void onAdVideoBarClick() {} // 点击了

        default void onAdClose() {} // 广告关闭

        default void onVideoError() {} // 播放错误

        default void onVideoComplete() {} // 播放完成

        /**----如果有再一次播放，请监听以下方法-----**/
        /**
         * 奖励结果
         * @param isRewardValid 是否获得奖励
         * @param rewardBundleModel
         */
        default void onAgainRewardArrived(boolean isRewardValid, RewardBundleModel rewardBundleModel) {};

        /**
         * 跳过广告
         */
        default void onAgainSkippedVideo(){};

        default void onAgainAdShow() {} // 开始

        default void onAgainAdVideoBarClick() {} // 点击了

        default void onAgainAdClose() {} // 广告关闭

        default void onAgainVideoError() {} // 播放错误

        default void onAgainVideoComplete() {} // 播放完成


    }
}
