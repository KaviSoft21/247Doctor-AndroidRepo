package au.com.twoFourSevendoctor.channelDoctor.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import au.com.twoFourSevendoctor.channelDoctor.R;
import au.com.twoFourSevendoctor.channelDoctor.utilities.Contants;

public class ConfirmationActivity extends Activity{

	Button logout,call,book,home;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation);

		logout = (Button) findViewById(R.id.logout_btn);
		call = (Button) findViewById(R.id.call_btn);
		book = (Button) findViewById(R.id.book);
		home=(Button) findViewById(R.id.home_btn);

		SharedPreferences prefs = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE); 
		if(prefs.getBoolean("isloggedUser", false)){

			// Should display logout
			logout.setVisibility(Button.VISIBLE);
			home.setVisibility(Button.GONE);
		}else{
			//Hide logout
			logout.setVisibility(Button.GONE);
			home.setVisibility(Button.VISIBLE);

		}

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent lIntent=new Intent(ConfirmationActivity.this, LoadingActivity.class);
				startActivity(lIntent);
			}
		});

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				SharedPreferences.Editor editor = getSharedPreferences(Contants.TAG_MY_PREFS_NAME, MODE_PRIVATE).edit();
				editor.putBoolean("isloggedUser",false);

				editor.commit();


				Intent intent = new Intent(ConfirmationActivity.this,LoadingActivity.class);
				startActivity(intent);



			}
		});


		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:1800 247 477"));
				startActivity(callIntent);
			}
		});

		book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent bIntent=new Intent(ConfirmationActivity.this, BookingActivity.class);
				startActivity(bIntent);
			}
		});
	}
}
