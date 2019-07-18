import java.util.Scanner;

public class Main  {
	
	public static void main(String[] argc) throws Exception {
		Scanner inputScanner = new Scanner(System.in);
						
		System.out.println("Nhap ten file .jff bieu dien NFA: ");				
		String fileName = inputScanner.nextLine();
		
		Otomat NFA = OtomatTranducer.OtomatTranduce(fileName);
		Otomat DFA = NFA.convertNFAtoDFA();
		OtomatTranducer.createDocument(DFA, "DFA.jff");		
		System.out.println("XUAT FILE DFA.JFF THANH CONG!");
	}
}
