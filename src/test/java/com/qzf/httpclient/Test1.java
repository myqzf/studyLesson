package com.qzf.httpclient;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

public class Test1 {

	public static void main(String[] args) throws Exception{
//		logininfo();
		logininfo1();
	}
	public static void logininfo() throws Exception{
		
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		
		HttpClientContext context = HttpClientContext.create();
		
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("sessionToken", "4648569c46ab4854b66a746de06b43e8");
		cookie.setDomain("skillchina.bjuri.com");
		cookieStore.addCookie(cookie);
		httpClientBuilder.setDefaultCookieStore(cookieStore);
		
		CloseableHttpClient httpclient = httpClientBuilder.build();
		
		HttpGet httpget2 = new HttpGet("http://skillchina.bjuri.com/cpnsp/admin/sp/getLoginedInfo");
		CloseableHttpResponse response2 = httpclient.execute(httpget2, context);
		
//		HttpClientContext clientContext = HttpClientContext.adapt(context);
//		HttpHost target = clientContext.getHttpRoute().getTargetHost();
//		HttpRequest request = clientContext.getRequest();
//		HttpResponse response = clientContext.getResponse();
//		RequestConfig config = clientContext.getRequestConfig();
		try {
			HttpEntity entity2 = response2.getEntity();
			System.out.println(EntityUtils.toString(entity2, "utf-8"));
		}finally {
			response2.close();
		}
	}
	
public static void logininfo1() throws Exception{
		
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		
		HttpClientContext context = HttpClientContext.create();
		
		Map<String, String> cookies = new LinkedHashMap<String, String>();
		cookies.put("__cfduid", "d22102371ba4b9d26f7a0256d0b21ff3d1537163438");
		cookies.put("email", "poiu00998@126.com");
		cookies.put("expire_in", "1541577911");
		cookies.put("ip", "eb496f74700f0760f335b6b7cdf925cc");
		cookies.put("key", "ff3024bc178d7b0a7a2bb064762bdece0c477971652f6");
		cookies.put("PHPSESSID", "a4glg16atmtboklbnc85pp9s46");
		cookies.put("uid", "830");
		
		CookieStore cookieStore = new BasicCookieStore();
    for (Map.Entry<String, String> cookieEntry : cookies.entrySet()) {
        BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
        cookie.setDomain("v3.mimi.ooo");
        cookieStore.addCookie(cookie);
    }
		httpClientBuilder.setDefaultCookieStore(cookieStore);
		
		CloseableHttpClient httpclient = httpClientBuilder.build();
		
		HttpGet httpget1 = new HttpGet("https://v3.mimi.ooo/user");
		CloseableHttpResponse response1 = httpclient.execute(httpget1, context);
		
//		HttpClientContext clientContext = HttpClientContext.adapt(context);
//		HttpHost target = clientContext.getHttpRoute().getTargetHost();
//		HttpRequest request = clientContext.getRequest();
//		HttpResponse response = clientContext.getResponse();
//		RequestConfig config = clientContext.getRequestConfig();
		try {
			HttpEntity entity1 = response1.getEntity();
			System.out.println(EntityUtils.toString(entity1, "utf-8"));
		}finally {
			response1.close();
		}
		
		HttpPost httppost = new HttpPost("https://v3.mimi.ooo/user/checkin");
		CloseableHttpResponse response2 = httpclient.execute(httppost, context);
		
		try {
			HttpEntity entity2 = response2.getEntity();
			String result = EntityUtils.toString(entity2, "utf-8");
			System.out.println(result);
		}finally {
			response1.close();
		}
	}
	
}
