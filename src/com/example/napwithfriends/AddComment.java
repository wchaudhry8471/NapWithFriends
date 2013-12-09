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

public class AddComment extends Activity implements OnClickListener{
	
	// instantiates entry fields 
	private EditText title, message, email;
	private Button  mSubmit;
	
	 //  Dialog 
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    
    
    
    //localhost :  
    //private static final String POST_COMMENT_URL = "http:// 192.168.43.51:80/webservice/addcomment.php";
    
    // Emulator:
    private static final String POST_COMMENT_URL = "http://10.0.2.2:80/webservice/addcomment.php";
    
  //server:
    //private static final String POST_COMMENT_URL = "http://www.mybringback.com/webservice/addcomment.php";
    
    
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
    // add comment page link 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_comment);
		
		// check xml field EditText fields 
		title = (EditText)findViewById(R.id.title);
		message = (EditText)findViewById(R.id.message);
		email = (EditText)findViewById(R.id.email);
		
		// submit button 
		mSubmit = (Button)findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
				new PostComment().execute();
	}
	
	
	class PostComment extends AsyncTask<String, String, String> {
		
		// activity progress notification 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddComment.this);
            pDialog.setMessage("Posting Comment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// check for successfull conversion to String 
            int success;
            String post_title = title.getText().toString();
            String post_message = message.getText().toString();
            String post_email = email.getText().toString();
            
          // Saved name  
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AddComment.this);
            String post_username = sp.getString("fullname", "No Name Available");
            
            try {
                // Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("fullname", post_username));
                params.add(new BasicNameValuePair("title", post_title));
                params.add(new BasicNameValuePair("message", post_message));
                params.add(new BasicNameValuePair("email", post_email));
 
                Log.d("request!", "starting");
                
                // user data is Posted  
                JSONObject json = jsonParser.makeHttpRequest(
                		POST_COMMENT_URL, "POST", params);
 
                // JSON response element 
                Log.d("Post Comment attempt", json.toString());
 
                // success
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Comment Added!", json.toString());    
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(AddComment.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
