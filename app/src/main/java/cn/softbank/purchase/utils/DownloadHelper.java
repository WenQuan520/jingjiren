package cn.softbank.purchase.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Environment;

public class DownloadHelper {

	@SuppressLint("NewApi")
	public File downloadNewVersion(String uri,ProgressDialog progressDialog){
		
		File file=new File(Environment.getExternalStorageDirectory(), uri.substring(uri.lastIndexOf("/")+1));
		
		try {
			URL url=new URL(uri);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode()==200){
				
				progressDialog.setProgressNumberFormat("%1dKB/%2dKB");
				progressDialog.setMax(conn.getContentLength()/1024);
				
				InputStream in=conn.getInputStream();
				FileOutputStream fos=new FileOutputStream(file);
				byte[] buffer=new byte[1024];
				int len=0;
				int num=0;
				while((len=in.read(buffer))!=-1){
					fos.write(buffer,0,len);
					num+=len;
					progressDialog.setProgress(num/1024);
				}
				in.close();
				fos.close();
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
	}
}
