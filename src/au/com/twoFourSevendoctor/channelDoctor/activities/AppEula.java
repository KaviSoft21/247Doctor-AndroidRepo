package au.com.twoFourSevendoctor.channelDoctor.activities;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import au.com.twoFourSevendoctor.channelDoctor.R;

public class AppEula {


	
	private String EULA_PREFIX="eula_";
	private static final String ASSET_EULA ="EULA";
	private Activity mActivity;

	public AppEula (Activity context){
		mActivity=context;
	}
	
	private PackageInfo getPackageInfo(){
		
		PackageInfo packageInfo=null;
		
		try {
			packageInfo=mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packageInfo;
	}
	
	public void display(){
		PackageInfo versionNo=getPackageInfo();
		
		final String eulaKey=EULA_PREFIX+versionNo.versionName+"-"+versionNo.versionCode;
		final SharedPreferences preference=PreferenceManager.getDefaultSharedPreferences(mActivity);
		boolean hasShown=preference.getBoolean(eulaKey, false);
	
		if(!hasShown){
		
		// Display the EULA
				
			AlertDialog.Builder builder=new AlertDialog.Builder(mActivity)
				.setTitle(mActivity.getText(R.string.eula_title)+" "+versionNo.versionName)
				.setMessage(readEula(mActivity))
				.setPositiveButton(R.string.eula_accept,new Dialog.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//Mark this version as read.
						SharedPreferences.Editor editor=preference.edit();
						editor.putBoolean(eulaKey,true);
						//editor.commit();
						editor.apply();
						dialog.dismiss();		
					}
					
				})
				.setNegativeButton(R.string.eula_refuse,new Dialog.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//Close the activity as they have declined the EULA
						
							
						NotificationManager notification=(NotificationManager) mActivity.getSystemService(mActivity.NOTIFICATION_SERVICE);
						notification.cancelAll();
						
						mActivity.finish();
					}
					
				})
				
			.setCancelable(false);
			builder.create().show();
			
		}
	}
	
	
	private static CharSequence readEula(Activity mActivity){
		BufferedReader in=null;
		String line;
		StringBuilder buffer=new StringBuilder();
		try{
		in=new BufferedReader(new InputStreamReader(mActivity.getAssets().open(ASSET_EULA)));
		
		
		while( (line = in.readLine()) != null ){
			buffer.append(line).append('\n');
		}
		
		}catch (IOException e) {
			// TODO: handle exception
			e.getMessage();
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException e){
					e.getMessage();
				}
			}
		}
		return buffer;
	}
}
