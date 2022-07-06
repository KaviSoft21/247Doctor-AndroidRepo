package au.com.twoFourSevendoctor.channelDoctor.activities;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import au.com.twoFourSevendoctor.channelDoctor.R;

import au.com.twoFourSevendoctor.channelDoctor.handlers.HTTPProvider;
import au.com.twoFourSevendoctor.channelDoctor.utilities.Contants;
import au.com.twoFourSevendoctor.channelDoctor.utilities.MessageDisplayer;




public class CallbackActivity extends Activity{
	
	private ProgressDialog pDialog;
	private static String URL;
	
	Button callbackbtn;
	TextView bannertxt;
	EditText  firstNamecbtxt,lastnamecbtxt,contactnumcbtxt;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callback);
		callbackbtn = (Button) findViewById(R.id.callbackbtn);
		
		//Validate contact number 

		/*		((EditText)findViewById(R.id.contactNumtxt)).setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (!hasFocus) {
							contactnumcbtxt=(EditText) findViewById(R.id.contactNumtxt);
							String contactNumVal=contactnumcbtxt.getText().toString();
							bannertxt=(TextView) findViewById(R.id.callbackbanner);

							if(!isValidMobile(contactNumVal)){
								//Display error banner
								bannertxt.setVisibility(TextView.VISIBLE);
								String bannerText="<font color='#EE0000'>Please add valid contact number</font>";
								bannertxt.setText(Html.fromHtml(bannerText));
							//	nextOnebtn.setEnabled(false);
								ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
								//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
								contactnumcbtxt.setFocusable(true);
								MessageDisplayer.ErrorToast(CallbackActivity.this,"Please add valid contact number");
							}else{
								bannertxt.setVisibility(TextView.GONE);
								//nextOnebtn.setEnabled(true);
							}

						}
					}
				});
		
				*/
				
				
				callbackbtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						firstNamecbtxt=(EditText) findViewById(R.id.firstNamecbtxt);
						lastnamecbtxt=(EditText) findViewById(R.id.lastnamecbtxt);
						contactnumcbtxt=(EditText) findViewById(R.id.contactnumcbtxt);
						
						bannertxt=(TextView) findViewById(R.id.callbackbanner);

						if((firstNamecbtxt.getText().toString()).equals("")){

							bannertxt.setVisibility(TextView.VISIBLE);
							String bannerText="<font color='#EE0000'>Please add first name </font>";
							bannertxt.setText(Html.fromHtml(bannerText));

							//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
							firstNamecbtxt.setFocusable(true);
							MessageDisplayer.ErrorToast(CallbackActivity.this,"Please add first name");
							
						
						}
						
						else if((lastnamecbtxt.getText().toString()).equals("")){
							bannertxt.setVisibility(TextView.VISIBLE);
							String bannerText="<font color='#EE0000'>Please add last name </font>";
							bannertxt.setText(Html.fromHtml(bannerText));

							//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
							lastnamecbtxt.setFocusable(true);
							MessageDisplayer.ErrorToast(CallbackActivity.this,"Please add last name ");
						}else if((contactnumcbtxt.getText().toString()).equals("")){
							bannertxt.setVisibility(TextView.VISIBLE);
							String bannerText="<font color='#EE0000'>Please add  contact number</font>";
							bannertxt.setText(Html.fromHtml(bannerText));

							//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
							contactnumcbtxt.setFocusable(true);
							MessageDisplayer.ErrorToast(CallbackActivity.this,"Please add valid contact number");
							
						}else if(!isValidMobile(contactnumcbtxt.getText().toString()) || !isValidMedicare(contactnumcbtxt.getText().toString())){
								bannertxt.setVisibility(TextView.VISIBLE);
								String bannerText="<font color='#EE0000'>Please add valid contact number</font>";
								bannertxt.setText(Html.fromHtml(bannerText));

								//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
								contactnumcbtxt.setFocusable(true);
								MessageDisplayer.ErrorToast(CallbackActivity.this,"Please add valid contact number");
						}else {
							new callbackProceed().execute(); 

						}



					}

				});
				
	}
	
	
	private boolean isValidMobile(String phone) 
	{
		return android.util.Patterns.PHONE.matcher(phone).matches();    
	}


	private boolean isValidMedicare(String num) 
	{
		boolean returnVal=false;
		if(num.length() == 10){
			returnVal=true;
		}
		return returnVal;    
	}
	
	
	class callbackProceed extends AsyncTask<String, String, String>{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CallbackActivity.this);
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

			
			firstNamecbtxt=(EditText) findViewById(R.id.firstNamecbtxt);
			lastnamecbtxt=(EditText) findViewById(R.id.lastnamecbtxt);
			contactnumcbtxt=(EditText) findViewById(R.id.contactnumcbtxt);
		

			HashMap<String, String> param=new HashMap<String, String>();
			param.put(Contants.TAG_CALLBACK, "call");
			param.put(Contants.TAG_FNAME, firstNamecbtxt.getText().toString());
			param.put(Contants.TAG_LNAME, lastnamecbtxt.getText().toString());
			param.put(Contants.TAG_CONTNUM, contactnumcbtxt.getText().toString());

			

			HTTPProvider d=new HTTPProvider();
			String response =d.doHTTPPostRequest(param);
			System.out.println("Response ***"+response.toString());

			return response;
		}

		protected void onPostExecute(String result) {
			// dismiss the dialog once data updated
			pDialog.dismiss();
			TextView bannertxt=(TextView) findViewById(R.id.callbackbanner);

			String s=result.trim();
			JSONObject objServerResponse;
			try {
				System.out.println("Response 1 ");
				objServerResponse = new JSONObject(result);
				String returnString = objServerResponse.getString("success");

				System.out.println("Response 2 "+returnString);

				if(returnString.equalsIgnoreCase(Contants.TAG_SUCCESS)){
					//SUCCESS 
					
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#58d14d'>Request has been sent successfully.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					MessageDisplayer.ErrorToast(CallbackActivity.this,"Request has been sent successfully !!.");
					
				}else {
					//LOGIN FAIL

					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Oops Error !!.</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					MessageDisplayer.ErrorToast(CallbackActivity.this,"Oops Error !!.");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}



	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
