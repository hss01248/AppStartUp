package com.hss01248.test.compiler;

import com.google.auto.service.AutoService;
import com.hss01248.classnametoassets.base.compiler.AbstractWriteClassNamesToAssetsProcessor;
import com.hss01248.test_annotation.AppStartUpItem3333;

import java.lang.annotation.Annotation;

import javax.annotation.processing.Processor;

/**
 * @Despciption todo
 * @Author hss
 * @Date 09/08/2022 10:49
 * @Version 1.0
 */
@AutoService(Processor.class)
public class Test2Processor extends AbstractWriteClassNamesToAssetsProcessor {
    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return AppStartUpItem3333.class;
    }
}
