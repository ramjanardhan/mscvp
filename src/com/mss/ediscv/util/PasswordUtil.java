package com.mss.ediscv.util;
/*
 * This class is used for password Encryption
 */
public class PasswordUtil {
	
    public PasswordUtil() {
	}

	public static String encryptPwd(String srcPwd) {
		char asciiArr[] = srcPwd.toCharArray();
		int encryASCIIArr[] = new int[srcPwd.length()];
		for (int i = 0; i < encryASCIIArr.length; i++) {
			int asciiChar = (int) asciiArr[i] + 3;
			int asciiCharMulBy3 = asciiChar * 3;
			encryASCIIArr[i] = asciiCharMulBy3;
		}
		String encryPwd = "";
		for (int j = 0; j < encryASCIIArr.length; j++) {
			encryPwd = encryPwd + "#" + encryASCIIArr[j];
		}
		return encryPwd;
	}
	public static String decryptPwd(String encryPwd) {
		String encryPwdArr[] = encryPwd.split("#");
		String decryptedPwd = "";
		for (int lk = 0; lk < encryPwdArr.length; lk++) {
		
                    if (!encryPwdArr[lk].equalsIgnoreCase("")) {
				int asciiChar = Integer.parseInt(encryPwdArr[lk]);
				int asciiCharDivBy3 = asciiChar / 3;
				int asciiCharSubBy3 = asciiCharDivBy3 - 3;
				decryptedPwd = decryptedPwd + (char) asciiCharSubBy3;
			}
		}
		return decryptedPwd;
	}

public static void main(String args[]){
    
}
}