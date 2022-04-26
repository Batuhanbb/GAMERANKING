package controller;

import filereader.FileReader;
import results.ResultManager;

/**
 * @brief Singleton class
 * 
 */
public class GameController {
	
	private FileReader fileReader;
	private ResultManager resultManager;

	public GameController() {
	}

	private FileReader createFileReader() {
		if (fileReader == null) {
			fileReader = new FileReader();
		}
		return fileReader;
	}

	public FileReader fileReaderGetInstance() {
		return createFileReader();
	}

	private ResultManager createResultManager() {
		if (resultManager == null) {
			resultManager = new ResultManager();
		}
		return resultManager;
	}

	public ResultManager resultManagerGetInstance() {
		return createResultManager();
	}
}
