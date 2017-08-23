# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in Z:\Users\nEdAy\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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


    -keepattributes InnerClasses,Deprecated,SourceFile,LineNumberTable,EnclosingMethod
    #指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

    #优化  不优化输入的类文件
    -dontoptimize

    #预校验
    -dontpreverify

    #混淆时是否记录日志
    -verbose

    # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*

    #忽略警告
    -ignorewarning

    -keepattributes Signature
    -keepattributes Exceptions

    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment
    #如果引用了v4或者v7包
    -keep class android.support.** { *; }
    -dontwarn android.support.**

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService

    ##记录生成的日志数据,gradle build时在本项目根目录输出##

    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    ########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

    -keep public class com.example.R$*{
        public static final int *;
    }

    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }
    -keep class **.R$* {*;}
    -keep class **.R{*;}
    -dontwarn **.R$*
    -keepclassmembers class * {
       public <init> (org.json.JSONObject);
    }
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
################混淆保护自己项目的部分代码以及引用的第三方jar包################
    -dontwarn java.lang.invoke.*

    -keep class net.nashlegend.demo.Sample{ *; }

    -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }

    -keepclasseswithmembers class c.b.** { *; }
    -keep interface c.b.PListener{ *; }
    -keep interface c.b.QListener{ *; }

    -dontwarn bolts.**
    -keep class bolts.** { *; }

    -dontwarn com.facebook.**
    -keep class com.facebook.** { *; }

    -dontwarn com.google.gson.**
    -keep class com.google.gson.** { *; }

    -dontwarn org.eclipse.mat.**
    -keep class org.eclipse.mat.** { *; }

    -dontwarn com.squareup.**
    -keep class com.squareup.** { *; }
    -keep interface com.squareup.** { *; }

    -dontwarn com.nineoldandroids.**
    -keep class com.nineoldandroids.** { *; }

    -dontwarn okhttp3.**
    -keep class okhttp3.** { *; }

    -dontwarn okio.**
    -keep class okio.** { *; }

    -dontwarn rx.**
    -keep class rx.** { *; }

    -keep class android.net.http.SslError
    -keep class android.webkit.**{*;}
    -keep class cn.sharesdk.**{*;}
    -keep class cn.smssdk.**{*;}
    -keep class com.mob.**{*;}

    -dontwarn com.bigkoo.svprogresshud.**
    -keep class com.bigkoo.svprogresshud.** { *; }

    -dontwarn com.readystatesoftware.systembartint.**
    -keep class com.readystatesoftware.systembartint.** { *; }

    -dontwarn in.srain.cube.views.ptr.**
    -keep class in.srain.cube.views.ptr.** { *; }

    -keep class com.neday.bomb.view.**{*;}
    -keep class com.neday.bomb.entity.**{*;}
    -keep class com.neday.bomb.express.entity.**{*;}

    -keep class cn.sharesdk.**{*;}
    -dontwarn cn.sharesdk.**
    -keep class com.sina.**{*;}
    ################阿里百川——电商################
    -keepattributes Signature
    -keep class sun.misc.Unsafe { *; }
    -keep class com.taobao.** {*;}
    -keep class com.alibaba.** {*;}
    -keep class com.alipay.** {*;}
    -dontwarn com.taobao.**
    -dontwarn com.alibaba.**
    -dontwarn com.alipay.**
    -keep class com.ut.** {*;}
    -dontwarn com.ut.**
    -keep class com.ta.** {*;}
    -dontwarn com.ta.**
    -keep class org.json.** {*;}
    -keep class com.ali.auth.**  {*;}
    ################proguard-fresco.pro################
    -keep @com.facebook.common.internal.DoNotStrip class *
    -keepclassmembers class * {
        @com.facebook.common.internal.DoNotStrip *;
    }
    # Keep native methods
    -keepclassmembers class * {
        native <methods>;
    }
    -dontwarn okio.**
    -dontwarn com.squareup.okhttp.**
    -dontwarn okhttp3.**
    -dontwarn javax.annotation.**
    -dontwarn com.android.volley.toolbox.**
    # Works around a bug in the animated GIF module which will be fixed in 0.12.0
    -keep class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImpl {
        public AnimatedFactoryImpl(com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory,com.facebook.imagepipeline.core.ExecutorSupplier);
    }
    ################proguard-fresco.pro################
    -dontwarn com.igexin.**
    -keep class	com.igexin.**{*;}
    ################bugly################
    -keep public class com.tencent.bugly.**{*;}
    -dontwarn com.tencent.bugly.**
    ################AnyPref################
    -keepattributes Signature
    -keep class net.nashlegend.anypref.annotations.PrefModel
    -keepclasseswithmembernames @net.nashlegend.anypref.annotations.PrefModel class * {
        public <fields>;
    }
    ################takephoto################
    -keep class com.jph.takephoto.** { *; }
    -dontwarn com.jph.takephoto.**

    -keep class com.darsh.multipleimageselect.** { *; }
    -dontwarn com.darsh.multipleimageselect.**

    -keep class com.soundcloud.android.crop.** { *; }
    -dontwarn com.soundcloud.android.crop.**
    ################混淆保护自己项目的部分代码以及引用的第三方jar包################

    ##--------------- Begin: Gson ----------
    # Gson uses generic type information stored in a class file when working with fields. Proguard
    # removes such information by default, so configure it to keep all of it.
    -keepattributes Signature

    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    ##--------------- End: Gson ----------

    ##--------------- Begin: Retrolambda ----------
    -dontwarn java.lang.invoke.*
    ##--------------- End: Retrolambda ----------

    ##--------------- Begin: OkHttp ----------
    -keepattributes Signature
    -keepattributes *Annotation*
    -keep class com.squareup.okhttp.** { *; }
    -keep interface com.squareup.okhttp.** { *; }
    -dontwarn com.squareup.okhttp.**
    ##--------------- End: OkHttp ----------

    ##--------------- Begin: Retrofit ----------
    -dontwarn retrofit.**
    -keep class retrofit.** { *; }

    -keepclasseswithmembers class * {
        @retrofit.http.* <methods>;
    }
    ##--------------- End: Retrofit ----------

    ##--------------- Begin: Retrofit 2 -----------
    -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }
    -keepattributes Signature
    -keepattributes Exceptions

    -keepclasseswithmembers class * {
        @retrofit2.http.* <methods>;
    }
    ##--------------- End: Retrofit 2 -----------

    ##--------------- Begin: RxJava ----------
    -keep public class rx.Single { *; }

    -dontwarn sun.misc.Unsafe

    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
       long producerIndex;
       long consumerIndex;
    }

    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
       rx.internal.util.atomic.LinkedQueueNode producerNode;
       rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }

    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
       rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }
    ##--------------- End: RxJava ----------

    ##--------------- Begin: Okio ----------
    -dontwarn okio.**
    ##--------------- End: Okio ----------

    ##--------------- Begin: Model ----------
    -keepclassmembers class com.github.simonpercic.oklogexample.data.api.model.** {
      <fields>;
    }
    ##--------------- End: Model ----------
    -dontwarn com.avos.avoscloud.**

    -keep class android.net.http.** { *;}

    -keep class android.content.** { *; }

    -keep class android.support.v4.** { *; }
    -keep class com.sina.** { *; }
    -keep class com.weibo.** {*; }
    -keep class com.tencent.** {*; }
    -keep class com.sohu.cyan.android.sdk.api.** { *;}
    -keep class com.sohu.cyan.android.sdk.push.** { *;}
    -keep class com.sohu.cyan.android.sdk.entity.** { *;}
    -keep class com.sohu.cyan.android.sdk.response.** { *;}
    -keep class com.sohu.cyan.android.sdk.http.** { *;}
    -keep class com.google.gson.JsonObject { *; }
    -keep class com.badlogic.** { *; }
    -keep class * implements com.badlogic.gdx.utils.Json*
    -keep class com.google.** { *; }

    -keepattributes Signature

    -keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
        native <methods>;
    }
    -keepclasseswithmembers class * {
        public <init>(android.content.Context);
    }


    -keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
    }

    -keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
       public void *(android.view.View);
    }

    -keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }

    -keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
      public static final android.os.Parcelable$Creator *;
    }


    -keepclassmembers class com.sohu.cyan.android.sdk.activity.ShareActivity {
        public *;
    }

    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
    #保护指定的类文件和类的成员
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }


    #retrofit
    -dontwarn okio.**
    -dontwarn javax.annotation.**
