package filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @brief Verilen dosyalar� okuyan ve okunan datalar� d�zenleyen class
 * 
 */
public class FileReader {

	private final String nameFilePath = "names.txt";
	private final String matchFilePath = "matches.txt";

	public FileReader() {
	};

	/**
	 * @brief names.txt dosyas�n� okuyarak, ID ve oyuncu isimlerini mapleyen
	 *        fonksiyon
	 * 
	 * @return HashMap<Integer ID, String isim>
	 * 
	 * @note Mapteki key de�erleri dosyadan okumak yerine "1 - n" olarak
	 *       atanabilirdi ancak oyuncu ID leri s�ras�z olan bir dosya verilebilece�i
	 *       g�z �n�ne al�narak bu �ekilde bir kullan�m tercih edildi.
	 */
	public HashMap<Integer, String> getPlayers() throws FileNotFoundException {

		HashMap<Integer, String> players = new HashMap<Integer, String>();
		File nameFile = new File(nameFilePath);
		Scanner sc = new Scanner(nameFile);

		while (sc.hasNextLine()) {
			String parts[] = sc.nextLine().split(" ");
			players.put(Integer.parseInt(parts[0]) + 1, parts[1]); // players<ID+1, isim>, IDleri 1 artt�rarak 0 IDsini yokediyoruz
		}
		sc.close();
		return players;
	}

	/**
	 * @brief matches.txt dosyas�n� okuyarak, <ID, [ma� sonu�lar�]> �eklinde
	 *        mapleyen fonksiyon. ID, oyuncuyu temsil ederken, [ma� sonu�lar�] rakip
	 *        oyuncu ID lerini i�ermektedir. Rakip oyuncu ID leri, kazan�lan ma�lar
	 *        i�in pozitif, kaybedilenler i�in negatif tutulmaktad�r.
	 * 
	 * @example 23=[4, -37, 14], 24=[13, 15, -32, 3, 30], 25=[20, 14, -36, -37, -30]        
	 * 
	 * @return HashMap<Integer, List<Integer>>
	 * 
	 */
	public HashMap<Integer, ArrayList<Integer>> getMatches() throws FileNotFoundException {

		File matchFile = new File(matchFilePath);
		Scanner sc = new Scanner(matchFile);
		HashMap<Integer, ArrayList<Integer>> matchResults = new HashMap<Integer, ArrayList<Integer>>();

		while (sc.hasNextLine()) {
			String result_parts[] = sc.nextLine().split(" ");
			Integer winnerId = Integer.parseInt(result_parts[0]) + 1; // Her sat�rdaki ilk ID (kazanan)
			Integer loserId = Integer.parseInt(result_parts[1]) + 1; // Her sat�rdaki ikinci ID (kaybeden)

			/**
			 * Oyuncu ilk kez ma�a ��k�yorsa "matchResults" mapine eklenir, sonras�nda ma�
			 * sonucuna g�re value de�erine rakip ID eklenir. Oyuncunun �nceden oynad��� ma�
			 * var ise ma� sonucuna g�re value de�erine rakip ID eklenir.
			 */
			if (!matchResults.containsKey(winnerId)) {
				matchResults.put(winnerId, new ArrayList<Integer>());
			}
			matchResults.get(winnerId).add(loserId);

			if (!matchResults.containsKey(loserId)) {
				matchResults.put(loserId, new ArrayList<Integer>());
			}
			matchResults.get(loserId).add(winnerId *= -1);
		}
		sc.close();
		return matchResults;
	}
}
