package cryptoid.algorithms;

public class Polybius {
	private static char[][] matrix = { {'a','b','c','d','e'},{'f','g','h','i','k'},{'l','m','n','o','p'},{'q','r','s','t','u'},{'v','w','x','y','z'}};
	
	public Polybius() {
	}
	
	public static Boolean bruteAvailable() { return false; }
	public static Boolean analyseAvailable() { return false; }

	public static String cipher(String input) {
		String res = "";
		input = input.toLowerCase();
		input = input.replaceAll("j", "i");
		input = cleanString(input);
		for(int i=0; i < input.length();i++) {
			res += parsePolybius(input.charAt(i));
		}
		return res;
	}
	
	public static String decipher(String input) {
		String res = "";
		
		input = input.toLowerCase();
		String[] tab = input.split(" ");
		input = cleanPolybiusString(tab);

		tab = input.split(" ");
		for(int i=0; i < tab.length;i++) {
			if(i != 0)
				res += " ";
	
			for (int j=0; j < tab[i].length();j+=2) {

				res += unparsePolybius(tab[i].substring(j, j+2));
			}
		}
		return res;
	}
	
	private static char unparsePolybius(String input) {
		if(input.equals(""))
			return ' ';
		else
			return matrix[Integer.parseInt(input.substring(0,1))-1][Integer.parseInt(input.substring(1,2))-1];
	}
	
	private static String parsePolybius(char c) {
		String num = "";
		for(int i = 0; i < 5; i++) {
		     for(int j = 0; j < 5; j++) {
		       if( c == matrix[i][j]) {
		    	   num = String.valueOf(i+1)+String.valueOf(j+1);
		    	   break;
		       }
		       else if (c == ' ')
		    	   num = String.valueOf(c);
		     }
		 }
		return num;
	}
	
	private static String cleanString(String input) {
		String res = "";
		for(int i=0; i < input.length(); i++) {
			if(isValidChar(input.charAt(i)))
				res += input.charAt(i);
		}
		return res;
	}
	
	private static String cleanPolybiusString(String[] input) {
		String res = "";
		for(int i=0; i < input.length; i++) {
			if(i != 0)
				res += " ";

			for (int j=0; j < input[i].length();j+=2) {
				if(isValidPolybiusChar(input[i].substring(j, j+2))) {
					res += input[i].substring(j, j+2);
				}	
			}
		}

		return res;
	}
	
	private static Boolean isValidPolybiusChar(String s) {
		try {
		return((Integer.parseInt(s) >=11 && Integer.parseInt(s) <= 55) || s.equals(""));
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
	
	private static Boolean isValidChar(char c) {
		return(	(c >= 'A' && c <= 'Z') ||
				(c >= 'a' && c <= 'z') ||
				(c == ' '));
	}
	
	
	Boolean isValidString(String inputString) {
		Boolean valid = true;
		for (int i=0; i < inputString.length();i++) {
			if(!(isValidChar(inputString.charAt(i)))) {
				valid = false;
				break;
			}
		}
		return valid;
	}

}
