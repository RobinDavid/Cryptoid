package cryptoid.algorithms;

public class Base64Coding {

	public Base64Coding() {
	}
	
	public static Boolean bruteAvailable() { return false; }
	public static Boolean analyzeAvailable() { return false; }
	
	public static String encodeBase64(String input) {
		return android.util.Base64.encodeToString(input.getBytes() ,android.util.Base64.DEFAULT);
	}
	
	public static String decodeBase64(String input) {
		return new String(android.util.Base64.decode(input, android.util.Base64.DEFAULT));
	}
	
	public Boolean isValidString(String inputString) {//could not be abstract because implement mother class
		Boolean valid=true;
		for (int i=0; i < inputString.length();i++) {
			if(!(isBase64Char(inputString.charAt(i))))
				valid = false;
		}
		return valid;
	}
	
	public static float isBase64String(String input) {
		int nb_wrong_char = 0;
		for (int i=0; i < input.length(); i++) {
			if(!(isBase64Char(input.charAt(i)))) {
				nb_wrong_char++;
			}
		}
		return 100 - ((nb_wrong_char * 100) / input.length());
	}
	
	public static Boolean isBase64Char(char c) {
		return ((c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') ||
				(c == '/') || (c == '+') || (c == '='));
	}
}
