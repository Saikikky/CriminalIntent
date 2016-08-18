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
	
	//���fragment�Ĺ��췽�� ����������fragment argument
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
		//������������Ϊextra���ӵ�intent��
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		//����CrimeFragment.onActivityResult����
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	//ͨ��AlertDialog.Builder(context)��������һ��AlterDialogʵ��
	//�������������������������AlterDialog
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//CrimeFragment��Ҫ��ʾ�����ڴ��ݸ�DatePickerFragment
		//��ȡDate���󲢳�ʼ��DatePicker
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		
		//����һ��Calendar���� Ȼ����Date�������������� ���ɴ�Calendar������ȡ��������Ϣ
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_data, null);
		
		DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
		
		//onDateChangedListener������ �û��ı�DatePicker�ڵ�����֮�� Date���󼴿ɵõ�ͬ������
		datePicker.init(year, month, day, new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year, int month, int day) {
				mDate = new GregorianCalendar(year, month, day).getTime();
				
				//��Date�����д���浽��fragment argument�� ��ֹ�豸��תʱ����Date���ݵĶ�ʧ
				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});
		return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title)
				//.setPositiveButton(android.R.string.ok, null).create();
				
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					
					//�����½���sendResult˽�з���������������
				
					public void onClick(DialogInterface dailog, int which) {
						sendResult(Activity.RESULT_OK);
						
					}
				})
				.create();
	} 
}
