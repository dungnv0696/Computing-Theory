#include <iostream>
#include <vector>
using namespace std;

int main(){
	int n; 
	int count=0;	
	vector <int> w;
	
	do{
		cout << "Nhap so nguyen duong n= ";
		cin >> n;	
	}while(n<=0);
	
	cout << "Day cac tu Lyndon co do dai <= " << n << " tren {0,1} la: \n";
	
	for(int i=1; i<=n;i++){		
		w.push_back(-1);
		
		while(!w.empty()){
			w.back() += 1;
			int m = w.size();
						
			if (m==i){				
				for(int j=0;j<i;j++)
					cout << w[j];
				cout << "\n";
				count++;
			}
			
			while(w.size() < i){
				w.push_back(w[w.size()-m]);
			}
			
			while (w.back() == 1 && !w.empty()){
				w.pop_back();
			}
		}
	}
	
	cout << "Co tat ca " << count << " tu Lyndon co do dai <= " <<n<<" tren {0,1}";
}
