package EQ_DFA;

import java.util.Scanner;


public class Main {
	public static void main(String[] argc) throws Exception {
		OtomatTranducer otomatTranducer = new OtomatTranducer();
		Scanner inputScanner = new Scanner(System.in);
		
		System.out.println("Nhap ten file .jff A: ");
		String fileName1 = inputScanner.nextLine();
		Otomat A = otomatTranducer.OtomatTranduce(fileName1);
		
		System.out.println("Nhap ten file .jff B: ");
		String fileName2 = inputScanner.nextLine();
		Otomat B = otomatTranducer.OtomatTranduce(fileName2);
				
		Otomat minusA = otomatTranducer.minusOtomat(A);
		Otomat minusB = otomatTranducer.minusOtomat(B);
		
		Otomat A_intersect_minusB = otomatTranducer.mixOtomat(A, minusB, "Giao");
		Otomat minusA_intersect_B = otomatTranducer.mixOtomat(minusA, B, "Giao");
		Otomat C = otomatTranducer.mixOtomat(A_intersect_minusB, minusA_intersect_B, "Hop");
		
		System.out.println("Ket qua: "+C.EDFA());
	}	
}
