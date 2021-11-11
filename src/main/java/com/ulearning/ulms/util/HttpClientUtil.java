package com.ulearning.ulms.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类说明:HttpClient工具类
 * @ClassName: HttpClientUtil
 * @author hlz
 * @date 2017年12月25日 下午2:16:47
 *
 * Copyright (c) 2006-2017.Beijing WenHua Online Sci-Tech Development Co. Ltd
 * All rights reserved.
 */
@Slf4j
@Component
public class HttpClientUtil {
    public static final String  CONTENTTYPE = "application/json";
    /**
     * 方法描述：向客户端写数据
     * <p>由于没有指定输出到浏览器的数据类型，ajax请求时不要指定datatype，是json要用eval()转换</p>
     * @param response
     * @param arg 写入的json或XML或其他
     * @author WangJialu
     * @version Jul 22, 2013 10:41:53 PM
     */
    public static void writeToClient(HttpServletResponse response, String arg) {
        writeToClient(response, "text/html;charset=utf-8", "UTF-8", arg);
    }
    /**
     * 向客户端写数据
     * @param contentType  默认text/html;charset=utf-8
     * @param encoding 默认UTF-8
     */
    public static void writeToClient(HttpServletResponse response, String contentType, String encoding, String resStr){
        if(response != null){
            if(StringUtils.isBlank(contentType)){
                response.setContentType("text/html;charset=utf-8");
            }
            if(StringUtils.isBlank(encoding)){
                response.setCharacterEncoding("UTF-8");
            }
            response.setContentType(contentType);
            response.setCharacterEncoding(encoding);
            PrintWriter pw = null;
            try {
                pw = response.getWriter();
                pw.print(resStr);
                pw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(pw != null){
                    pw.close();
                }
            }
        }
    }

