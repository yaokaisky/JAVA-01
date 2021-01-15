package com.sky.cloud.studyeureka;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Base64;
public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            final String className = "Hello";
            final String methodName = "hello";
            Object instance = new HelloClassLoader().findClass(className).newInstance(); // 加载并初始化Hello类
            Method method = instance.getClass().getMethod(methodName);
            method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
//        String resourcePath = name.replace(".", "/");
        // 文件后缀
        final String suffix = ".xlass";

        String outPath = System.getProperty("user.dir") + File.separator;
        InputStream inputStream = null;
        String fName =  outPath + name + suffix;
        System.out.println(fName);
        File file = new File(fName);
        try{
            if(file.exists()){
                inputStream = new FileInputStream(file);
            }else {
                inputStream = this.getClass().getClassLoader().getResourceAsStream(fName);
            }
            if(inputStream == null){
                throw new RuntimeException("加载文件异常");
            }
            int length = inputStream.available();
            byte[] bytesArr = new byte[length];
            inputStream.read(bytesArr);
            byte[] classBytes = decode(bytesArr);
            return defineClass(name,classBytes,0,classBytes.length);
        }catch (Exception e){
            throw new ClassNotFoundException(name, e);
        }finally {
            close(inputStream);
        }
    }

    public byte[] decode(String base64){
        return Base64.getDecoder().decode(base64);
    }

    private static byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) (255 - byteArray[i]);
        }
        return targetArray;
    }

    private static void close(Closeable res) {
        if (null != res) {
            try {
                res.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
