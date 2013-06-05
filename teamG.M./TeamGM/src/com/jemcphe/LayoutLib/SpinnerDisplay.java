package com.jemcphe.LayoutLib;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SpinnerDisplay extends LinearLayout {

	Context _context;
	Spinner _dropDown;
	ArrayList<String> _teams;
	
	public SpinnerDisplay(Context context) {
		super(context);
		
		_context = context;
		LayoutParams lp;
		
		//Create Spinner
		_dropDown = new Spinner(_context);
		lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
		_dropDown.setLayoutParams(lp);
		
		//Create the Adapter that will populate dropDown
		ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, _teams);
		dropDownAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		_dropDown.setAdapter(dropDownAdapter);
		
		this.addView(_dropDown);
	}
	
}
