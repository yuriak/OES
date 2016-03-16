package edu.dufe.oes.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringEscapeUtils;

public class MyStringUtil {

	public static String toMD5(String string) throws NoSuchAlgorithmException{
		MessageDigest digest=MessageDigest.getInstance("MD5");
		digest.update(string.getBytes());
		String result = new BigInteger(1, digest.digest()).toString(16);
		return result;
	}
	
	public static String trimAndEscapeSql(String string){
		if (string==null) {
			string="";
		}
		return StringEscapeUtils.escapeSql(string.trim());
	}
}
