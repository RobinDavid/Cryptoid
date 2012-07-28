package cryptoid.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Caesar {

	public Caesar() {
		//so bruteforce and analyze are available
	}
	public static Boolean bruteAvailable() { return true; }
	public static Boolean analyseAvailable() { return true; }
	
	
	public static String cipher(String toCipherText, int shift) {

		StringBuffer buf = new StringBuffer( toCipherText);
		
		for (int i=0; i< toCipherText.length(); i++) {
			char buffer=toCipherText.charAt(i);
			int value=(int)buffer+shift;//result Ascii code of new char
			
			if(((int)buffer) >= 65 && ((int)buffer) <= 90) {
				if(value > 90) {
					buffer=(char)( (65-1) + (value - 90));
				}
				else if(value < 65) {
					buffer=(char)( (90+1) - (65 - value));
				}
				else
					buffer=(char)value;
			}
			else if(((int)buffer) >= 97 && ((int)buffer) <= 122 ) {
				if(value > 122) {
					buffer=(char)( (97-1) + (value - 122));
				}
				else if(value < 97) {
					buffer=(char)( (122+1) - (97 - value));
				}
				else
					buffer=(char)value;
			}
			else
				buffer=(char)((int)buffer);
			buf.setCharAt(i, buffer );
		}
		toCipherText=buf.toString();
		return toCipherText;
	}
	
	public static String decipher(String toPlainText, int shift) {
		StringBuffer buf = new StringBuffer( toPlainText);
		for (int i=0; i< toPlainText.length(); i++) {
			char buffer=toPlainText.charAt(i);
			int value=(int)buffer-shift;
			
			if(buffer >= 'A' && buffer <= 'Z') {
				if(value < 65) {
					buffer=(char)( (90+1) - (65 - value));
				}
				else if(value > 90) {
					buffer=(char)(  (65-1) +(value -90));
				}
				else
					buffer=(char)((int)toPlainText.charAt(i)-shift);
			}
			else if(buffer >= 'a' && buffer <= 'z') {
				if(value < 97) {
					buffer=(char)( (122+1) - ( 97 - value));//122=z but it is 123 look's better if you don't understand(ex if 122 - (a-1)=y so it should be 123
				}
				else if(value > 122) {
					buffer=(char)( (97-1) + (value - 122));
				}
				else
					buffer=(char)((int)toPlainText.charAt(i)-shift);
			}
			else
				buffer=(char)((int)toPlainText.charAt(i));
			buf.setCharAt(i, buffer );
		}
		toPlainText=buf.toString();
		return toPlainText;
	}
	
	public Boolean isValidString(String inputString) {
		//never call in this implementation of Caesar (because uncipher, cipher ignore wrong char)
		return true;
	}
	
	//--------------------------- Elements for cryptanalysis ---------------------------------
	public static float isCaesarString(String input) {
		int nb_wrong_char = 0;
		for (int i=0; i < input.length(); i++) {
			if(!((input.charAt(i) >= 'A' && input.charAt(i) <= 'Z') ||
				(input.charAt(i) >= 'a' && input.charAt(i) <= 'z'))) {
				nb_wrong_char++;
			}
		}
		return 100 - ((nb_wrong_char * 100) / input.length());
	}
	
	public static ArrayList<String> analyzeBasedOnShortWord(String input,String pre) {
		ArrayList<String> list= new ArrayList<String>();
		HashSet<String> hs = new HashSet<String>();
		input=input.toLowerCase();
		String[] elem = input.split(" ");
		for (int i=0; i < elem.length; i++) {
			if(elem[i].length() == 1) {
				if(elem[i].charAt(0) >= 'a' && elem[i].charAt(0) <= 'z')
					hs.add(elem[i]);
			}
		}
		
		//for i
        Iterator<String> it = hs.iterator();
        while(it.hasNext()) {
        	int buf = (int)(it.next().toLowerCase().charAt(0));
        	if(!(buf == 105))//if != i
        		list.add(pre+"i: found'"+(char)buf+"' -> "+decipher(input, (buf -105)));
        }
        
        //for a
        it = hs.iterator();
        while(it.hasNext()) {
        	int buf = (int)(it.next().toLowerCase().charAt(0));
        	if(!(buf == 97))//if != i
        		list.add(pre+"a: found'"+(char)buf+"' ->"+decipher(input, (buf - 97)));
        }
        return list;
	}
	
	public static ArrayList<String> analyzeBasedOnOccur(String input,String pre) {
		ArrayList<String> list= new ArrayList<String>();
		Integer[] tab = new Integer[27];//we will don't use the indice 0
		for(int i=0;i<tab.length;i++)
			tab[i]=0;
		input=input.toLowerCase();
		
		for (int i=0; i<input.length() ; i++ ) {
			//System.out.println(input.charAt(i)+" "+((int)input.charAt(i)-97));
			if(input.charAt(i) >= 'a' && input.charAt(i) <= 'z')
				tab[(int)input.charAt(i)-97]+=1;
		}
		int bestscore=0;
		char bestchar='e';
		for (int i=0;i<tab.length; i++) {
			if(tab[i] != 0)
				if(tab[i] > bestscore) {
					bestscore=tab[i];
					bestchar=(char)(i+97);
				}
		}
		list.add(pre+"Best char :"+bestchar+"\n"+pre+"Score :"+bestscore);
		list.add(pre+" Result :"+Caesar.decipher(input, ((int)bestchar - 101)));
		return list;
	}
	//--------------------------------------------------------------------------------
}
