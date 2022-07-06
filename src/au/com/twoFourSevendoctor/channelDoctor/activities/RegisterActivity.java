package au.com.twoFourSevendoctor.channelDoctor.activities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;













import javax.net.ssl.HttpsURLConnection;













import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import au.com.twoFourSevendoctor.channelDoctor.R;
import au.com.twoFourSevendoctor.channelDoctor.JSON.JSONParser;
import au.com.twoFourSevendoctor.channelDoctor.handlers.HTTPProvider;
import au.com.twoFourSevendoctor.channelDoctor.handlers.PasswordHandler;
import au.com.twoFourSevendoctor.channelDoctor.utilities.Contants;
import au.com.twoFourSevendoctor.channelDoctor.utilities.MessageDisplayer;
import au.com.twoFourSevendoctor.channelDoctor.utilities.ResourceLoader;

public class RegisterActivity extends Activity{

	// Progress Dialog
	private ProgressDialog pDialog;

	EditText userNametxt;
	EditText passwordtxt;
	
	EditText fname,lname;
	EditText passwordConfirmtxt;
	Button registerBtn;
	TextView bannertxt;
	
	private boolean isEmailExists=false;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		loadData();

		userNametxt=(EditText) findViewById(R.id.userName);
		passwordtxt=(EditText) findViewById(R.id.password);
		passwordConfirmtxt=(EditText) findViewById(R.id.passwordConfirm);
		registerBtn=(Button) findViewById(R.id.reg_btn);
		fname = (EditText) findViewById(R.id.fRName);
		lname =(EditText) findViewById(R.id.lRName);



		//Validate Email
	/*	((EditText)findViewById(R.id.userName)).setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				bannertxt=(TextView) findViewById(R.id.regbanner);

				if (!hasFocus) {
					
					new EmailChecker().execute();
					
				} 
			}
		});*/


		//Check pass digits
		((EditText)findViewById(R.id.password)).setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				bannertxt=(TextView) findViewById(R.id.regbanner);

