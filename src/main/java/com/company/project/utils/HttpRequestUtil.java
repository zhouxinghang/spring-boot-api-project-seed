package com.company.project.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
public class HttpRequestUtil {

    public static boolean isPostMethod(HttpUriRequest request){

        if(HttpPost.METHOD_NAME.equals(request.getMethod())){
            return true;
        }

        return false;
    }


    public static boolean isPostRequest(RequestBuilder builder){

        if (HttpPost.METHOD_NAME.equals(builder.getMethod())){
            return true;
        }

        return false;
    }

    public static boolean containDfpCookie(CookieStore cookieStore){

        if (CollectionUtils.isNotEmpty(cookieStore.getCookies())){
            for (Cookie cookie : cookieStore.getCookies()) {
                if (cookie.getName().equals("RAIL_DEVICEID")) {
                    return true;
                }
            }
        }
        return false;
    }
}
