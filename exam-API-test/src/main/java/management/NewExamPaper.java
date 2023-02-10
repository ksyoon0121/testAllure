package management;

import cn.kxy.base.business.EnterpriseData;
import cn.kxy.setting.business.ClassificationBusiness;
import com.alibaba.fastjson.JSONObject;
import com.lazy.assured.utils.RestAssuredRequestHandler;
import com.lazy.common.utils.ResourceFileUtil;

public class NewExamPaper {
    private RestAssuredRequestHandler requestHandler; //module之间的引用要在pom中加依赖
    final JSONObject newExamUrl=new ResourceFileUtil().parseJsonFile("url","newExamPaperURL.json");
    private final String classificationName = ClassificationBusiness.getPrimaryName();
    private final String classificationId = ClassificationBusiness.getPrimaryId();
    private final String requestBodyFolder = "requestbody/newExamPaper/newExampaper";

    public NewExamPaper(){
       requestHandler=new RestAssuredRequestHandler(true);
    }

    public String addNewPaper(String title) {
        System.out.println(title);
        String addNewPaperURL=RestAssuredRequestHandler.buildURL(newExamUrl.getString("addNewExamPaper"), EnterpriseData.getEnterpriseId());
        JSONObject requestBody=ResourceFileUtil.setJsonBodyValue(new ResourceFileUtil().parseJsonFile(requestBodyFolder,"addNewExamPaper.json"),
                "$.title", title,
                "$.classify_id", classificationId,
                "$.classify_name", classificationName);
        return requestHandler.sendPostRequest(addNewPaperURL,requestBody);
    }
}
