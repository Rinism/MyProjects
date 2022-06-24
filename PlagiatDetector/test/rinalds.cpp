//201RDB106 Rinalds Miezitis 4.grupa
#include <iostream>
#include<fstream>
#include<string>
#include<vector>
#include <sstream>
#include <regex>
#include <iomanip>
using namespace std;

ifstream inFile;
ofstream outFile;
string file_address = "db.csv";
string error_file_address = "err.txt";

struct Route {
    public:
        string startStop;
        string endStop;
        string day;
        string time;
        double price;
};

void ltrim (string &str) {
    str.erase (str.begin(), find_if (str.begin(), str.end(), [] (unsigned char ch) {
        return !isspace (ch);
    } ) );
}
void rtrim (string &str) {
    str.erase (find_if (str.rbegin(), str.rend(), [] (unsigned char ch) {
        return !isspace (ch);
    } ).base(), str.end() );
}
//trim data from unnecessary spaces
void trim (string &str) {
    ltrim (str);
    rtrim (str);
}
//a - display all trips on specified route
void searchRoute (vector<Route> &Routes) {
    string startStop;
    getline (cin,startStop);
    string endStop;
    getline (cin,endStop);
    cout<<"result:\n";
    for (auto n : Routes)
        if (n.startStop.find(startStop) != string::npos && n.endStop.find(endStop) != string::npos)
             cout<< fixed << setprecision(2) << n.startStop<<" " << n.endStop <<
            " " << n.day << " " << n.time << " " <<  n.price<<"\n";
}
//b - display all trips on a specific day
void searchDay (vector<Route> &Routes) {
    string day;
    getline (cin,day);
    cout<< "result:\n";
    for (auto n : Routes)
        if (n.day.find(day) != string::npos)
            cout<< fixed << setprecision(2) << n.startStop<<" " << n.endStop <<
            " " << n.day << " " << n.time << " " << n.price<<"\n";
}
//c - display all trips based on price
void searchPrice (vector<Route> &Routes) {
    string price;
    getline (cin,price);
    cout<<"result:\n";
    for (auto n : Routes)
        if (std::stod(price) >= n.price)
             cout<< fixed << setprecision(2) << n.startStop<<" " << n.endStop <<
            " " << n.day << " " << n.time << " " << n.price<<"\n";
}
//d - print contents of error file
void searchErrors () {
    cout<<"result:\n";
    inFile.open (error_file_address,ios::in);
    vector<string> Errors;
    string line;
    while(getline(inFile, line)){
      if(isalpha(line[0])){
        line.erase(remove(line.begin(), line.end(), ' '), line.end());
        line.erase(remove(line.begin(), line.end(), '\r'), line.end());
        Errors.push_back(line);     
      }
    }
    for(string data: Errors)
      cout << data << endl;
    
    inFile.close();
}
//check if route contain only letters
bool IsLettersOnly(string str){
    trim(str);
	for (int i = 0; i < str.size(); i++){
		int uppercaseChar = toupper(str[i]);
		if (uppercaseChar < 'A' || uppercaseChar > 'Z'){
			return false;
		}
	}
	return true;
}
//check if string contain floating number
bool isValidPrice(string &str){
    char* ptr;
    strtof(str.c_str(), &ptr);
    return (*ptr) == '\0';
}

//check if time is valid
bool IsValidTime(string str){
    const regex pattern("([01]?[0-9]|2[0-3]):[0-5][0-9]");

    if (str.empty()) return false;

    if(regex_match(str, pattern)) return true;

    else return false;
}
//check if day of the week is valid
bool IsValidDay (string str){
    bool res = false;
    const char *days[7] = {"Pr","Ot","Tr","Ce","Pt","St","Sv"};
    for (int i = 0; i < 7; i++){
        if(str.compare(days[i]) != 0){
            res = false;
        } else {
            res = true;
            break;
        }
    }
    return res;
}
//count lines in file
int countLines (){
    int aNumOfLines = 0;
    ifstream aInputFile(file_address);

    string aLineStr;
    while (getline(aInputFile, aLineStr)){
        if (!aLineStr.empty())
        aNumOfLines++;
    }
return aNumOfLines;
}
//add data to vector or error file
void processLine(string &line, vector<Route> &Routes) {

    string startStop, endStop, day, time, price;
    Route database[countLines()];

      for (int i = 0; i < countLines(); i++) {
        int error = 0;
        stringstream ss;
        ss << line;

        //check if line contains valid data count
        int count = std::count (line.begin(), line.end(), ',');
        if(count >= 5 || count <= 3) error++;

        //adding specific data in route database
        getline(ss, startStop, ',');
        trim(startStop);
        if(IsLettersOnly(startStop) && !(startStop).empty()){
            database[i].startStop = startStop;       
        } else error++;

        getline(ss, endStop, ',');
        trim(endStop);
        if(IsLettersOnly(endStop) && !(endStop).empty()){
            database[i].endStop = endStop;       
        } else error++;

        getline(ss, day, ',');
        trim(day);
        if(!(day).empty() && IsValidDay(day)){
            database[i].day = day;           
        } else error++;

        getline(ss, time, ',');
        trim(time);
        if(!(time).empty() && IsValidTime(time)){
            database[i].time = time;            
        } else error++;

        getline(ss, price, ',');
        trim(price);
        if(!(price).empty() && isValidPrice(price)){
            database[i].price = atof(price.c_str());         
        } else error++;

        //add invalid database info to error file
        if(error > 0 && !(line.empty())){      
          outFile.open(error_file_address, ios_base::app);
          outFile << line << endl;        
          outFile.close();                             
          return;           
        } else {
          //add valid database info to vector
          Routes.push_back(database[i]);            
        }
        getline(inFile, line);
      }
    return;
}
//check if file exists
bool testFileNotExists() {
    inFile.open (file_address,ios::in);
    return (!inFile);
}
int main (int argc, char **argv) {  
    if (testFileNotExists()) {
        cout<<"File not found!"<<endl;
        return -1;
    }
    //remove err.txt if exists,to prevent program
    //from writing to the err.txt each time you run it
    int n = error_file_address.length();
    char char_err[n + 1];
    strcpy(char_err, error_file_address.c_str());
    remove(char_err);

    vector<Route> Routes;
    string line;
    while (getline(inFile, line))
        (processLine(line,Routes));
    inFile.close();

    string command;
    while (true) {
        getline (cin,command);
        if (command == "a") searchRoute(Routes);
        else if(command == "b") searchDay (Routes);
        else if (command == "c") searchPrice(Routes);
        else if(command == "d") searchErrors();
        else if (command == "e") break;
    }
    return 0;
}