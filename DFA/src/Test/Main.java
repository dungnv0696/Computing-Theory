package Test;

import XML.OtomatTranducer;
import Otomat.*;

import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Main  {
	
	public static void main(String[] argc) throws Exception {
		OtomatTranducer otomatTranducer = new OtomatTranducer();
		Scanner inputScanner = new Scanner(System.in);
						
		while(true) {
			System.out.println("\n -------------Menu----------");
			System.out.println(" |   1: Bai 1               |");
			System.out.println(" |   2: Bai 2               |");
			System.out.println(" |   3: Thoat               |");
			System.out.println(" ---------------------------");
			System.out.println("Nhap lua chon: ");
			String choose = inputScanner.nextLine();
			switch(choose) {
			case "1":
				System.out.println("Nhap ten file .jff : ");
				String fileName = inputScanner.nextLine();
				Otomat otomat = otomatTranducer.OtomatTranduce(fileName);
				
				System.out.println("ban muon in ra man hinh thong tin cua Otomat vua nhap khong? Nhap 'No' de tu choi");
				String userChoose = inputScanner.nextLine();
				if (!userChoose.equals("No"))  
					otomat.inThongTinOtomat();

				while(true) {
					System.out.println("Nhap xau Input: ");
					String inputString = inputScanner.nextLine();		
					otomat.kiemTraXau(inputString);
					System.out.println("Ban muon thu lai khong? (Nhap 'No' de thoat)");
					String luaChon = inputScanner.nextLine();
					if (luaChon.equals("No"))
						break;
					}
				break;
			case "2":
				System.out.println("Nhap ten file .jff 1: ");
				String fileName1 = inputScanner.nextLine();
				Otomat otomat1 = otomatTranducer.OtomatTranduce(fileName1);
				
				System.out.println("Nhap ten file .jff 2: ");
				String fileName2 = inputScanner.nextLine();
				Otomat otomat2 = otomatTranducer.OtomatTranduce(fileName2);
				
				Otomat otomatIntersect = OtomatTranducer.mixOtomat(otomat1, otomat2, "Giao");
				OtomatTranducer.createDocument(otomatIntersect, "Giao.jff");
				Otomat otomatUnion = OtomatTranducer.mixOtomat(otomat1, otomat2, "Hop");
				OtomatTranducer.createDocument(otomatUnion, "Hop.jff");
				System.out.println("Da xuat file .jff thanh cong:");
				
				System.out.println("Ban co muon in ra man hinh thong tin cua 2 Otomat vua nhap \n"
								 + "va thong tin cua 2 Otomat Giao, Hop khong? \n"
								 + "Nhap 'No' de tu choi");
				String userWantToPrint = inputScanner.nextLine();
				if (userWantToPrint.equals("No")) continue;
				else {
					otomat1.inThongTinOtomat();
					otomat2.inThongTinOtomat();
					otomatIntersect.inThongTinOtomat();
					otomatUnion.inThongTinOtomat();
				}
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
