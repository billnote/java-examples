/**
 * 
 */
package com.bill.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * Http发送器
 * 
 * @author Alex.Zhou
 * 
 */
public class HttpSender
{
	private static final int BUFFER_SIZE = 1024;
	private static PoolingHttpClientConnectionManager httpClientConnectionManager;
	private static RequestConfig config;
	private static final int TIME_OUT = 10000;

	static
	{
		httpClientConnectionManager = new PoolingHttpClientConnectionManager();
		httpClientConnectionManager.setMaxTotal(200);
		httpClientConnectionManager.setDefaultMaxPerRoute(80);
		config = RequestConfig.custom()
				.setConnectionRequestTimeout(2000)
				.setConnectTimeout(2000)
				.setSocketTimeout(2000)
				.setExpectContinueEnabled(true)
				.setStaleConnectionCheckEnabled(true).build();
	}

	public static final String doGet(String address) throws IOException
	{
		try (CloseableHttpResponse response = getRequest(address, null))
		{
			String responseString = readStringFromResponse(response);
			return responseString;
		}
	}

	public static final String doGet(String address, Hashtable<String, String> headers) throws IOException
	{
		try (CloseableHttpResponse response = getRequest(address, headers))
		{
			String responseString = readStringFromResponse(response);
			return responseString;
		}
	}

	public static final String doPost(String address, String postData) throws ClientProtocolException, IOException
	{
		try (CloseableHttpResponse response = postRequest(address, TIME_OUT, postData, null))
		{
			String responseString = readStringFromResponse(response);

			return responseString;
		}
	}

	public static final String doPost(String address, byte[] postData) throws ClientProtocolException, IOException
	{
		try (CloseableHttpResponse response = postRequest(address, TIME_OUT, postData, null))
		{
			String responseString = readStringFromResponse(response);

			return responseString;
		}
	}

	public static final byte[] doPost2(String address, byte[] postData) throws ClientProtocolException, IOException
	{
		try (CloseableHttpResponse response = postRequest(address, TIME_OUT, postData, null))
		{
			byte[] responseString = readBytesFromResponse(response);

			return responseString;
		}
	}
	
	public static final String doPost(String address, Map<String, String> valueMap) throws ClientProtocolException, IOException
	{
		List<NameValuePair> form = new ArrayList<NameValuePair>();
		if (null != valueMap && valueMap.size() > 0)
		{
			for (Map.Entry<String, String> entry : valueMap.entrySet())
			{
				form.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}

		try (CloseableHttpResponse response = postRequest(address, TIME_OUT, form, null))
		{
			String responseString = readStringFromResponse(response);

			return responseString;
		}
	}

	private static final CloseableHttpResponse getRequest(String address, Hashtable<String, String> headers)
			throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpClient = getHttpClient();

		HttpGet httpGet = new HttpGet(address);
		if (headers != null)
		{
			for (Entry<String, String> entry : headers.entrySet())
			{
				httpGet.setHeader(entry.getKey(), entry.getValue());
			}
		}

		return httpClient.execute(httpGet);

	}

