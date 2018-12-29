package com.qzf.httpclient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpClientTest2 {

	public static void main(String[] args) throws Exception{
//		basicHttpClientConnectionManager();
//		multiThread();
		crateSocket();
	}
	
	public static void basicHttpClientConnectionManager() throws Exception{
		HttpClientContext context = HttpClientContext.create();
		HttpClientConnectionManager connMrg = new BasicHttpClientConnectionManager();
//		HttpRoute route = new HttpRoute(new HttpHost("www.baidu.com", 80));
//		// 获取新的连接. 这里可能耗费很多时间
//		ConnectionRequest connRequest =connMrg.requestConnection(route, null);
//		//等待10秒 超时
//		HttpClientConnection conn = connRequest.get(10, TimeUnit.SECONDS);
		try {
			// 如果创建连接失败
//			if(!conn.isOpen()) {
//				connMrg.connect(conn, route, 1000, context);
//				connMrg.routeComplete(conn, route, context);
//			}
			

			// 进行自己的操作...
			CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connMrg).build();
			Thread a = new Thread(() -> req(httpclient));
			a.setName("a");
			Thread b = new Thread(() -> req(httpclient));
			b.setName("b");
			
			a.start();
			b.start();
			
//			CloseableHttpResponse response = httpclient.execute(new HttpGet("http://www.baidu.com/"), context);
//			System.out.println(response.getStatusLine());
//			EntityUtils.consume(response.getEntity());
		}finally {
			//connMrg.releaseConnection(conn, null, 1, TimeUnit.MINUTES);
		}
	}
	

static void req(CloseableHttpClient httpClient) {
    HttpGet httpGet = new HttpGet("http://www.baidu.com");
    CloseableHttpResponse response = null;
    try {
        System.out.println(Thread.currentThread().getName() + " ---0");
        response = httpClient.execute(httpGet);
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName() + " ---1");
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (response != null) response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
	
	public static void poolingHttpClientConnectionManager() {
		//HttpRouteDirector
//		ManagedHttpClientConnection SystemDefaultRoutePlanner
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		//将最大总连接数增加到200
		cm.setMaxTotal(200);
		//将每条路由的默认最大连接数增加到20 
		cm.setDefaultMaxPerRoute(20);
		//将目标主机的最大连接数增加到50
		HttpHost localhost = new HttpHost("localhost", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 50);
		
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(cm)
				.build();
	}
	
	public static void multiThread() throws Exception{
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(cm)
				.build();
		
		String[] uriToGet = {
				"http://www.baidu.com/",
				"http://www.qq.com/",
				"http://www.sohu.com/",
				"http://www.sina.com.cn/"
		};
		
		GetThread[] threads = new GetThread[uriToGet.length];
		for(int i = 0; i<threads.length; i++) {
			HttpGet httpget = new HttpGet(uriToGet[i]);
			threads[i] = new GetThread(httpClient, httpget);
		}
		
		for(int j = 0; j<threads.length;j++) {
			threads[j].start();
		}
		
		for (int j = 0; j < threads.length; j++) {
			threads[j].join();
		}
	}
	
	public static void connecionKeepAliveStrategy() {
		ConnectionKeepAliveStrategy mystrategy = new ConnectionKeepAliveStrategy() {
			
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
				while(it.hasNext()) {
					HeaderElement he = it.nextElement();
					String param = he.getName();
					String value = he.getValue();
					if(value!=null && param.equalsIgnoreCase("timeout")) {
						try {
							return Long.parseLong(value) * 1000;
						}catch(NumberFormatException ignore) {
							
						}
					}
				}
				HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
				if("www.naught-server.com".equalsIgnoreCase(target.getHostName())){
					return 5 * 1000;
				}else {
					return 30 * 1000;
				}
			}
		};
		CloseableHttpClient client = HttpClients.custom()
				.setKeepAliveStrategy(mystrategy)
				.build();
	}
	
	public static void crateSocket() throws IOException {
		HttpClientContext cleintContext = HttpClientContext.create();
		PlainConnectionSocketFactory sf = PlainConnectionSocketFactory.getSocketFactory();
		Socket socket = sf.createSocket(cleintContext);
		int timout = 1000;
		HttpHost target = new HttpHost("xnjpx.bjupi.com");
		InetSocketAddress remoteAddress = new InetSocketAddress(InetAddress.getByAddress(new byte[] {47,97,21,1}), 80);
		sf.connectSocket(timout, socket, target, remoteAddress, null, cleintContext);
	}
	
	public static void socketFactory() {
		PlainConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory layersf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", plainsf)
					.register("https", layersf)
					.build();
		
		HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
		HttpClients.custom()
					.setConnectionManager(cm)
					.build();

	}
	
	public static void sslSocketFactory() throws Exception {
		HttpClientContext clientContext = HttpClientContext.create();
		KeyStore keystore = KeyStore.getInstance("jks");
		SSLContext sslContext = SSLContexts.custom()
				.loadTrustMaterial(keystore, new TrustSelfSignedStrategy())
				.build();
	}
	
	
	public static void hostnameVerifier() {
		SSLContext sslContext = SSLContexts.createSystemDefault();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext, 
				NoopHostnameVerifier.INSTANCE);
	}
	
	public static void publicSuffix() throws IOException {
		PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(
				PublicSuffixMatcher.class.getResource("my-copy-effective_tld_names.dat"));
		DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
		
		//禁用对公共服务列表的验证
		//DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
	}
	
	public static void defaultProxy() {
		HttpHost proxy = new HttpHost("someproxy", 8080);
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		CloseableHttpClient httpClient = HttpClients.custom()
				.setRoutePlanner(routePlanner)
				.build();
		
	}
	
	public static void jreSystemDefaultProxy() {
		SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
		CloseableHttpClient httpClient = HttpClients.custom()
				.setRoutePlanner(routePlanner)
				.build();
		
	}
	
	public static void customRoutePlanner() {
		HttpRoutePlanner routePlanner = new HttpRoutePlanner() {
			
			@Override
			public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
				return new HttpRoute(target, null, new HttpHost("someproxy", 8080), "https".equalsIgnoreCase(target.getSchemeName()));
			}
		};
		CloseableHttpClient httpClient = HttpClients.custom()
				.setRoutePlanner(routePlanner)
				.build();
	}
}
