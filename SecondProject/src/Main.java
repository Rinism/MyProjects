import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

//201RDB106 Rinalds Miezītis 4.grupa

public class Main {

	private int id;
    private String pilseta;
    private String datums;
    private int dienuskaits;
    private double cena;
    private String transports;
    private String filePath = "db.csv";
    private static ArrayList<Main> TripList = new ArrayList<>();
    
    Main() {

    }    
    @SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
    	Main trip = new Main();
        
        trip.create(TripList);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            loop:
            for (; ; ) {
              
                String input = br.readLine();
                String[] splitedInput = input.split(" ",2);
                
                if (splitedInput[0].equals("add")) {
                    trip.insert(splitedInput[1]);
                    continue;                	
                }                    
                if (splitedInput[0].equals("del")) {
                	trip.delete(splitedInput[1]);
                    continue;
                }                    
                if (splitedInput[0].equals("edit")) {
                	trip.edit(splitedInput[1]);
                    continue;
                }                    
                if (input.equals("print")) {
                	trip.print(TripList);
                    continue;
                }                    
                if (input.equals("sort")) {
                	trip.sort(TripList);
                    continue;
                }                    
                if (splitedInput[0].equals("find")) {
                	trip.find(splitedInput[1]);
                    continue;
                }                                                                      
                if (input.equals("avg")) {
                	trip.average(TripList);
                    continue;
                }                    
                if (input.equals("exit")) {
                    trip.saveFile(TripList);
                    break loop;
                }                            
                System.out.println("wrong command");
            }            
        } catch (IllegalArgumentException e) {
            System.out.println("Input error");
        }
    }

    private Main(int id, String pilseta, String datums, int dienuskaits, double cena, String transports) {
    	this.id = id;
        this.pilseta = pilseta;
        this.datums = datums;
        this.dienuskaits = dienuskaits;
        this.cena = cena;
        this.transports = transports;
    }

    private static void title() {
    	String leftAlignFormat = "%-4s%-21s%-13s%-9s%-6s%1s%n";      
        System.out.printf("------------------------------------------------------------\n");
        System.out.format(leftAlignFormat,"ID","City","Date","Days","Price","Vehicle");
        System.out.printf("------------------------------------------------------------\n");
    }
    
    static void print(ArrayList<Main> TripList) {
        title();
        String leftAlignFormat = "%-4s%-21s%-9s  %5s  %8.02f %-7s%n";
        for (Main trip : TripList) {
            System.out.format(leftAlignFormat, trip.id, trip.pilseta,
            		trip.datums, trip.dienuskaits,trip.cena, trip.transports);
        }
        System.out.printf("------------------------------------------------------------\n");
    }

    void create(ArrayList<Main> TripList) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] str = line.split(";");
                TripList.add(new Main(Integer.parseInt(str[0]), str[1], str[2], Integer.parseInt(str[3]),
                        Double.parseDouble(str[4]), (str[5])));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    static void insert(String splitInput) throws IOException {
    	
    	Main trip = new Main();
        try {
        	String[] split = splitInput.split(";", -1);;

        	if(split.length != 6) {
        		System.out.println("wrong field count");
        		return;
        	}
        	else {
        		
        	//id
            int id = Integer.parseInt(split[0]);
            int line=0;
            for (line = 0; line < TripList.size(); line++) {
        		if(id == TripList.get(line).getID()) {
        			System.out.println("wrong id");
        			return;
        		}
			}            
            if (id>=1000 || id<=99) {
            	System.out.println("wrong id");
            return;
            }  else
            trip.id= id;
           		
            //city
            String pilseta = split[1];
            String lower = pilseta.toLowerCase();
            String output = lower.substring(0, 1).toUpperCase() + lower.substring(1);
            trip.pilseta = output;
            
            //date
            String datums = split[2];             
            String[] str = datums.split("/");
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            
            int diena = Integer.parseInt(str[0]);
            int menesis = Integer.parseInt(str[1]);
           
            if((str[0].length() == 2) && (str[1].length() == 2)
            		&& (str[2].length() == 4)) {            
            	try {
            		if (format.parse(datums) != null)
            			if((diena>31 || diena<1) || (menesis>12 || menesis<1)) {
            				System.out.println("wrong date");
            				return;
            			}
            			else {
            				trip.datums = datums;
            			}				
            	} catch (ParseException e) {
            		System.out.println("wrong date");
            		return;
            	}     
            }
            	else {
            		System.out.println("wrong date");
            		return;
            	}
            
            //days
            trip.dienuskaits = Integer.parseInt(split[3]);
           
            //price
            trip.cena = Double.parseDouble(split[4]);
            
            //vehicle
            String transports = split[5];
            String upper = transports.toUpperCase();
            if(upper.equals("TRAIN") || upper.equals("BUS") ||
            		upper.equals("BOAT") || upper.equals("PLANE")) {
				trip.transports = upper;	
			} else {
				System.out.println("wrong vehicle");
				return;
			}
            
            TripList.add(trip);
            System.out.print("added\n");
        	}
        } catch (IllegalArgumentException e) {
            System.out.println("wrong input");    	    	 
        }
    }    
    
    void delete(String splitInput) throws IOException {
    	Main trip = new Main(); 
        int line=0;
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {           	      
        	try {
        	
        	String[] split = splitInput.split(" ");        	        	
        	trip.id = Integer.parseInt(split[0]);
        	
        	if(split.length != 1) {
        		System.out.println("wrong field count");
        		return;
        	}
        	else {
        		if(trip.id >= 1000 || trip.id <= 99) 
        			System.out.println("wrong id");  
        		else 
        			found = true;
       	
        	while ((br.readLine()) != null && line < TripList.size()) {    
        		if (found==false) {        			
        			break;
        		}
        		else {                 	
        			if (trip.id == TripList.get(line).getID()) {       
        				TripList.remove(line);
        				System.out.print("deleted\n");       				
        				found=false;
        				break;
        			}
        			else {               	
        				line=line+1;
        			}        			
        		}            
        	}
        	if(found==true) {
        	if(line==TripList.size()) {
    			System.out.println("wrong id"); 
        	}
        	}
        	}
        	}
        catch (IllegalArgumentException e) {
            System.out.println("Input error");
        }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");            
        }   
    }
    
    void edit(String splitInput) throws IOException {
    	Main trip = new Main(); 
        
        boolean found = false;
        int line=0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { 
        	       
        try {
        	String[] split = splitInput.split(";", -1);
        	if(split.length != 6) {
        		System.out.println("wrong field count");
        		return;
        	}
        	else {
            trip.id = Integer.parseInt(split[0]);
        	
            if(trip.id >= 1000 || trip.id <= 99) 
        		System.out.println("wrong id");  
        	else 
       		found = true;
       	
        	while ((br.readLine()) != null && line < TripList.size()) {    
        		if (found==false) {        			
        			break;
        		}
        		else {                 	
        			if (trip.id == TripList.get(line).getID()) {         				
        				    
        					//city
        					String pilseta = split[1];
        					if(!pilseta.equals("")) {        					
        				    String lower = pilseta.toLowerCase();
        				    String output = lower.substring(0, 1).toUpperCase() + lower.substring(1);
        					TripList.get(line).pilseta = output; }
        					
        					//date
        					String datums = split[2];
        					if(!datums.equals("")) {
        					String[] str = datums.split("/");
        					DateFormat format = new SimpleDateFormat("dd/MM/yyyy");        					          			       					
        					           
        				    int diena = Integer.parseInt(str[0]);
        				    int menesis = Integer.parseInt(str[1]);
        				           
        				    if((str[0].length() == 2) && (str[1].length() == 2)
        				            	&& (str[2].length() == 4)) {            
        				    	try {
        				    		if (format.parse(datums) != null)
        				    			if(diena>31 || diena<1 || menesis>12 || menesis<1) {
        				    				System.out.println("wrong date");
        				            		return;
        				    			}
        				            	else {
        				            		TripList.get(line).datums = datums;
        				            	}
        				    		
        				    	} catch (ParseException e) {
        				    		System.out.println("wrong date");
        				            return;
        				    	}         				    	
        				            }       					
        				    else {
        				    	System.out.println("wrong date");
        				        return;       				    		
        					}
        					}
        					//days
        					if(!split[3].equals("")) {  
        					int dienuskaits = Integer.parseInt(split[3]);        					    
        					TripList.get(line).dienuskaits = dienuskaits;}
        					
        					//price
        					if(!split[4].equals("")) {
        					double cena = Double.parseDouble(split[4]);        					      
        					TripList.get(line).cena = cena;}
        					     
        					//vehicle
        					String transports = split[5];
        					if(!transports.equals("")) {      
        					String upper = transports.toUpperCase();
        					if(upper.equals("TRAIN") || upper.equals("BUS") ||
        							upper.equals("BOAT") || upper.equals("PLANE")) {
        						TripList.get(line).transports = upper;	
        					} else {
        						System.out.println("wrong vehicle");
        						break;
        					}
        					}
        				System.out.print("changed\n");        				   
        				found=false;
        				break;
        			}
        			else {               	
        				line=line+1;       				
        			}      			
        		}       		        		
        	}
        	if(found==true) {
            	if(line==TripList.size()) {
        			System.out.println("wrong id"); 
            	}
            }
        	}
        } catch (IllegalArgumentException e) {
        	System.out.println("wrong input");
        }
        }                       
        catch (FileNotFoundException e) {
            System.out.println("File not found");            
        }
    }
    
    void sort(ArrayList<Main> TripList) {  
    	
    	TripList.sort(Comparator.comparing(Main::getGads).thenComparing(Main::getMenesis).thenComparing(Main::getDiena));;
    	 System.out.print("sorted\n");        
    }   
    
    void find(String splitInput) throws IOException {
        boolean print = false;

        try {
        	String[] split = splitInput.split(";", -1);;

        	if(split.length != 1) {
        		System.out.println("wrong field count");
        		return;
        	}
        	else {
            
            Double neededprice = Double.parseDouble(split[0]);

            for (Main trip : TripList) {
                if (trip.cena <= neededprice) {
                    if (!print) {
                    	title();
                    }

                    String leftAlignFormat = "%-4s%-21s%-9s  %5s  %8.02f %-7s%n";
                    System.out.format(leftAlignFormat, trip.id, trip.pilseta,
                    		trip.datums, trip.dienuskaits,trip.cena, trip.transports);                               
                    print = true;                   
                }
            }
            System.out.printf("------------------------------------------------------------\n");
            if (!print) System.out.println("Tadu ierakstu nav");
        	}
        } catch (IllegalArgumentException e) {
            System.out.println("wrong price");
        }       
    }
    
    void average(ArrayList<Main> TripList) throws IOException {

        int count = 0;
        double sum = 0.0;

        for (Main trip : TripList) {
                sum = sum + trip.cena;
                count++;
            }       
        System.out.printf("average=%.2f%n", sum/count);
    }
            
	private int getID() {
    	  
        return id;               
    }      
 
    private String getDiena() {
    	  String[] str = datums.split("/");
    	  String result = str[0];
    	  
        return result;           
    }
    
    private String getMenesis() {
	    String[] str = datums.split("/");
	    String result = str[1];

       return result;
    }        
    
    private String getGads() {
	    String[] str = datums.split("/");
	    String result = str[2];

       return result;
    }        
    
    void saveFile(ArrayList<Main> TripList) throws IOException {

        PrintWriter out = new PrintWriter(new FileWriter(filePath));

        for (Main trip : TripList) {
            out.println(trip.id + ";" + trip.pilseta + ";" + trip.datums + ";" +
            		trip.dienuskaits + ";" + trip.cena + ";" + trip.transports);
        }
        out.close();
    }
}
