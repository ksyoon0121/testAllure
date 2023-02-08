package cn.kxy.base.business;

public class EnvironmentData {
    public static String host;
    public static String getHost(){
        if(host==null){
            String env=System.getProperty("env");
            switch(env){
                case("PRO"):
                    host="https://coolapi.coolcollege.cn";
                    break;
                case("T"):
                    host="https://jccoolapi.coolcollege.cn";
                    break;
                default:
                    host="https://ct6coolapi.coolcollege.cn";
                    break;
            }

        }
        return host;
    }
}
