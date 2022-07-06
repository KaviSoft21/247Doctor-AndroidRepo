package au.com.twoFourSevendoctor.channelDoctor.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import au.com.twoFourSevendoctor.channelDoctor.utilities.ResourceLoader;

public class HTTPProvider {
	 URL url;
     String response = "";
     OutputStream os = null;
     HttpURLConnection conn=null;
	
	public String doHTTPPostRequest(HashMap<String, String> param){
		InputStream is = null;
		 String result= null;
		
		  try {
	            url = new URL(ResourceLoader.getConnector());

	            conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(15000);
	            conn.setConnectTimeout(15000);
	            conn.setRequestMethod("POST");
	            conn.setDoInput(true);
	            conn.setDoOutput(true);


	          os = conn.getOutputStream();
	            BufferedWriter writer = new BufferedWriter(
	                    new OutputStreamWriter(os, "UTF-8"));
	            writer.write(getPostDataString(param));

	            writer.flush();
	            writer.close();
	            os.close();
	            int responseCode=conn.getResponseCode();
	            System.out.println("Response code "+responseCode);

	            InputStream iss;
	            if (responseCode == HttpsURLConnection.HTTP_OK) {
	            	
	            	/*iss=conn.getInputStream();
	            	int ch;
	            	StringBuffer sb=new StringBuffer();
	            	
	            	while((ch=is.read()) != -1){
	            		sb.append((char)ch);
	            	}
	            	String responseStr=sb.toString();
	            	System.out.println("Response "+responseStr);*/
	            	
	            	
	            	
	                String line;
	                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                while ((line=br.readLine()) != null) {
	                //	System.out.println("Response 1"+line);
	                    response+=line;
	                }
	                
	                
	            }
	            else {
	                response="";

	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	        	if(os != null){
	        		try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        	
	        	
	        	if(is != null){
	        		try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        	
	        	if(conn != null){
	        		conn.disconnect();
	        	}
	        	
	        }

	        //System.out.println("Response 2"+response.toString());
	        return response;
	
		
	}
	
	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        System.out.println("query string "+ result.toString());
        return result.toString();
    }

}
