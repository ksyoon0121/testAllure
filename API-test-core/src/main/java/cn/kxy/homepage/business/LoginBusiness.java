package cn.kxy.homepage.business;

import cn.kxy.base.business.EnterpriseData;
import com.lazy.assured.utils.RestAssuredRequestHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginBusiness {
    public static String login_url = "/login-api/v3/login";
    public static String loginCoolColleague(final String loginMobile, final String password){
        //HashMap无序,LinkedHashMap有序,可分为插入顺序和访问顺序两种
        // enterpriseId为String，buildurl中requestbody为String,object
        Map requestBody=new LinkedHashMap<String, String>(){{
            put("mobile",loginMobile);
            put("passwoed",password);
            put("password_encrypted", "0");
            put("login_type", "mobile_password_login");
            put("enterprise_id", EnterpriseData.getEnterpriseId());
            put("sms_code", "");
        }};
        RestAssuredRequestHandler requestHandler=new RestAssuredRequestHandler(false);
        return requestHandler.sendPostRequest(RestAssuredRequestHandler.buildURL(login_url),requestBody);
    }
}
