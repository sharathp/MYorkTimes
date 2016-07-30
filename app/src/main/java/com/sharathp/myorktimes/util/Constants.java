package com.sharathp.myorktimes.util;

public class Constants {

    public static final String BASE_URL_API_NY_TIMES = "http://api.nytimes.com/";
    public static final String BASE_URL_NY_TIMES = "https://nytimes.com";
    public static final String URL_IMAGE_NY_TIMES = BASE_URL_NY_TIMES + "/%s";

    public static final int ROUND_TRANSFORMATION_RADIUS = 10;
    public static final int ROUND_TRANSFORMATION_MARGIN = 5;

    public static String getImageUrl(final String relativePath) {
        return String.format(URL_IMAGE_NY_TIMES, relativePath);
    }
}