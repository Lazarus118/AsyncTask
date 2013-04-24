package com.joseluishdz.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	String json;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Task task = new Task();
		task.execute();
		TextView jsonText = (TextView)findViewById(R.id.json);
		jsonText.setText(json);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class Task extends AsyncTask<String, Long, String> {
		private ProgressDialog dialog;
		ProgressDialog pd;
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(MainActivity.this);
			pd.setMessage("Loadding");
			pd.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			HttpGet request = new HttpGet("http://answers.yahooapis.com/AnswersService/V1/getByCategory?appid=IEmbxB4g&category_id=396545664&output=json");
			HttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse;
			
			try {
	            httpResponse = client.execute(request);

	            HttpEntity entity = httpResponse.getEntity();

	            if (entity != null) {

	                InputStream instream = entity.getContent();
	                json = convertStreamToString(instream);
	                instream.close();
	            }

	        } catch (ClientProtocolException e)  {
	            client.getConnectionManager().shutdown();
	            e.printStackTrace();
	        } catch (IOException e) {
	            client.getConnectionManager().shutdown();
	            e.printStackTrace();
	        }
			return null;
		}
		
		protected void onProgressUpdate(Long... progress) {
		
		}
		
		protected void onPostExecute(String result) {
			pd.dismiss();
		}
		
		private String convertStreamToString(InputStream is) {

	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();

	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
		
	}

}
