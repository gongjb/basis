package com.yft.home;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;


import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.yft.home.bean.HomeConfigBean;
import com.yft.zbase.utils.UIUtils;

public class VideoBannerPageView extends LinearLayout {
    private HomeConfigBean.BannerListBean mBannerBean;
    private PlayerView playerView;
    //开始播放视频
    private FrameLayout flVideo;
    private ImageView imageView;
    private static SimpleExoPlayer player;
    private static MediaSource mediaSource;

    private static ProgressiveMediaSource.Factory factory;

    public HomeConfigBean.BannerListBean getBannerBean() {
        return mBannerBean;
    }

    public VideoBannerPageView(Context context) {
        super(context);
        init();
    }

    public VideoBannerPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoBannerPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VideoBannerPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        View.inflate(getContext(), R.layout.item_banner_video_us_layout, this);
    }


    public static void cleanMediaSource() {
        mediaSource = null;
    }

    public void setData(HomeConfigBean.BannerListBean bannerListBean) {
        if (bannerListBean == null) return;
        this.mBannerBean = bannerListBean;
        imageView = findViewById(R.id.iv_icon);
        TextView tvName = findViewById(R.id.tv_name);
        UIUtils.setImgUrl(imageView, bannerListBean.getImage());
        tvName.setText(bannerListBean.getName());
        playerView = findViewById(R.id.player_view);
        playerView.setControllerAutoShow(true);
        playerView.setControllerHideOnTouch(true);
        playerView.setControllerHideDuringAds(true);
        playerView.setResizeMode(RESIZE_MODE_ZOOM);
        playerView.setUseController(false);
        flVideo = findViewById(R.id.fl_video);

        // 初始化ExoPlayer
        if (player == null) {
            factory = new ProgressiveMediaSource.Factory(
                    new DefaultHttpDataSourceFactory(Util.getUserAgent(getContext().getApplicationContext(), getContext().getString(com.yft.zbase.R.string.app_name))));

            player = ExoPlayerFactory.newSimpleInstance(getContext(),
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
        }
    }

    public void play() {
        if (mediaSource != null) return; // 已经有媒体在播放了。
        if (this.mBannerBean == null || TextUtils.isEmpty(this.mBannerBean.getVideoUrl())) return;
        flVideo.setVisibility(View.VISIBLE);
        playerView.setPlayer(player);

        String mediaUrl = this.mBannerBean.getVideoUrl(); // 替换为你的视频URL
        mediaSource = factory.createMediaSource(Uri.parse(mediaUrl));
        // 创建一个循环播放的媒体源
        LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
        // 准备播放器
        player.prepare(loopingSource);
        // 开始播放
        player.setPlayWhenReady(true);
        // 静音播放
        player.setVolume(0);
    }

    public static void stop() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    public static void restart() {
        if (player != null) {
            player.setPlayWhenReady(true);
            // 静音播放
            player.setVolume(0);
        }
    }


    public static void clean() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
