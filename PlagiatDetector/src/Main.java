import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
	
	static File testfile;
	public static String checkPath;
	public static String map = "";
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {
		
		File infofile = new File("infofile.txt");
		PrintStream fileStream = new PrintStream(infofile);
		
		System.out.print("Input the path of the Map: "); //(test)map
		map = sc.next();
		
		Plagirism obj = new Plagirism();
		
		File folder = new File(map);
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles) {
			Plagirism.checkPath = String.valueOf(file); 
			testfile = file; 
			if (String.valueOf(file).isEmpty())
				System.out.println("The Path File is Empty, Enter the Proper Path map Name");
			else if (!testfile.isFile()) {
				System.out.println("Wrong Path, Enter the Proper Path File Name");
			} else {
				try {
					fileStream.println(obj.getPlagiarism());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}	
		System.out.println("\nInformation file has been saved at:\n" + infofile.getAbsolutePath());
		fileStream.close();
	}
}

class Plagirism {
	public static String checkPath;
	public static Scanner sc = new Scanner(System.in);
	public static String Console[] = new String[1000];
	static int m;
	public static int mfile;
	public static String aString;
	public static int denom;
	public static String moreString;
	public static String moreConsole[]=new String[1000];
	

	
	public String getPlagiarism() throws IOException {
		
		StringBuilder stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");
	    
		String[] Check = new String[100000];
		int t;
		int j;
		int cfile = 0;
		int match = 0;
		float percentage;
		float finalpercentage;
		float cpercentage = 0;
		int sel[] = new int[100000];
		for (int b = 0; b < 100000; b++)
			sel[b] = 0;
		
		BufferedReader brc = new BufferedReader(new FileReader(checkPath));
		try {
			StringBuilder sbc = new StringBuilder();
			String line = brc.readLine();
			while (line != null) {
				sbc.append(line);
				sbc.append(System.lineSeparator());
				line = brc.readLine();
			}
			String everything = sbc.toString();
			//System.out.println(everything);
			StringTokenizer st1 = new StringTokenizer(everything, "\n.");
			int i = 0;
			while (st1.hasMoreTokens()) {
				i++;
				Check[i] = st1.nextToken();
			}
			denom=i;
//			for( i=1;Check[i]!=null;i++)
//			System.out.println("Line"+i+"="+Check[i]);
		} finally {
			brc.close();
		}

		File folder = new File(Main.map);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {			
				if(!(((Main.testfile).getAbsolutePath()).equals((file).getAbsolutePath()))){
				BufferedReader br = new BufferedReader(new FileReader(file));
				try {
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();
					while (line != null) {
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}
					String everything = sb.toString();
					//System.out.println(everything);
					StringTokenizer st2 = new StringTokenizer(everything, "\n.");
					t = 0;
					String[] Source = new String[100000];
					
					while (st2.hasMoreTokens()) {
						t++;
						Source[t] = st2.nextToken();
					}
					match = 0;
					for (j = 1; Check[j] != null; j++) {
						for (int k = 1; Source[k] != null; k++) {
							if ((Check[j]).equals(Source[k])) {{
									sel[j] = 1;
									match++;
									moreConsole[j-1]="The String:" +Check[j]+ "is Copied from this file";
									stringBuilder.append(match + " " + j);
									stringBuilder.append(ls);
									break;									
								}
//							System.out.println(match + "" + j);
							}							
						}
					}
					j = j - 1;
//					System.out.println("Match= " + match + " j= " + j);
					stringBuilder.append("Match = " + match + ", j = " + j);
					stringBuilder.append(ls);
					percentage = (float) match * 100 / j;
					if (percentage > 0.0) {
//						System.out.println(percentage + "% copied from source file " + (cfile + 1));
						stringBuilder.append("The file " + (Main.testfile) + " " + percentage + "% plagiarised from the file " + (file));
						stringBuilder.append(ls);
						Console[m] ="The file " + (Main.testfile) +" "+  percentage + "% plagiarised from the file " + (file);
						mfile=cfile+1;
						m++;					
					}
					cfile++;
//					if(percentage>cpercentage)
				} finally {
					br.close();
				}
			}
			}
		}
		for (int b = 0; b < 100000; b++) {
			cpercentage = cpercentage + sel[b];
		}
		if (cpercentage == 0) {
			finalpercentage = 0;
		} else {
			finalpercentage = cpercentage / denom * 100;
		}
		// System.out.println("/////////////////////////////////////\nThe Percentage of Plagiarism is" + finalpercentage);
		Console[m] = "****************************************************\nThe Percentage of Plagiarism is "+ finalpercentage;
		moreConsole[m] = "****************************************************\nThe Percentage of Plagiarism is "+ finalpercentage;

		aString = Console[0];
		moreString=Console[0]+"\n"+moreConsole[0];
		stringBuilder.append("____________________________________________________________");
		stringBuilder.append(ls);
		for (int k = 1; k < m; k++) {
			aString += "\n" + Console[k];
			moreString+= "\n____________________________________________________________\n" + Console[k]+"\n" + moreConsole[k];
			}
		aString+= "\n" +Console[m];
		moreString+= "\n"+Console[m];
		stringBuilder.append(moreString);
		stringBuilder.append(ls);
		//System.out.println(moreString);
		m=0;
		stringBuilder.append("****************************************************"+"\n\n");
		stringBuilder.append(ls);
		
		return stringBuilder.toString();
	}
}