package com.example.napwithfriends;

// packages required for full functionality 
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// instantiates login activity page and sets up touch screen features 
public class Login extends Activity implements OnClickListener {

	private EditText user, pass;
	private Button mSubmit, mRegister;

	// current amount loaded 
	private ProgressDialog pDialog;

	// instatiates parser 
	JSONParser jsonParser = new JSONParser();

	
	
	 // logging in via a localhost 	
	 //private static final String LOGIN_URL = "http:// 192.168.43.51:80/webservice/login.php";

	// Access via an emulator 
	private static final String LOGIN_URL = "http://10.0.2.2:80/webservice/login.php";

	// Accessing from Real Server 
	 //private static final String LOGIN_URL = "http://www.mybringback.com/webservice/login.php";

	// JSON response IDs 
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// method stub 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// login input screen 
		user = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);

		// buttons 
		mSubmit = (Button) findViewById(R.id.login);
		mRegister = (Button) findViewById(R.id.register);

		// event handlers for clicks 
		mSubmit.setOnClickListener(this);
		mRegister.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// click based action in reference to buttons 
		switch (v.getId()) {
		case R.id.login:
			new AttemptLogin().execute();
			break;
		case R.id.register:
			Intent i = new Intent(this, Register.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}
	// asychronous loads 
	class AttemptLogin extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// check for success 
			int success;
			String username = user.getText().toString();
			String password = pass.getText().toString();
			try {
				//  Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));

				Log.d("request!", "starting");
				//  HTTP request
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						params);

				// json response
				Log.d("Login attempt", json.toString());

				// json success 
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());
					// In the case that user data needs to be accessed 
					/*SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(Login.this);
					Editor edit = sp.edit();
					edit.putString("username", username);
					edit.commit();*/
					
					// intent for new activity page 
					Intent i = new Intent(Login.this, ReadComments.class);
					finish();
					startActivity(i);
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss when deleted 
			pDialog.dismiss();
			if (file_url != null) {
				// popup message 
				Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
			}

		}

	}

}
