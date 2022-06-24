import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
    	
    	/*
    	a -> 00
    	c -> 01
    	g -> 10
    	t -> 11   	    	    	   	   	
    	*/
    	    	   	 	   	 
    	System.out.println("\nBurtu sifresana                  -comp");
        System.out.println("Burtu atsifresana                -decomp");
        System.out.println("Izvadit informaciju par \n" +
        "programmas izstradataju          -about");
        System.out.println("Pabeigt darbu                    -exit");
         
 		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
 			loop:
            for (; ; ) {        
 				System.out.print("\nIzvele: ");
 				String input = br.readLine(); 				
                String[] splitedInput = input.split(" ",SpaceCount(input)+1);   
                
                if (splitedInput[0].equals("comp")) {                	
                	if(isValidInputFormat(splitedInput[1])){
                		StringToHex(splitedInput[1]);
                        continue;	
                	} else {
                		 System.out.println("wrong command format");
                	}
                }                    
                else if (splitedInput[0].equals("decomp")) {              	                	
                	byte[] bytes = new byte[Integer.parseInt(splitedInput[1])];
                	if(SpaceCount(input)-1 == Integer.parseInt(splitedInput[1])) { 
                		for (int i = 0; i < bytes.length; i++) {                 			
                			bytes[i] = (byte) Integer.parseInt(splitedInput[2+i]);                 			
                		}
                		if(isValidByteSize(bytes, splitedInput)) {
                			ByteToHex(bytes);
                    		HexToLetter(bytes);
                		} else {
                			System.out.println("wrong command format");
                			continue;
                		}               			                 		
                	} else {
                		System.out.println("wrong command format");
                		continue;
                	}
                }                    
                else if (input.equals("about")) {
                	System.out.println(about());
                    continue;                   
                }
                else if (input.equals("exit")) {
                    exit();
                    break loop;
                } 
                else System.out.println("wrong command");                                             
            }
 		} catch (IllegalArgumentException e) {
 			System.out.println("wrong command format");
 		}
    } 
    
    //comp
    private static void StringToHex (String str) {
    	str = str.toUpperCase();    	
    	byte[] byteArray = new byte[str.toCharArray().length];
    	
    	//encoding characters with specific bits
    	for (int i = 0; i < byteArray.length; i++) {
    		if(str.charAt(i)== 'A') {
    			byteArray[i] = (byte) 00;
    		} else if (str.charAt(i)== 'C') {
    			byteArray[i] = (byte) 01;
    		}else if (str.charAt(i)== 'T') {
    			byteArray[i] = (byte) 11;
    		}else if (str.charAt(i)== 'G') {
    			byteArray[i] = (byte) 10;
    		}
    	}
    	//byte array to string
    	String binary = "";
    	for(byte b: byteArray) {
    		binary += (String.format("%02d", b) + " ").trim();
        }
    	//unused bits filled with 0
    	while (binary.length()!=16) {
    		binary += "0";
    	}    	
    	    	
    	//outputs the number of characters and element values in hexadecimal counting system
    	System.out.println(str.length() + " " + binaryToHex(binary));			
    }
    
    //decomp
    private static void HexToLetter (byte[] bytes) {    	
    	//converting bytes to binary  	
    	String binary = "";
    	for (int i = 1; i < bytes.length; i++) 
    		binary += String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
    	
    	String[] array = binary.split("");
    	String letters = "";
    	int nucleotids = bytes[0];

    	//decoding characters with specific bits
    	for (int i = 0; i < array.length-1; i=i+2) {
    		if((array[i]+array[i+1]).equals("00") && letters.length()<nucleotids) {
    			letters += "A";
    		} else if ((array[i]+array[i+1]).equals("01") && letters.length()<nucleotids) {
    			letters += "C";
    		} else if ((array[i]+array[i+1]).equals("10") && letters.length()<nucleotids) {
    			letters += "G";
    		} else if ((array[i]+array[i+1]).equals("11") && letters.length()<nucleotids) {
    			letters += "T";
    		}    		
    	}    	
    	//outputs decoded letters
		System.out.println(letters);    	
    }
       
    private static int SpaceCount(String input) {
    	int space = 0;
    	for(int i=0; i<input.length(); i++){
    		char ch = input.charAt(i);
    		 if(ch==' ') space++;   		    
    		} 	
		return space;    	
    }
    
    private static boolean isValidByteSize(byte[] bytes, String[] splitedInput) {
    	int count = 0;
		for (int i = 0; i < bytes.length; i++)    
			if(Integer.parseInt(splitedInput[2+i]) > -128 && Integer.parseInt(splitedInput[2+i]) < 127) 
				count++;			
				
		if(count == bytes.length) return true;   		
		else return false;
	}
       
    private static boolean isValidInputFormat(String input) {
    	int count;
    	char[] chSearch = {'C', 'A', 'T', 'G'};
    	for (int i = 0; i < input.length(); i++) {
            char ch = input.toUpperCase().charAt(i);
            count = 0;
            for (int j = 0; j < chSearch.length; j++) {
               if (chSearch[j] == ch) count++;
            }
               if(count == 0) return false;                                   
    	}
		return true;  	
    }

	//used for encoding
    private static String binaryToHex(String binary) {
        int decimalValue = 0;
        int length = binary.length() - 1;
        for (int i = 0; i < binary.length(); i++) {
            decimalValue += Integer.parseInt(binary.charAt(i) + "") * Math.pow(2, length);
            length--;
        }
        return decimalToHex(decimalValue);
    }
    
    //used for encoding
    private static String decimalToHex(int decimal){
        String hex = "";
        while (decimal != 0){
            int hexValue = decimal % 16;
            hex = toHexChar(hexValue) + hex;
            decimal = decimal / 16;
        }
        return hex.replace("0", " ");
    }
    
    //used for encoding
    private static char toHexChar(int hexValue) {
        if (hexValue <= 9 && hexValue >= 0)
            return (char)(hexValue + '0');
        else
            return (char)(hexValue - 10 + 'A');
    }
    
    //used for decoding
    private static void ByteToHex (byte[] bytes) {   	
	    StringBuilder sb = new StringBuilder();
	    for (byte b : bytes) {
	    	sb.append(Integer.toHexString(b& 0xFF).toUpperCase()+ " ");
	    }
	    //outputs byte element values in hexadecimal counting system
	    System.out.println(sb.toString());	    	
    }
    
    public static String about() {	
		return "201RDB106 Rinalds Miezītis 4.grupa";	
	}
    
    public static void exit() {	
		System.exit(0);	
	}		
}