package com.jung.notify.common;

import java.security.MessageDigest;

public class Sha256 {
	
	public static String encrypt(String text)  {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(text.getBytes());
	        return bytesToHex(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		} 

        return "";
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
