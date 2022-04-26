package results;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import controller.GameController;

/**
 * @brief Dosyalardan okunan verileri düzenleyerek raporlamalar için datalarý
 *        oluþturan class
 * 
 */
public class ResultManager {

	private ArrayList<Integer> playerRankList = new ArrayList<Integer>(); // Score sýralamalý player listesi
	private HashMap<Integer, String> playerMap = new HashMap<Integer, String>(); // Oyunculardan oluþan map
	private HashMap<Integer, ArrayList<Integer>> matchMap = new HashMap<Integer, ArrayList<Integer>>(); // Maç sonuçlarýný içeren map

	/**
	 * @note Aþaðýdaki 3 map yaratýlmadan da "matchMap" kullanýlarak raporlamalar
	 *       yapýlabilirdi. Ancak ileride farklý fonksiyonaliteler ve raporlama
	 *       seçenekleri eklenebileceði göz önüne alýnarak ve raporlamalarda
	 *       performans/kolaylýk saðlamasý açýsýndan da aþaðýdaki maplerde oluþturuldu.
	 */
	private HashMap<Integer, Integer> playerScoreMap = new HashMap<Integer, Integer>(); // Score bazlý azalan þekilde sýralý skor mapi																				// 
	private HashMap<Integer, Integer> playerWinMap = new HashMap<Integer, Integer>(); // Oyuncularýn galibiyet sayýlarýnýn olduðu map																					// 
	private HashMap<Integer, Integer> playerLoseMap = new HashMap<Integer, Integer>(); // Oyuncularýn maðlubiyet sayýlarýnýn olduðu map

	public ResultManager() {
	}

	/**
	 * @return playerRankList
	 */
	public ArrayList<Integer> getPlayerRankList() {
		return playerRankList;
	}

	/**
	 * @return playerMap
	 */
	public HashMap<Integer, String> getPlayerMap() {
		return playerMap;
	}

	/**
	 * @return playerScoreMap
	 */
	public HashMap<Integer, Integer> getPlayerScoreMap() {
		return playerScoreMap;
	}

	/**
	 * @return playerWinMap
	 */
	public HashMap<Integer, Integer> getPlayerWinMap() {
		return playerWinMap;
	}

	/**
	 * @return playerLoseMap
	 */
	public HashMap<Integer, Integer> getPlayerLoseMap() {
		return playerLoseMap;
	}

	/**
	 * @return matchMap
	 */
	public HashMap<Integer, ArrayList<Integer>> getMatchMap() {
		return matchMap;
	}

	/**
	 * @brief Raporlamalar için oluþturulmasý gereken yapýlarý kuran method.
	 *        Dosyalar sadece bu method üzerinden okunacak þekilde tasarlanmýþtýr.
	 * 
	 *        Method, öncelikle dosyalarýn okunmasýný saðlar. Ardýndan playerMap,
	 *        matchMap, playerWinMap, playerLoseMap ve playerScoreMap maplerini doldurur.      
	 * 
	 *        Skorlama için kullanýlan method: score = <win_count> * 3 - <lose_count>   
	 *        
	 * @param GameController facade
	 */
	public void scoreHandler(GameController facade) throws FileNotFoundException {

		playerMap = facade.fileReaderGetInstance().getPlayers();
		matchMap = facade.fileReaderGetInstance().getMatches();

		/**
		 * @note Hiçbir maçta oynamamýþ oyuncularýn var olabileceði göz önüne alýnarak
		 *       score, win ve lose maplerine her oyuncu için value olarak 0 atamasý yapýlmýþtýr. 
		 * 
		 */
		for (Map.Entry<Integer, String> player : playerMap.entrySet()) {
			playerScoreMap.put(player.getKey(), 0);
			playerWinMap.put(player.getKey(), 0);
			playerLoseMap.put(player.getKey(), 0);
		}

		// matchMap kullanýlarak oyuncularýn maç sonuçlarý ve skorlarý oluþturulur.
		for (Map.Entry<Integer, ArrayList<Integer>> match : matchMap.entrySet()) {
			int score = 0;
			ArrayList<Integer> matchList = match.getValue();
			Integer playerId = match.getKey();

			for (int i = 0; i < matchList.size(); i++) {
				if (matchList.get(i) > 0) { //galibiyet
					score += 3;
					playerWinMap.merge(playerId, 1, Integer::sum);

				} else { //maðlubiyet
					score--;
					playerLoseMap.merge(playerId, 1, Integer::sum);
				}
			}
			playerScoreMap.put(playerId, score);
		}

		scoreSort();
		createRankList();
	}

	/**
	 * @brief Oyuncularýn scorelarýný azalan bir þekilde sýralayan method
	 */
	private void scoreSort() {

		playerScoreMap = playerScoreMap.entrySet().stream().sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * @brief Oyuncularýn scorelarýna göre rank listesi oluþmasýný saðlayan method
	 */
	private void createRankList() {

		for (Map.Entry<Integer, Integer> player : playerScoreMap.entrySet()) {
			playerRankList.add(player.getKey());
		}
	}
}
