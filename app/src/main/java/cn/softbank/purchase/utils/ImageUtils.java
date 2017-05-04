/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.softbank.purchase.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import cn.yicheng.jingjiren.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

@SuppressLint("DefaultLocale")
public class ImageUtils {
	// public static String getThumbnailImagePath(String imagePath) {
	// String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
	// path += "th" + imagePath.substring(imagePath.lastIndexOf("/") + 1,
	// imagePath.length());
	// EMLog.d("msg", "original image path:" + imagePath);
	// EMLog.d("msg", "thum image path:" + path);
	// return path;
	// }
	
	public static DisplayImageOptions imgOptionsSmall=new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.considerExifParams(true)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.showImageOnLoading(R.drawable.default_img_small)
	.showImageForEmptyUri(R.drawable.default_img_small)
	.showImageOnFail(R.drawable.default_img_small).build();
	
	public static DisplayImageOptions imgOptionsMiddle=new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.considerExifParams(true)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.showImageOnLoading(R.drawable.default_img_middle)
	.showImageForEmptyUri(R.drawable.default_img_middle)
	.showImageOnFail(R.drawable.default_img_middle).build();
	
	public static DisplayImageOptions imgOptionsBig=new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.considerExifParams(true)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.showImageOnLoading(R.drawable.default_img_big)
	.showImageForEmptyUri(R.drawable.default_img_big)
	.showImageOnFail(R.drawable.default_img_big).build();
	
	public static DisplayImageOptions imgOptionsGroup=new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.considerExifParams(true)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.showImageOnLoading(R.drawable.mmq_img_quntouxiang1)
	.showImageForEmptyUri(R.drawable.mmq_img_quntouxiang1)
	.showImageOnFail(R.drawable.mmq_img_quntouxiang1).build();
	
	
	/**
	 * 获得图片参数
	 * 
	 * @param defaultImgId
	 *            占位图大小 DEFAULT_IMG_BIG=0;//小图片 DEFAULT_IMG_SMALL=1;//大图片
	 */
	public static DisplayImageOptions getImageOptions(int defaultImgId) {
		return new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnLoading(defaultImgId)
				.showImageForEmptyUri(defaultImgId)
				.showImageOnFail(defaultImgId).build();
	}

	public static DisplayImageOptions getImageOptionsNload(int defaultImgId) {
		return new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(defaultImgId)
				.showImageOnFail(defaultImgId).build();
	}

//	public static String getImagePath(String remoteUrl) {
//		String imageName = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1,
//				remoteUrl.length());
//		String path = PathUtil.getInstance().getImagePath() + "/" + imageName;
//		return path;
//
//	}
//
//	public static String getThumbnailImagePath(String thumbRemoteUrl) {
//		String thumbImageName = thumbRemoteUrl.substring(
//				thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
//		String path = PathUtil.getInstance().getImagePath() + "/" + "th"
//				+ thumbImageName;
//		return path;
//	}

}
