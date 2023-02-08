package com.lazy.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPatch;
import com.alibaba.fastjson.JSONPath;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ResourceFileUtil {
    public JSONObject parseJsonFile(String folder,String fileName){
        String fileContent=readSingleFile(folder,fileName);
        JSONObject jsonObject=JSONObject.parseObject(fileContent);
        return jsonObject;
    }
    public String readSingleFile(String folder,String fileName){
        InputStream inputStream=getInputStream(folder,fileName);
        StringBuilder stringBuilder=new StringBuilder();
        try(InputStreamReader streamReader=new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader=new BufferedReader(streamReader)){
            String line;
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line).append("\n");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return stringBuilder.toString();
    }
    public InputStream getInputStream(String folder,String fileName){
        String wholePath=folder+'/'+fileName;
        ClassLoader classLoader=getClass().getClassLoader();
        InputStream inputStream=classLoader.getResourceAsStream(wholePath);
//        Class.getResource(String name)：在当前调用类的同一路径下查找该资源
//        ClassLoader.getResource(String name)：在根目录下查找该资源文件，即"/"或classpath目录，name不能有/
//        if(inputStream == null) {
//            System.out.println("get file input stram from API-test-core resource file");
//            System.out.println(wholePath);
//            try {
//                inputStream = new FileInputStream(classLoader.getResource(wholePath).getFile());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return inputStream;
    }

    public static JSONObject setJsonBodyValue(JSONObject requestBody,Object...parameters){
        if(parameters==null||parameters.length%2!=0){
            System.err.println("body parameters %2！=0 ");
            return null;
        }
        for(int i=0;i<parameters.length;i+=1){
            JSONPath.set(requestBody,String.valueOf(parameters[i]),parameters[++i]);
        }
       return requestBody;
    }

    public FileOutputStream getOutPutStream(String folder, String fileName) {
        String wholeFilePath = folder + "/" + fileName;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(wholeFilePath).getFile());
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
