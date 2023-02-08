package cn.kxy.setting.business;

import cn.kxy.base.business.EnterpriseData;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lazy.assured.utils.RestAssuredRequestHandler;
import com.lazy.common.utils.ResourceFileUtil;

public class ClassificationBusiness {

	private final static JSONObject classificationURLObject = (new ResourceFileUtil()).parseJsonFile("url/knowledgeAPI", "classificationURL.json");
	
	// 查询分类

	public static String queryClassification() {
		String getClassificationListURL = RestAssuredRequestHandler.buildURL(classificationURLObject.getString("getClassificationList"), EnterpriseData.getEnterpriseId());
		RestAssuredRequestHandler requestHandler = new RestAssuredRequestHandler(true);
		return requestHandler.sendGetRequest(getClassificationListURL, "auth_type", "edit", "parent_id", "0", "include_unclassified", "false");

	}
	static String  body =ClassificationBusiness.queryClassification();
	//获取一级分类的name
	public static String getPrimaryName() {
		String body =ClassificationBusiness.queryClassification();
		String name=(String) JSONPath.read(body, "$.list[0].name");
		return name;
	}

	//获取一级分类的id
	public static String getPrimaryId() {
		String body = ClassificationBusiness.queryClassification();
		String id = JSONPath.read(body, "$.list[0].id").toString();
		return id;
	}

}
