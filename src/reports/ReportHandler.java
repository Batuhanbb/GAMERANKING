package reports;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import controller.GameController;
import results.ResultManager;

import java.util.Objects;
import java.util.Scanner;

/**
 * @brief Olu�turulmu� mapleri kullanarak raporlamalar� haz�rlayan class
 * 
 */
public class ReportHandler {

	private ArrayList<Integer> playerRankList;
	private HashMap<Integer, String> playerMap;
	private HashMap<Integer, Integer> playerWinMap;
	private HashMap<Integer, Integer> playerLoseMap;
	private HashMap<Integer, Integer> playerScoreMap;
	private HashMap<Integer, ArrayList<Integer>> matchMap;
	static Scanner sc = new Scanner(System.in);

	/**
	 * @brief ResultManager class�nda olu�turdu�umuz mapleri alarak olu�turulmu� constructor.
	 */
	public ReportHandler(ResultManager resultManager) {
		this.playerRankList = resultManager.getPlayerRankList();
		this.playerMap = resultManager.getPlayerMap();
		this.playerWinMap = resultManager.getPlayerWinMap();
		this.playerLoseMap = resultManager.getPlayerLoseMap();
		this.playerScoreMap = resultManager.getPlayerScoreMap();
		this.matchMap = resultManager.getMatchMap();
	}

	/**
	 * @brief Oyuncular�n ID, isim, galibiyet, ma�lubiyet ve skorlar�n�, skor
	 *        baz�nda azalarak raporlayan method
	 * 
	 * @example 1-) 36 Jacquelynn -> G:5 M:1 SKOR:14
	 *		    2-) 13 Hunter -> G:5 M:2 SKOR:13
	 *		    3-) 31 Fernanda -> G:5 M:2 SKOR:13
	 */
	protected void showRankList() {

		if (playerRankList.size() != 0) {
			for (int i = 0; i < playerRankList.size(); i++) {
				Integer playerId = playerRankList.get(i);
				System.out.println(i + 1 + "-) " + (playerId - 1) + " " + playerMap.get(playerId) + " -> G:"
						+ playerWinMap.get(playerId) + " M:" + playerLoseMap.get(playerId) + " SKOR:"
						+ playerScoreMap.get(playerId));
			}
		} else {
			System.out.println("bip - bop. Sistem ar�zas�. (Oyuncu listesi bulunamad�!)");
		}
	}

	/**
	 * @brief Oyuncular�n kimlerle ma� yapt�klar�n� ve sonu�lar�n� raporlamaya yarayan method
	 * 
	 * @example 1-) Wesley -> Micheline: M, 
	 *			2-) Melodie -> Solange: G, Dave: M, Antwan: M, Hunter: M, Marica: G, Windy: M, 
	 * 			
	 */
	private void showMatchResults() {

		for (int i = 0; i < playerMap.size(); i++) {
			String match_results = "";

			for (int j = 0; j < matchMap.get(i + 1).size(); j++) {
				int matchScore = matchMap.get(i + 1).get(j);
				if (matchScore < 0) {
					matchScore *= -1;
				}
				match_results += playerMap.get(matchScore);
				if (matchMap.get(i + 1).get(j) > 0) {
					match_results += ": G, ";
				} else {
					match_results += ": M, ";
				}
			}
			System.out.println((i + 1) + "-) " + playerMap.get(i + 1) + " -> " + match_results);
		}
	}

	/**
	 * @brief Skor listesindeki s�ralamaya g�re ard���k 2 ki�inin ma� yapmas�n� �neren method
	 * 
	 * @example 1-) Jacquelynn VS Hunter
	 *			2-) Fernanda VS Jaye
	 *			3-) Tai VS Denyse
	 * 			
	 */
	private void suggestedNextMatches() {
		int lineIndex = 1;
		for (int i = 0; i < playerRankList.size(); i += 2, lineIndex++) {
			System.out.println(lineIndex + "-) " + playerMap.get(playerRankList.get(i)) + " VS "
					+ playerMap.get(playerRankList.get(i + 1)));
		}
	}

