package cryptoid.algorithms;

public class Morse {

	public Morse() {
		// TODO Auto-generated constructor stub
	}
	
	public static Boolean bruteAvailable() { return false; }
	public static Boolean analyseAvailable() { return false; }
	
	public static String cipher(String input) {
		input = input.toUpperCase();
		input = cleanString(input);
		String res = "";
		for(int i=0; i < input.length();i++) {
			res += parseMorse(input.charAt(i));
		}
		return res;
	}
	
	public static String decipher(String input) {
		String res = "";
		input = cleanMorseString(input);
		String[] tab= input.split(" ");
		//from now there's no wrong morse chars
		for(int i=0; i <tab.length;i++) {
			res += unparseMorse(tab[i]);
		}
		return res;
	}
	
	private static Boolean isValidMorseChar(String s) {
		Boolean valid = false;
		String[] alphabet = { ".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--..","/"};
		for (int i=0; i < alphabet.length; i++) {
			if(alphabet[i].equals(s)) {
				valid = true;
				break;
			}
		}
		return valid;
	}
	
	public static Boolean isValidMorseString(String input) {
		Boolean valid= true;
		String[] tab = input.split(" ");
		for (int i=0; i <tab.length; i++) {
			if(!(isValidMorseChar(tab[i]))){
				valid = false;
				break;
			}
		}
		return valid;
	}
	
	private static Boolean isValidChar(char c) {
		return ((c >= 'A' && c <= 'Z') || c == ' ');
	}
	
	private static Boolean isMorseChar(char c ) {
		return (c == '-' || c== '.' || c==' ' || c== '/');
	}
	
	
	public Boolean isValidString(String inputString) {
		inputString.toUpperCase();
		Boolean valid = true;
		for (int i=0; i<inputString.length();i++){
			if (!(isValidChar(inputString.charAt(i)))) {
				valid = false;
				break;
			}
		}
		return valid;
	}

	public static float isMorseString(String input) {
		int nb_wrong_char = 0;
		for (int i=0; i < input.length(); i++) {
			if(!(isMorseChar(input.charAt(i)))) {
				nb_wrong_char++;
			}
		}
		return 100 - ((nb_wrong_char * 100) / input.length());
	}
	
	public String reportWrongChar(String input) {
		input.toUpperCase();
		String res = "";
		for (int i=0; i < input.length(); i++) {
			if(!(isValidChar(input.charAt(i)))) {
				res += input.charAt(i)+"(char "+i+")\n";
			}
		}
		return res;
	}
	
	public String reportWrongMorseChar(String input) {
		String res = "";
		String[] tab = input.split(" ");
		for (int i=0; i <tab.length; i++) {
			if(!(isValidMorseChar(tab[i]))){
				res += tab[i]+"\n";
			}
		}
		return res;
	}
	
	private static String cleanMorseString(String input) {
		String res = "";
		String[] tab = input.split(" ");
		for (int i=0; i <tab.length; i++) {
			if(isValidMorseChar(tab[i])){
				res += tab[i]+" ";
			}
		}
		return res;
	}
	
	private static String cleanString(String input) {
		String res = "";
		for (int i=0; i < input.length(); i++) {
			if(isValidChar(input.charAt(i)))
				res += input.charAt(i);
		}
		return res;
	}
	
	private static String parseMorse(char c) {
		String res = "";
		switch(c) {
		case 'A': res = ".- "; 	break;
		case 'B': res = "-... "; break;
		case 'C': res = "-.-. "; break;
		case 'D': res = "-.. "; break;
		case 'E': res = ". "; break;
		case 'F': res = "..-. "; break;
		case 'G': res = "--. "; break;
		case 'H': res = ".... "; break;
		case 'I': res = ".. "; break;
		case 'J': res = ".--- "; break;
		case 'K': res = "-.- "; break;
		case 'L': res = ".-.. "; break;
		case 'M': res = "-- "; break;
		case 'N': res = "-. "; break;
		case 'O': res = "--- "; break;
		case 'P': res = ".--. "; break;
		case 'Q': res = "--.- "; break;
		case 'R': res = ".-. "; break;
		case 'S': res = "... "; break;
		case 'T': res = "- "; break;
		case 'U': res = "..- "; break;
		case 'V': res = "...- "; break;
		case 'W': res = ".-- "; break;
		case 'X': res = "-..- "; break;
		case 'Y': res = "-.-- "; break;
		case 'Z': res = "--.. "; break;
		case ' ': res = "/ "; break;//or / to choose
		}
		return res;
	}
	
	private static char unparseMorse(String s) {
		char res =' ';//error instead
		if (s == ".-") res = 'A';//could not d a switch with a string
		else if (s.equals("-...")) res = 'B';
		else if (s.equals("-.-.")) res = 'C';
		else if (s.equals("-..")) res = 'D';
		else if (s.equals(".")) res = 'E';
		else if (s.equals("..-.")) res = 'F';
		else if (s.equals("--.")) res = 'G';
		else if (s.equals("....")) res = 'H';
		else if (s.equals("..")) res = 'I';
		else if (s.equals(".---")) res = 'J';
		else if (s.equals("-.-")) res = 'K';
		else if (s.equals(".-..")) res = 'L';
		else if (s.equals("--")) res = 'M';
		else if (s.equals("-.")) res = 'N';
		else if (s.equals("---")) res = 'O';
		else if (s.equals(".--.")) res = 'P';
		else if (s.equals("--.-")) res = 'Q';
		else if (s.equals(".-.")) res = 'R';
		else if (s.equals("...")) res = 'S';
		else if (s.equals("-")) res = 'T';
		else if (s.equals("..-")) res = 'U';
		else if (s.equals("...-")) res = 'V';
		else if (s.equals(".--")) res = 'W';
		else if (s.equals("-..-")) res = 'X';
		else if (s.equals("-.--")) res = 'Y';
		else if (s.equals("--..")) res = 'Z';
		else if (s.equals("")) res = ' ';
		else if (s.equals("/")) res = ' ';
		return res;
	}
}