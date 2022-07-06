package au.com.twoFourSevendoctor.channelDoctor.activities;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.R.string;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import au.com.twoFourSevendoctor.channelDoctor.R;
import au.com.twoFourSevendoctor.channelDoctor.handlers.HTTPProvider;
import au.com.twoFourSevendoctor.channelDoctor.utilities.Contants;
import au.com.twoFourSevendoctor.channelDoctor.utilities.MessageDisplayer;

public class BookingActivity extends Activity implements View.OnClickListener{
	private ProgressDialog pDialog;


	Button btnTimePicker;

	EditText  txtTime,btnDatePicker,btn_expiryDate,email,postcodetxt,contactNum,medicareNum,firstname;
	Spinner genderspn,preDoctor,preTime,preGen;
	private int mYear, mMonth, mDay, mHour, mMinute;
	JSONArray doctors = null;
	TextView bannertxt;
	private boolean isPostcodeValidate,userClickNext;
	private boolean hasData= false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		/*email=(EditText) findViewById(R.id.emailtxt);
		email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);*/

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);
		isPostcodeValidate=false;
		userClickNext=false;
		
		postcodetxt = (EditText) findViewById(R.id.postcodetxt);
		postcodetxt.setEnabled(false);

		

		final Button nextOnebtn=(Button) findViewById(R.id.nextOnebtn);
		Button backbtn=(Button) findViewById(R.id.Backbtn);
		Button nextTwobnt=(Button) findViewById(R.id.nextTwobtn);

		final LinearLayout secOne=(LinearLayout) findViewById(R.id.section_1);
		final LinearLayout secTwo=(LinearLayout) findViewById(R.id.section_2);
		secTwo.setVisibility(LinearLayout.GONE);
		loadData();

		
		
		
		System.out.println("ERROR test 1");
 
		postcodetxt=(EditText) findViewById(R.id.postcodetxt);


		genderspn = (Spinner) findViewById(R.id.spinnerGender);
		List<String> list = new ArrayList<String>();
		list.add("Male");
		list.add("Female");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		genderspn.setAdapter(dataAdapter);

		System.out.println("ERROR test 2");
		
		//Need to remove after got data from database ++++++++++++++++++

		/*preDoctor = (Spinner) findViewById(R.id.spinnerPreDoc);
		List<String> listdoc = new ArrayList<String>();
		listdoc.add("James Smith");
		listdoc.add("Marry Huggins");
		listdoc.add("Lorra Devis");
		ArrayAdapter<String> dataAdapterDoc = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listdoc);
		dataAdapterDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		preDoctor.setAdapter(dataAdapterDoc);

		 */		//++++++++++++++++++++++++++++++++++++++++

		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		//getTime() returns the current date in default time zone
		Date date = calendar.getTime();
		int day = calendar.get(Calendar.DATE);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		System.out.println("ERROR test 3");
		
		preTime = (Spinner) findViewById(R.id.spinnerTime);
		List<String> lists = new ArrayList<String>();

		if (dayOfWeek == 1){
			/*lists.add("4pm to 6pm");*/
			lists.add("8am to 10am");
			lists.add("10am to 12pm");
			lists.add("12pm to 2pm");
			lists.add("2pm to 4pm");
			lists.add("4pm to 6pm");
			lists.add("6pm to 8pm");
			lists.add("8pm to 10pm");
			lists.add("10pm to 12am");


		}else if(dayOfWeek == 7){
			lists.add("12pm to 2pm");
			lists.add("2pm to 4pm");
			lists.add("4pm to 6pm");
			lists.add("6pm to 8pm");
			lists.add("8pm to 10pm");
			lists.add("10pm to 12am");
		}else{
			lists.add("6pm to 8pm");
			lists.add("8pm to 10pm");
			lists.add("10pm to 12am");
		}

		System.out.println("ERROR test 4");
		
		ArrayAdapter<String> dataAdapters = new ArrayAdapter<String>(BookingActivity.this,android.R.layout.simple_spinner_item,lists);
		dataAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		preTime.setAdapter(dataAdapters);

		
		//ADD HERE
		
		
		

		/*	//Add doctor ID
		preGen = (Spinner) findViewById(R.id.spinnerPreDocGen);
		List<String> listg = new ArrayList<String>();
		listg.add("Male");
		listg.add("Female");

		ArrayAdapter<String> dataAdapterg = new ArrayAdapter<String>(BookingActivity.this,android.R.layout.simple_spinner_item,listg);
		dataAdapterg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		preGen.setAdapter(dataAdapterg);


		 */

		//+++++++++++++++++++++++++++++++++++
		
		System.out.println("ERROR test 5");
		
		
		btnDatePicker=(EditText) findViewById(R.id.btn_date);
		//  btnTimePicker=(Button)findViewById(R.id.btn_time);
		//     txtDate=(EditText)findViewById(R.id.in_date);
		// txtTime=(EditText)findViewById(R.id.in_time);

		btnDatePicker.setOnClickListener( this);

		btn_expiryDate=(EditText) findViewById(R.id.btn_expiryDate);
		btn_expiryDate.setOnClickListener( this);









		//++++++++++++++++++++++++++++++++

		//Validate Email
		((EditText)findViewById(R.id.emailtxt)).setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					//System.out.println("Response User type finished");
					email=(EditText) findViewById(R.id.emailtxt);
					String emalVal=email.getText().toString();
					bannertxt=(TextView) findViewById(R.id.bookingbanner);


					if(!isValidMail(emalVal)){
						//Display error banner
						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add valid email address</font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						///nextOnebtn.setEnabled(false);
						ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
						//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
						//nextOnebtn.setBackgroundColor(Color.GRAY);
						email.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add valid email address");
					}else{
						bannertxt.setVisibility(TextView.INVISIBLE);
						///nextOnebtn.setEnabled(true);
					}

				} 
			}
		});

		//Validate contact number 

		((EditText)findViewById(R.id.contactNumtxt)).setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					contactNum=(EditText) findViewById(R.id.contactNumtxt);
					String contactNumVal=contactNum.getText().toString();
					bannertxt=(TextView) findViewById(R.id.bookingbanner);

					if(!isValidMobile(contactNumVal)){
						//Display error banner
						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add valid contact number</font>";
						bannertxt.setText(Html.fromHtml(bannerText));
					//	nextOnebtn.setEnabled(false);
						ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
						//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
						contactNum.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add valid contact number");
					}else{
						bannertxt.setVisibility(TextView.GONE);
						//nextOnebtn.setEnabled(true);
					}

				}
			}
		});

		//Validate Medicare Number

		((EditText)findViewById(R.id.ediNumbertxt)).setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					medicareNum=(EditText) findViewById(R.id.ediNumbertxt);
					String medicareNumVal=medicareNum.getText().toString();
					bannertxt=(TextView) findViewById(R.id.bookingbanner);

					if(!isValidMedicare(medicareNumVal)){
						//Display error banner
						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add valid medicare number</font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
						mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					//	nextOnebtn.setEnabled(false);
						medicareNum.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add valid medicare number");
						
					}else{
						bannertxt.setVisibility(TextView.GONE);
						//nextOnebtn.setEnabled(true);
					}

				}
			}
		});



		//Chcekc suburb
		((EditText)findViewById(R.id.suburbtxt)).setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					//System.out.println("Response User type finished");
					new PostCodeChecker().execute();

				}
			}
		}); 



		nextTwobnt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText ediNumbertxt=(EditText) findViewById(R.id.ediNumbertxt);
				EditText refNumtxt=(EditText) findViewById(R.id.refNumtxt);
				EditText btn_expiryDate=(EditText) findViewById(R.id.btn_expiryDate);
				ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
				bannertxt=(TextView) findViewById(R.id.bookingbanner);

				if((ediNumbertxt.getText().toString()).equals("") || (ediNumbertxt.getText().toString()).length() != 10){

					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add medicare number </font>";
					bannertxt.setText(Html.fromHtml(bannerText));

					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					ediNumbertxt.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add medicare number");
					
				}else if(!isMedicareValid((ediNumbertxt.getText().toString()),false)){
					
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Invalid Medicare number</font>";
					bannertxt.setText(Html.fromHtml(bannerText));

					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					ediNumbertxt.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Invalid Medicare number");
				}
				
				else if(( refNumtxt.getText().toString()).equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add refference number </font>";
					bannertxt.setText(Html.fromHtml(bannerText));

					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					refNumtxt.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add refference number");
				}else if((btn_expiryDate.getText().toString()).equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add expiry date</font>";
					bannertxt.setText(Html.fromHtml(bannerText));

					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					btn_expiryDate.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add expiry date");
				}else {
					new BookingExecuter().execute();

				}



			}

		});




		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				secOne.setVisibility(LinearLayout.VISIBLE);
				secTwo.setVisibility(LinearLayout.GONE);
			}
		});


		nextOnebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// View list = (View)findViewById(R.id.myviewId); 
				//secTwo.setVisibility(RelativeLayout.VISIBLE);
				/*secOne.setVisibility(LinearLayout.GONE);
	    	secTwo.setVisibility(LinearLayout.VISIBLE);*/
				
				userClickNext = true;
				new PostCodeChecker().execute();
				
			/*	PostCodeChecker pc=new PostCodeChecker();
				
				if(pc.getStatus() == AsyncTask.Status.FINISHED){

					System.out.println("isPostcodeValidate"+isPostcodeValidate);
					
					
					SimpleDateFormat format = new SimpleDateFormat("d-MM-yyy");  

					EditText streettxt=(EditText) findViewById(R.id.streettxt);
					EditText suburbtxt=(EditText)findViewById(R.id.suburbtxt);
					EditText postcodetxt=(EditText) findViewById(R.id.postcodetxt);
					EditText firstNametxt=(EditText) findViewById(R.id.firstNametxt);
					EditText lastNametxt=(EditText) findViewById(R.id.lastNametxt);
					EditText email=(EditText) findViewById(R.id.emailtxt);
					EditText contactNumtxt=(EditText) findViewById(R.id.contactNumtxt);
					EditText btn_date=(EditText) findViewById(R.id.btn_date);
					ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);


					bannertxt=(TextView) findViewById(R.id.bookingbanner);

					if( (streettxt.getText().toString()).equals("")){

						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add street</font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						secOne.setVisibility(LinearLayout.VISIBLE);
						secTwo.setVisibility(LinearLayout.GONE);
						streettxt.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add street");
						
					}else if((suburbtxt.getText().toString()).equals("")){
						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add suburb </font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						secOne.setVisibility(LinearLayout.VISIBLE);
						secTwo.setVisibility(LinearLayout.GONE);	
						//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
						suburbtxt.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add suburb");
					}else if((postcodetxt.getText().toString()).equals("")){
						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add postcode </font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						secOne.setVisibility(LinearLayout.VISIBLE);
						secTwo.setVisibility(LinearLayout.GONE);	

					}else if((firstNametxt.getText().toString()).equals("")){

						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add first name </font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						secOne.setVisibility(LinearLayout.VISIBLE);
						secTwo.setVisibility(LinearLayout.GONE);
						//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
						firstNametxt.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add first name");

					}else if((lastNametxt.getText().toString()).equals("")){
						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add last name </font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						secOne.setVisibility(LinearLayout.VISIBLE);
						secTwo.setVisibility(LinearLayout.GONE);
						//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
						lastNametxt.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add last name");
					}else if((email.getText().toString()).equals("") || !isValidMail((email.getText().toString()))){

						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add valid email </font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						secOne.setVisibility(LinearLayout.VISIBLE);
						secTwo.setVisibility(LinearLayout.GONE);
						//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
						email.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add valid email ");
					}else if((contactNumtxt.getText().toString()).equals("") || ((contactNumtxt.getText().toString()).length() != 10) ){

						bannertxt.setVisibility(TextView.VISIBLE);
						String bannerText="<font color='#EE0000'>Please add valid contact number </font>";
						bannertxt.setText(Html.fromHtml(bannerText));
						secOne.setVisibility(LinearLayout.VISIBLE);
						secTwo.setVisibility(LinearLayout.GONE);
						//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
						streettxt.setFocusable(false);
						contactNumtxt.setFocusable(true);
						MessageDisplayer.ErrorToast(BookingActivity.this,"Please add valid contact number");
					
					} else
						try {
							if((btn_date.getText().toString()).equals("") ||  (System.currentTimeMillis() < (format.parse((btn_date.getText().toString()))).getTime())){

								bannertxt.setVisibility(TextView.VISIBLE);
								String bannerText="<font color='#EE0000'>Please add birth day </font>";
								bannertxt.setText(Html.fromHtml(bannerText));
								secOne.setVisibility(LinearLayout.VISIBLE);
								secTwo.setVisibility(LinearLayout.GONE);
							//	mainScrollView.fullScroll(ScrollView.FOCUS_UP);
								btn_date.setFocusable(true);
								MessageDisplayer.ErrorToast(BookingActivity.this,"Please add birth day");
							}else if(!isPostcodeValidate){
								bannertxt.setVisibility(TextView.VISIBLE);
								String bannerText="<font color='#EE0000'>Sorry!! We are not servicing for this suburb</font>";
								bannertxt.setText(Html.fromHtml(bannerText));
								secOne.setVisibility(LinearLayout.VISIBLE);
								secTwo.setVisibility(LinearLayout.GONE);
								//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
								suburbtxt.setFocusable(true);
								MessageDisplayer.ErrorToast(BookingActivity.this,"Sorry!! We are not servicing for this suburb");
							}else {
								
								bannertxt.setVisibility(TextView.GONE);
								secOne.setVisibility(LinearLayout.GONE);
								secTwo.setVisibility(LinearLayout.VISIBLE);
								//new PostCodeChecker().execute();
								//	new loadSpinnnerData().execute();


							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}else{
					System.out.println("isPostcodeValidate  not fileshed");
				}
				
				
				*/

			}
		});

		System.out.println("ERROR test 6");
		
		if(isLoggedUser()){
			System.out.println("ERROR logged user");
		 new fillDefaultData().execute();
		}else{
			System.out.println("ERROR not logged user");
			SharedPreferences prefs = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE); 
		if(prefs.getBoolean("hasData", false)){
			//hasData = true;
			System.out.println("ERROR HAS DATA");
			fillData();
			new PostCodeChecker().execute();
		}

		}

	}


	private void fillData(){
		System.out.println("ERROR not logged user start");
		SharedPreferences prefs = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE); 
		((EditText)findViewById(R.id.streettxt)).setText(prefs.getString("street", ""));
		((EditText)findViewById(R.id.suburbtxt)).setText(prefs.getString("suburb", ""));
		((EditText)findViewById(R.id.postcodetxt)).setText(prefs.getString("postcode", ""));

		((EditText)findViewById(R.id.firstNametxt)).setText(prefs.getString("firstname", ""));
		((EditText)findViewById(R.id.lastNametxt)).setText(prefs.getString("lastname", ""));
		
		((EditText)findViewById(R.id.emailtxt)).setText(prefs.getString("email", ""));
		
		
		((EditText)findViewById(R.id.contactNumtxt)).setText(prefs.getString("contactNo", ""));
		((EditText)findViewById(R.id.btn_date)).setText(prefs.getString("bday", ""));
		((EditText)findViewById(R.id.ediNumbertxt)).setText(prefs.getString("medicareNo", ""));
		((EditText)findViewById(R.id.refNumtxt)).setText(prefs.getString("refferenceNo", ""));
		((EditText)findViewById(R.id.btn_expiryDate)).setText(prefs.getString("expiryDate", ""));
		
		/*Spinner sp=((Spinner)findViewById(R.id.spinnerGender));
		String gender=prefs.getString("gender", "");
		
		
		if(gender.equals("0")){
			sp.setSelection(1);
		}else if(gender.equals("1")){
			sp.setSelection(2);
		}*/

		/*prefs = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE); 
		if(prefs.getBoolean("isloggedUser", false)){
			((EditText)findViewById(R.id.emailtxt)).setEnabled(false);
		}*/

		
		System.out.println("ERROR not logged user done");

	}


	private boolean isLoggedUser(){

		SharedPreferences prefs = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE); 
		boolean val = prefs.getBoolean("isloggedUser", false);

		return val ;
	}

	private boolean isValidMail(String email)
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
	
	
	public static boolean isMedicareValid(String input, boolean validateWithIRN){
		
		
	    int[] multipliers = new int[]{1, 3, 7, 9, 1, 3, 7, 9};
	    String pattern = "^(\\d{8})(\\d)";
	    String medicareNumber = input.replace(" " , "");
	    int length = validateWithIRN ? 11 : 10;

	    if (medicareNumber.length() != length) {return false;}
	    
	    

	    Pattern medicatePattern = Pattern.compile(pattern);
	    Matcher matcher = medicatePattern.matcher(medicareNumber);
	    
	    
	    if (matcher.find()){

	        String base = matcher.group(1);
	        String checkDigit = matcher.group(2);

	        
	        
	        int total = 0;
	        for (int i = 0; i < multipliers.length; i++){
	            total += base.charAt(i) * multipliers[i];
	        }

	         
	        return ((total % 10) == Integer.parseInt(checkDigit));
	    }

	    return false;

	}
	
	
	
	
	


	public void loadData(){
		String colored = "*";

		TextView textstreet = (TextView)findViewById(R.id.textstreet);
		String streetTxt = "Street";
		SpannableStringBuilder builderOne = new SpannableStringBuilder();

		builderOne.append(streetTxt);
		int startOne = builderOne.length();
		builderOne.append(colored);
		int endOne = builderOne.length();

		builderOne.setSpan(new ForegroundColorSpan(Color.RED), startOne, endOne, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textstreet.setText(builderOne);

		//++++++++++++++

		TextView textsuburb = (TextView)findViewById(R.id.textsuburb);
		String suburbTxt = "Suburb";
		SpannableStringBuilder builderTwo = new SpannableStringBuilder();

		builderTwo.append(suburbTxt);
		int startTwo = builderTwo.length();
		builderTwo.append(colored);
		int endTwo = builderTwo.length();

		builderTwo.setSpan(new ForegroundColorSpan(Color.RED), startTwo, endTwo, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textsuburb.setText(builderTwo);


		//+++++++++++++++
		TextView textpostcode = (TextView)findViewById(R.id.textpostcode);
		String postcodeTxt = "Postcode";
		SpannableStringBuilder builderThree = new SpannableStringBuilder();

		builderThree.append(postcodeTxt);
		int startThree = builderThree.length();
		builderThree.append(colored);
		int endThree = builderThree.length();

		builderThree.setSpan(new ForegroundColorSpan(Color.RED), startThree, endThree, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textpostcode.setText(builderThree);

		//+++++++++++++++++++++++++++++++++++++++++++

		TextView textfname = (TextView)findViewById(R.id.textfname);
		String fnameTxt = "First Name";
		SpannableStringBuilder builderFour = new SpannableStringBuilder();

		builderFour.append(fnameTxt);
		int startFour = builderFour.length();
		builderFour.append(colored);
		int endFour = builderFour.length();

		builderFour.setSpan(new ForegroundColorSpan(Color.RED), startFour, endFour, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textfname.setText(builderFour);

		//++++++++++++++++++++++++
		TextView textlname = (TextView)findViewById(R.id.textlastname);	
		String lnameTxt = "Last Name";
		SpannableStringBuilder builderFive = new SpannableStringBuilder();

		builderFive.append(lnameTxt );
		int startFive = builderFive.length();
		builderFive.append(colored);
		int endFive = builderFive.length();

		builderFive.setSpan(new ForegroundColorSpan(Color.RED), startFive, endFive, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textlname.setText(builderFive);

		//++++++++++++++++++++++++++

		TextView textemail = (TextView)findViewById(R.id.email);
		String emailTxt = "Email";
		SpannableStringBuilder builderSix = new SpannableStringBuilder();

		builderSix.append(emailTxt);
		int startSix = builderSix.length();
		builderSix.append(colored);
		int endSix = builderSix.length();

		builderSix.setSpan(new ForegroundColorSpan(Color.RED), startSix, endSix, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textemail.setText(builderSix);

		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		TextView textcontactNum = (TextView)findViewById(R.id.contactNumber);	
		String contactNumTxt = "Contact Number";
		SpannableStringBuilder builderSev = new SpannableStringBuilder();

		builderSev.append( contactNumTxt);
		int startSev = builderSev.length();
		builderSev.append(colored);
		int endSev = builderSev.length();

		builderSev.setSpan(new ForegroundColorSpan(Color.RED), startSev, endSev, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textcontactNum.setText(builderSev);

		//++++++++++++++++++++++++++++++++++++++++++++

		TextView textdob = (TextView)findViewById(R.id.dobtext);	
		String dobTxt = "Date of Birth";
		SpannableStringBuilder builderEi = new SpannableStringBuilder();

		builderEi.append(dobTxt);
		int startEi = builderEi.length();
		builderEi.append(colored);
		int endEi = builderEi.length();

		builderEi.setSpan(new ForegroundColorSpan(Color.RED), startEi, endEi, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textdob.setText(builderEi);

		//+++++++++++++++++++

		/*TextView textgender = (TextView)findViewById(R.id.textGender);	
		String genderTxt = "Gender";
		SpannableStringBuilder builderNin = new SpannableStringBuilder();

		builderNin.append(genderTxt);
		int startNin = builderNin.length();
		builderNin.append(colored);
		int endNin = builderNin.length();

		builderNin.setSpan(new ForegroundColorSpan(Color.RED), startNin, endNin, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textgender.setText(builderNin);*/


		//++++++++++++++++++

		TextView textmediNum = (TextView)findViewById(R.id.ediNumber);	
		String mediNumTxt = "Medicare Number";
		SpannableStringBuilder builderTen = new SpannableStringBuilder();

		builderTen.append(mediNumTxt);
		int startTen = builderTen.length();
		builderTen.append(colored);
		int endTen = builderTen.length();

		builderTen.setSpan(new ForegroundColorSpan(Color.RED),startTen, endTen, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textmediNum.setText(builderTen);

		//+++++++

		TextView textrefNum = (TextView)findViewById(R.id.refNumtext);	
		String refNumTxt = "Refference Number";
		SpannableStringBuilder builderEle = new SpannableStringBuilder();

		builderEle.append(refNumTxt );
		int startEle = builderEle.length();
		builderEle.append(colored);
		int endEle = builderEle.length();

		builderEle.setSpan(new ForegroundColorSpan(Color.RED), startEle, endEle, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textrefNum.setText(builderEle);

		//++++++++++++++++++

		TextView textexpDate = (TextView)findViewById(R.id.textexpiryDate);	
		String expDateTxt = "Expiry Date";
		SpannableStringBuilder builderTwel = new SpannableStringBuilder();

		builderTwel .append(expDateTxt);
		int startTwel = builderTwel .length();
		builderTwel .append(colored);
		int endTwel= builderTwel .length();

		builderTwel .setSpan(new ForegroundColorSpan(Color.RED),startTwel,endTwel, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textexpDate.setText(builderTwel );



	}


	//++++++++++++++++++++++



	@Override
	public void onClick(View v) {

		if (v == btnDatePicker) {

			// Get Current Date
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);


			DatePickerDialog datePickerDialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					String myFormat = "MM/dd/yy"; //In which you need put here
					SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

					btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

				}
			}, mYear, mMonth, mDay);
			datePickerDialog.show();
		}


		if (v == btn_expiryDate) {

			// Get Current Date
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);


			DatePickerDialog datePickerDialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					String myFormat = "MM/yy"; //In which you need put here
					SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

					btn_expiryDate.setText((monthOfYear + 1) + "-" + year);

				}
			}, mYear, mMonth, mDay);


			((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

			datePickerDialog.show();
		}
	}


	//++++++++++++++++++++




	class PostCodeChecker extends AsyncTask<String, String, String>{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BookingActivity.this);
			//pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setTitle("Checking Location");
			pDialog.setMessage("Please wait  ...");
			//pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			EditText suburbtxt=(EditText) findViewById(R.id.suburbtxt);
			String postcode=suburbtxt.getText().toString();
			HashMap<String, String> param=new HashMap<String, String>();
			param.put(Contants.TAG_POST, "POSTCODE");
			param.put(Contants.TAG_POSTCODE, postcode.toUpperCase());

			HTTPProvider d=new HTTPProvider();
			String response =d.doHTTPPostRequest(param);
			System.out.println("TEST postcode Response ***"+response.toString());

			return response;
		}

		protected void onPostExecute(String result) {
			// dismiss the dialog once data updated
			pDialog.dismiss();
			Button nextOnebtn=(Button) findViewById(R.id.nextOnebtn);

			bannertxt=(TextView) findViewById(R.id.bookingbanner);


			String s=result.trim();
			JSONObject objServerResponse;
			try {

				objServerResponse = new JSONObject(result);
				String returnString = objServerResponse.getString("SUCCESS");
				final LinearLayout secOne=(LinearLayout) findViewById(R.id.section_1);
				final LinearLayout secTwo=(LinearLayout) findViewById(R.id.section_2);
				postcodetxt =(EditText) findViewById(R.id.postcodetxt);
				if(returnString.equalsIgnoreCase(Contants.TAG_SUCCESS)){
					isPostcodeValidate=true;
					System.out.println("TEST postcode check true");
					System.out.println("TEST "+objServerResponse.getString("postcode"));
					bannertxt.setVisibility(TextView.GONE);
					
					
					
					postcodetxt.setText(objServerResponse.getString("postcode"));
					/*nextOnebtn.setEnabled(true);
					System.out.println("postcode Button set true");

					secOne.setVisibility(LinearLayout.GONE);
					secTwo.setVisibility(LinearLayout.VISIBLE);
*/

				}else {

					
					isPostcodeValidate =false;
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Sorry!! We are not servicing for this suburb</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					postcodetxt.setText(objServerResponse.getString(""));

					//nextOnebtn.setEnabled(false);
					
					/*secOne.setVisibility(LinearLayout.VISIBLE);
					secTwo.setVisibility(LinearLayout.GONE);*/

					//ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);

					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);

					MessageDisplayer.ErrorToast(BookingActivity.this,"Sorry!! We are not servicing for this suburb");
				
					
				
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			final LinearLayout secOne=(LinearLayout) findViewById(R.id.section_1);
			final LinearLayout secTwo=(LinearLayout) findViewById(R.id.section_2);
			
			if(userClickNext){
				userClickNext = false;
				SimpleDateFormat format = new SimpleDateFormat("d-MM-yyy");  

				EditText streettxt=(EditText) findViewById(R.id.streettxt);
				EditText suburbtxt=(EditText)findViewById(R.id.suburbtxt);
				EditText postcodetxt=(EditText) findViewById(R.id.postcodetxt);
				EditText firstNametxt=(EditText) findViewById(R.id.firstNametxt);
				EditText lastNametxt=(EditText) findViewById(R.id.lastNametxt);
				EditText email=(EditText) findViewById(R.id.emailtxt);
				EditText contactNumtxt=(EditText) findViewById(R.id.contactNumtxt);
				EditText btn_date=(EditText) findViewById(R.id.btn_date);
				ScrollView mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);


				bannertxt=(TextView) findViewById(R.id.bookingbanner);

				if( (streettxt.getText().toString()).equals("")){

					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add street</font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					secOne.setVisibility(LinearLayout.VISIBLE);
					secTwo.setVisibility(LinearLayout.GONE);
					streettxt.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add street");
					
				}else if((suburbtxt.getText().toString()).equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add suburb </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					secOne.setVisibility(LinearLayout.VISIBLE);
					secTwo.setVisibility(LinearLayout.GONE);	
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					suburbtxt.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add suburb");
				/*}else if((postcodetxt.getText().toString()).equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add postcode </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					secOne.setVisibility(LinearLayout.VISIBLE);
					secTwo.setVisibility(LinearLayout.GONE);	*/

				}else if((firstNametxt.getText().toString()).equals("")){

					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add first name </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					secOne.setVisibility(LinearLayout.VISIBLE);
					secTwo.setVisibility(LinearLayout.GONE);
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					firstNametxt.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add first name");

				}else if((lastNametxt.getText().toString()).equals("")){
					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add last name </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					secOne.setVisibility(LinearLayout.VISIBLE);
					secTwo.setVisibility(LinearLayout.GONE);
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					lastNametxt.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add last name");
				}else if((email.getText().toString()).equals("") || !isValidMail((email.getText().toString()))){

					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add valid email </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					secOne.setVisibility(LinearLayout.VISIBLE);
					secTwo.setVisibility(LinearLayout.GONE);
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					email.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add valid email ");
				}else if((contactNumtxt.getText().toString()).equals("") || ((contactNumtxt.getText().toString()).length() != 10) ){

					bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>Please add valid contact number </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					secOne.setVisibility(LinearLayout.VISIBLE);
					secTwo.setVisibility(LinearLayout.GONE);
					//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
					streettxt.setFocusable(false);
					contactNumtxt.setFocusable(true);
					MessageDisplayer.ErrorToast(BookingActivity.this,"Please add valid contact number");
				
				} else
					try {
						if((btn_date.getText().toString()).equals("") ||  (System.currentTimeMillis() < (format.parse((btn_date.getText().toString()))).getTime())){

							bannertxt.setVisibility(TextView.VISIBLE);
							String bannerText="<font color='#EE0000'>Please add birth day </font>";
							bannertxt.setText(Html.fromHtml(bannerText));
							secOne.setVisibility(LinearLayout.VISIBLE);
							secTwo.setVisibility(LinearLayout.GONE);
						//	mainScrollView.fullScroll(ScrollView.FOCUS_UP);
							btn_date.setFocusable(true);
							MessageDisplayer.ErrorToast(BookingActivity.this,"Please add birth day");
						}else if(!isPostcodeValidate){
							bannertxt.setVisibility(TextView.VISIBLE);
							String bannerText="<font color='#EE0000'>Sorry!! We are not servicing for this suburb</font>";
							bannertxt.setText(Html.fromHtml(bannerText));
							secOne.setVisibility(LinearLayout.VISIBLE);
							secTwo.setVisibility(LinearLayout.GONE);
							//mainScrollView.fullScroll(ScrollView.FOCUS_UP);
							suburbtxt.setFocusable(true);
							MessageDisplayer.ErrorToast(BookingActivity.this,"Sorry!! We are not servicing for this suburb");
						}else {
							
							bannertxt.setVisibility(TextView.GONE);
							secOne.setVisibility(LinearLayout.GONE);
							secTwo.setVisibility(LinearLayout.VISIBLE);
							//new PostCodeChecker().execute();
							//	new loadSpinnnerData().execute();


						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			


		}

	}



	class fillDefaultData extends AsyncTask<String, String, String>{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BookingActivity.this);
			//pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage("Data loading  ...");
			//pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			System.out.println("ERROR START");
			SharedPreferences prefs = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE); 
			String userId = prefs.getString("userId",null);
			
			System.out.println("TEST LOGGED USER -"+userId);


			String postcode=postcodetxt.getText().toString();
			HashMap<String, String> param=new HashMap<String, String>();
			param.put(Contants.TAG_GETUSERDATA, "USERDATA");
			param.put(Contants.TAG_USERID, userId);

			HTTPProvider d=new HTTPProvider();
			String response =d.doHTTPPostRequest(param);
			System.out.println("TEST Response ***"+response.toString());

			
			return response;


		}

		protected void onPostExecute(String result) {
			// dismiss the dialog once data updated
			pDialog.dismiss();
			Button nextOnebtn=(Button) findViewById(R.id.nextOnebtn);




			String s=result.trim();
			JSONObject objServerResponse,dataObj;
			try {

				objServerResponse = new JSONObject(result);
				String returnString = objServerResponse.getString("success");

				if(returnString.equalsIgnoreCase(Contants.TAG_SUCCESS)){
					System.out.println("TEST Response *** SUCCESS 1" );
					dataObj=objServerResponse.getJSONObject("data"); 
					System.out.println("TEST Response *** SUCCESS 2" );
					
					
					((EditText)findViewById(R.id.firstNametxt)).setText(dataObj.getString("firstName"));
					((EditText)findViewById(R.id.lastNametxt)).setText(dataObj.getString("lastName"));
					((EditText)findViewById(R.id.contactNumtxt)).setText(dataObj.getString("contactNumber"));
					((EditText)findViewById(R.id.emailtxt)).setText(dataObj.getString("contactEmail"));
					
					System.out.println("TEST LOGGED USER 3"); 
					String genString=dataObj.getString("gender");
					
					System.out.println("TEST GEN"+genString);
					
					
					Spinner genSpinner=((Spinner)findViewById(R.id.spinnerGender));
					
					
					/*if("0".equals(dataObj.getString("gender"))){
						System.out.println("TEST GEN 0 true");
						genSpinner.setSelection(1);
					}else if("1".equals(dataObj.getString("gender"))){
						System.out.println("TEST GEN 1 true");
						genSpinner.setSelection(2);
					}*/
					
					
					System.out.println("TEST LOGGED USER 4");
							
					((EditText)findViewById(R.id.suburbtxt)).setText(dataObj.getString("name"));
					((EditText)findViewById(R.id.postcodetxt)).setText(dataObj.getString("code"));
					((EditText)findViewById(R.id.streettxt)).setText(dataObj.getString("address1"));
					((EditText)findViewById(R.id.btn_date)).setText(dataObj.getString("DOB"));

					((EditText)findViewById(R.id.ediNumbertxt)).setText(dataObj.getString("medicareNumber"));
					((EditText)findViewById(R.id.refNumtxt)).setText(dataObj.getString("refferenceNumber"));
					((EditText)findViewById(R.id.btn_expiryDate)).setText(dataObj.getString("expiryDate"));
					System.out.println("TEST LOGGED USER 5");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new PostCodeChecker().execute();
			System.out.println("ERROR FINISH");

		}

	}










	class loadSpinnnerData extends AsyncTask<String, String, String>{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BookingActivity.this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setTitle("Please wait");
			pDialog.setMessage("Data is loading  ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub


			HashMap<String, String> param=new HashMap<String, String>();
			param.put(Contants.TAG_PRE_DOCTOR, "PREDOC");


			HTTPProvider d=new HTTPProvider();
			String response =d.doHTTPPostRequest(param);
			System.out.println("Response ***"+response.toString());

			return response;
		}

		protected void onPostExecute(String result) {
			// dismiss the dialog once data updated
			pDialog.dismiss();

			String s=result.trim();


			JSONObject objServerResponse,rowObj;
			try {
				objServerResponse = new JSONObject(s);
				String returnString = objServerResponse.getString("SUCCESS");
				System.out.println("Response val "+returnString);

				JSONObject val=objServerResponse.getJSONObject("data");
				System.out.println("Response json obj 1"+val.length());
				System.out.println("Response data "+val.getString("1"));

				//Get selected docGEn
				/*preDoctor = (Spinner) findViewById(R.id.spinnerPreDoc);
				List<String> listdoc = new ArrayList<String>();


				for(int i=1; i<=val.length(); i++){
					String rowdata = val.getString(Integer.toString(i));
					rowObj=new  JSONObject(rowdata);
					JSONObject row=rowObj.getJSONObject("data");
					rowObj=val.getJSONObject(i);
					listdoc.add(rowObj.getString("name"));

					System.out.println("Response name "+rowObj.getString("name"));
				}


				ArrayAdapter<String> dataAdapterDoc = new ArrayAdapter<String>(BookingActivity.this,android.R.layout.simple_spinner_item, listdoc);
				dataAdapterDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				preDoctor.setAdapter(dataAdapterDoc);
				 */


				/*if(returnString.equalsIgnoreCase(Contants.TAG_SUCCESS)){
					System.out.println("Response SUCCESS "+returnString);
				}*/



				/*genderspn = (Spinner) findViewById(R.id.spinnerGender);
				List<String> list = new ArrayList<String>();
				list.add("Male");
				list.add("Female");
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				genderspn.setAdapter(dataAdapter);
				 */


			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}




	class BookingExecuter extends AsyncTask<String, String, String>{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BookingActivity.this);
			//pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setTitle("Request a Doctor");
			pDialog.setMessage("Please wait  ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String streettxt= ((EditText)findViewById(R.id.streettxt)).getText().toString();
			String suburbtxt= ((EditText)findViewById(R.id.suburbtxt)).getText().toString();
			String postcodetxt= ((EditText)findViewById(R.id.postcodetxt)).getText().toString();
			String firstNametxt= ((EditText)findViewById(R.id.firstNametxt)).getText().toString();
			String lastNametxt= ((EditText)findViewById(R.id.lastNametxt)).getText().toString();
			String emailtxt= ((EditText)findViewById(R.id.emailtxt)).getText().toString();
			String contactNumtxt= ((EditText)findViewById(R.id.contactNumtxt)).getText().toString();
			String btn_date= ((EditText)findViewById(R.id.btn_date)).getText().toString();

			//gender spinner
			String genVal=((Spinner)findViewById(R.id.spinnerGender)).getSelectedItem().toString();
			String spinnerGender ;
			if(genVal.equals("Male")){
				spinnerGender ="0";
			}else{
				spinnerGender ="1";
			}


			String ediNumbertxt= ((EditText)findViewById(R.id.ediNumbertxt)).getText().toString();
			String refNumtxt= ((EditText)findViewById(R.id.refNumtxt)).getText().toString();
			String btn_expiryDate= ((EditText)findViewById(R.id.btn_expiryDate)).getText().toString();
			String symptonstxt= ((EditText)findViewById(R.id.symptonstxt)).getText().toString();

			//Pre doc spinner
			//String spinnerPreDoc = ((Spinner)findViewById(R.id.spinnerPreDoc)).getSelectedItem().toString();
			//Pre time spinner
			String spinnerTime = ((Spinner)findViewById(R.id.spinnerTime)).getSelectedItem().toString();
			/*long spinnerTimeS = ((Spinner)findViewById(R.id.spinnerTime)).getSelectedItemId();
			String spinnerTime =Long.toString(spinnerTimeS);*/

			//Pre gen
			/*String pregenVal=((Spinner)findViewById(R.id.spinnerPreDocGen)).getSelectedItem().toString();
			String spinnerPreGen ;

			if(pregenVal.equals("Male")){
				 spinnerPreGen ="0";
			}else{
				 spinnerPreGen="1";
			}
			 */
			
			
			String reasontxt= ((EditText)findViewById(R.id.reasontxt)).getText().toString();

			
			//Save data only if not logged user
			if(!isLoggedUser()){ 
			
			SharedPreferences.Editor editor = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE).edit();
			editor.putBoolean("hasData",true);
			editor.putString("street", streettxt);
			editor.putString("suburb", suburbtxt);
			editor.putString("postcode", postcodetxt);
			editor.putString("firstname", firstNametxt);
			editor.putString("lastname", lastNametxt);
			editor.putString("email", emailtxt);

			editor.putString("contactNo", contactNumtxt);
			editor.putString("bday", btn_date);
			editor.putString("gender", spinnerGender);
			editor.putString("medicareNo", ediNumbertxt);
			editor.putString("refferenceNo", refNumtxt);
			editor.putString("expiryDate", btn_expiryDate);
			editor.commit();

			}


			HashMap<String, String> param=new HashMap<String, String>();
			param.put(Contants.TAG_BOOK, "booking");
			param.put(Contants.TAG_STNAME,streettxt);

			param.put(Contants.TAG_SUBNAME,suburbtxt);
			param.put(Contants.TAG_PCODE,postcodetxt);
			param.put(Contants.TAG_FNAME,firstNametxt);
			param.put(Contants.TAG_LNAME,lastNametxt);
			param.put(Contants.TAG_EMAIL,emailtxt);
			param.put(Contants.TAG_CONTNUM,contactNumtxt);
			param.put(Contants.TAG_BDATE,btn_date); 

			param.put(Contants.TAG_GEN,spinnerGender);
			
			
			
			param.put(Contants.TAG_MEDINUM,ediNumbertxt);
			param.put(Contants.TAG_REFNUM,refNumtxt);
			param.put(Contants.TAG_EXPDATE,btn_expiryDate);
			
			
			param.put(Contants.TAG_SYMPTON,symptonstxt);
			//param.put(Contants.TAG_PREDOC,spinnerPreDoc);
			param.put(Contants.TAG_PRETIME,spinnerTime);
			//param.put(Contants.TAG_PREDOCGEN, spinnerPreGen);
			param.put(Contants.TAG_REASON,reasontxt);





			SharedPreferences prefs = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE); 
			if(prefs.getBoolean("isloggedUser", false)){
				String id=prefs.getString("userId", null);
				if(id != null && !id.isEmpty()){
					param.put(Contants.TAG_USERID,id);
				}
			}


			HTTPProvider d=new HTTPProvider();
			String response =d.doHTTPPostRequest(param);
			System.out.println("TEST Response ***"+response.toString());


			return response;
		}

		protected void onPostExecute(String result) {
			// dismiss the dialog once data updated
			pDialog.dismiss();




			bannertxt=(TextView) findViewById(R.id.bookingbanner);

			String s=result.trim();  
			JSONObject objServerResponse;
			try {
				objServerResponse = new JSONObject(s);
				String returnString = objServerResponse.getString("success");
				System.out.println("TEST Response "+returnString); 


				if(returnString.equalsIgnoreCase(Contants.TAG_SUCCESS)){
					bannertxt.setVisibility(TextView.GONE);
					System.out.println("TEST Response SUCCESS "+returnString);

					Intent conFirmIntent=new Intent (BookingActivity.this,ConfirmationActivity.class);
					startActivity(conFirmIntent);

				}else {
					
					ConnectivityManager connectivityManager 
			          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			    if( activeNetworkInfo == null && !activeNetworkInfo.isConnected()){
			    	bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'>  You don't have internet!!  Please try again later. </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					MessageDisplayer.ErrorToast(BookingActivity.this,"You don't have internet!!  Please try again later.");
			    }else{ 
			    	bannertxt.setVisibility(TextView.VISIBLE);
					String bannerText="<font color='#EE0000'> Oops! An error occurred.  Please try again. </font>";
					bannertxt.setText(Html.fromHtml(bannerText));
					MessageDisplayer.ErrorToast(BookingActivity.this," Oops! An error occurred.  Please try again.");
			    }

					

				}
				/*Intent conFirmIntent=new Intent (BookingActivity.this,ConfirmationActivity.class);
				startActivity(conFirmIntent);*/

			} catch (JSONException e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

	}

}
