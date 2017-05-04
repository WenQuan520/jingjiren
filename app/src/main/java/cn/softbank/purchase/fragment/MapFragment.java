package cn.softbank.purchase.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.softbank.purchase.base.BaseFragment;
import cn.yicheng.jingjiren.R;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

public class MapFragment extends BaseFragment implements AMap.OnMarkerClickListener{
	
	private AMap aMap;
	private double lat;
	private double lon;
	
	
	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container) {
		View viewRoot = View.inflate(getActivity(), R.layout.fragment_homemap, null);
		return viewRoot;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		aMap = ((SupportMapFragment) baseActivity
				.getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		
		lat = getActivity().getIntent().getDoubleExtra("lat", 30.3);
		lon = getActivity().getIntent().getDoubleExtra("lon", 120.2);
		
		System.out.println("lat=="+lat);
		System.out.println("lon=="+lon);
		
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));  
        MarkerOptions markerOptions = new MarkerOptions();  
        markerOptions.position(new LatLng(lat, lon));  
        markerOptions.title("");  
        markerOptions.visible(true);  
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker));  
        
        View infoContent = View.inflate(context, R.layout.amp_view, null);
        
        String title = getActivity().getIntent().getStringExtra("title");
    	String content = getActivity().getIntent().getStringExtra("content");
    	((TextView)infoContent.findViewById(R.id.tv_title)).setText(TextUtils.isEmpty(title)?"暂无信息":title);
    	((TextView)infoContent.findViewById(R.id.tv_content)).setText(TextUtils.isEmpty(content)?"暂无信息":content);
        
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(infoContent)));
        
        aMap.addMarker(markerOptions); 
        
//        maker.showInfoWindow();
	}
	
	//view 转bitmap

	public static Bitmap convertViewToBitmap(View view) {

	    view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

	    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

	    view.buildDrawingCache();

	    Bitmap bitmap = view.getDrawingCache();

	    return bitmap;

	}
	
	@Override
	protected void processClick(View view) {
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
//		mDialog.show();
		arg0.hideInfoWindow();
		try {
			String content = getActivity().getIntent().getStringExtra("content");
	    	
			Uri mUri = Uri.parse("geo:" + lat + "," + lon + "?q="
					+ content);
			Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
			startActivity(mIntent);
//			String url="http://m.amap.com/?q="+detailData.getLat() + "," + detailData.getLon()+"&name=杰瑞教育"+"&view=detail"+"&key=abaf87ad5dd004d248f5954fadd4c0bc";
//			startActivity(new Intent(context, WebViewActivity.class).putExtra("URI", url));
			
		} catch (Exception e) {
			baseActivity.showToast("请安装地图软件");
		}
		return true;
	}
	
}
