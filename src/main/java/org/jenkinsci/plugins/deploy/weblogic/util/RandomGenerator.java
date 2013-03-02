package org.jenkinsci.plugins.deploy.weblogic.util;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class RandomGenerator {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private static  final Random rand = new SecureRandom(); 

	public static char generateAlphaNumericChar() {
		int val = rand.nextInt(36);
		if (val < 10)
			return (char)(val + 48);
		return (char)(val + 87);
	}
	
	public static char generateAlphabeticChar() {
		int val = rand.nextInt(26);
		return (char)(val + 97);
	}
	
	public static char generateNumericChar() {
		int val = rand.nextInt(10);
		return (char)(val + 48);
	}
	
	public static String generateAlphabeticString(int length) {
		StringBuffer sb = new StringBuffer(length);

		for (int i = 0; i < length; i++)
			sb.append(generateAlphabeticChar());
		
		return sb.toString();
	}
	
	public static String generateAlphaNumericString(int length) {
		StringBuffer sb = new StringBuffer(length);

		for (int i = 0; i < length; i++)
			sb.append(generateAlphaNumericChar());
		
		return sb.toString();
	}
	
	public static String generateNumericString(int length) {
		StringBuffer sb = new StringBuffer(length);

		for (int i = 0; i < length; i++)
			sb.append(generateNumericChar());
		
		return sb.toString();
	}
}