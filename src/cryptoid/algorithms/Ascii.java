package cryptoid.algorithms;

public class Ascii {

	public Ascii() {
	}
	
	public static Boolean bruteAvailable() { return false; }
	public static Boolean analyseAvailable() { return true; }
	
	
	//----------------- Plain to * -------------------
	public static String stringToHex(String input) {
		String res = "";//new String[] { };
		for(int i=0; i< input.length(); i++) {
			res += Integer.toHexString((int)input.charAt(i))+" ";
		}
		return res;
	}
	
	public static String stringToDec(String input) {
		String res = "";
		for(int i=0; i < input.length(); i++) {
			res += (int)input.charAt(i)+" ";
		}
		return res;
	}
	
	public static String stringToBinary(String input) {
		String res = "";
		for(int i=0; i < input.length(); i++) {
			res += Integer.toBinaryString(Integer.valueOf(input.charAt(i)))+" ";
		}
		return res;
	}
	//-----------------------------------------
	
	//----------------- Hex to * ----------------------
	public static String hexToDec(String input) {
		String[] temp = input.split(" ");
		Integer[] res = new Integer[temp.length];
		for(int i=0; i < temp.length; i++) {
			try {
			res[i] = Integer.parseInt(temp[i], 16);
			}
			catch (NumberFormatException e) {}
		}
		String result="";
		for(int i=0; i < res.length; i++) {
			result += res[i]+" ";
		}
		return result;
	}
	
	public static String hexToBinary(String input) {
		String res = "";
		String[] temp=input.split(" ");
		for(int i=0; i < temp.length; i++) {
			try {
				res += Integer.toBinaryString(Integer.parseInt(temp[i],16))+" ";
			}
			catch (NumberFormatException e) {}
		}
		return res;
	}
	
	public static String hexToString(String input) {
		String[] buf = input.split(" ");
		Integer[] res = new Integer[buf.length];
		for(int i=0; i < buf.length; i++) {
			try {
			res[i] = Integer.parseInt(buf[i], 16);
			}
			catch (NumberFormatException e) {}
		}
		String result = "";
		for(int i=0; i < buf.length; i++) {
			result += (char)(int)res[i];
		}
		return result;
	}
	//---------------------------------------------------------
	
	//----------------------- Dec to * ----------------------
	public static String decToString(String input) {
		String[] buf = input.split(" ");
		String res = "";
		for(int i=0; i < buf.length; i++) {
			try {
			res += (char)Integer.parseInt(buf[i]);
			}
			catch (NumberFormatException e) {}
		}
		return res;
	}
	
	public static String decToBinary(String input) {
		String res="";
		String[] buf= input.split(" ");
		for(int i=0; i < buf.length;i++) {
			try {
				res +=	Integer.toBinaryString(Integer.parseInt(buf[i]))+" ";
			}
			catch (NumberFormatException e) {}
		}
		return res;
	}
	
	public static String decToHex(String input) {
		String res="";
		String[] buf= input.split(" ");
		for(int i=0; i < buf.length;i++) {
			try {
				res +=	Integer.toHexString(Integer.parseInt(buf[i]))+" ";
			}
			catch (NumberFormatException e) {}
		}
		return res;
	}
	//-------------------------------------------------------
	
	//--------------------- Binary to * -------------------------
	public static String binaryToDec(String input) {
		String[] buf = input.split(" ");
		String res="";
		for(int i=0; i < buf.length; i++) {
			try {
				res += Integer.parseInt(buf[i], 2)+" ";
			}
			catch (NumberFormatException e) {}
		}
		return res;
	}
	
	public static String binaryToString(String input) {
		String[] buf = input.split(" ");
		String res="";
		for(int i=0; i < buf.length; i++) {
			try {
				res += (char)Integer.parseInt(buf[i], 2)+" ";
			}
			catch (NumberFormatException e) {}
		}
		return res;
	}
	
	public static String binaryToHex(String input) {
		String[] buf = input.split(" ");
		String res="";
		for(int i=0; i < buf.length; i++) {
			try {
				res += Integer.toHexString(Integer.parseInt(buf[i], 2))+" ";
			}
			catch (NumberFormatException e) {}
		}
		return res;
	}
	//----------------------------------------------------------

	
	
	
	//---------------- Elements for Cryptanalysis ----------------
	public static float isHexString(String input) {
		int nb_wrong_char = 0;
		for (int i=0; i<input.length();i++) {
			if(!(
				(input.charAt(i) >= '0' && input.charAt(i) <= '9') ||
				(input.charAt(i) >= 'A' && input.charAt(i) <= 'F') ||
				(input.charAt(i) >= 'a' && input.charAt(i) <= 'f'))) {
					nb_wrong_char++;
				//if not between 0-9 or A-Z or a-z or ' '
			}
		}
		return 100 - ((nb_wrong_char * 100) / input.length());//return average of wrong chars
	}
	
	public static Boolean isHexChar(char c) {
		Boolean valid=true;
		if(!((c >= '0' && c <= '9') ||
			(c >= 'A' && c <= 'F') ||
			(c >= 'a' && c <= 'f'))) {
			valid=false;
		}
		return valid;
	}
	
