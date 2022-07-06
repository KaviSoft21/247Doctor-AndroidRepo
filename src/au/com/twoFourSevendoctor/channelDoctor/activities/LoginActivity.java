package au.com.twoFourSevendoctor.channelDoctor.activities;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import au.com.twoFourSevendoctor.channelDoctor.R;
import au.com.twoFourSevendoctor.channelDoctor.handlers.HTTPProvider;
import au.com.twoFourSevendoctor.channelDoctor.handlers.PasswordHandler;
import au.com.twoFourSevendoctor.channelDoctor.utilities.Contants;
import au.com.twoFourSevendoctor.channelDoctor.utilities.MessageDisplayer;


public class LoginActivity  extends Activity{

	private ProgressDialog pDialog;
	private static String URL;

	EditText passwordLogtxt,email;
	TextView bannertxt;
	Button loginbtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loadData();

		loginbtn=(Button) findViewById(R.id.loginbtn);
		loginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Validate
				email=(EditText) findViewById(R.id.userNameLotxt);
				passwordLogtxt=(EditText) findViewById(R.id.passwordLogtxt);
				bannertxt=(TextView) findViewById(R.id.loginbanner);

				if((email.getText().toString()).equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add email.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					email.setFocusable(true);
					passwordLogtxt.setFocusable(false);
					MessageDisplayer.ErrorToast(LoginActivity.this,"Please add email.");

				}else if(!isValidMail((email.getText().toString()))){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add valid email.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					email.setFocusable(true);
					passwordLogtxt.setFocusable(false);
					MessageDisplayer.ErrorToast(LoginActivity.this,"Please add email.");
				}else if ((passwordLogtxt.getText().toString()).equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add password.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					email.setFocusable(false);
					passwordLogtxt.setFocusable(true);
					MessageDisplayer.ErrorToast(LoginActivity.this,"Please add password.");
				}else{
					bannertxt.setVisibility(TextView.INVISIBLE);
					new  ProceedLogin().execute();
				}





			}
		});



		//Validate Email
		((EditText)findViewById(R.id.userNameLotxt)).setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					//System.out.println("Response User type finished");
					email=(EditText) findViewById(R.id.userNameLotxt);
					String emalVal=email.getText().toString();
					bannertxt=(TextView) findViewById(R.id.loginbanner);

					if(!email.equals("")){
						if(!isValidMail(emalVal)){
							//Display error banner
							bannertxt.setVisibility(TextView.VISIBLE);
							String bannerText="<font color='#EE0000'>Incorrect email address</font>";
							bannertxt.setText(Html.fromHtml(bannerText));
							MessageDisplayer.ErrorToast(LoginActivity.this,"Incorrect email address");

						}else{
							bannertxt.setVisibility(TextView.INVISIBLE);


						}

					}

				} 
			}
		});



	}

	private boolean isValidMail(String email)
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}






	public void loadData(){
		String colored = "*";

		TextView textuname = (TextView)findViewById(R.id.textUname);
		String unametTxt = "User Name";
		SpannableStringBuilder builderOne = new SpannableStringBuilder();

		builderOne.append(unametTxt);
		int startOne = builderOne.length();
		builderOne.append(colored);
		int endOne = builderOne.length();

		builderOne.setSpan(new ForegroundColorSpan(Color.RED), startOne, endOne, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textuname.setText(builderOne);


		TextView textUpass = (TextView)findViewById(R.id.textUpass);
		String passwordTxt = "Password";
		SpannableStringBuilder builderTwo= new SpannableStringBuilder();

		builderTwo.append( passwordTxt);
		int startT = builderTwo.length();
		builderTwo.append(colored);
		int endT = builderTwo.length();

		builderTwo.setSpan(new ForegroundColorSpan(Color.RED), startT, endT, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textUpass.setText(builderTwo);

	}

	class ProceedLogin extends AsyncTask<String, String, String>{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			//pDialog.setTitle("Please wait");
			pDialog.setMessage("Please wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			email=(EditText) findViewById(R.id.userNameLotxt);
			passwordLogtxt=(EditText) findViewById(R.id.passwordLogtxt);


			HashMap<String, String> param=new HashMap<String, String>();
			param.put(Contants.TAG_LOGIN, "LOGIN");
			param.put(Contants.TAG_UNAME, email.getText().toString());
			//param.put(Contants.TAG_PASS, PasswordHandler.md5(passwordLogtxt.getText().toString()) );
			param.put(Contants.TAG_PASS, passwordLogtxt.getText().toString());


			HTTPProvider d=new HTTPProvider();
			String response =d.doHTTPPostRequest(param);
			System.out.println("Response ***"+response.toString());

			return response;
		}

		protected void onPostExecute(String result) {
			// dismiss the dialog once data updated
			pDialog.dismiss();
			TextView bannertxt=(TextView) findViewById(R.id.loginbanner);

			String s=result.trim();
			JSONObject objServerResponse;
			try {
				System.out.println("Response 1 ");
				objServerResponse = new JSONObject(result);
				String returnString = objServerResponse.getString("success");

				System.out.println("Response 2 "+returnString);

				if(returnString.equalsIgnoreCase(Contants.TAG_SUCCESS)){
					//SUCCESS LOGIN
					String userId=objServerResponse.getString("userId");
					System.out.println("Response 3 ");

					SharedPreferences.Editor editor = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putBoolean("isloggedUser",true);
					editor.putString("userId", userId);
					System.out.println("Response saved id"+userId);
					editor.commit();

					//Get userID
					Intent intent=new Intent(LoginActivity.this, BookingActivity.class);
					startActivity(intent);
				}else {
					//LOGIN FAIL

					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Incorrect username or password.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					MessageDisplayer.ErrorToast(LoginActivity.this,"Incorrect username or password.");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}



	}

}
