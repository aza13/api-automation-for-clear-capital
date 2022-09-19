import constants.FilePathNames;
import helper.TestDataUsaHelper;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelDataHandler;

import java.lang.reflect.Method;
import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestDataUSA {


   private TestDataUsaHelper testDataUsaHelper;
   private static Logger logger = Logger.getLogger(TestDataUSA.class);

   @BeforeClass(alwaysRun = true)
    public void init() {
       testDataUsaHelper = new TestDataUsaHelper();
   }

   @DataProvider(name = "DataProviderForTestDataUsa")
   public Object[][] dataForTestDataUsa(Method method) throws Exception
   {
      String testMethod = method.getName();
      logger.info("testDataUsa is choosing Test method as - "+testMethod);
      String excelPath =  FilePathNames.EXCEL_TEST_DATA ;
      String sheetName = null;
      if(testMethod.equals("testDataUsa")) {
         sheetName = FilePathNames.GET_DATA_BY_STATE;
      }

      else
         logger.error("Invalid test method in testDataUsa");

      ExcelDataHandler excel = new ExcelDataHandler(excelPath, sheetName);
      return excel.getTableDataInMap();
   }

   @Test(dataProvider = "DataProviderForTestDataUsa", description = "Test get GetData")
    public void testDataUsa(Map<Object, String> map) {
      Response response = testDataUsaHelper.getDataUSA();

      int count = response.jsonPath().getList("data").size();
      for (int i = 0; i < count; i++) {
         String states = response.jsonPath().getString("data[" + i + "].State");
         String arr [] = states.split(" ");
         Set<String> uniqueStates = new HashSet<>(Arrays.asList(arr));
         if (uniqueStates.size() == arr.length) {
            System.out.println("There are no duplicate states");
         }else {
            System.out.println("There are duplicate states");
         }
      }


      for (int i = 0; i < count; i++) {
         String years = response.jsonPath().getString("data[" + i + "].Year");
         String [] arrYears = years.split(" ");
         Set<String> uniqueYears = new HashSet<>(Arrays.asList(arrYears));
         if (uniqueYears.size() ==1) {
            System.out.println("Same year");
         }


      }

   }

}
