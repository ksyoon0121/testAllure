package cn.kxy.base.business;

import cn.kxy.homepage.business.LoginBusiness;
import com.alibaba.fastjson.JSONPath;


public class TokenData {
    private static String token; //token用static
    public TokenData(){
    }
    public static String getMangerToken(){
        if (token==null || token.isEmpty()){  //isE报错时切换module和project为lambada8，因为languagelevel不匹配
            String response= LoginBusiness.loginCoolColleague(System.getProperty("loginMobile"),System.getProperty("password"));
            token=String.valueOf(JSONPath.read(response,"$.data.action_token"));
        }else{
            token=System.getProperty("token");
        }
        return token;
    }
}
