# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in G:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.

# Disable debug info output
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String,int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}


#指定代码的压缩级别
-optimizationpasses 5

#包名不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化 不优化输入的类文件
-dontoptimize

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

#保护注解
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions,InnerClasses,Deprecated,SourceFile,LineNumberTable,Annotation,EnclosingMethod

-keep public class * extends com.yft.zbase.base.BaseViewModel
-keep class cn.sd.ld.ui.helper.**{*;}
-keep class android.**{ *;}
-keep class androidx.**{ *;}
-keep class com.alibaba.android.vlayout.**{ *;}

-keepclasseswithmembernames class * {
  native <methods>;
}
-keepclassmembers class * extends android.app.Activity {
  public void *(android.view.View);
}
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
  *;
}


-keep class * extends android.view.View{*;}
-keep class * extends android.app.Dialog{*;}

-keep class android.net.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class com.fuan.market.databinding.**{*;}
-keep class org.apache.**{*;}
-keep class com.alibaba.**{ *;}
-keep class com.chenenyu.**{ *;}
-keep class com.google.**{ *;}
-keep class com.yft.zbase.**{ *;}
-keep class * extends com.yft.zbase.adapter.CommonAdapter
-keep class java.lang.**{ *;}

-keep class com.yft.home.**{ *;}
-keep class com.github.mikephil.charting.**{ *;}


-keep,allowobfuscation interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class *
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

-keep class com.fuan.market.WelcomeActivity
-keep class androidx.databinding.** { *; }
-keep class com.shuyu.** { *; }
-keep class tv.danmaku.** { *; }
-keep class com.reelshort.supershort.** { *; }
-keep class * extends  androidx.databinding.ViewDataBinding
-keep class androidx.viewbinding.** { *; }

-keepattributes javax.xml.bind.annotation.*
-keepattributes javax.annotation.processing.*




