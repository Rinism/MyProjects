//  171RIC208 Sandis Blūmentāls 4.grupa

#include <iostream>
#include<fstream>
#include<string>
#include<vector>
#include <sstream>
#include <regex>
#include <iomanip>
using namespace std;

string err = "err.txt";
string db = "db.csv";

class Route {
  public:
    string departureDay;
    string departureTime;
    string departure;
    string arrival;
    double ticketPrice;
};


void spaceRemove(string &line){ 
  line.erase(remove(line.begin(), line.end(), '\r'), line.end());
  line.erase(remove(line.begin(), line.end(), ' '), line.end());
}

void c(vector<Route> &Routes) {
  string ticketPrice;
  getline (cin,ticketPrice);
  cout<<"result:\n";
  for (auto r : Routes)
    if (std::stod(ticketPrice) >= r.ticketPrice)
      cout<< fixed << setprecision(2) << r.departure<<" " << r.arrival << " " << r.departureDay << 
      " "<< r.departureTime << " " << r.ticketPrice<<"\n";
}

void b(vector<Route> &Routes) {
  string departureDay;
  getline (cin,departureDay);
  cout<< "result:\n";
  for (auto r : Routes)
    if (r.departureDay.find(departureDay) != string::npos)
      cout<< fixed << setprecision(2) << r.departure<<" " << r.arrival <<
      " " << r.departureDay << " " << r.departureTime << " " << r.ticketPrice<<"\n";
}

void a(vector<Route> &Routes) {
  string departure;
  getline (cin,departure);
  string endStop;
  getline (cin,endStop);
  cout<<"result:\n";
  for (auto r : Routes)
    if (r.departure.find(departure) != string::npos && r.arrival.find(endStop) != string::npos)
      cout<< fixed << setprecision(2) << r.departure<<" " << r.arrival <<
      " " << r.departureDay << " " << r.departureTime << " " <<  r.ticketPrice<<"\n";
}

void d(ifstream &file) {
  cout<<"result:\n";
  file.open(err,ios::in);
  vector<string> err;
  string fileLine;
  while(getline(file, fileLine)){
    spaceRemove(fileLine);
    err.push_back(fileLine);     
  }
  for(string data: err)
    cout << data << endl;
  file.close();
}

bool IsLettersOnly (const std::string & str) {
  for (auto ch : str)
    if (!( (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')))
      return false;
  return true;
}

bool IsTimeValid (string s){
  string h = s.substr(0, s.find(":"));
  for (int j = 0; j < h.length(); j++){
    if (isdigit(h[j]) == false) return false;
    else return true;
  }
  string m = s.substr(h.size()+1, s.size());
  for (int j = 0; j < s.length(); j++){
    if (isdigit(s[j]) == false) return false;
    else return true;
  }
return false;
}

bool IsDayValid (string s){
  bool res = false;
  string line = "Pr,Ot,Tr,Ce,Pt,St,Sv";
  string token, delimiter = ",";
  size_t pos = 0; 
  while ((pos = line.find(delimiter)) != std::string::npos) {
    token = line.substr(0, pos);
    if(s.compare(token) != 0){
      res = false;
      line.erase(0, pos + delimiter.length());
    } else {
      return true;
      break;
    }
  }
  return false;   
}

int countLines (string fileAddress){
  int numLines = 0;
  ifstream inputFile(fileAddress);
  string s;
  while (getline(inputFile, s)){
    if (!s.empty())
    numLines++;
  }
return numLines;
}
 void removeOldErrorFile(){
  int n = err.length();
  char char_err[n + 1];
  strcpy(char_err, err.c_str());
  remove(char_err);
 }

void processLine (string &line, ifstream &file,vector<Route> &Routes, string fileAddress) { 
  string startStop, endStop, departureDay, departureTime, ticketPrice;
  Route database[countLines(fileAddress)];
  ofstream ofile;
  ofile.open (err, ios_base::app);

  for (int j = 0; j < countLines(fileAddress); j++) {
    spaceRemove(line);
    int err = 0;
    stringstream s;
    s << line;

    int countData = std::count (line.begin(), line.end(), ',');
      if(countData >= 5 || countData <= 3) err++;

    getline(s, startStop, ',');
    getline(s, endStop, ',');
    getline(s, departureDay, ',');
    getline(s, departureTime, ',');
    getline(s, ticketPrice, ',');

    if(IsLettersOnly(startStop) && !(startStop).empty()){
      database[j].departure = startStop;
      startStop = "";
    } else err++;

    if(IsLettersOnly(endStop) && !(endStop).empty()){
      database[j].arrival = endStop;
      endStop = "";
    } else err++;

    if(IsLettersOnly(departureDay) && !(departureDay).empty() && IsDayValid(departureDay)){
        database[j].departureDay = departureDay;
        departureDay = "";
     } else err++;

    if(!(departureTime).empty() && IsTimeValid(departureTime)){
      database[j].departureTime = departureTime;
      departureTime = "";
    } else err++;

    if(!(ticketPrice).empty()){
      database[j].ticketPrice = atof(ticketPrice.c_str());
      ticketPrice = "";
    } else err++;

    if(err != 0){
        if(isalpha(line[0])){               
          ofile << line << endl;        
          ofile.close();            
          return;
        }
    } else Routes.push_back(database[j]);  
    getline(file, line);
  }
  return;
}

void openFile (ifstream &file) {
  file.open (db,ios::in);
}

int main (int argc, char **argv) {
  ifstream file;
  openFile(file);   
  removeOldErrorFile();

  vector<Route> Routes;
  string line;
  while (getline(file, line))
    (processLine(line,file,Routes,db));
  file.close();

  string command;
  while (true) {
    getline (cin,command);
    if (command == "a") 
    a(Routes);
    else if(command == "b") 
    b (Routes);
    else if (command == "c") 
    c(Routes);
    else if(command == "d") 
    d(file);
    else if (command == "e") 
    break;
    }
  return 0;
}