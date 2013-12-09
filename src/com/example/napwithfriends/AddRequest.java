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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRequest extends Activity implements OnClickListener{
	
	private EditText title, message, email;
	private Button  mSubmit;
	
	 // Dialog for load progress 
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    
    // localhost 
    //private static final String POST_COMMENT_URL = "http:// 192.168.43.51:80/webservice/addcomment.php";
    
    // Emulator:
    private static final String POST_COMMENT_URL = "http://10.0.2.2:80/webservice/addcomment.php";
    
  // server:
    //private static final String POST_COMMENT_URL = "http://www.mybringback.com/webservice/addcomment.php";
    
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//pointing to graphical layout for Request page 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_request);
		
		//ids for input fields 
		title = (EditText)findViewById(R.id.title);
		message = (EditText)findViewById(R.id.message);
		email = (EditText)findViewById(R.id.email);
		
		// button 
		mSubmit = (Button)findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
				new PostComment().execute();
	}
	
	
	class PostComment extends AsyncTask<String, String, String> {
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddRequest.this);
            pDialog.setMessage("Posting Request...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			 // Check for success 
            int success;
            String post_title = title.getText().toString();
            String post_message = message.getText().toString();
            String post_email = email.getText().toString();
            
          // Saved fullname Data
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AddRequest.this);
            String post_fullname = sp.getString("fullname", "No Name Available");
            
            try {
                //  Key Value Pairs 
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("fullname", post_fullname));
                params.add(new BasicNameValuePair("title", post_title));
                params.add(new BasicNameValuePair("message", post_message));
                params.add(new BasicNameValuePair("email", post_email));

                
 
                Log.d("request!", "starting");
                
                //Posting user data
                JSONObject json = jsonParser.makeHttpRequest(
                		POST_COMMENT_URL, "POST", params);
 
                // JSON
                Log.d("Post Comment attempt", json.toString());
 
                // JSON success 
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Added!", json.toString());    
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		
        protected void onPostExecute(String file_url) {
            // dismiss 
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(AddRequest.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
