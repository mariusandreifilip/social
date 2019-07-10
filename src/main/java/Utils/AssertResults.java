/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import net.sf.json.JSONObject;

/**
 *
 * @author andrei Filip
 * Assert result details:expected, actual, mismach, assert details
 */
public class AssertResults {
    private boolean passed;
    private Object expected;
    private String mismatch;
    private String assertTitle;
    private Object actual;

    public AssertResults(String assertTitle, boolean passed, Object expected, String mismatch) {
       this(assertTitle, passed, expected, mismatch,null);
    }
    public AssertResults(boolean passed) {
        this(null, passed, null, null,null);
    }
    public AssertResults(String assertTitle, boolean passed, Object expected, String mismatch, Object actual) {
        this.passed = passed;
        this.expected = expected;
        this.mismatch = mismatch;
        this.assertTitle = assertTitle;
        this.actual = actual;
    }
    public boolean isPassed() {
        return passed;
    }
    public Object getActual() {
        return actual;
    }
    public JSONObject getActualInJSON(){
        return JSONObject.fromObject(actual);
    }
    public boolean isFailed() {return !passed; }
    public String getAssertTitle() {
        return assertTitle;
    }
    public void setStatus(boolean status) {
        this.passed = status;
    }
    public Object getExpected() {
        return expected;
    }
    public void setExpected(JSONObject expected) {
        this.expected = expected;
    }
    public String getMismatch() {
        return mismatch;
    }
    public void setMismatch(String mismatch) {
        this.mismatch = mismatch;
    }

}
