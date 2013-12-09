package com.example.napwithfriends;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ReadComments extends ListActivity {

	// Progress 
	private ProgressDialog pDialog;

	

	// localhost :
	 //private static final String READ_COMMENTS_URL = "http:// 192.168.43.51:80/webservice/comments.php";

	// Emulator:
	private static final String READ_COMMENTS_URL = "http://10.0.2.2:80/webservice/comments.php";

	// server:
	// private static final String READ_COMMENTS_URL = "http://www.mybringback.com/webservice/comments.php";

	// JSON 
	private static final String TAG_TITLE = "title";
	private static final String TAG_POSTS = "posts";
	private static final String TAG_FULLNAME = "fullname";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_EMAIL = "email";

	// comments in array 
	private JSONArray mComments = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;

	// sets layout for read comments page 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// note that use read_comments.xml instead of our single_post.xml
		setContentView(R.layout.read_comments);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// loading the comments 
		new LoadComments().execute();
	}

		// function for add comment activity access 
	public void addComment(View v) {
		Intent i = new Intent(ReadComments.this, AddComment.class);
		startActivity(i);
	}
	
	// access AddRequest page  
	public void addRequest(View view) {
		Intent i = new Intent(ReadComments.this, AddRequest.class);
		startActivity(i);
	}


	public void updateJSONdata() {

		// array list instantiated 
		mCommentList = new ArrayList<HashMap<String, String>>();

		
		JSONParser jParser = new JSONParser();
		
		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

		
		try {

			// number of posts available 
			mComments = json.getJSONArray(TAG_POSTS);

			// looping through objects returned 
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				// string in tags 
				String title = c.getString(TAG_TITLE);
				String content = c.getString(TAG_MESSAGE);
				String fullname = c.getString(TAG_FULLNAME);
				String email = c.getString(TAG_EMAIL);
				
				
				// Hashmap for reading on single comments 
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(TAG_TITLE, title);
				map.put(TAG_MESSAGE, content);
				map.put(TAG_FULLNAME, fullname);
				map.put(TAG_EMAIL, email); 

				// adding to ArrayList
				mCommentList.add(map);

				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts the parsed data into the listview.
	 */
	private void updateList() {
		// setup of list adaptor 
		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
				R.layout.single_comment, new String[] { TAG_TITLE, TAG_MESSAGE,
						TAG_FULLNAME, TAG_EMAIL }, new int[] { R.id.title, R.id.message,
						R.id.fullname, R.id.email });

		// instantiate adapter 
		setListAdapter(adapter);
		
		// action upon click 
		ListView lv = getListView();	
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				        ReadComments.this);
				

				//  Dialog Title
				alertDialog2.setTitle("Nap with this person?");
				 
				//  Dialog Message
				alertDialog2.setMessage("Are you sure?");
				 
				//  Icon to Dialog
				alertDialog2.setIcon(R.drawable.delete);
				 
				// Accept Button 
				alertDialog2.setPositiveButton("Accept",
				        new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				            	
				            	// changes layout color 
				            	 LinearLayout main = (LinearLayout) findViewById(R.id.top_layover); 
				            	 main.setBackgroundColor(0xff00ff00);
				            	 LinearLayout shell = (LinearLayout) findViewById(R.id.bottom_layover); 
				            	 shell.setBackgroundColor(0xff00ff00);
				                // Write your code here to execute after dialog
				                Toast.makeText(getApplicationContext(),
				                        "Accepted", Toast.LENGTH_SHORT)
				                        .show();
				            }
				        });
				// Decline 
				alertDialog2.setNegativeButton("Decline",
				        new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				            	
				            	// change layout color upon decline 
				            	 LinearLayout main = (LinearLayout) findViewById(R.id.top_layover); 
				            	 main.setBackgroundColor(0xff0099cc);
				            	 LinearLayout shell = (LinearLayout) findViewById(R.id.bottom_layover); 
				            	 shell.setBackgroundColor(0xff0099cc);
				                // Write your code here to execute after dialog
				                Toast.makeText(getApplicationContext(),
				                        "Declined", Toast.LENGTH_SHORT)
				                        .show();
				                dialog.cancel();
				            }
				        });
				 
				// Showing Alert
				alertDialog2.show();

			}
		});
	}

	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReadComments.this);
			pDialog.setMessage("Loading Comments...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			updateJSONdata();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			updateList();
		}
	}
}
