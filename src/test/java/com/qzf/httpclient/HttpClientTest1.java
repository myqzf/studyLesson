package com.qzf.httpclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.SSLException;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.web.client.HttpClientErrorException;



public class HttpClientTest1 {

	public static void main(String[] args) throws Exception{
		httpResponse();
		//messageHeaders();
		//headerElement();
    //httpContext();
		//httpRequestInterceptor();
//    urlUtil();
		StringEntity();
	}
	public static void httpRequest() throws Exception{
		URI uri = new URIBuilder()
        .setScheme("http")
        .setHost("www.google.com")
        .setPath("/search")
        .setParameter("q", "httpclient")
        .setParameter("btnG", "Google Search")
        .setParameter("aq", "f")
        .setParameter("oq", "")
        .build();
		HttpGet httpget = new HttpGet(uri);
		System.out.println(httpget.getURI());
	}
	
	public static void httpResponse() {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 
				HttpStatus.SC_OK, "OK");

				System.out.println(response.getProtocolVersion());
				System.out.println(response.getStatusLine().getStatusCode());
				System.out.println(response.getStatusLine().getReasonPhrase());
				System.out.println(response.getStatusLine().toString());
	}
	
	public static void messageHeaders(){
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 
		    HttpStatus.SC_OK, "OK");
		response.addHeader("Set-Cookie", 
		    "c1=a; path=/; domain=localhost");
		response.addHeader("Set-Cookie", 
		    "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
		Header h1 = response.getFirstHeader("Set-Cookie");
		System.out.println(h1);
		Header h2 = response.getLastHeader("Set-Cookie");
		System.out.println(h2);
		Header[] hs = response.getHeaders("Set-Cookie");
		System.out.println(hs.length);
	}
	
	public static void headerIterator() {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
		response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

		HeaderIterator it = response.headerIterator("Set-Cookie");

		while (it.hasNext()) {
		    System.out.println(it.next());
		}
	}
	
	public static void headerElement() {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
		response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

		HeaderElementIterator it = new BasicHeaderElementIterator(
		    response.headerIterator("Set-Cookie"));

		while (it.hasNext()) {
		    HeaderElement elem = it.nextElement(); 
		    System.out.println(elem.getName() + " = " + elem.getValue());
		    NameValuePair[] params = elem.getParameters();
		    for (int i = 0; i < params.length; i++) {
		        System.out.println(" " + params[i]);
		    }
		}
	}
	
	public static void stringEntity() throws Exception{
		StringEntity myEntity = new StringEntity("important message",ContentType.create("text/plain", "utf-8"));
		
		System.out.println(myEntity.getContentType());
		System.out.println(myEntity.getContentLength());
		System.out.println(EntityUtils.toString(myEntity));
		System.out.println(EntityUtils.toByteArray(myEntity).length);
	}
	
	public static void resouceRelease() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://localhost/");
		CloseableHttpResponse response = httpclient.execute(httpget);
		
		try {
			HttpEntity entity = response.getEntity();
			if(entity != null) {
				InputStream instream = entity.getContent();
				try {
					//do something useful
				}finally {
					instream.close();
				}
			}
		}finally {
			response.close();
		}
	}
	
	public static void httpEntity() throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://localhost/");
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			HttpEntity entity = response.getEntity();
			if(entity != null) {
				long len =entity.getContentLength();
				if(len != -1 && len < 2048) {
					System.out.println(EntityUtils.toString(entity));
				}else {
					entity = new BufferedHttpEntity(entity);
				}
			}
		}finally {
			response.close();
		}
	}
	
	public static void fileEntity() {
		File file = new File("somefile.txt");
		FileEntity entity = new FileEntity(file, ContentType.create("text/plain", Consts.UTF_8));
		HttpPost httppost = new HttpPost("http://localhost/action.do");
		httppost.setEntity(entity);
	}
	
	public static void urlEncodedFormEntity() throws Exception{
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("param1", "value1"));
		formparams.add(new BasicNameValuePair("param2", "value2"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);
		HttpPost httppost = new HttpPost("http://localhost/handler.do");
		httppost.setEntity(entity);
	}
	
	public static void StringEntity() throws Exception{
		StringEntity entity = new StringEntity("important message", ContentType.create("text/plain", Consts.UTF_8));
		entity.setChunked(true);
		HttpPost httppost = new HttpPost("http://localhost/action.do");
		httppost.setEntity(entity);
	}
	
	public static void responseHandler() throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet("http://localhost/json");
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				StatusLine statusLine = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				if(statusLine.getStatusCode() >= 300) {
					throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
				}
				if(entity == null) {
					throw new ClientProtocolException("Response contains no content");
				}
				
				return EntityUtils.toString(entity, Consts.UTF_8);
			}
			
		};
		 String responseBody = httpclient.execute(httpget, responseHandler);
		 System.out.println(responseBody);
	}
	
	public static void httpclientInterface() {
		ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				long keepAlive = super.getKeepAliveDuration(response, context);
				if(keepAlive == -1) {
				 //如果服务器没有设置keep-alive这个参数，我们就把它设置成5秒
          keepAlive = 5000;
				}
				return keepAlive;
			}
		};
		//自定义httpclient
		CloseableHttpClient httpclient = HttpClients.custom().setKeepAliveStrategy(keepAliveStrat).build();
	}
	
	public static void httpContext() throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
		HttpGet httpget1 = new HttpGet("http://192.168.1.101:8088/cpnsp/admin/sp/login/admin/123456");
		httpget1.setConfig(requestConfig);
		
		HttpClientContext context = HttpClientContext.create();
		CloseableHttpResponse response1 = httpclient.execute(httpget1, context);
		try {
			HttpEntity entity1 = response1.getEntity();
			System.out.println(EntityUtils.toString(entity1, "utf-8"));
		}finally{
			response1.close();
		}
		HttpGet httpget2 = new HttpGet("http://192.168.1.101:8088/cpnsp/admin/sp/getLoginedInfo");
		CloseableHttpResponse response2 = httpclient.execute(httpget2, context);
		
		HttpClientContext clientContext = HttpClientContext.adapt(context);
		HttpHost target = clientContext.getHttpRoute().getTargetHost();
		HttpRequest request = clientContext.getRequest();
		HttpResponse response = clientContext.getResponse();
		RequestConfig config = clientContext.getRequestConfig();
		try {
			HttpEntity entity2 = response2.getEntity();
			System.out.println(EntityUtils.toString(entity2, "utf-8"));
		}finally {
			response2.close();
		}
		
	}
	
	public static void httpRequestInterceptor() throws IOException{
		CloseableHttpClient httpclient = HttpClients.custom().addInterceptorLast(new HttpRequestInterceptor() {
			
			@Override
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				AtomicInteger count = (AtomicInteger)context.getAttribute("count");
				request.addHeader("Count", Integer.toString(count.getAndIncrement()));
			}
		}).build();
		
		AtomicInteger count = new AtomicInteger(1);
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAttribute("count", count);
		
		HttpGet httpget = new HttpGet("http://sjbj.lishanchuanmei.com");
		for(int i = 0; i<10; i++) {
			CloseableHttpResponse response = httpclient.execute(httpget, localContext);
			try {
				HttpEntity entity = response.getEntity();
			}finally {
				response.close();
			}
			
		}
	}
	
	public static void retryHandle() {
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if(executionCount >= 5) {
					//超过最大重试次数
					return false;
				}
				if(exception instanceof InterruptedIOException) {
					//超时
					return false;
				}
				if(exception instanceof UnknownHostException) {
					//目标服务不可达
					return false;
				}
				if(exception instanceof ConnectTimeoutException) {
					//连接被拒绝
					return false;
				}
				if(exception instanceof SSLException) {
					//ssl握手异常
					return false;
				}
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest); 
				if(idempotent) {
					//如果请求时幂等的，就再次尝试
					return false;
				}
				return false;
			}
		}; 
		CloseableHttpClient httpclient = HttpClients.custom()
				.setRetryHandler(retryHandler)
				.build();
	}
	
	public static void laxRedirctStrategy() {
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setRedirectStrategy(redirectStrategy)
				.build();
	}
	
	public static void urlUtil() throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpClientContext context = HttpClientContext.create();
		HttpGet httpget = new HttpGet("http://jingdong.com");
		CloseableHttpResponse response = httpclient.execute(httpget, context);
		try {
			HttpHost target = context.getTargetHost();
			List<URI> redirctLocations = context.getRedirectLocations();
			URI location = URIUtils.resolve(httpget.getURI(), target, redirctLocations);
			System.out.println("Final HTTP location: "+ location.toASCIIString());
		}finally {
			response.close();
		}
	}
	
}
