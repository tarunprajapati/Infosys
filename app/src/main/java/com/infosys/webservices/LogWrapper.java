package com.infosys.webservices;

import android.util.Log;

/**
 * Created by radhika on 5/9/15.
 */
public class LogWrapper {

    static String className;
    static String fullClassName;
    static String methodName;
    static int lineNumber;
    static String positionMsg;
    static String mode = "1";

    private static void update(StackTraceElement[] sElements) {
        // Examine the method in which LogWrapper was used.
        fullClassName = sElements[1].getClassName();
        className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();

        // Construct a "clickable" message.
        // This is the important thing.
        positionMsg = fullClassName + "." + methodName + "(" + className
                + ".java:" + lineNumber + ")";
    }

    public static void v(String tag, Object message) {
        update(new Throwable().getStackTrace());
        Log.v(tag, message.toString());
        Log.v(tag, "at" + positionMsg);
    }

    public static void d(String tag, Object message) {
        update(new Throwable().getStackTrace());
        Log.d(tag, message.toString());
        Log.d(tag, "at" + positionMsg);
    }

    public static void v(String message) {

        if (mode.equals("1")) {
            update(new Throwable().getStackTrace());
            Log.i(positionMsg, message.toString());
        } else {
        }
    }

}

