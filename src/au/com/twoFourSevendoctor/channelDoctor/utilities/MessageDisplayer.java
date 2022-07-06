package au.com.twoFourSevendoctor.channelDoctor.utilities;

import android.content.Context;
import android.widget.Toast;

public class MessageDisplayer {
	 public static void ErrorToast(Context context, String message) {
	     Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	  }

}
