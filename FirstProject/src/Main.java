/*
 *      LZW Compression/Decompression	
 *      
 *      Rinalds Miezītis 201rdb106 4.grupa           
 */
import java.io.*;
import java.util.*;
import java.util.List;

public class Main {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		System.out.println("\nSapspiest failu                    -comp");
        System.out.println("Dekodet failu                      -decomp");
        System.out.println("Noteikt faila izmeru               -size");
        System.out.println("Salidzinat faila saturu            -equal");
        System.out.println("Ievadit informaciju par \n" +
        "programmas izstradatajiem          -about");
        System.out.println("Pabeigt darbu                      -exit");
        
		Scanner sc = new Scanner(System.in);
		CompressDecompress cd = new CompressDecompress();
		while (true) {	        
				System.out.print("\nIzvele: ");
				String input = sc.next();
				switch (input) {
				case "exit": {
					exit();
				}
				case "comp": {
					try {
						System.out.print("source file name: ");
						String sourceFileName = sc.next();
						System.out.print("archive name: ");
						String archiveFileName = sc.next();
						cd.Compress(sourceFileName, archiveFileName);
					} catch (IOException e) {
						System.out.println("wrong input");					
					}
					break;
				}
				case "decomp": {
					try {
						System.out.print("archive name: ");
						String archiveFileName = sc.next();
						System.out.print("file name: ");
						String resultFileName = sc.next();
						cd.Decompress(archiveFileName, resultFileName);
					} catch (IOException e) {
						System.out.println("wrong input");						
					}
					break;
				} case "size": {
					try {
						System.out.print("file name: ");
						String sourceFileName = sc.next();
						cd.Size(sourceFileName);
					} catch (IOException e) {
						System.out.println("wrong input");						
					}
					break;
				} case "equal": {
					try {
						System.out.print("first file name: ");
						String sourceFileName = sc.next();
						System.out.print("second file name: ");
						String sourceFileName2 = sc.next();
						cd.Equal(sourceFileName, sourceFileName2);					
					} catch (IOException e) {
						System.out.println("wrong input");						
					}
					break;
				} case "about": {
					System.out.println(about());
					break;
				}				
				default: System.out.println("Wrong input");
					break;
				}
			}
	}
	// Izvadīt informāciju par programmas izstrādātājiem
	public static String about() {
		return  "\n201rdb106 Rinalds Miezītis 4.grupa\n";	
	}
	
	public static void exit() {	
		System.out.println("Darbs ir pabeigts");
		System.exit(0);	
	}		
}


class CompressDecompress {
	// Saspiest failu
	@SuppressWarnings("resource")
	public void Compress(String inputFile, String outputFile) throws IOException {		
		FileInputStream fInput = new FileInputStream(inputFile);		
		FileOutputStream fOutput1 = new FileOutputStream(outputFile);
		long fileSize = new File(inputFile).length();
		byte[] allBytes = new byte[(int) fileSize];
		char[] uncompressed = new char[(int) fileSize];
		fInput.read(allBytes);
		for (int i = 0; i < fileSize; i++) {
			uncompressed[i] = (char) (allBytes[i] & 0xFF);
		}
		int dSize = 256;
		List<Integer> encoded = new ArrayList<Integer>();
		Map<String, Integer> dict = new HashMap<String, Integer>();
		for (int i = 0; i < 256; i++)
			dict.put("" + (char) i, i);
		String s = "";
		for (char l : uncompressed) {
			String sc = s + l;
			if (dict.containsKey(sc))
				s = sc;
			else {
				encoded.add(dict.get(s));
				dict.put(sc, dSize++);
				s = "" + l;
			}
		}
		if (s != "")
			encoded.add(dict.get(s));		
		fOutput1.write("LZW".getBytes());
		StringBuffer toWrite = new StringBuffer();
		int i;
		for (i = 0; i < encoded.size(); i++) {
			if (encoded.get(i) != null) {
				if ((encoded.get(i) > 255) && (encoded.get(i) <= 65535)) {
					toWrite.append("1");
					toWrite.append(String.format("%8s", Integer.toBinaryString(encoded.get(i) >>> 8)).replace(' ', '0'));
					toWrite.append("0");
					toWrite.append(
							String.format("%8s", Integer.toBinaryString(encoded.get(i) & 0xFF)).replace(' ', '0'));
				} else if (encoded.get(i) > 65535) {
					toWrite.append("1");
					toWrite.append(
							String.format("%8s", Integer.toBinaryString(encoded.get(i) >>> 16)).replace(' ', '0'));
					toWrite.append("1");
					toWrite.append(String.format("%8s", Integer.toBinaryString((encoded.get(i) >>> 8) & 0xFF))
							.replace(' ', '0'));
					toWrite.append("0");
					toWrite.append(
							String.format("%8s", Integer.toBinaryString(encoded.get(i) & 0xFF)).replace(' ', '0'));
				} else {
					toWrite.append("0");
					toWrite.append(
							String.format("%8s", Integer.toBinaryString(encoded.get(i) & 0xFF)).replace(' ', '0'));
				}
			} else {
				toWrite.append("000000000");
			}
		}
		int iLeftovers = toWrite.length() % 8;
		for (i = 0; i < 8 - iLeftovers; i++) {
			toWrite.append('0');
		}
		toWrite.append(String.format("%8s", Integer.toBinaryString((8 - iLeftovers) & 0xFF)).replace(' ', '0'));
		int h, b = 0;
		byte[] toFile = new byte[toWrite.length() / 8];
		for (h = 7; h < toWrite.length(); h = h + 8) {
			toFile[b] = (byte) (Integer.parseInt(toWrite.substring(h - 7, h + 1), 2));
			b++;
		}
		System.out.println("File is compressed");
		fOutput1.write(toFile);		
		fInput.close();
		fOutput1.close();
	}
	
