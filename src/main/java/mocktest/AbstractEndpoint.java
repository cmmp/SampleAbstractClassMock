package mocktest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractEndpoint {

	public String request(String method, String url, String data) {
		HttpURLConnection connection = null;
		try {
			if ((method.compareTo("GET") == 0) && (data != null) && (!data.isEmpty())) {
				if (url.contains("?"))
					url = url.concat("&");
				else
					url = url.concat("?");
				url = url.concat(data);
			}
			// Create connection
			URL requestUrl = new URL(url);
			connection = (HttpURLConnection) requestUrl.openConnection();

			connection.setRequestMethod(method);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			if ((method.compareTo("GET") != 0) && (data != null) && (!data.isEmpty())) {
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));
				connection.setDoInput(true);
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(data);
				wr.flush();
				wr.close();
			}

			// Get Response
			int lastCode = connection.getResponseCode();
			// System.out.println("response code: " + lastCode);

			InputStream is;
			if (lastCode >= 400)
				is = connection.getErrorStream();
			else
				is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
		return null;
	}

}
