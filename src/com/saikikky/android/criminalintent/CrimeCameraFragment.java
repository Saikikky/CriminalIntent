package com.saikikky.android.criminalintent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG = "CrimeCameraFragment";
	
	//新增照片文件名extra
	public static final String EXTRA_PHOTO_FILENAME = 
			"com.saikikky.android.criminalintent.photo_filename";
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	
	private View mProgressContainer;
	
	
	//实现传入takePicture方法的接口
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			mProgressContainer.setVisibility(View.VISIBLE);
			
		}
	};
	
	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//Create a filename
			String filename = UUID.randomUUID().toString()+".jpg";
			//save the jpeg data to disk
			FileOutputStream os = null;
			boolean success = true;
			
			try {
				os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			} catch (Exception e) {
				Log.e(TAG, "Error writing to file " + filename, e);
				success = false;
			} finally {
				try {
					if (os != null)
						os.close();
				} catch (Exception e) {
					Log.e(TAG, "Error closing file " + filename, e);
					success = false;
				}
			}
			//在CrimeCameraFragment中设置返回值
			if (success) {
				//Log.i(TAG, "JPEG saved at " + filename);
				//将文件名放置在extra中并附加到intent
				Intent i = new Intent();
				i.putExtra(EXTRA_PHOTO_FILENAME, filename);
				//将结果代码置为RESULT_OK
				getActivity().setResult(Activity.RESULT_OK, i);
			} else {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}
			getActivity().finish();
		}
	};
	//初始相机fragment类 实例化布局并引用各组件
	@Override
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
		
		mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);
		
		Button takePictureButton = (Button)v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//按键之后返回上一层
				//getActivity().finish();
				//照相
				if (mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, mJpegCallback);
				}
			}
		});
		mSurfaceView = (SurfaceView)v.findViewById(R.id.crime_camera_surfaceView);
		
		//获取surfaceHold实例 是与surface对象联系的纽带
		//划线的两个是强烈不赞成的弃用代码
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//实现SurfaceHold.Callback接口
		holder.addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if (mCamera != null) {
					mCamera.stopPreview();
				}
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				//告诉相机用这个surface作为它的视图
				try {
					if (mCamera != null) {
						mCamera.setPreviewDisplay(holder);
					}
				}catch (IOException exception) {
					Log.e(TAG, "Error setting up preview display", exception);
				}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
				if (mCamera == null) return;
				//surface改变尺寸 更新相机视图
				Camera.Parameters parameters = mCamera.getParameters();
				//下一次重置
				//Size s = null;
				//调用该方法设置预览尺寸
				Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
				parameters.setPreviewSize(s.width, s.height);
				
				//得到最合适的尺寸 并将其设置为相机要创建的图片尺寸
				s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
				parameters.setPictureSize(s.width, s.height);
				
				mCamera.setParameters(parameters);
				try {
					mCamera.startPreview();
				} catch (Exception e) {
					Log.e(TAG, "Could not start preview", e);
					mCamera.release();
					mCamera = null;
				}
			}
		});
		return v;
	}
	
	//在onResum方法中打开相机
	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
		} else {
			mCamera = Camera.open();
		}
	}
	
	//及时释放相机资源
	@Override
	public void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	//该方法接受一组预览尺寸 然后找出具有最大数目像素的尺寸 找出设备支持的最佳尺寸
	private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for (Size s : sizes) {
			int area = s.width * s.height;
			if (area > largestArea) {
				bestSize = s;
				largestArea = area;
			}
		}
		return bestSize;
	}
}