				if (!hasFocus) {
					//System.out.println("Response User type finished");
					passwordtxt=(EditText) findViewById(R.id.password);
					String passworVal=passwordtxt.getText().toString();


					if( (!passworVal.equals(""))  && passworVal.length() > 4 ){
						bannertxt.setVisibility(TextView.INVISIBLE);
					}else{
						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Password must have at least 5 digits. </font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						MessageDisplayer.ErrorToast(RegisterActivity.this,"Password must have at least 5 digits.");
					}

				}
			}


		});



		registerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//Check password same if success send to save 

				String userName=userNametxt.getText().toString();
				String password=passwordtxt.getText().toString();
				String passwordCon =passwordConfirmtxt.getText().toString();
				String lRame =lname.getText().toString();
				String fRname = fname.getText().toString();

				bannertxt=(TextView) findViewById(R.id.regbanner);
				ScrollView mainScrollView = (ScrollView) findViewById(R.id.regScroll);
				

				if(fRname.equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Add first name.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					fname.setFocusable(true);
					lname.setFocusable(false);
					passwordtxt.setFocusable(false);
					passwordConfirmtxt.setFocusable(false);
					userNametxt.setFocusable(false);
					MessageDisplayer.ErrorToast(RegisterActivity.this,"Add first name.");
					
				}else if(lRame.equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Add last name.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					fname.setFocusable(false);
					lname.setFocusable(true);
					passwordtxt.setFocusable(false);
					passwordConfirmtxt.setFocusable(false);
					userNametxt.setFocusable(false);
					MessageDisplayer.ErrorToast(RegisterActivity.this,"Add last name.");
					
				}else if(!isValidMail(userName) || userName.equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add valid email address. </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					fname.setFocusable(false);
					lname.setFocusable(false);
					passwordtxt.setFocusable(false);
					passwordConfirmtxt.setFocusable(false);
					userNametxt.setFocusable(true);
					MessageDisplayer.ErrorToast(RegisterActivity.this,"Please add valid email address.");
				/*}else if(isEmailExists){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Email address already exists. </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					mainScrollView.fullScroll(ScrollView.FOCUS_UP);*/
				}else if(password.length() <4){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Passwords should have at least 5 digits. </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					fname.setFocusable(false);
					lname.setFocusable(false);
					passwordtxt.setFocusable(true);
					passwordConfirmtxt.setFocusable(false);
					userNametxt.setFocusable(false);
					MessageDisplayer.ErrorToast(RegisterActivity.this,"Passwords should have at least 5 digits.");
				
				}else if(!password.equals(passwordCon)){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Passwords should be equal.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
				//	mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					fname.setFocusable(false);
					lname.setFocusable(false);
					passwordtxt.setFocusable(true);
					passwordConfirmtxt.setFocusable(true);
					userNametxt.setFocusable(false);
					MessageDisplayer.ErrorToast(RegisterActivity.this,"Passwords should be equal.");
				}
				
				else{
					//new RegisterUser().execute();
					new EmailChecker().execute();
				}


				/*
				if(userName.equals(passwordConfirmtxt) && userName.length() <4 && isValidMail(userNametxt)){
					new RegisterUser().execute();
				}else{
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Add valid data</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
				}*/


			}
		});
	}

	private boolean isValidMail(String email)
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}


	public void loadData(){
		String colored = "*";




		TextView textRFname = (TextView)findViewById(R.id.textRFname);
		String fnameTxt = "First Name";
		SpannableStringBuilder builderOnef = new SpannableStringBuilder();

		builderOnef.append(fnameTxt);
		int startOnef = builderOnef.length();
		builderOnef.append(colored);
		int endOnef = builderOnef.length();

		builderOnef.setSpan(new ForegroundColorSpan(Color.RED), startOnef, endOnef, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textRFname.setText(builderOnef);

		//++++

		TextView textRlname = (TextView)findViewById(R.id.textRlname);
		String lnameTxt = "Last Name";
		SpannableStringBuilder builderOnel = new SpannableStringBuilder();

		builderOnel.append(lnameTxt);
		int startOnel = builderOnel.length();
		builderOnel.append(colored);
		int endOnel = builderOnel.length();

		builderOnel.setSpan(new ForegroundColorSpan(Color.RED), startOnel, endOnel, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textRlname.setText(builderOnel);

		//+++++++++++++

		//++++

		TextView userName = (TextView)findViewById(R.id.textRUname);
		String userNameTxt = "Email";
		SpannableStringBuilder builderOne = new SpannableStringBuilder();

		builderOne.append(userNameTxt);
		int startOne = builderOne.length();
		builderOne.append(colored);
		int endOne = builderOne.length();

		builderOne.setSpan(new ForegroundColorSpan(Color.RED), startOne, endOne, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		userName.setText(builderOne);

		//+++++++++


		TextView textRUpass = (TextView)findViewById(R.id.textRUpass);
		String passRTxt = "Password";
		SpannableStringBuilder builderTw = new SpannableStringBuilder();

		builderTw.append(passRTxt);
		int startTw = builderTw.length();
		builderTw.append(colored);
		int endTw = builderTw.length();

		builderTw.setSpan(new ForegroundColorSpan(Color.RED), startTw, endTw, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textRUpass.setText(builderTw);

		//+++++


		TextView textRConUpass = (TextView)findViewById(R.id.textRConUpass);
		String passRConTxt = "Confirm Password";
		SpannableStringBuilder builderTh = new SpannableStringBuilder();

		builderTh.append(passRConTxt);
		int startTh =builderTh.length();
		builderTh.append(colored);
		int endTh = builderTh.length();

		builderTh.setSpan(new ForegroundColorSpan(Color.RED), startTh, endTh, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textRConUpass.setText(builderTh);
	}



	class RegisterUser extends AsyncTask<String, String, String>{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage("Saving information ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}


		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String userName=userNametxt.getText().toString();
			String password=passwordtxt.getText().toString();
			String firstname=fname.getText().toString();
			String lastname=lname.getText().toString();
			/*InputStream is = null;
			 String result= null;*/
			// Building Parameters

			HashMap<String, String> param=new HashMap<String, String>();
			param.put(Contants.TAG_REGISTRATION, "reg");
			param.put(Contants.TAG_FNAME, firstname);
			param.put(Contants.TAG_LNAME, lastname);
			param.put(Contants.TAG_USERNAME, userName);
			//param.put(Contants.TAG_PASSWORD,  PasswordHandler.md5(password));
			param.put(Contants.TAG_PASSWORD,password);


			HTTPProvider d=new HTTPProvider();
			String response =d.doHTTPPostRequest(param);
			System.out.println("Response 2"+response.toString());

			return response;

		}


		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String result) {
			// dismiss the dialog once data updated
			pDialog.dismiss();
			String s=result.trim();

			if(s.equalsIgnoreCase(Contants.TAG_SUCCESS)){
				Intent intent=new Intent(RegisterActivity.this,BookingActivity.class);
				startActivity(intent);
			}else {
				Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
			}




			JSONObject objServerResponse;
			try {
				System.out.println("Response 1 ");
				objServerResponse = new JSONObject(result);
				String returnString = objServerResponse.getString("success");

				System.out.println("Response 2 "+returnString);

				if(returnString.equalsIgnoreCase(Contants.TAG_SUCCESS)){
					//SUCCESS LOGIN
					bannertxt.setVisibility(TextView.GONE);

					System.out.println("Response 3 "); 
					String userId= objServerResponse.getString("userID");

					SharedPreferences.Editor editor = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putBoolean("isloggedUser",true);
					editor.putString("userId", userId);
					System.out.println("Response saved id"+userId);
					editor.commit(); 

					//Get userID
					Intent intent=new Intent(RegisterActivity.this, BookingActivity.class);
					startActivity(intent);
				}else {
					//Reg FAIL
					
					ConnectivityManager connectivityManager 
			          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			    if( activeNetworkInfo == null && !activeNetworkInfo.isConnected()){
			    	bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>  You don't have internet!!  Please try again later. </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					MessageDisplayer.ErrorToast(RegisterActivity.this,"You don't have internet!!  Please try again later.");
			    }else{ 
			    	bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'> Oops! An error occurred.  Please try again. </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					MessageDisplayer.ErrorToast(RegisterActivity.this," Oops! An error occurred.  Please try again.");
			    }
			    
			    
					/*bannertxt=(TextView) findViewById(R.id.regbanner);
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Oops! An error occurred. Please try again.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
*/
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}

	class EmailChecker extends AsyncTask<String, String, String>{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage("Please wait ...");
			pDialog.setTitle("Check availability of username");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}


		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String userName=userNametxt.getText().toString();


			HashMap<String, String> param=new HashMap<String, String>();
			param.put(Contants.TAG_CHECKUSERNAME, "checkUsername");
			param.put(Contants.TAG_USERNAME, userName);



			HTTPProvider d=new HTTPProvider();
			String response =d.doHTTPPostRequest(param);
			System.out.println("Response 2"+response.toString());

			return response;

		}


		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String result) {
			// dismiss the dialog once data updated
			pDialog.dismiss();
			bannertxt=(TextView) findViewById(R.id.regbanner);

			JSONObject objServerResponse;
			try {
				System.out.println("Response 1 ");
				objServerResponse = new JSONObject(result);
				String returnString = objServerResponse.getString("SUCCESS");

				System.out.println("Response 2 "+returnString);

				if(returnString.equalsIgnoreCase(Contants.TAG_SUCCESS)){
					//Email already exists
					System.out.println("Response email address already exists.");
					isEmailExists = true;
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>This email already exists</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					MessageDisplayer.ErrorToast(RegisterActivity.this,"This email already exists");


				}else{
					new RegisterUser().execute();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}


}
