package com.yft.zbase.utils;

import static com.yft.zbase.utils.Logger.LOGE;
import static com.yft.zbase.utils.Utils.format;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.yft.zbase.R;
import com.yft.zbase.server.UserLevelType;
import com.yft.zbase.ZBaseApplication;
import com.yft.zbase.bean.CommodityBean;
import com.yft.zbase.bean.TargetBean;
import com.yft.zbase.server.ILanguage;
import com.yft.zbase.server.LanguageManage;
import com.yft.zbase.widget.RoundRelativeLayout;

import java.util.List;

public class UIUtils {

    @BindingAdapter(value = {"url", "placeholder", "error", "radius"}, requireAll = false)
    public static void setImgUrl(ImageView imageView, String url, int placeholder, int error, int radius) {
        RequestBuilder requestOptions = Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholder)
                .thumbnail(0.1f)
                .error(error);
        if (radius > 0) {
            RoundedCorners roundedCorners = new RoundedCorners(Utils.dip2px(imageView.getContext(), radius));
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            requestOptions.apply(options);
        }
        requestOptions.into(imageView);
    }


    public static String setStringIn(String inStr, String actual) {
        LOGE("===>1111111111111111");
//        for (LanguageTo languageTo : LanguageTo.values()) {
//            if (languageTo.getCn().equals(inStr) || languageTo.getEn().equals(inStr)) {
//                if (iLanguage.getLanguageType().equals(ILanguage.CN_TYPE)) {
//                    return String.format(languageTo.getCn(), actual);
//                } else {
//                    return String.format(languageTo.getEn(), actual);
//                }
//            }
//        }
        return "";
    }

    //photoUrl
    @BindingAdapter(value = {"photoUrl"}, requireAll = false)
    public static void setPhotoUrl(ImageView imageView, int level) {
        UserLevelType type = Utils.getUserLevel(level);
        if (type == null || level == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(type.getImageId());
        }
    }



    public static void setImgUrl(ImageView imageView, Context context, String url) {
        RequestBuilder requestOptions = Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f);
        requestOptions.into(imageView);
    }


    @BindingAdapter(value = {"url", "app"}, requireAll = false)
    public static void setImgUrl(ImageView imageView, String url, String app) {
        RequestBuilder requestOptions = Glide.with(ZBaseApplication.get())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f);
        requestOptions.into(imageView);
    }



    @BindingAdapter(value = {"filePath"}, requireAll = false)
    public static void setFileImgUrl(ImageView imageView, String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
        }
    }


    @BindingAdapter(value = {"url", "placeholder", "error", "radius"}, requireAll = false)
    public static void setImgUrl(ImageView imageView, int url, int placeholder, int error, int radius) {
        RequestBuilder requestOptions = Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholder)
                .thumbnail(0.1f)
                .error(error);// 商品详情
        if (radius > 0) {
            RoundedCorners roundedCorners = new RoundedCorners(Utils.dip2px(imageView.getContext(), radius));
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            requestOptions.apply(options);
        }
        requestOptions.into(imageView);
    }



    @BindingAdapter(value = {"url", "radius"}, requireAll = false)
    public static void setImgUrl(ImageView imageView, int url, int radius) {
        setImgUrl(imageView, url, 0, 0, radius);
    }

    @BindingAdapter(value = {"rid"}, requireAll = false)
    public static void setImgUrl(ImageView imageView, int rid) {
        setImgUrl(imageView, rid, 0, 0, 0);
    }


    @BindingAdapter(value = {"url"}, requireAll = false)
    public static void setImgUrl(ImageView imageView, String url) {
        setImgUrl(imageView, url, 0, 0, 0);
    }

    @BindingAdapter(value = {"url_thumbnail", "placeholder", "error", "radius"}, requireAll = false)
    public static void setThumbnailImgUrl(ImageView imageView, String url, int placeholder, int error, int radius) {
        try {
            RequestBuilder requestOptions = Glide.with(imageView.getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeholder)
                    .thumbnail(0.1f)
                    .error(error);
            if (radius > 0) {
                RoundedCorners roundedCorners = new RoundedCorners(Utils.dip2px(imageView.getContext(), radius));
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                requestOptions.apply(options);
            }
            requestOptions.into(imageView);
        }catch (Exception e) {

        }

    }

    @BindingAdapter(value = {"url", "radius2"}, requireAll = false)
    public static void setImgUrl(ImageView imageView, String url, int radius) {
        setImgUrl(imageView, url, 0, 0, radius);
    }

    public static void preload(Context context, TargetBean openStartBean) {
        if (openStartBean == null || TextUtils.isEmpty(openStartBean.getImage())) {return;}

        Glide.with(context)
                .load(openStartBean.getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        LOGE("图片预加载失败！！");
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        LOGE("图片预加载完成！！");
                        return true;
                    }
                }).preload();
    }

    /**
     * 按图片原比例加载
     * @param imageView
     * @param url
     */
    public static void loadRatioImage(final ImageView imageView, final String url) {
        RequestBuilder requestOptions = Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ImageViewTarget imageViewTarget = (ImageViewTarget) target;
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        float w = bitmap.getWidth();
                        float h = bitmap.getHeight();
                        float p = w / h;
                        ViewGroup.LayoutParams layoutParams = imageViewTarget.getView().getLayoutParams();
                        float screenWidth =  Utils.getScreenWidth(imageViewTarget.getView().getContext());
                        layoutParams.width = (int) screenWidth;
                        layoutParams.height = (int) (screenWidth / p);
                        imageViewTarget.getView().setLayoutParams(layoutParams);
                        return false;
                    }
                });

        requestOptions.into(imageView);
    }

    @BindingAdapter(value = {"groupPadding", "position"}, requireAll = false)
    public static void setGroupPadding(LinearLayout view, @DimenRes int groupPadding, int position) {
        int rad = view.getContext().getResources().getDimensionPixelSize(groupPadding);
    }

    @BindingAdapter(value = "setViewRadius")
    public static void setViewRadius(View view, @DimenRes int radius) {
        int rad = view.getContext().getResources().getDimensionPixelSize(radius);
        view.setClipToOutline(true);
        view.post(() -> view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view1, Outline outline) {
                outline.setRoundRect(0, 0, view1.getWidth(), view1.getHeight(), rad);
            }
        }));
    }

    //setBackgroundSel
    @BindingAdapter(value =  {"setBackgroundSel", "setBackgroundNot", "isSel"})
    public static void setBackground(View view, @ColorRes int sel, @ColorRes int notSel, boolean isSel) {
        int color = 0;
        if (isSel) {
            color = view.getContext().getResources().getColor(sel);
        } else {
            color = view.getContext().getResources().getColor(notSel);
        }
        view.setBackgroundColor(color);
    }

    @BindingAdapter(value =  {"drawableSel", "drawableNot", "isSel"})
    public static void setBackgroundDrawable(View view, @DrawableRes int sel, @DrawableRes int notSel, boolean isSel) {
         Drawable drawable;
        if (isSel) {
            drawable = view.getContext().getResources().getDrawable(sel);
        } else {
            drawable = view.getContext().getResources().getDrawable(notSel);
        }
        view.setBackgroundDrawable(drawable);
    }

    @BindingAdapter(value =  {"isProfitIng"})
    public static void setBackgroundSrc(ImageView imageView, boolean isFinish) {

    }


    public static void cleanImage(Context context) {
        Glide.get(context).clearDiskCache();
        Glide.get(context).clearMemory();
    }

    public static void setImgUrl() {

    }

    public static void showToast(String message) {
        Toast.makeText(ZBaseApplication.get(), message, Toast.LENGTH_LONG).show();
    }

    @BindingAdapter(value = "roundClassifySelect")
    public static void setRoundSelView(RoundRelativeLayout roundView, boolean isSel) {
        roundView.setStrokeColor(isSel ? Color.parseColor("#FF7D00") : Color.parseColor("#ffffff"));
    }

    @BindingAdapter(value = "classifyStyle")
    public static void setClassifyTextStyle(TextView roundView, boolean isSel) {
       // roundView.setBackgroundColor(isSel ? roundView.getContext().getResources().getColor(R.color.btn_color) : Color.parseColor("#00000000"));
        setViewRadius(roundView, R.dimen.ui_11_dp);
        roundView.setTextColor(isSel ? roundView.getContext().getResources().getColor(R.color.btn_color)
                : roundView.getContext().getResources().getColor(R.color.text_color_33));
    }

    @BindingAdapter(value = "classifyRadius")
    public static void setClassifyTextStyle(TextView roundView, @DimenRes int radius) {
        roundView.setBackgroundColor(roundView.getContext().getResources().getColor(R.color.btn_color));
        setViewRadius(roundView, radius);
    }

    /**
     * 打开指定的APK
     * @param mContext
     * @param packageName
     * @return
     */
    public static boolean openApk(Context mContext, String packageName, String activity){
        try {
            // com.tencent.mobileqq/com.tencent.mobileqq.activity.HomeActivity
            // com.tencent.mm/com.tencent.mm.ui.LauncherUI

            Intent intent = new Intent();
            ComponentName cmp = new ComponentName(packageName, activity);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            intent.setComponent(cmp);
            mContext.startActivity(intent);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @BindingAdapter(value = {"textPic"}, requireAll = false)
    public static void setTextPic(TextView view, String pic) {
        double d = Long.parseLong(pic) / 100.0d;
        view.setText("¥" + Utils.format(d));
    }

    @BindingAdapter(value = {"textToPic"}, requireAll = false)
    public static void setTextTopic(TextView view, String pic) {
        double d = Long.parseLong(pic) / 100.0d;
        view.setText(Utils.format(d));
    }

    @BindingAdapter(value = {"textLongPic"}, requireAll = false)
    public static void setTextTopic(TextView view, long pic) {
        double d = pic / 100.0d;
        view.setText(Utils.format(d));
    }

    // 开放给xml调用
    @BindingAdapter(value = {"integral", "zone"}, requireAll = false)
    public static void textIntegral(TextView tvIntegral, String integral, String zone) {
        setIntegral(integral, zone, tvIntegral);
    }

    @BindingAdapter(value = {"cashbackDays"}, requireAll = false)
    public static void textCashbackDays(TextView tvCashbackDays, CommodityBean commodityBean) {
        if (tvCashbackDays == null) {
            return;
        }
        if (commodityBean == null || commodityBean.getCashbackDays() <= 0) {
            return;
        }

        if (Constant.ACTIVITY.equals(commodityBean.getZone())) {
            try {
                double d = (Long.parseLong(commodityBean.getPriceSales()) / 100.0d) / commodityBean.getCashbackDays();
                tvCashbackDays.setText("每日品鉴奖励: ¥" + format(d));
            } catch (Exception e) {
                e.printStackTrace();
                tvCashbackDays.setText("");
            }
        }  else {
            tvCashbackDays.setText("");
        }
    }



    public static void setIntegral(String integral, String zoom, TextView tvIntegral) {

        if (tvIntegral == null) {
            return;
        }

        if (!Constant.NORMAL.equals(zoom)) {
            tvIntegral.setText("");
            return;
        }

        if (TextUtils.isEmpty(integral)) {
            tvIntegral.setText("");
            return;
        }

        try {
            double d = Double.parseDouble(integral);
            String integralStr = Utils.getIntegral(d);
            if (d <= 0.0d) {
                tvIntegral.setText("");
            } else {
                tvIntegral.setText(String.format("积分: %s", integralStr));
            }
        } catch (Exception e){
            tvIntegral.setText("");
        }
    }

    /**
     * 清除默认动画
     * @param recyclerView
     * @return
     */
    public static boolean removeAnimator(RecyclerView recyclerView) {
        try {
            SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) recyclerView.getItemAnimator();
            simpleItemAnimator.setAddDuration(0);
            simpleItemAnimator.setChangeDuration(0);
            simpleItemAnimator.setMoveDuration(0);
            simpleItemAnimator.setRemoveDuration(0);
            simpleItemAnimator.setSupportsChangeAnimations(false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void changeGoodsHeight(List<CommodityBean> commodityBeans, Context context) {
        if (!Utils.isCollectionEmpty(commodityBeans)) {
            double width = Utils.getBodyWidth(context);
            for (CommodityBean commodityBean : commodityBeans) {
                double ratio = 1.0d;
                if (commodityBean.getShowImageRatio() > 0.0d) {
                    ratio = Utils.formatTo2(commodityBean.getShowImageRatio());
                }
                double d0 = width / 2.0d; // 得到容器的宽度
                double d1 = d0 / ratio; // 得到高
                commodityBean.setHeight((int)d1);
            }
        }
    }

    public static void startToScale(View view) {
        // 创建一个从中心点开始扩展的缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0f, 1f, // 在X轴上从0倍缩放到1倍
                1f, 1f, // 在Y轴上保持不变（因为是水平线，所以Y轴不需要缩放）
                Animation.RELATIVE_TO_SELF, 0.5f, // 从视图的中心点开始动画
                Animation.RELATIVE_TO_SELF, 0.5f); // 保持Y轴的中心点不变
        // 设置动画的持续时间
        scaleAnimation.setDuration(1000); // 1000毫秒，即1秒
        // 设置动画的填充模式，以保持动画结束后的状态
        scaleAnimation.setFillAfter(true);
        // 开始动画
        view.startAnimation(scaleAnimation);
    }
}
