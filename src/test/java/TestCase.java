

import Utils.AssertResults;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static Utils.ConfigConstants.SHOW_TEST_RESULTS;
import static Utils.ConfigConstants.SKIPPED_TESTS;
import static Utils.SignConstants.TEST_DELIMITER;

/**
 * Created by Andrei Filip on 7/8/19.
 */
public class TestCase {
    private List<String> steps;
    private static final Logger log = Logger.getLogger(TestCase.class);
    private String testTitle;
    private String testID;
    private String testResult;
    private boolean isPassed;
    private List<AssertResults> results;
    private String testSteps;
    private boolean shouldPass;
    private static TestCase instance = null;
    private int expectedMsgNr;
    private JSONArray apiDetails;
    private boolean isSkipped = false;


    public static TestCase getInstance() {
        if (instance == null) {
            instance = new TestCase();
        }
        return instance;
    }
    public void reset() {
        resetVariables();
    }
    public void addApiResult(JSONObject result) {
        apiDetails.add(result);
    }
    public String getTestID() {
        return testID;
    }
    public boolean isPassed() {
        return isPassed == shouldPass;
    }
    public void shouldPass(boolean shouldPass) {
        this.shouldPass = shouldPass;
    }
    public String getTestResult() {
        return testResult;
    }

    /**
     * Add test result
     * @param msg (String) assert details
     * @param expected (Object) expected result
     * @param actual (Object) actual result
     * @param compareResult (String) compare result
     * @param status (boolean) true if passed
     * @return  (AssertResults)
     */
    public AssertResults addAssertResults(String msg, Object expected, Object actual, String compareResult, boolean status) {
        AssertResults ar = new AssertResults(msg, status, expected, compareResult, actual);
        addResult(ar);
        return ar;
    }

    /**
     * Add result to results list
     * @param r (AssertResult)
     */
    public void addResult(AssertResults r) {
        results.add(r);
        setResults();
    }

    /**
     * Add test step
     * @param step (String)
     */
    public void addTestStep(String step) {
        String s = step.contains("Expected") ? step : (steps.size() + 1 - expectedMsgNr) + ": " + step;
        boolean stepExists = stepExists(step);
        if (!stepExists) {
            if (step.contains("Expected")) {
                expectedMsgNr++;
            }
            steps.add(s);
        }
    }

    /**
     * @return (String) Test Title
     */
    public String getTestTitle() {
        return testTitle;
    }

    /**
     * @return (String) test steps
     */
    public String getTestSteps() {
        testSteps = "\nTest steps:\n\n";
        for (String step : steps) {
            this.testSteps += step + "\n\n";
        }
        return testSteps;
    }

    /**
     * Compare results; check if tests is passed
     */
    public void checkResults() {
        isSkipped = isTestSkipped(testID) || testID.contains("skipped");
        if (!isSkipped) {
            Assert.assertTrue(isPassed(), "(" + testID + ") " + testTitle + ":" + "\n" + getTestResult());
        }
    }

    /**
     * Create a new instance of testCase
     */
    private TestCase() {
        resetVariables();
    }

    /**
     * @return (boolean) true if step exist in list of steps
     */
    private boolean stepExists(String step) {
        for (String s : steps) {
            if (s.contains(step)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reset testCase instance
     */
    private void resetVariables() {
        steps = new ArrayList<>();
        results = new ArrayList<>();
        apiDetails = new JSONArray();
        testTitle = "";
        testID = "";
        testResult = "";
        isPassed = true;
        shouldPass = true;
        testSteps = "";
        expectedMsgNr = 0;
    }

    /**
     * Create test results to be posted in testRail
     */
    private void setResults() {
        JSONObject data = new JSONObject();
        int i = 0;
        for (AssertResults r : results) {
            if (r != null) {
                JSONObject assestionData = new JSONObject();
                String mismatch = r.isPassed() ? "" :
                        r.getMismatch().replaceAll("\n", ", ").replaceAll(" ; ", " ||| ").replaceAll(" +", " ").trim();
                assestionData.put("status", r.isPassed() ? ">>>PASSED<<<" : ">>>FAILED<<<");
                assestionData.put("mismatch", "[" + mismatch + "]");
                assestionData.put("expected", r.getExpected());
                assestionData.put("actual", r.getActual());
                data.put("ASSERT#" + i + " (" + r.getAssertTitle() + ")", assestionData);
                if (isPassed) {
                    isPassed = r.isPassed();
                }
            } else {
                data.put("error", "no_assertion");
                isPassed = false;
            }
            i++;
        }
        testResult = data.toString(4).replace("\"", "");
    }

    /**
     * Log test results
     */
    public void printAssertResults() {

        log.info("\n--------------<Assert Results>---------------");
        for (AssertResults assertR : results) {
            log.info(">>>" + assertR.getAssertTitle());
            log.info("[EXPECTED] " + assertR.getExpected());
            log.info("[ACTUAL] " + assertR.getActual() + (assertR.isPassed() ? "\n" : ""));
            if (assertR.isFailed()) {
                log.info("[MISMATCH] " + assertR.getMismatch() + "\n");
            }
        }
    }

    /**
     * Log Api details
     */
    public void printApiDetails() {
        log.info("\n--------------<API Details>---------------");
        for (Object api : apiDetails) {
            JSONObject details = JSONObject.fromObject(api);
            log.info("URL: " + details.getString("url") + " - STATUS:" + details.getString("status"));
            log.info("PAYLOAD: " + details.getString("payload"));
            log.info("RESPONSE: " + details.getString("response") + "\n");
        }
    }

    /**
     * Log test status
     */
    public void printStatus() {
        log.warn("TEST: (" + getTestID() + ")" + getTestTitle() + "--> " + (isSkipped?"Skipped(failed)":isPassed()?"Passed":"Failed")+"\n");
        if (isPassed()) {
            if(SHOW_TEST_RESULTS){ printAssertResults();}
        } else if (!isSkipped) {
            printTestSteps();
            printAssertResults();
            printApiDetails();
            log.info(TEST_DELIMITER);
        }

    }

    /**
     * Log test steps
     */
    public void printTestSteps() {
        log.info( testID + ": " + testTitle + "\n" + getTestSteps());
    }

    /**
     * Set testCase title and test id
     * @param testID (String)
     * @param testTitle (String)
     */
    public void setTestCase(String testID, String testTitle) {
        this.testID = testID;
        this.testTitle = testTitle;
    }

    /**
     * Verify if test is skipped
     * @param testID (String)
     * @return (boolean) true if test is skipped
     */
    private static boolean isTestSkipped(String testID) {
        for (String t : SKIPPED_TESTS) {
            if (t.equals(testID)) {
                return true;
            }
        }
        return false;
    }

    public static Object[][] filter(Object[][] testData) {
        List<Object[]> result = new ArrayList<>();
        for (Object[] test : testData) {
            if (!TestCase.isTestSkipped((String)test[0]))
                result.add(test);
//            if ((test[0]).equals("22649924"))
//                result.add(test);
        }
        return result.toArray(new Object[][]{});
    }

}
