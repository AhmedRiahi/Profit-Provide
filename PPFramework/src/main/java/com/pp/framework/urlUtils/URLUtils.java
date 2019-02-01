package com.pp.framework.urlUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {
	
	public static String generateFullURL(String baseURLString,String referenceURL) throws MalformedURLException {
		
		URL baseUrl;
		try {
			baseUrl = new URL(referenceURL);
			return baseUrl.toString();
		}catch(MalformedURLException e){
			baseUrl = new URL(baseURLString);
		}
		
		String cleanBaseURL = baseUrl.getProtocol()+"://"+baseUrl.getHost();
		if(referenceURL.charAt(0) != '/') {
			referenceURL = '/'+referenceURL;
		}
		URL fullURL = new URL(cleanBaseURL+referenceURL);
		return fullURL.toString();
	}
}