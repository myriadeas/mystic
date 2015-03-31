package my.com.myriadeas.mystic.component.http4;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.camel.component.http4.HttpClientConfigurer;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;

public class ProxyAndRedirectHttpClientConfigurer implements HttpClientConfigurer {
	private String proxyHost = "127.0.0.1";
	private int proxyPort = -1;
	

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}



	public void setProxyPort(int proxyHost) {
		this.proxyPort = proxyHost;
	}



	@Override
	public void configureHttpClient(HttpClient httpClient) {
		if (proxyHost != null && proxyHost != "" && proxyPort > 0){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyHost, proxyPort));
            //This is the conflict of camel setup when using https over http proxy. 
            //example: proxyHost = 127.0.0.1, proxyScheme=http. clientHost= www.jkr.gov.my, clientScheme=https. Conflict. 
            //error java.lang.IllegalStateException: Scheme 'http' not registered. will be thrown. 
            //hence manually register both http and https scheme if there is proxy configured.
            //should remove in future version if camel fixed this issue
            // or find other way to fix it. TODO
            registerScheme(httpClient.getConnectionManager().getSchemeRegistry());
		}
		
		((DefaultHttpClient) httpClient).setRedirectStrategy(new DefaultRedirectStrategy() {                
			public boolean isRedirected(
		            final HttpRequest request,
		            final HttpResponse response,
		            final HttpContext context) throws ProtocolException {
				
				boolean isRedirect=false;
	            try {
	                isRedirect = super.isRedirected(request, response, context);
	            } catch (ProtocolException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            if (!isRedirect) {
	                int responseCode = response.getStatusLine().getStatusCode();
	                if (responseCode == 301 || responseCode == 302) {
	                    return true;
	                }
	            }
	            return isRedirect;
		    }
	    });
		
		
	}
    
    private void registerScheme(org.apache.http.conn.scheme.SchemeRegistry schemeRegistry) {
        if(schemeRegistry.get("https") == null) {
            schemeRegistry.register(new org.apache.http.conn.scheme.Scheme("https", 443, 
                org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory()));
        }
        if(schemeRegistry.get("http") == null) {
            schemeRegistry.register(new org.apache.http.conn.scheme.Scheme("http", 80,
				new org.apache.http.conn.scheme.PlainSocketFactory()));
        }
    }

}
