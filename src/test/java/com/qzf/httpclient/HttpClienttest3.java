package com.qzf.httpclient;

import java.io.IOError;
import java.io.IOException;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.SetCookie;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;

public class HttpClienttest3 {

	public static void main(String[] args) {
		//SetCookie2
		//CookieSpec
	}
	public static void clientCookie() {
		BasicClientCookie cookie = new BasicClientCookie("name", "value");
		cookie.setDomain(".mycompany.com");
		cookie.setPath("/");
		cookie.setAttribute(ClientCookie.PATH_ATTR, "/");
		cookie.setAttribute(ClientCookie.DOMAIN_ATTR, ".mycompany.com");
	}
	
	public static void cookieSpec() {
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.DEFAULT)
				.build();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig)
				.build();
		RequestConfig localConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD_STRICT)
				.build();
		HttpGet httpGet = new HttpGet("/");
		httpGet.setConfig(localConfig);
	}
	
	public static void customCookieSpec() {
		PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();
		Registry<CookieSpecProvider> r = RegistryBuilder.<CookieSpecProvider>create()
				.register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider(publicSuffixMatcher))
				.register(CookieSpecs.STANDARD, new RFC6265CookieSpecProvider(publicSuffixMatcher))
				//.register("easy", new EasySpecProvider())
				.build();
		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec("easy").build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultCookieSpecRegistry(r)
				.setDefaultRequestConfig(requestConfig)
				.build();
	}
	
	public static void cookiStore() {
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("name", "value");
		cookie.setDomain(".mycompany.com");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore)
				.build();
	}
	
	
	public static void httpContext() throws IOException{
		CloseableHttpClient httpClient = HttpClients.custom().build();
		
		Lookup<CookieSpecProvider> cookieSpecReg ;
		CookieStore cookieStore;
		
		HttpClientContext context = HttpClientContext.create();
		//context.setCookieSpecRegistry(cookieSpecReg);
		//context.setCookieStore(cookieStore);
		HttpGet httpget = new HttpGet("http://somehost/");
		CloseableHttpResponse response = httpClient.execute(httpget, context);
		
		//cookie origin 
		CookieOrigin cookieOrigin = context.getCookieOrigin();
		//Cookie spec
		CookieSpec cookieSpec = context.getCookieSpec();
	}
}
