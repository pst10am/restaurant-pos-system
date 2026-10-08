package test;

import java.awt.Color;

public class TestColor {
	
	public static void main(String[] args) {
		Color col1 = Color.MAGENTA;
		
		String rstr = col1.getRed()==0 ? "00" : Integer.toHexString(col1.getRed());
		String gstr = col1.getGreen()==0 ? "00" : Integer.toHexString(col1.getGreen());
		String bstr = col1.getBlue()==0 ? "00" : Integer.toHexString(col1.getBlue());
		
		String hxstr = String.format("#%s%s%s\n", rstr, gstr, bstr).toUpperCase();
		
		System.out.println(hxstr);
	}

}