	private static CloseableHttpClient getHttpClient()
	{
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).setConnectionManager(httpClientConnectionManager).build();
		return client;
	}

	private static final CloseableHttpResponse postRequest(String address, int timeout, byte[] postData,
			Hashtable<String, String> headers) throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(address);
		httpPost.setConfig(RequestConfig.custom().setConnectTimeout(timeout).build());

		if (headers != null)
		{
			for (Entry<String, String> entry : headers.entrySet())
			{
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}

		HttpEntity paramsEntity = new ByteArrayEntity(postData);

		httpPost.setEntity(paramsEntity);

		return httpClient.execute(httpPost);
	}

	private static final CloseableHttpResponse postRequest(String address, int timeout, List<NameValuePair> form,
			Hashtable<String, String> headers) throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpClient = getHttpClient();

		HttpPost httpPost = new HttpPost(address);
		httpPost.setConfig(RequestConfig.custom().setConnectTimeout(timeout).build());

		if (headers != null)
		{
			for (Entry<String, String> entry : headers.entrySet())
			{
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}

		if (form != null)
		{
			@SuppressWarnings("deprecation")
			HttpEntity paramsEntity = new UrlEncodedFormEntity(form, HTTP.UTF_8);
			httpPost.setEntity(paramsEntity);
		}

		return httpClient.execute(httpPost);
	}

	/**
	 * 
	 * @param address
	 * @param timeout
	 * @param postData
	 * @param headers
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static final CloseableHttpResponse postRequest(String address, int timeout, String postData,
			Hashtable<String, String> headers) throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpClient = getHttpClient();

		HttpPost httpPost = new HttpPost(address);
		httpPost.setConfig(RequestConfig.custom().setConnectTimeout(timeout).build());

		if (headers != null)
		{
			for (Entry<String, String> entry : headers.entrySet())
			{
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
		}

		HttpEntity paramsEntity = new StringEntity(postData);

		httpPost.setEntity(paramsEntity);

		return httpClient.execute(httpPost);

	}

	/**
	 * Read byte array from HttpResponse
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytesFromResponse(HttpResponse response) throws IOException
	{
		HttpEntity responseEntity = response.getEntity();
		try (InputStream inputStream = responseEntity.getContent())
		{
			List<Byte> resultBytes = new ArrayList<Byte>();
			byte[] inputBuffer = new byte[BUFFER_SIZE];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(inputBuffer)) != -1)
			{
				for (int i = 0; i < bufferLength; i++)
				{
					resultBytes.add(inputBuffer[i]);
				}
			}

			byte[] results = new byte[resultBytes.size()];
			int index = 0;
			for (Byte obj : resultBytes)
			{
				results[index++] =  obj;
			}
			return results;
		}
	}

	/**
	 * Read string from HttpResponse
	 * 
	 * @param response
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String readStringFromResponse(HttpResponse response) throws IllegalStateException, IOException
	{
		HttpEntity responseEntity = response.getEntity();
		// long length = responseEntity.getContentLength();
		// String encoding = responseEntity.getContentEncoding().getValue();
		// String contentType = responseEntity.getContentType().getValue();
		try (InputStreamReader inputReader = new InputStreamReader(responseEntity.getContent(),
				Encoding.UTF8.getStrName()))
		{
			try (BufferedReader reader = new BufferedReader(inputReader))
			{
				StringBuffer buffer = new StringBuffer();
				String bufferLine;
				while ((bufferLine = reader.readLine()) != null)
				{
					buffer.append(bufferLine);
				}
				return buffer.toString();
			}
		}
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		Map<String, String> cookies = new HashMap<>();
		cookies.put("Cookie", "pgv_pvid=2761467104; sd_userid=35981465093557476; sd_cookie_crttime=1465093557476; wxtokenkey=15affb19443f80dabaecb14e8186d121ea128469c0f5ebc3471a304f528579a2; wxticket=1755238864; wxticketkey=e797d9542045e7941bef28f3467d4d61ea128469c0f5ebc3471a304f528579a2");
		doPost("http://mp.weixin.qq.com/mp/newappmsgvote", "action=vote&__biz=MzA5MjI2NTAzMg%3D%3D&uin=MTM3MzMxNjE0MA%3D%3D&key=f8ab7b995657050b03f6f7acdd3fcc872a858a3c0fe08025fa7d5726ea30a4a25966dc33835c9558b7118c183dcc3fe5&pass_ticket=u71dTlY%252BbN9%252BooW%252FSlINqMtu9iV9hV%252Fa%252B3Zj7ub6c3dzrhyLvcHs5G6TsVavUwvp&f=json&json=%7B%22super_vote_item%22%3A%5B%7B%22vote_id%22%3A5523878%2C%22item_idx_list%22%3A%7B%22item_idx%22%3A%5B%225%22%5D%7D%7D%2C%7B%22vote_id%22%3A5523879%2C%22item_idx_list%22%3A%7B%22item_idx%22%3A%5B%223%22%5D%7D%7D%2C%7B%22vote_id%22%3A5523880%2C%22item_idx_list%22%3A%7B%22item_idx%22%3A%5B%222%22%5D%7D%7D%2C%7B%22vote_id%22%3A5523881%2C%22item_idx_list%22%3A%7B%22item_idx%22%3A%5B%228%22%5D%7D%7D%2C%7B%22vote_id%22%3A5523882%2C%22item_idx_list%22%3A%7B%22item_idx%22%3A%5B%228%22%5D%7D%7D%2C%7B%22vote_id%22%3A5523883%2C%22item_idx_list%22%3A%7B%22item_idx%22%3A%5B%2210%22%5D%7D%7D%2C%7B%22vote_id%22%3A5523884%2C%22item_idx_list%22%3A%7B%22item_idx%22%3A%5B%224%22%2C%2224%22%5D%7D%7D%2C%7B%22vote_id%22%3A5523885%2C%22item_idx_list%22%3A%7B%22item_idx%22%3A%5B%225%22%5D%7D%7D%5D%2C%22super_vote_id%22%3A4002624%7D&idx=1&mid=504059642&wxtoken=585574792");
	}
}
