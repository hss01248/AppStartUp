package com.hss01248.login.status;


import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public interface LoginLogoutObserver<T>  extends DefaultLifecycleObserver {

    void login(T userDetail);

    void logout(boolean isFromLoginPage);

    @Override
    default void onDestroy(@NonNull LifecycleOwner owner) {
        try {
            if(LoginStatusUtil.onceObservers.contains(this)){
                LoginStatusUtil.onceObservers.remove(this);
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        try {
            if(LoginStatusUtil.observers.contains(this)){
                LoginStatusUtil.observers.remove(this);
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }
}