	/**
	 * @brief Oyuncu �zelinde s�ralamas�n� g�steren method
	 * 
	 * @example Jeanine s�ralamalas�: 26
	 * 
	 * @param String username
	 * 			
	 */
	private void showRankByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " s�ralamalas�: " + (playerRankList.indexOf(playerId) + 1));
		}
	}

	/**
	 * @brief Oyuncu �zelinde galibiyet-ma�lubiyet say�s�n� g�steren method
	 * 
	 * @example Jeanine -> G:2 M:3
	 * 
	 * @param String username
	 * 			
	 */
	private void showWinLoseByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " -> G:" + playerWinMap.get(playerId) + " M:" + playerLoseMap.get(playerId));
		}
	}

	/**
	 * @brief Oyuncu �zelinde skorunu g�steren method
	 * 
	 * @example Jeanine skoru: 3
	 * 
	 * @param String username
	 * 			
	 */
	private void showScoreByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " skoru: " + playerScoreMap.get(playerId));
		}
	}

	/**
	 * @brief Oyuncu �zelinde galibiyet say�s�n� g�steren method
	 * 
	 * @example Jeanine galibiyet say�s�: 2
	 * 
	 * @param String username
	 * 			
	 */
	private void showWinByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " galibiyet say�s�: " + playerWinMap.get(playerId));
		}
	}

	/**
	 * @brief Oyuncu �zelinde ma�lubiyet say�s�n� g�steren method
	 * 
	 * @example Jeanine ma�lubiyet say�s�: 3
	 * 
	 * @param String username
	 * 			
	 */
	private void showLoseByUsername(String username) {
		Integer playerId = getIdByUsername(username);
		if (playerId != -1) {
			System.out.println(username + " ma�lubiyet say�s�: " + playerLoseMap.get(playerId));
		}
	}

	/**
	 * @brief username parametre alan methodlar i�in, oyuncunun IDsini d�nen method.
	 * 
	 * @note Bu method genel kullan�mlar i�in Utils class� olu�turularak i�ine konabilirdi.
	 * 
	 * @param String username
	 * 
	 * @return Integer playerID
	 * 			
	 */
	private Integer getIdByUsername(String username) {
		for (Entry<Integer, String> entry : playerMap.entrySet()) {
			if (Objects.equals(username, entry.getValue())) {
				return entry.getKey();
			}
		}
		System.out.println("Hatal� isim giri�i!");
		return -1;
	}

	/**
	 * @brief username girilmesi istenen methodlar i�in kullan�c�dan username alan method
	 * 
	 * @return String playerName
	 * 			
	 */
	private String getUsernameInput() {
		System.out.println("L�tfen oyuncu ismini giriniz:");
		String playerName = sc.next();
		return playerName;
	}

	public static void main(String[] args) throws FileNotFoundException {

		GameController facade = new GameController();
		facade.resultManagerGetInstance().scoreHandler(facade);
		ReportHandler reportHandler = new ReportHandler(facade.resultManagerGetInstance());
		boolean exit = false;

		while (!exit) {
			System.out.println(
					"        **** GAME RANKING ****\nL�tfen a�a��daki se�eneklerden g�rmek istedi�iniz sonucun ba��ndaki numaray� giriniz:\n\n"
							+ "1-) Skor S�ral� Liste\n" + "2-) Ma� Sonu�lar�\n" + "3-) �nerilen Kar��la�ma Listesi\n"
							+ "4-) Oyuncu S�ralamas�n� G�ster\n" + "5-) Oyuncu Skorunu G�ster\n"
							+ "6-) Oyuncu Galibiyet Ma�lubiyet Say�s�n� G�ster\n"
							+ "7-) Oyuncu Galibiyet Say�s�n� G�ster\n" + "8-) Oyuncu Ma�lubiyet Say�s�n� G�ster\n");
			int option = sc.nextInt();
			String playerName = "";

			switch (option) {
			case 1:
				reportHandler.showRankList();
				break;
			case 2:
				reportHandler.showMatchResults();
				break;
			case 3:
				reportHandler.suggestedNextMatches();
				break;
			case 4:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showRankByUsername(playerName);
				break;
			case 5:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showScoreByUsername(playerName);
				break;
			case 6:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showWinLoseByUsername(playerName);
				break;
			case 7:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showWinByUsername(playerName);
				break;
			case 8:
				playerName = reportHandler.getUsernameInput();
				reportHandler.showLoseByUsername(playerName);
				break;
			default:
				System.out.println("Yanl�� de�er girdiniz.");
			}

			System.out.println("\nDevam etmek istiyor musunuz? [E/H]");
			if (sc.next().equalsIgnoreCase("H")) {
				System.out.println("Bye =)");
				exit = true;
			}
		}
	}
}
