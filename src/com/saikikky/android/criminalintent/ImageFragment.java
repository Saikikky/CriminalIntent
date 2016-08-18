package com.saikikky.android.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
	
	//ImageFragment类需要知道Crime照片的路径
	public static final String EXTRA_IMAGE_PATH = 
			"com.saikikky.android.criminalintent.image_path";
	
	//创建ImageFragment 接受照片文件路径并放置到argumentbundle中附加给ImageFragment
	public static ImageFragment newInstance(String imagePath) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
		
		ImageFragment fragment = new ImageFragment();
		fragment.setArguments(args);
		//设置fragment样式
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		
		return fragment;
	}
	
	private ImageView mImageView;
	
	//创建mImageView并从argument获取文件路径
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		mImageView = new ImageView(getActivity());
		String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
		
		//然后获取缩小版的图片并设置给ImageView
		BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);
		
		mImageView.setImageDrawable(image);
		
		return mImageView;
	}
	
	// 图片不再需要 就主动覆盖onDestroyView()方法以释放内存
	@Override 
	public void onDestroyView() {
		super.onDestroyView();
		PictureUtils.cleanImageView(mImageView);
	}
}
