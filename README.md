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

```groovy
api project(":startup-api")
annotationProcessor project(":startup-compile")
//稍后改成远程
```



//java代码里加注解,实现任一初始化回调

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





# todo: 使用注解处理器实现依赖注入功能-findbyType