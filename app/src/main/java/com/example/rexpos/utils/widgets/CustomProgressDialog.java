package com.example.rexpos.utils.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.rexpos.R;


public class CustomProgressDialog extends ProgressDialog {
	public static final String DEBUG_TAG = "CustomProgressDialog";
	String mMessage;
	private TextView progressMessage;
	public CustomProgressDialog(Context context, String message) {
		super(context, R.style.CustomAlertDialogStyle);
		this.mMessage = message;
		// TODO Auto-generated constructor stub
	}

	public CustomProgressDialog(Context context) {
		super(context, R.style.CustomAlertDialogStyle);
		this.mMessage = "Loading...";
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);

		setCancelable(false);

		progressMessage = (TextView) findViewById(R.id.progressMessage);
		progressMessage.setText(mMessage);
	}
}
