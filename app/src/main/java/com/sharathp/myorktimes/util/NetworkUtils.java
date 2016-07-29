package com.sharathp.myorktimes.util;

import java.io.IOException;

public class NetworkUtils {

    public static boolean isOnline() {
        final Runtime runtime = Runtime.getRuntime();
        try {
            final Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            final int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
