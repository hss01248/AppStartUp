package com.hss01248.startup.compile;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * @Despciption todo
 * @Author hss
 * @Date 23/07/2022 16:23
 * @Version 1.0
 */
public class ClassCodeGen {

    //生成java类，及相应的方法
    public static TypeSpec generateJavaCode(List<String> classNames, String className){
        return TypeSpec.classBuilder("AppStartUpItems")
                .addModifiers(Modifier.PUBLIC) //public修饰
                .addMethod(generateMethod(classNames)) //添加方法
                .build();
    }
     static MethodSpec generateMethod(List<String> classNames){
        //获取类名
        return MethodSpec.methodBuilder("callbackClassNames")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(ArrayList.class)
                //.addParameter(className,ParamName)
                .addCode(generateMethodCode(classNames))
                .build();
    }

     static List<String> callbackClassNames(){
        List<String> strings = new ArrayList<>();
        strings.add("xxxxx");
        return strings;
    }
    private static String generateMethodCode(List<String> classNames) {
        StringBuilder code = new StringBuilder();
        code.append("ArrayList<String> strings = new ArrayList<>();\n");
        for (String clazz : classNames) {
            String findViewCode = " strings.add(\""+clazz + "\");\n";
            code.append(findViewCode);
        }
        code.append(" return strings;");
        return code.toString();
    }

    /*作者：Coolbreeze
    链接：https://juejin.cn/post/7001827533440172068
    来源：稀土掘金
    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。*/
}
