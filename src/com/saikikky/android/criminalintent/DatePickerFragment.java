package com.saikikky.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {
	 
	public static final String EXTRA_DATE = "com.saikikky.android.criminalintent.date";
	
	private Date mDate;
	
	//替代fragment的构造方法 创建和设置fragment argument
	public static DatePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) 
			return;
		//将日期数据作为extra附加到intent上
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		//调用CrimeFragment.onActivityResult方法
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	//通过AlertDialog.Builder(context)方法构造一个AlterDialog实例
	//后面的两个函数都是用来设置AlterDialog
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//CrimeFragment将要显示的日期传递给DatePickerFragment
		//获取Date对象并初始化DatePicker
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		
		//创建一个Calendar对象 然后用Date对象对其进行配置 即可从Calendar对象中取回所需信息
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_data, null);
		
		DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
		
		//onDateChangedListener监听器 用户改变DatePicker内的日期之后 Date对象即可得到同步更新
		datePicker.init(year, month, day, new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year, int month, int day) {
				mDate = new GregorianCalendar(year, month, day).getTime();
				
				//将Date对象回写保存到了fragment argument中 防止设备旋转时发生Date数据的丢失
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title)
				//.setPositiveButton(android.R.string.ok, null).create();
				
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					//调用新建的sendResult私有方法并传入结果代码
				
					public void onClick(DialogInterface dailog, int which) {
						sendResult(Activity.RESULT_OK);
						
					}
				})
				.create();
	} 
}
