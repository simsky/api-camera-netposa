package com.welton.video.np;

public class Generator {

	public static void main(String[] args) {
		
		String pattern = "https://m.22pq.com/book/18/18723/%s.html\r\n";
		for(int i=19723; i<=20101; i++) {
			
			System.out.format(pattern, i);
			
			if(i<=19738) continue;
			
			System.out.format(pattern, i+"_2");
			System.out.format(pattern, i+"_3");
		}

	}

}