    /**
     * 方法描述：向客户端写数据
     * <p>由于原项目中有几处只写数字类型数据的地方，这里不得不加此方法</p>
     * @param response
     * @param number 写入数字类型数据
     * @author WangJialu
     * @version Jul 22, 2013 10:41:53 PM
     */
    public static void writeToClient(HttpServletResponse response, Number number) {
        response.setContentType("text/html;");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(number);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                out.close();
            }
        }

    }

    /**
     * 
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     * @Title: doGet
     * @return String
     * @param url
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 判断状态码是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            // 返回响应体的内容
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } else if (response.getStatusLine().getStatusCode() == 500){
        	throw new Exception("接口调用失败");
        }
        return null;
    }

    /**
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     * @param url 被下载文件的url
     * @param path 下载文件的路径和文件名
     * @throws Exception
     */
    public static void downloadFile(String urlStr, String path) {
    	try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
	        // 声明 http get 请求   参考https://bbs.csdn.net/topics/380184988
			URL url = new URL(urlStr);
			URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			HttpGet httpGet = new HttpGet(uri);
	        // 发起请求
	        CloseableHttpResponse response = httpClient.execute(httpGet);

	        // 判断状态码是否为200
	        if (response.getStatusLine().getStatusCode() == 200) {
	            // 返回响应体的内容
	        	InputStream inputStream = response.getEntity().getContent();
	        	int index;
	            byte[] bytes = new byte[1024];
	            FileOutputStream downloadFile = new FileOutputStream(path);
	            while ((index = inputStream.read(bytes)) != -1) {
	                downloadFile.write(bytes, 0, index);
	                downloadFile.flush();
	            }
	            inputStream.close();
	            downloadFile.close();
	        	
	        } else if (response.getStatusLine().getStatusCode() == 500){
	        	
	        	throw new Exception("接口调用失败");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     * @Title: doGet
     * @return String
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> map) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (map != null) {
            // 遍历map,拼接请求参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        // 调用不带参数的get请求
        return doGet(uriBuilder.build().toString());

    }
    
    
    public static String doGetWithHeader(String url, Map<String, Object> headers) throws Exception {
        CloseableHttpClient httpClient = getIgnoeSSLClient();
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("X-App-ID","" + headers.get("X-App-ID"));
        httpGet.setHeader("X-Timestamp","" + headers.get("X-Timestamp"));
        httpGet.setHeader("X-Sign-Type","" + headers.get("X-Sign-Type"));
        httpGet.setHeader("X-Sign","" + headers.get("X-Sign"));
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 判断状态码是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            // 返回响应体的内容
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } else if (response.getStatusLine().getStatusCode() == 500){
        	throw new Exception("接口调用失败");
        }
        return null;
    

    }
    
    /**
     * 获取忽略证书验证的client
     *
     * @return
    * @throws Exception
     */
    public static CloseableHttpClient getIgnoeSSLClient() throws Exception {
    	   SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
    	      @Override
    	public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
    	         return true;
    	      }
    	   }).build();

    	   //创建httpClient
    	CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).
    	         setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    	   return client;
    	}

    /**
     * 
     * 带参数的post请求
     * @Title: doPost
     * @return String
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> map) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 判断map是否为空，不为空则进行遍历，封装from表单对象
        if (map != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            // 构造from表单对象
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

            // 把表单放到post里
            httpPost.setEntity(urlEncodedFormEntity);
        }
        // 发起请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } else if (response.getStatusLine().getStatusCode() == 500){
        	throw new Exception("接口调用失败");
        }
        return null;
    }


    public static String doPostObject(String url, Object obj) throws Exception {
    	return doPost(url, objectToMap(obj));
    }

    public static String doGetObject(String url, Object obj) throws Exception {
    	return doGet(url, objectToMap(obj));
    }

    /**
     * 
     * 不带参数post请求
     * @Title: doPost
     * @return HttpResult
     * @param url
     * @return
     * @throws Exception
     */
    public static String doPost(String url) throws Exception {
        return doPost(url, null);
    }

    /**
     * 
     * Object转Map
     * @Title: objectToMap
     * @return Map<String,Object>
     * @param obj
     * @return
     * @throws Exception
     */
    private static Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }     
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (null != field.get(obj)) {
                map.put(field.getName(), field.get(obj));
            }
        }
        return map;
    }

    public static JSONObject getUrlAccess(String urlStr) {
    	JSONObject result = new JSONObject();
    	try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(urlStr);
            RequestConfig requestConfig = RequestConfig.custom()    
                    .setConnectTimeout(5000).setConnectionRequestTimeout(1000)    
                    .setSocketTimeout(5000).build();
            httpGet.setConfig(requestConfig);// 设置超时时间
            CloseableHttpResponse response = httpClient.execute(httpGet);
            result.put("status", response.getStatusLine().getStatusCode());
            result.put("xFrameOptions", response.getHeaders("X-Frame-Options"));
            return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	result.put("status", -1);
    	result.put("xFrameOptions", null);
    	return result;
    }


    /**
     * 携带摘要认证的Get请求
     * @author yangjia
     * */
    public static Map<String,Object> doGetWithDigest(String url, JSONObject json,String domainIp,String domainPort,String username,String password) throws Exception {
        CloseableHttpClient httpClient = null;
        String result =null;
        Map<String, Object> maps = new HashMap<String, Object>(16);
        try {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            int port = 80;
            if(StringUtil.valid(domainPort)){
                port = StringUtil.getInt(domainPort);
            }
            credsProvider.setCredentials(new AuthScope(domainIp, port),
                    new UsernamePasswordCredentials(username, password));
            httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
            // 声明httpGet请求
            HttpGet httpGet = new HttpGet(url);
            // 构造消息头
            httpGet.addHeader("Content-type","application/json; charset=utf-8");
            httpGet.setHeader("Accept", "application/json");
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            maps.put("result", result);
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return maps;
    }

    /**
     * 携带摘要认证的Post请求
     * @author yangjia
     * */
    public static Map<String,Object> doPostWithDigest(String url, JSONObject json,String domainIp,String domainPort,String username,String password) throws Exception {
        CloseableHttpClient httpClient = null;
        String result =null;
        Map<String, Object> maps = new HashMap<String, Object>(16);
        try {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            int port = 80;
            if(StringUtil.valid(domainPort)){
                port = StringUtil.getInt(domainPort);
            }
            credsProvider.setCredentials(new AuthScope(domainIp, port),
                    new UsernamePasswordCredentials(username, password));
            httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
            // 声明httpPost请求
            HttpPost httpPost = new HttpPost(url);
            if(json == null){
                json = new JSONObject();
            }
            StringEntity paramEntity = new StringEntity(json.toJSONString(), "UTF-8");
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            maps.put("result", result);
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return maps;
    }

    /**
     * 带请求头的GET
     * */
    public static String doGet(String url, JSONObject headers) {
        CloseableHttpClient httpClient = null;
        String result =null;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpGet请求
            HttpGet httpGet = new HttpGet(url);
            if(headers != null){
                headers.keySet().stream().forEach(header -> {
                    httpGet.setHeader(header, headers.getString(header));
                });
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 带请求头的POST
     * */
    public static String doPost(String url, JSONObject body, JSONObject headers) {
        CloseableHttpClient httpClient = null;
        String result =null;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpPost请求
            HttpPost httpPost = new HttpPost(url);
            if(body == null){
                body = new JSONObject();
            }
            StringEntity paramEntity = new StringEntity(body.toJSONString(), "UTF-8");
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);
            if(headers != null){
                headers.keySet().stream().forEach(header -> {
                    httpPost.setHeader(header, headers.getString(header));
                });
            }

            // 发起请求
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
                // 判断状态码是否为200
                if (response.getStatusLine().getStatusCode() == 200) {
                    // 返回响应体的内容
                    return EntityUtils.toString(response.getEntity(), "UTF-8");
                } else if (response.getStatusLine().getStatusCode() == 500){
                    log.error("接口调用失败: url: [{}], body: [{}]", url, body.toJSONString());
                    throw new Exception("接口调用失败");
                }
            } catch (ConnectTimeoutException | SocketTimeoutException e) {
                log.error("连接超时了... 重新发送请求... : url: [{}], body: [{}]", url, body.toJSONString());
                doPost(url, body, headers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 带请求头的PUT
     * */
    public static String doPut(String url, JSONObject body, JSONObject headers) {
        CloseableHttpClient httpClient = null;
        String result =null;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpPost请求
            HttpPut httpPut = new HttpPut(url);
            if(body == null){
                body = new JSONObject();
            }
            StringEntity paramEntity = new StringEntity(body.toJSONString(), "UTF-8");
            paramEntity.setContentType("application/json");
            httpPut.setEntity(paramEntity);
            if(headers != null){
                headers.keySet().stream().forEach(header -> {
                    httpPut.setHeader(header, headers.getString(header));
                });
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpPut);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 云会议
     * @author yangjia
     * */
    public static String doGetCloudMetting(String url, JSONObject body,JSONObject headers) {
        boolean logFlag = url.contains("realTimeInfo") || url.contains("conferences/token");
        if(!logFlag){
//            System.out.println("请求url:" + url);
        }
        CloseableHttpClient httpClient = null;
        String result =null;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpGet请求
            HttpGet httpGet = new HttpGet(url);
            // 构造消息头
            httpGet.addHeader("Content-type","application/json; charset=utf-8");
            httpGet.setHeader("Accept", "application/json");
            if(headers != null){
                if(StringUtil.valid(headers.getString("Authorization"))){
                    httpGet.setHeader("Authorization",headers.getString("Authorization"));
                }
                if(StringUtil.valid(headers.getString("Content-Type"))){
                    httpGet.setHeader("Content-Type",headers.getString("Content-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Access-Token"))){
                    httpGet.setHeader("X-Access-Token",headers.getString("X-Access-Token"));
                }
                if(StringUtil.valid(headers.getString("X-Login-Type"))){
                    httpGet.setHeader("X-Login-Type",headers.getString("X-Login-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Password"))){
                    httpGet.setHeader("X-Password",headers.getString("X-Password"));
                }
                if(StringUtil.valid(headers.getString("X-Conference-Authorization"))){
                    httpGet.setHeader("X-Conference-Authorization",headers.getString("X-Conference-Authorization"));
                }
                if(StringUtil.valid(headers.getString("x-wlk-Authorization"))){
                	httpGet.setHeader("x-wlk-Authorization",headers.getString("x-wlk-Authorization"));
                }
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            if(!logFlag){
//                System.out.println("返回的消息：" + result);
//                System.out.println("返回的状态: " + response.getStatusLine().getStatusCode());
//                System.out.println("返回的类型: " + response.getEntity().getContentType());
            }
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    /**
     * 云会议
     * @author yangjia
     * */
    public static String doPostCloudMetting(String url, JSONObject body,JSONObject headers) {
        CloseableHttpClient httpClient = null;
        String result =null;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpPost请求
            HttpPost httpPost = new HttpPost(url);
            if(body == null){
                body = new JSONObject();
            }
            StringEntity paramEntity = new StringEntity(body.toJSONString(), "UTF-8");
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);
            if(headers != null){
                if(StringUtil.valid(headers.getString("Authorization"))){
                    httpPost.setHeader("Authorization",headers.getString("Authorization"));
                }
                if(StringUtil.valid(headers.getString("Content-Type"))){
                    httpPost.setHeader("Content-Type",headers.getString("Content-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Access-Token"))){
                    httpPost.setHeader("X-Access-Token",headers.getString("X-Access-Token"));
                }
                if(StringUtil.valid(headers.getString("X-Login-Type"))){
                    httpPost.setHeader("X-Login-Type",headers.getString("X-Login-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Password"))){
                    httpPost.setHeader("X-Password",headers.getString("X-Password"));
                }
                if(StringUtil.valid(headers.getString("X-Conference-Authorization"))){
                    httpPost.setHeader("X-Conference-Authorization",headers.getString("X-Conference-Authorization"));
                }
                if(StringUtil.valid(headers.getString("x-wlk-Authorization"))){
                	httpPost.setHeader("x-wlk-Authorization",headers.getString("x-wlk-Authorization"));
                }
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 云会议
     * @author yangjia
     * */
    public static String doPostCloudMeeting(String url, JSONArray body, JSONObject headers) {
        CloseableHttpClient httpClient = null;
        String result =null;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpPost请求
            HttpPost httpPost = new HttpPost(url);
            if(body == null){
                body = new JSONArray();
            }
            StringEntity paramEntity = new StringEntity(body.toJSONString(), "UTF-8");
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);
            if(headers != null){
                if(StringUtil.valid(headers.getString("Authorization"))){
                    httpPost.setHeader("Authorization",headers.getString("Authorization"));
                }
                if(StringUtil.valid(headers.getString("Content-Type"))){
                    httpPost.setHeader("Content-Type",headers.getString("Content-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Access-Token"))){
                    httpPost.setHeader("X-Access-Token",headers.getString("X-Access-Token"));
                }
                if(StringUtil.valid(headers.getString("X-Login-Type"))){
                    httpPost.setHeader("X-Login-Type",headers.getString("X-Login-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Password"))){
                    httpPost.setHeader("X-Password",headers.getString("X-Password"));
                }
                if(StringUtil.valid(headers.getString("X-Conference-Authorization"))){
                    httpPost.setHeader("X-Conference-Authorization",headers.getString("X-Conference-Authorization"));
                }
                if(StringUtil.valid(headers.getString("x-wlk-Authorization"))){
                    httpPost.setHeader("x-wlk-Authorization",headers.getString("x-wlk-Authorization"));
                }
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 云会议
     * @author yangjia
     * */
    public static int doPostCloudMettingStatusCode(String url, JSONObject body,JSONObject headers) {
        CloseableHttpClient httpClient = null;
        String result =null;
        int statusCode = 0;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpPost请求
            HttpPost httpPost = new HttpPost(url);
            if(body == null){
                body = new JSONObject();
            }
            StringEntity paramEntity = new StringEntity(body.toJSONString(), "UTF-8");
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);
            if(headers != null){
                if(StringUtil.valid(headers.getString("Authorization"))){
                    httpPost.setHeader("Authorization",headers.getString("Authorization"));
                }
                if(StringUtil.valid(headers.getString("Content-Type"))){
                    httpPost.setHeader("Content-Type",headers.getString("Content-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Access-Token"))){
                    httpPost.setHeader("X-Access-Token",headers.getString("X-Access-Token"));
                }
                if(StringUtil.valid(headers.getString("X-Login-Type"))){
                    httpPost.setHeader("X-Login-Type",headers.getString("X-Login-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Password"))){
                    httpPost.setHeader("X-Password",headers.getString("X-Password"));
                }
                if(StringUtil.valid(headers.getString("X-Conference-Authorization"))){
                    httpPost.setHeader("X-Conference-Authorization",headers.getString("X-Conference-Authorization"));
                }
                if(StringUtil.valid(headers.getString("x-wlk-Authorization"))){
                	httpPost.setHeader("x-wlk-Authorization",headers.getString("x-wlk-Authorization"));
                }
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            statusCode = response.getStatusLine().getStatusCode();
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return statusCode;
    }

    /**
     * 云会议
     * @author yangjia
     * */
    public static String doPutCloudMetting(String url, JSONObject body,JSONObject headers) {
        CloseableHttpClient httpClient = null;
        String result =null;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpPost请求
            HttpPut httpPut = new HttpPut(url);
            if(body == null){
                body = new JSONObject();
            }
            StringEntity paramEntity = new StringEntity(body.toJSONString(), "UTF-8");
            paramEntity.setContentType("application/json");
            httpPut.setEntity(paramEntity);
            if(headers != null){
                if(StringUtil.valid(headers.getString("Authorization"))){
                    httpPut.setHeader("Authorization",headers.getString("Authorization"));
                }
                if(StringUtil.valid(headers.getString("Content-Type"))){
                    httpPut.setHeader("Content-Type",headers.getString("Content-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Access-Token"))){
                    httpPut.setHeader("X-Access-Token",headers.getString("X-Access-Token"));
                }
                if(StringUtil.valid(headers.getString("X-Login-Type"))){
                    httpPut.setHeader("X-Login-Type",headers.getString("X-Login-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Password"))){
                    httpPut.setHeader("X-Password",headers.getString("X-Password"));
                }
                if(StringUtil.valid(headers.getString("X-Conference-Authorization"))){
                    httpPut.setHeader("X-Conference-Authorization",headers.getString("X-Conference-Authorization"));
                }
                if(StringUtil.valid(headers.getString("x-wlk-Authorization"))){
                	httpPut.setHeader("x-wlk-Authorization",headers.getString("x-wlk-Authorization"));
                }
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpPut);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 云会议
     * @author yangjia
     * */
    public static int doPutCloudMettingStatusCode(String url, JSONObject body,JSONObject headers) {
        int statusCode = 0;
        CloseableHttpClient httpClient = null;
        String result =null;
        try {
            httpClient = HttpClients.custom().build();
            // 声明httpPost请求
            HttpPut httpPut = new HttpPut(url);
            if(body == null){
                body = new JSONObject();
            }
            StringEntity paramEntity = new StringEntity(body.toJSONString(), "UTF-8");
            paramEntity.setContentType("application/json");
            httpPut.setEntity(paramEntity);
            if(headers != null){
                if(StringUtil.valid(headers.getString("Authorization"))){
                    httpPut.setHeader("Authorization",headers.getString("Authorization"));
                }
                if(StringUtil.valid(headers.getString("Content-Type"))){
                    httpPut.setHeader("Content-Type",headers.getString("Content-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Access-Token"))){
                    httpPut.setHeader("X-Access-Token",headers.getString("X-Access-Token"));
                }
                if(StringUtil.valid(headers.getString("X-Login-Type"))){
                    httpPut.setHeader("X-Login-Type",headers.getString("X-Login-Type"));
                }
                if(StringUtil.valid(headers.getString("X-Password"))){
                    httpPut.setHeader("X-Password",headers.getString("X-Password"));
                }
                if(StringUtil.valid(headers.getString("X-Conference-Authorization"))){
                    httpPut.setHeader("X-Conference-Authorization",headers.getString("X-Conference-Authorization"));
                }
                if(StringUtil.valid(headers.getString("x-wlk-Authorization"))){
                	httpPut.setHeader("x-wlk-Authorization",headers.getString("x-wlk-Authorization"));
                }
            }
            // 发起请求
            CloseableHttpResponse response = httpClient.execute(httpPut);
            org.apache.http.HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            statusCode = response.getStatusLine().getStatusCode();
            EntityUtils.consume(response.getEntity());
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return statusCode;
    }
    public static String sendHttpPost(String url, String body) throws Exception {
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.addHeader("Content-Type", "application/json");
    	httpPost.setEntity(new StringEntity(body));
    	CloseableHttpResponse response = httpClient.execute(httpPost);
    	System.out.println(response.getStatusLine().getStatusCode() + "\n");
    	HttpEntity entity = response.getEntity();
    	String responseContent = EntityUtils.toString(entity, "UTF-8"); 
    	System.out.println(responseContent);
    	response.close();
    	httpClient.close();
    	return responseContent;
    	}
    public static String sendPost(String url, String param, String contentType) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", contentType);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            // out = new PrintWriter(conn.getOutputStream());
            out = new OutputStreamWriter(conn.getOutputStream(), "utf-8"); // 8859_1
            out.write(param); // post的关键所在
            // 发送请求参数
            // out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            int code = conn.getResponseCode();
            if(code == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }else{
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
