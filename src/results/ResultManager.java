package results;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import controller.GameController;

/**
 * @brief Dosyalardan okunan verileri d�zenleyerek raporlamalar i�in datalar�
 *        olu�turan class
 * 
 */
public class ResultManager {

	private ArrayList<Integer> playerRankList = new ArrayList<Integer>(); // Score s�ralamal� player listesi
	private HashMap<Integer, String> playerMap = new HashMap<Integer, String>(); // Oyunculardan olu�an map
	private HashMap<Integer, ArrayList<Integer>> matchMap = new HashMap<Integer, ArrayList<Integer>>(); // Ma� sonu�lar�n� i�eren map

	/**
	 * @note A�a��daki 3 map yarat�lmadan da "matchMap" kullan�larak raporlamalar
	 *       yap�labilirdi. Ancak ileride farkl� fonksiyonaliteler ve raporlama
	 *       se�enekleri eklenebilece�i g�z �n�ne al�narak ve raporlamalarda
	 *       performans/kolayl�k sa�lamas� a��s�ndan da a�a��daki maplerde olu�turuldu.
	 */
	private HashMap<Integer, Integer> playerScoreMap = new HashMap<Integer, Integer>(); // Score bazl� azalan �ekilde s�ral� skor mapi																				// 
	private HashMap<Integer, Integer> playerWinMap = new HashMap<Integer, Integer>(); // Oyuncular�n galibiyet say�lar�n�n oldu�u map																					// 
	private HashMap<Integer, Integer> playerLoseMap = new HashMap<Integer, Integer>(); // Oyuncular�n ma�lubiyet say�lar�n�n oldu�u map

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
	 * @brief Raporlamalar i�in olu�turulmas� gereken yap�lar� kuran method.
	 *        Dosyalar sadece bu method �zerinden okunacak �ekilde tasarlanm��t�r.
	 * 
	 *        Method, �ncelikle dosyalar�n okunmas�n� sa�lar. Ard�ndan playerMap,
	 *        matchMap, playerWinMap, playerLoseMap ve playerScoreMap maplerini doldurur.      
	 * 
	 *        Skorlama i�in kullan�lan method: score = <win_count> * 3 - <lose_count>   
	 *        
	 * @param GameController facade
	 */
	public void scoreHandler(GameController facade) throws FileNotFoundException {

		playerMap = facade.fileReaderGetInstance().getPlayers();
		matchMap = facade.fileReaderGetInstance().getMatches();

		/**
		 * @note Hi�bir ma�ta oynamam�� oyuncular�n var olabilece�i g�z �n�ne al�narak
		 *       score, win ve lose maplerine her oyuncu i�in value olarak 0 atamas� yap�lm��t�r. 
		 * 
		 */
		for (Map.Entry<Integer, String> player : playerMap.entrySet()) {
			playerScoreMap.put(player.getKey(), 0);
			playerWinMap.put(player.getKey(), 0);
			playerLoseMap.put(player.getKey(), 0);
		}

		// matchMap kullan�larak oyuncular�n ma� sonu�lar� ve skorlar� olu�turulur.
		for (Map.Entry<Integer, ArrayList<Integer>> match : matchMap.entrySet()) {
			int score = 0;
			ArrayList<Integer> matchList = match.getValue();
			Integer playerId = match.getKey();

			for (int i = 0; i < matchList.size(); i++) {
				if (matchList.get(i) > 0) { //galibiyet
					score += 3;
					playerWinMap.merge(playerId, 1, Integer::sum);

				} else { //ma�lubiyet
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
	 * @brief Oyuncular�n scorelar�n� azalan bir �ekilde s�ralayan method
	 */
	private void scoreSort() {

		playerScoreMap = playerScoreMap.entrySet().stream().sorted((i1, i2) -> i2.getValue().compareTo(i1.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * @brief Oyuncular�n scorelar�na g�re rank listesi olu�mas�n� sa�layan method
	 */
	private void createRankList() {

		for (Map.Entry<Integer, Integer> player : playerScoreMap.entrySet()) {
			playerRankList.add(player.getKey());
		}
	}
}
