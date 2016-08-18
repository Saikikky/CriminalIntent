package com.saikikky.android.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends DialogFragment {
	
	//ImageFragment����Ҫ֪��Crime��Ƭ��·��
	public static final String EXTRA_IMAGE_PATH = 
			"com.saikikky.android.criminalintent.image_path";
	
	//����ImageFragment ������Ƭ�ļ�·�������õ�argumentbundle�и��Ӹ�ImageFragment
	public static ImageFragment newInstance(String imagePath) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
		
		ImageFragment fragment = new ImageFragment();
		fragment.setArguments(args);
		//����fragment��ʽ
		fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		
		return fragment;
	}
	
	private ImageView mImageView;
	
	//����mImageView����argument��ȡ�ļ�·��
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		mImageView = new ImageView(getActivity());
		String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
		
		//Ȼ���ȡ��С���ͼƬ�����ø�ImageView
		BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);
		
		mImageView.setImageDrawable(image);
		
		return mImageView;
	}
	
	// ͼƬ������Ҫ ����������onDestroyView()�������ͷ��ڴ�
	@Override 
	public void onDestroyView() {
		super.onDestroyView();
		PictureUtils.cleanImageView(mImageView);
	}
}
