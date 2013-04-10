package com.hourtracker;

import android.text.InputFilter;
import android.text.Spanned;

/*
 * http://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
 */
public class MoneyInputFilter implements InputFilter {
	
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
		
		int decimalPos = -1;
		
		int length = dest.length();
		for (int currCharPos = 0; currCharPos < length; currCharPos++) {
			char currChar = dest.charAt(currCharPos);
			if (currChar == '.'||currChar == ',') {
				decimalPos = currCharPos;
				break;
			}
		}
		
		if (decimalPos >= 0) {
			if (source.equals(".")||source.equals(","))
				return "";
			if (dEnd <= decimalPos)
				return null;
			if (length-decimalPos>2)
				return "";
		}
		
		return null;
	}

}
