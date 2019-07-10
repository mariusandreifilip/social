package Utils;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplacementProperties extends Properties {
    private static final long serialVersionUID = 1L;

    private static final String PROPERTY_REGEX = "\\$\\{((?:\\\\\\{|\\\\\\}|[^\\{\\}])+)(?<!\\\\)\\}";
    private static final String CLEANER_REGEX = "\\\\(\\{|\\}|\\\\)";

    private static final String WINDOWS_PATH_REGEX = "\\\\";

    private static final Logger log = Logger
        .getLogger(ReplacementProperties.class.getName());

    private Pattern pWindowsPath = Pattern
        .compile(ReplacementProperties.WINDOWS_PATH_REGEX);

    public ReplacementProperties(File inputFile) throws IOException {
        BufferedInputStream bis = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(inputFile);
            bis = new BufferedInputStream(fis);

            super.load(bis);
        }
        finally {
            if (bis != null) {
                bis.close();
            }

            if (fis != null) {
                fis.close();
            }
        }
    }

    @Override
    public String getProperty(String key) {
        String value;

        value = System.getProperty(key);

        if (value == null) {
            value = super.getProperty(key);
        }

        return (value == null ? null : this.expandVariables(value));
    }

    /**
     * Akin to {@link Properties#store(OutputStream, String)}, but expands
     * variables before writing. Useful for passing to a properties-loader which
     * does not support variable-expansion, such as
     * {@link java.util.logging.LogManager#readConfiguration(java.io.InputStream)}
     * .
     * 
     * @param out
     *            - where to store the properties
     * 
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public void storeWithVariableExpansion(OutputStream out)
        throws UnsupportedEncodingException, Exception {
        this.storeWithVariableExpansion(new BufferedWriter(
            new OutputStreamWriter(out, "8859_1")), true);
    }

    /**
     * Expand all variables of the form ${MyFoo}
     */
    private String expandVariables(String value) {

        //
        // Can be called recursively, create new matchers for each pass.
        //
        Matcher mVariable = Pattern.compile(
            ReplacementProperties.PROPERTY_REGEX).matcher("");
        Matcher mCleaner = Pattern.compile(ReplacementProperties.CLEANER_REGEX)
            .matcher("");

        boolean haveVariables = true;

        while (haveVariables) {
            mVariable.reset(value);

            //
            // TODO
            // FIXME
            // "Matcher.find()" triggers infinite loop if value string is a key
            // ${My.Foo.Var} which does not exist
            if (mVariable.find()) {
                StringBuffer buf = new StringBuffer();

                do {
                    String variable = mVariable.group(1);

                    String subVariable = this.getProperty(variable);

                    if (subVariable != null) {
                        int idx = subVariable.indexOf('$');

                        if (idx > -1) {
                            do {
                                subVariable = subVariable.substring(0, idx)
                                    + "\\" + subVariable.substring(idx);
                            }
                            while ((idx = subVariable.indexOf('$', idx + 2)) > -1);
                        }

                        if (subVariable.contains("\\")) {
                            Matcher mWindowsPath = this.pWindowsPath
                                .matcher(subVariable);

                            subVariable = mWindowsPath.replaceAll("/");
                        }

                        mVariable.appendReplacement(buf, subVariable);
                    }
                    else {
                        ReplacementProperties.log
                            .warning("No match for key '" + variable
                                + "' derived from value '" + value + "'");

                        // Else stuck in current loop
                        mVariable.appendReplacement(buf, "");
                    }
                }
                while (mVariable.find());

                mVariable.appendTail(buf);
                value = buf.toString();
            }
            //
            // All variables have been replaced.
            //
            else {
                mCleaner.reset(value);

                if (mCleaner.find()) {
                    StringBuffer finalbuf = new StringBuffer();

                    do {
                        mCleaner.appendReplacement(finalbuf, mCleaner.group(1));
                    }
                    while (mCleaner.find());

                    mCleaner.appendTail(finalbuf);
                    value = finalbuf.toString();
                }

                haveVariables = false;

            }
        }

        return value;
    }

    private void storeWithVariableExpansion(BufferedWriter bw,
        boolean escUnicode) throws Exception {
        //
        // Would prefer to avoid reflection, but the only other solution is to
        // copy-n-paste a bunch of code from the Properties class.
        //
        Method method = Properties.class.getDeclaredMethod("saveConvert",
            String.class, boolean.class, boolean.class);

        method.setAccessible(true);

        synchronized (this) {
            for (Enumeration<?> e = keys(); e.hasMoreElements();) {
                String key = (String) e.nextElement();
                String val = (String) get(key);

                val = this.expandVariables(val);

                key = (String) method.invoke(this, key, true, escUnicode);

                //
                // No need to escape embedded and trailing spaces for value,
                // hence pass false to flag.
                //
                val = (String) method.invoke(this, val, false, escUnicode);

                bw.write(key + " = " + val);
                bw.newLine();
            }
        }

        bw.flush();
    }
}
