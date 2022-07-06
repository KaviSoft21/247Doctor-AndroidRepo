package au.com.twoFourSevendoctor.channelDoctor.activities;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import au.com.twoFourSevendoctor.channelDoctor.R;


public class LoadingActivity extends Activity {

	Button registerBtn;
	Button loginBtn;
	Button callBtn;
	Button bookBtn;
	Button teleconBtn;
	Button callBackBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//new AppEula(LoadingActivity.this).display();
		setContentView(R.layout.activity_main);
		
		TextView urltxt=(TextView) findViewById(R.id.site);
		String linkUrl = "<a href='https://www.247doctor.com.au'>www.247doctor.com.au</a> ";
		urltxt.setText(Html.fromHtml(linkUrl));
		urltxt.setMovementMethod(LinkMovementMethod.getInstance());

		registerBtn = (Button) findViewById(R.id.register_btn);
		registerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent registerIntent=new Intent(LoadingActivity.this,RegisterActivity.class );
				startActivity(registerIntent);			
			}
		});
		
		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent loginIntent =new Intent(LoadingActivity.this,LoginActivity.class);
				startActivity(loginIntent);
				
			}
		});
		
		
		callBtn=(Button) findViewById(R.id.callnow_btn);
		callBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:1800 247 477"));
				startActivity(callIntent);
			}
		});
		
		bookBtn=(Button) findViewById(R.id.booknow_btn);
		bookBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent loginIntent =new Intent(LoadingActivity.this,BookingActivity.class);
				startActivity(loginIntent);
			}
		});
		
		
		bookBtn=(Button) findViewById(R.id.telecon_btn);
		bookBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent teleIntent =new Intent(LoadingActivity.this,TeleconsultActivity.class);
				startActivity(teleIntent);
			}
		});
		
		
		bookBtn=(Button) findViewById(R.id.callback_btn);
		bookBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent loginIntent =new Intent(LoadingActivity.this,CallbackActivity.class);
				startActivity(loginIntent);
			}
		});  
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
}
