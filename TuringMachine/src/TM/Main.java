package TM;

import java.util.Scanner;


public class Main {
	public static void main(String[] argc) throws Exception {
		Scanner inputScanner = new Scanner(System.in);
		
		System.out.println("Nhap ten file .jff : ");
		String fileName = inputScanner.nextLine();
		TuringMachine A = TuringMachine.readXML(fileName);
		//A.inThongTinTuringMachine();
		System.out.println(A.checkAStarLanguage());
	}	
}
