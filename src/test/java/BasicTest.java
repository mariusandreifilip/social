
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;


/**
 * Created by Andrei Filip on 6/8/19.
 */

public class BasicTest {

    protected static final Logger log = Logger.getLogger(BasicTest.class);
    public TestCase testCase;



    @BeforeClass
    public void setUpClass() throws Exception {

    }

    @BeforeMethod
    public void setUp() throws Exception {
        testCase = TestCase.getInstance();



    }

    @AfterMethod
    public void tearDown() throws Exception {
        testCase.reset();


    }

    @AfterClass
    public void tearDownClass() throws Exception {

    }

    public void setupTestData() throws Exception {


    }

    public void resetTestData() throws Exception {

    }

    protected void logSuiteName(String name){
        log.info("____________________________START "+name+" ____________________________");
    }


}
