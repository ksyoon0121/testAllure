package cn.kxy.base.business;

public class EnterpriseData {
    static String id="";
    public static String getEnterpriseId(){

        String env=System.getProperty("env");
        switch (env){
            case("PRO"):
                id="1067985194709028888";
                break;
            case("T"):
                id="951057547274620933";
                break;
            default:
                id="951057547274620933";
                break;
        }
        return id;
    }
}
