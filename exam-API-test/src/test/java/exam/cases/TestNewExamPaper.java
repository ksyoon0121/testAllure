package exam.cases;

import com.alibaba.fastjson.JSONPath;
import com.lazy.common.utils.DataDrivenLoader;
import management.NewExamPaper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class TestNewExamPaper {
    private NewExamPaper newExamPaper;
    private String paperId;

    @BeforeClass
    public void setUp() {
        System.out.println("run test of TestNewExamPaper");
        newExamPaper = new NewExamPaper();
    }


    @DataProvider
    public Object[][] simpleDataProvider() {
        Object[][] provider = new Object[2][1];
        provider[0][0] = "Automation1";
        provider[1][0] = "Automation2";
        return provider;
    }

    @DataProvider
    public Object[][] excelDataProvider(Method method) {
        System.out.println(method.getDeclaringClass().getName());
        System.out.println(method.getName());

        Object[][] testData = DataDrivenLoader.loadExcelData("TestNewExamPaperData.xlsx", method.getDeclaringClass().getName(), method.getName());
        return testData;
    }
//测试用例组的顺序是：先执行被依赖的组的方法用例，再执行没有依赖关系的 最后执行需要依赖关系的用例方法；
    @Test(description = "add new exam paper", dataProvider ="excelDataProvider", priority = 1)
    public void testAddNewExamPaper(String paperTitle,String test) {
        String response = newExamPaper.addNewPaper(paperTitle);
        paperId = JSONPath.read(response, "$.data").toString();
        Assert.assertEquals("true", JSONPath.read(response,"$.success").toString());
        System.out.println("paper title: " + paperTitle);
        System.out.println(test);
        System.out.println("paper ID: " + paperId);
        DataDrivenLoader.updateTestData("TestNewExamPaperData.xlsx", "exam.cases.TestNewExamPaper", "testGetNewExamPaper", "paperId", paperId);
//        DataDrivenLoader.updateTestData("TestNewExamPaperData.xlsx", "newexam.cases.TestNewExamPaper", "testDeleteNewExamPaper", "paperId", paperId);
    }

    @Test(description = "get new exam paper detail", dataProvider = "excelDataProvider", dependsOnMethods = "testAddNewExamPaper", priority = 2)
    public void testGetNewExamPaper(String paperTitle, String paperId) {
        System.out.println(paperId);
//        String response = newExamPaper.getNewPaperDetail(paperId);
//        Assert.assertEquals(JSONPath.read(response, "$.data.id").toString(), paperId);
//        Assert.assertEquals(JSONPath.read(response, "$.data.title").toString(), paperTitle);
//        System.out.println(paperId);
    }

}
