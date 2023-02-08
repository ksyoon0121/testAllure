package com.lazy.assured.utils;
import cn.kxy.base.business.EnvironmentData;
import cn.kxy.base.business.TokenData;
import io.restassured.RestAssured;  //得在pom中加入依赖，才能有这个包
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RestAssuredRequestHandler {
    private final int COMMON_SUCCESS_CODE = 200; //常量，为了防止数据在方法体中被修改
    private Map<String,String> requestHeader;
    private String token;

//    public RestAssuredRequestHandler(){
//        requestHeader=new HashMap<String, String>(){{
//            //双大括号初始化/匿名内部类初始化法，本质是匿名内部类+实例化代码块
//            // 首先第一层花括号定义了一个继承了HashMap的匿名内部类
//            //第二层花括号其实是这个匿名内部类的非静态初始化块，块中可以直接调用父类的非私有方法，这里调用了HashMap类中的put()方法给这个匿名内部类初始化赋值。
//            token = TokenData.getMangerToken();
//            put("content-type","application/json");
//            put("x-access-token",token);
//        }};
//    }

    public RestAssuredRequestHandler(boolean isRequireToken){
        requestHeader = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }};
        if (isRequireToken){
            token=TokenData.getMangerToken();
            requestHeader.put("x-access-token", token);
        }
    }

    public String sendGetRequest(String url, String... queryParameters) {
        RequestSpecification request = RestAssured.given();
        request.config((RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation()))).headers(requestHeader);

        if(queryParameters != null && queryParameters.length > 0) {
            Map<String, Object> parameterMap = formatParameters(queryParameters);
            request.queryParams(parameterMap);
        }

        Response response = request.get(url);
        verifyStatusCode(response, COMMON_SUCCESS_CODE);
        return response.body().asString();
    }

    //String... args 表示的是一个可变长度的参数列表，表示此处接受的参数为0到多个Object类型的对象，或者是一个Object[]
//    public String sendGetRequest(String url, String...queryParameters) {
//        RequestSpecification request=RestAssured.given();
//        request.config(RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())).headers(requestHeader);
//    }

    public String sendPostRequest(String url,Map<String,Object> requestBody, String... queryParams){
        RequestSpecification request=RestAssured.given();
        request.config(RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation())).headers(requestHeader);;
        if(queryParams!=null && queryParams.length>0){
            request.queryParams(formatParameters(queryParams));
        }
        Response response=(requestBody==null)?request.post(url):request.body(requestBody).post(url);
        verifyStatusCode(response,COMMON_SUCCESS_CODE);
        return response.body().asString();
    }

    public Map<String, Object> formatParameters(String...params){
        if(params==null || params.length%2!=0){
            System.err.println("The parameter format is wrong!It should be like: parameter1, value1, parameter2, value2...");
            return null;
        }
        Map<String,Object>  paramMap=new LinkedHashMap<String, Object>();
        for(int i=0;i<params.length;i++){
            paramMap.put(params[i],params[++i]);
            System.out.println(paramMap);
        }
        return  paramMap;
    }

    public void verifyStatusCode(Response response,Integer successCode){
        try{
            response.then().statusCode(successCode);    //then是断言
        }catch(AssertionError error){
            response.body().prettyPrint();             //Get and print the JSON as a prettified string
            throw error;
        }
    }

    public static String buildURL(String baseURL,String... args ){
        baseURL = EnvironmentData.getHost() + baseURL;
        if (args == null) {
            System.err.println("The arguments format is wrong!It should be like: parameter1, value1, parameter2, value2...");
            return null;
        }
        String result = baseURL;
        if(args.length>0){
            String regex = "(\\{+(\\w|-|_)+}+)";
            // (\{+(\w|-|_)+}+)
            //在idea string中反转字符要再加一个反转号
            // ( )	标记一个子表达式的开始和结束位置。
            // +	匹配前面的子表达式一次或多次。
            // \w:用于匹配字母,数字或下划线字符 +为至少出现1个以上字符
            // {	标记限定符表达式的开始。要匹配 {，请使用 \{。要匹配 {，请使用 \{。
            // |	指明两项之间的一个选择。要匹配 |，请使用 \|。
            // \将下一个字符标记为或特殊字符、或原义字符、或向后引用、或八进制转义符。例如， 'n' 匹配字符 'n'。'\n' 匹配换行符。序列 '\\' 匹配 "\"，而 '\(' 则匹配 "("。
            //正则表达式里的()表达式，必须能完全匹配才行 enterprise-id中的-用-匹配，字母用w匹配
            for (String arg:args){
                result=result.replaceFirst(regex,arg);
            }
        }

        return result;

    }
}