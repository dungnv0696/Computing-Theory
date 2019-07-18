package grammar;

import java.util.Scanner;

//public class Main {
	/*public static void main(String[] args) throws Exception {
		String input = "a a b b b c";
		//Grammar cky = new Grammar("a b b c");	
		
		G.printProductionRule();
		G.isChomskyNorm();
		G.convertToChomskyNorm();
		
		System.out.println();
		G.printProductionRule();
		G.isChomskyNorm();
		
		XML.createDocument(G, "xyz.jff");
		//System.out.println(String.valueOf(G.solve()));
	
	}
}*/

public class Main  {
	public static void main(String[] argc) throws Exception {
		Scanner inputScanner = new Scanner(System.in);
						
		while(true) {
			System.out.println("\n -------------Menu----------");
			System.out.println(" |   1: Chuan hoa Chomsky   |");
			System.out.println(" |   2: Thuat toan CYK      |");
			System.out.println(" |   3: Thoat               |");
			System.out.println(" ---------------------------");
			System.out.println("Nhap lua chon: ");
			String choose = inputScanner.nextLine();
			switch(choose) {
			case "1":
				System.out.println("Nhap ten file .jff muon chuan hoa Chomsky : ");
				String fileName1 = inputScanner.nextLine();
				Grammar G1 = new Grammar();
				XML.grammarTranducer(fileName1, G1);
				if (G1.isChomskyNorm())
					System.out.println("Van pham "+fileName1+" da o dang chuan chomsky roi`, khong can` chuan hoa'");
				else {
					G1.convertToChomskyNorm();
					//G1.printProductionRule();
					XML.createDocument(G1, "Chomsky.jff");
					System.out.println("Chuan hoa thanh cong, xuat file Chomsky.jff thanh cong");
					//System.out.println(G1.getStartSymbol());
				}
				
				break;
			case "2":
				System.out.println("Nhap ten file .jff muon' kiem tra xau co sinh boi van pham khong bang` thuat toan CYK:");
				String fileName2 = inputScanner.nextLine();
				Grammar G2 = new Grammar();
				XML.grammarTranducer(fileName2, G2);
				if(G2.isChomskyNorm()) {
					System.out.println("Nhap xau Input: ");
					String input = inputScanner.nextLine();
					String input1 = XML.insert_space(input);
					G2.setInput(input1);
					if(G2.solve())
						System.out.println("Xau "+input+" sinh boi van pham "+fileName2);
					else
						System.out.println("Xau "+input+" KHONG sinh boi van pham "+fileName2);
				}
				else
					System.out.println("Van pham "+fileName2+" khong o dang chuan Chomsky, vui long kiem tra lai");
				break;
			case "3": 
				System.exit(0);
				break;
			default:
				System.out.println("Vui long nhap dung lua chon");
				break;
			}
		}
	}
}
