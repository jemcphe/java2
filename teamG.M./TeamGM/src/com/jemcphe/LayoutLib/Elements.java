package com.jemcphe.LayoutLib;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Elements {
	
	//Function to return a LinearLayout that contains EditText w/ button
	public static LinearLayout singleEntryWithButton(Context context, String label, String hint, String buttonText) {
		
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ll.setLayoutParams(lp);
		
		//Create TextView, set label, and add to View
		TextView labelText = new TextView(context);
		labelText.setText(label);
		ll.addView(labelText);

		//Create EditText, setHint, add to View
		EditText entryText = new EditText(context);
		entryText.setHint(hint);
		entryText.setId(1);
		ll.addView(entryText);
		
		//Create Button, setText, add to view
		Button button = new Button(context);
		button.setText(buttonText);
		button.setId(2);
		button.setTag(entryText);
		ll.addView(button);
		
		return ll;
	}
	//Create RadioGroup that holds an array of values
	public static RadioGroup getTeam(Context context, String [] teams) {
		RadioGroup teamBox = new RadioGroup(context);
		
		for(int i=0; i<teams.length; i++){
			RadioButton rb = new RadioButton(context);
			rb.setText(teams[i]);
			rb.setId(i+1);
			teamBox.addView(rb);
		}
		
		return teamBox;
	}
}
