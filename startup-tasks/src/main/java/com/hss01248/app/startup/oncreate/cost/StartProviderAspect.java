package com.hss01248.app.startup.oncreate.cost;



/**
 * @Despciption todo
 * @Author hss
 * @Date 24/01/2022 17:34
 * @Version 1.0
 */
//@Aspect
public class StartProviderAspect {

//android.content.ContentProvider+.onCreate(*)

    //execution(* android.content.ContentProvider+.onCreate(..)) ||
    //@Around(" execution(* androidx.startup.Initializer+.create(..))")
    /*public void weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object xx = LogMethodAspect.logAround(true, "ProviderAs", true,joinPoint, new IAround() {
            @Override
            public void onResult(ProceedingJoinPoint joinPoin, Object result, String desc, long cost) {
                try {
                    DispatchRunnable.costMapp.put(cost,"startup-"+joinPoint.getThis().getClass().getSimpleName());
                }catch (Throwable throwable){
                    throwable.printStackTrace();
                }
            }
        });
    }*/

}
