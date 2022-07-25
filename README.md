# app 启动工具

# app启动的几个可插入点以及执行顺序

### 1 application的attachBaseContext

### 2 contentProvider的attachInfo和OnCreate

可以通过manifest里的initOrder控制不同contentProvider的执行顺序,值越大,越先执行

### 3 androidx的startup库

本质上是通过manifest将初始化代码归拢到同一个initOrder=0的contentProvider

需要在manifest里声明,稍显繁琐.

### 4 application的onCreate方法

### 5 第一个activity的onCreate

很多后台进程启动走不到这里,因为没有界面.

# 本工具作用:

将以上的流程归拢到一个接口,利用注解自动处理.

```java
public interface AppStartUpCallback {

    default void onFirstProviderInit(Application app){}

    default void onAndroidXStartUpInit(Application app){}

    default void onBeforeApplicationOnCreate(Application app){}

    default void onFirstActivityCreated(Application app, Activity activity, Bundle savedInstanceState){}


   default int orderOfFirstProviderInit(){
       return 0;
   }

    default int orderOfAndroidXStartUpInit(){
       return 0;
    }

    default int orderOfBeforeApplicationOnCreate(){
       return 0;
    }

    default int orderOfFirstActivityCreated(){
       return 0;
    }
}
```

# 使用方式:

[![](https://jitpack.io/v/hss01248/AppStartUp.svg)](https://jitpack.io/#hss01248/AppStartUp)

```groovy
//api project(":startup-api")
//annotationProcessor project(":startup-compile")

api "com.github.hss01248.AppStartUp:startup-api:1.0.3"
annotationProcessor "com.github.hss01248.AppStartUp:startup-compile:1.0.3"

//额外功能: application oncreate的有向无环图启动
api "com.github.hss01248.AppStartUp:startup-tasks:1.0.3"
```



//java/kotlin代码里加注解,实现任一初始化回调

```java
@AppStartUpItem
public class MyStartup3 implements AppStartUpCallback {

    @Override
    public void onAndroidXStartUpInit(Application app) {
        AppStartUpCallback.super.onAndroidXStartUpInit(app);
        LogUtils.w("onAndroidXStartUpInit---MyStartup3");
    }

    @Override
    public int orderOfAndroidXStartUpInit() {
        return -2;
    }
}
```



# 原理

> 仅依赖于注解处理器,不需要asm,gradle plugin

通过注解找到AppStartUpCallback的实现类的类名

然后将类名作为文件名写到当前module的src/main/assets/startupclasses/目录下,比如:

```
/Users/hss/github2/AppStartUpDemo/app/src/main/assets/startupclasses/com.hss01248.appstartup.demo.MyStartup2
```

这个空文件会随着打包带到最终apk中

那么在app启动的最开始,去assets/startupclasses/目录下读取文件列表

将文件名反射拿到class,创建对象,加入到AppStartUpCallback的list, 后续各阶段的初始从这个list里取对象调用即可.



### 获取module路径的代码:

```java
       final FileObject fo = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", name.toString());
                String temFilePath = fo.toUri().getPath();
                System.out.println("--------->AppStartUpItem:  temFilePath " + temFilePath);
                //java apt
//Users/hss/github2/AppStartUpDemo/testlib/build/intermediates/javac/debug/classes/xxx
                //kotlin apt
//Users/hss/aku/module-offline-pay/Module-Offline-Pay/build/tmp/kapt3/classes/debug/xxx
                int idx = temFilePath.indexOf("/build/intermediates/");
                if(idx < 0){
                    idx = temFilePath.indexOf("/build/tmp/");
                }
                String outputPath = temFilePath.substring(0, idx);
```





# todo: 使用注解处理器实现依赖注入功能-findbyType