	// Dekodēt failu
	@SuppressWarnings("resource")
	public boolean Decompress(String inputFile, String outputFile) throws IOException {	
		
		FileInputStream fInput = new FileInputStream(inputFile);	
		FileOutputStream fOutput2 = new FileOutputStream(outputFile);
		long fileSize = new File(inputFile).length();
		byte[] allBytes = new byte[(int) fileSize];
		List<Integer> compressed = new ArrayList<Integer>();
		fInput.read(allBytes);

		if ((char) allBytes[0] != 'L' || (char) allBytes[1] != 'Z' || (char) allBytes[2] != 'W') {
			fInput.close();
			fOutput2.close();
			return false;
		}
		StringBuffer codeString = new StringBuffer();
		int i, j;
		for (j = 3; j < allBytes.length; j++) {
			codeString.append(String.format("%8s", Integer.toBinaryString(allBytes[j] & 0xFF)).replace(' ', '0'));
		}
		for (i = 1; i <= allBytes[allBytes.length - 1] + 8; i++) {
			codeString.deleteCharAt(codeString.length() - 1);
		}		
		StringBuffer s = new StringBuffer();
		for (i = 0; i < codeString.length();) {
			if (codeString.charAt(i) == '1') {
				if (codeString.charAt(i + 9) == '1') {
					s.append(codeString.substring(i + 1, i + 9));
					s.append(codeString.substring(i + 10, i + 18));
					s.append(codeString.substring(i + 19, i + 27));
					compressed.add(Integer.parseInt(s.substring(0, 24), 2));
					s.delete(0, 24);
					i = i + 27;
				} else {
					s.append(codeString.substring(i + 1, i + 9));
					s.append(codeString.substring(i + 10, i + 18));
					compressed.add(Integer.parseInt(s.substring(0, 16), 2));
					s.delete(0, 16);
					i = i + 18;
				}
			} else {
				compressed.add(Integer.parseInt(codeString.substring(i + 1, i + 9), 2));
				i = i + 9;
			}
		}		
		int dSize = 256;
		String f = "" + (char) (int) compressed.remove(0);
		StringBuilder decoded = new StringBuilder(f);
		Map<Integer, String> dict = new HashMap<Integer, String>();
		for (i = 0; i < 256; i++)
			dict.put(i, "" + (char) i);
		for (int l : compressed) {
			String entry;
			if (dict.containsKey(l))
				entry = dict.get(l);
			else if (l == dSize)
				entry = f + f.charAt(0);
			else
				throw new IllegalArgumentException("Bad Compression");
			decoded.append(entry);
			dict.put(dSize++, f + entry.charAt(0));
			f = entry;
		}		
		byte[] toWrite = new byte[decoded.length()];
		for (i = 0; i < decoded.length(); i++) {
			toWrite[i] = (byte) decoded.charAt(i);
		}
		System.out.println("File is decompressed");
		fOutput2.write(toWrite);
		fInput.close();
		fOutput2.close();
		return true;
	}
	// Noteikt faila izmēru baitos
	public void Size(String sourceFileName) throws FileNotFoundException {
		
		File filename = new File(sourceFileName);

			long bytes = filename.length();
			if(bytes!=0) {
			System.out.println("size: " + bytes);
			} else {
				System.out.println("file not found");
				return;
			}
			
	}
	// Salīdzināt failu saturu
	public void Equal(String sourceFileName1, String sourceFileName2) throws FileNotFoundException {
		
		File filename1 = new File(sourceFileName1);
		File filename2 = new File(sourceFileName2);
		
			long bytes1 = filename1.length();			
			long bytes2 = filename2.length();
			
			if(bytes1!=0 && bytes2!=0) {			
				if (bytes1 ==  bytes2) {
					System.out.println("\n1. File size: "+ bytes1 +" bytes\n"+ 
							"2. File size: "+bytes2+ " bytes\n\n" + 
							"-true");
				} else {
					System.out.println("\n1. File size: "+ bytes1 +" bytes\n"+ 
							"2. File size: "+bytes2+ " bytes\n" + 
							"-false");
				}
			} else {
				System.out.println("file not found");		
				return;
			}
	}
}