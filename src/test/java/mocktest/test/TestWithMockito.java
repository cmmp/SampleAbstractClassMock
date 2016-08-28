package mocktest.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import mocktest.AbstractEndpoint;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractEndpoint.class)
public class TestWithMockito {

	@Test
	public void testRequestValidData() throws Exception {
		
		AbstractEndpoint endpoint = Mockito.mock(AbstractEndpoint.class, Mockito.CALLS_REAL_METHODS);
			
		String method = "GET";
		String resource = "http://www.google.com";
		String data = "index.html";

		URL requestUrl = PowerMockito.mock(URL.class); // we need PowerMockito because URL is a final class
		HttpURLConnection conn = Mockito.mock(HttpURLConnection.class);
		InputStream is = Mockito.mock(InputStream.class);
		InputStreamReader isr = Mockito.mock(InputStreamReader.class);
		BufferedReader rd = Mockito.mock(BufferedReader.class);

		PowerMockito.whenNew(URL.class).withArguments(resource + "?" + data).thenReturn(requestUrl);
		PowerMockito.whenNew(InputStreamReader.class).withArguments(is).thenReturn(isr);
		PowerMockito.whenNew(BufferedReader.class).withArguments(isr).thenReturn(rd);

		PowerMockito.when(requestUrl.openConnection()).thenReturn(conn);
		
		PowerMockito.when(conn.getResponseCode()).thenReturn(200);
		PowerMockito.when(conn.getInputStream()).thenReturn(is);
		PowerMockito.when(rd.readLine()).thenReturn("Hello :-)").thenReturn(null);

		assertEquals("Hello :-)\r", endpoint.request(method, resource, data));
	}

}
