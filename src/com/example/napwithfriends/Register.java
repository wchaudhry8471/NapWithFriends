package com.example.napwithfriends;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{
	
	private EditText user, pass, name, year, house, rm, gdr;
	private Button  mRegister;
	
	 // Dialog instantiated  
    private ProgressDialog pDialog;
 
    // JSONParser instantiated 
    JSONParser jsonParser = new JSONParser();
    
    // localhost 
    //private static final String REGISTER_URL = "http:// 192.168.43.51:80/webservice/register.php";
    
    // Emulator:
    private static final String REGISTER_URL = "http://10.0.2.2:80/webservice/register.php";
    
  // server:
    //private static final String REGISTER_URL = "http://www.mybringback.com/webservice/register.php";
    
    // saved ID
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		user = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);
		name = (EditText)findViewById(R.id.fullname);
		year = (EditText)findViewById(R.id.year);
		house = (EditText)findViewById(R.id.house);
		rm = (EditText)findViewById(R.id.room);
		gdr = (EditText)findViewById(R.id.gender);
		
		

		mRegister = (Button)findViewById(R.id.register);
		mRegister.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
				new CreateUser().execute();
		
	}
	
	class CreateUser extends AsyncTask<String, String, String> {

		// user creation dialog 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
        // convert user input from text to string and store 
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            String fullname = name.getText().toString();
            String yearstr = year.getText().toString();
            String housestr = house.getText().toString();
            String room = rm.getText().toString();
            String gender = gdr.getText().toString();
            
            try {
                //  Parameters for storage 
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("fullname", fullname));
                params.add(new BasicNameValuePair("year", yearstr));
                params.add(new BasicNameValuePair("house", housestr));
                params.add(new BasicNameValuePair("room", room));
                params.add(new BasicNameValuePair("gender", gender));
 
                Log.d("request!", "starting");
                
                //Post Data 
                JSONObject json = jsonParser.makeHttpRequest(
                       REGISTER_URL, "POST", params);
 
                // Read JSON 
                Log.d("Registering attempt", json.toString());
 
                // JSON success 
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("User Created!", json.toString()); 
                	
                	// save fullname for entry into Requests table 
                	SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(Register.this);
					Editor edit = sp.edit();
					edit.putString("fullname", fullname);										
					edit.commit();
					
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		
        protected void onPostExecute(String file_url) {
            // dismiss the Dialog 
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