	public static float isBinaryString(String input, Boolean strict) {
		float nb_wrong_char = 0;
		for (int i=0; i<input.length();i++) {
			if(strict) {
				if (!((input.charAt(i) == '0') || (input.charAt(i) == '1')))
						nb_wrong_char++;
			} else {
				if (!((input.charAt(i) == '0') || (input.charAt(i) == '1') || (input.charAt(i) == ' ')))
					nb_wrong_char++;
			}
		}
		return 100 - ((nb_wrong_char * 100) / input.length());
	}
	
	private static Boolean isBinaryChar(char c) {
		return (c == '1' || c =='0');
	}
	
	public static float isDecString(String input) {
		float nb_wrong_char = 0;
		for (int i=0; i<input.length();i++) {
			if(!(input.charAt(i) >= '0' && input.charAt(i) <= '9'))
				nb_wrong_char++;
		}
		return 100 - ((nb_wrong_char * 100) / input.length());
	}
	
	public static Boolean isDecChar(char c) {
		return (c >= '0' && c <= '9');
	}
	
	public static void analyze(String raw, int deep, int maxdeep) {
		String pre="";
		String res="";
		if(deep == maxdeep) {
			return;//we leave the method if the deepness is 0
		}
		else
			for (int e=0;e < deep;e++)
				pre+=" ";
		float accuracy = 0;
		
		deep++;
		System.out.println(pre+" To be analyze "+deep+" :"+raw);
		
		accuracy = isHexString(raw);
		if(accuracy >= 95) {
			System.out.println(pre+"Good Hex average : "+accuracy+"% for "+raw);
			res = approximateHexToString(raw);
			analyze(res, deep, maxdeep);
		}
		accuracy = isBinaryString(raw,false);
		if(accuracy >= 90) {
			System.out.println(pre+"Good Binary average : "+accuracy+"% for "+raw);
			res = approximateBinaryTo(raw, true, false);
			analyze(res, deep,maxdeep);
			res = approximateBinaryTo(raw, false, true);
			analyze(res, deep,maxdeep);			
		}
		accuracy = isDecString(raw);
		if(accuracy >= 80) {
			//Polybius for global
			System.out.println(pre+"Good Decimal average : "+accuracy+"% for "+raw);
			res = approximateDecToString(raw);
			analyze(res,deep,maxdeep);
		}
	}
	
	public static String approximateHexToString(String input) {
		String res="";
		//if ((input.length() % 2) != 0)
		//	input+="0";
		int i=0;
		while( i < input.length()) {
			//System.out.println("Analyze : "+input.charAt(i)+"  "+input.charAt(i+1));
			if(isHexChar(input.charAt(i)))
				if(i != (input.length()-1))
					if(isHexChar(input.charAt(i+1))){
						try {
							res+= (char)(int)Integer.parseInt(input.substring(i, i+2),16);
						}
						catch (NumberFormatException e) {}
						i+=2;
					}
					else {
						res+=input.charAt(i);
						i++;
					}
				else {
					res+=input.charAt(i);
					i++;
				}
			else {
				res +=input.charAt(i);
				i++;
			}
		}
		
		return res;
	}
	
	public static String approximateBinaryTo(String input,Boolean toString,Boolean toDec) {
		String res="";
		//if ((input.length() % 2) != 0)
		//	input+="0";
		int i=0;
		while( i < input.length()) {
			//System.out.println("Analyze : "+input.charAt(i)+"  "+input.charAt(i+1));
			if(isBinaryChar(input.charAt(i)))
				if(i != (input.length()-7))
					if((isBinaryString(input.substring(i, i+8), true)) == 100){
						if(toString)
							try {
								res+= (char)(int)Integer.parseInt(input.substring(i, i+8),2);
							}
						catch (NumberFormatException e) {}
						else if(toDec)
							try {
								res+= Integer.parseInt(input.substring(i, i+8),2);
							}
						catch (NumberFormatException e) {}
						i+=8;
					}
					else {
						res+=input.charAt(i);
						i++;
					}
				else {
					res+=input.charAt(i);
					i++;
				}
			else {
				res +=input.charAt(i);
				i++;
			}
		}
		
		return res;
	}
	
	public static String approximateDecToString(String input) {
		String res="";
		//if ((input.length() % 2) != 0)
		//	input+="0";
		int i=0;
		while( i < input.length()) {
			//System.out.println("Analyze : "+input.charAt(i)+"  "+input.charAt(i+1));
			if(isDecChar(input.charAt(i)))
				if(i != (input.length()-1))
					if(isDecChar(input.charAt(i+1))){
						try {
							res+= (char)(int)(Integer.parseInt(input.substring(i, i+2)));
						}
						catch (NumberFormatException e) {}
						i+=2;
					}
					else {
						res+=input.charAt(i);
						i++;
					}
				else {
					res+=input.charAt(i);
					i++;
				}
			else {
				res +=input.charAt(i);
				i++;
			}
		}
		
		return res;
	}
	//------------------------------------------------------------
}