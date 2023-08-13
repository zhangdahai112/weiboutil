package com;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class GetCookieUtil {
    public static String getSub() throws Exception {
        String url = "https://passport.weibo.com/visitor/genvisitor";

        // Set headers
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");

        // Set data
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("cb", "gen_callback");
        data.put("fp", "{\"os\":\"1\",\"browser\":\"Chrome95,0,4621,0\",\"fonts\":\"undefined\",\"screenInfo\":\"1920*1080*24\",\"plugins\":\"Portable Document Format::internal-pdf-viewer::Chromium PDF Plugin|::mhjfbmdgcfjbbpaeojofohoefgiehjai::Chromium PDF Viewer\"}");

        // Send POST request
        String response = sendPostRequest(url, headers, data);

        // Parse response
        response = response.replace("window.gen_callback && gen_callback(", "");
        response = response.replace(");", "").replace("\\", "");
        JSONObject jsons = parseJson(response);
        String t = (String) jsons.getJSONObject("data").get("tid");

        String url2 = "https://passport.weibo.com/visitor/visitor?a=incarnate&t=" + t + "&w=2&c=095&gc=&cb=cross_domain&from=weibo&_rand=0.34268151967150073";

        // Send GET request
        response = sendGetRequest(url2, headers);

        // Parse response
        response = response.replace("window.cross_domain && cross_domain(", "");
        response = response.replace(");", "").replace("\\", "");
        JSONObject jsons2 = parseJson(response);
        return jsons2.getJSONObject("data").getString("sub");
    }

    public static String sendPostRequest(String url, HashMap<String, String> headers, HashMap<String, String> data) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set request method
        con.setRequestMethod("POST");

        // Set request headers
        for (String key : headers.keySet()) {
            con.setRequestProperty(key, headers.get(key));
        }

        // Set data
        StringBuilder postData = new StringBuilder();
        for (String key : data.keySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(key, "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(data.get(key), "UTF-8"));
        }

        // Send post data
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            os.write(postDataBytes);
            os.flush();
        }

        // Get response
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    public static String sendGetRequest(String url, HashMap<String, String> headers) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set request method
        con.setRequestMethod("GET");

        // Set request headers
        for (String key : headers.keySet()) {
            con.setRequestProperty(key, headers.get(key));
        }

        // Get response
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    public static JSONObject parseJson(String json) throws Exception {
        return JSONObject.parseObject(json);
    }
}

