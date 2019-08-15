package cmu.assignment.pool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputFileReader {

	private String filePath = null;
	private int totalRecords = -1;
	private final String SPACE = " ";
	private final String RECORD_SEPERATOR = SPACE;

	public InputFileReader(String filePath) {
		File inputFile = new File(filePath);
		if (!inputFile.exists())
			throw new IllegalArgumentException("Given file does not exists. File: " + filePath);
		this.filePath = filePath;
	}

	public ArrayList<GuardShift> readInputRecords() throws IOException {
		File inputFile = new File(filePath);
		String line;
		BufferedReader reader = null;
		ArrayList<GuardShift> listGuardShifts = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			line = reader.readLine();

			this.totalRecords = Integer.parseInt(line);
			listGuardShifts = new ArrayList<GuardShift>(totalRecords);

			for (int counter = 1; counter <= totalRecords; counter++) {
				line = reader.readLine();
				GuardShift shift = parseShift(line);
				listGuardShifts.add(shift);
			}

		} catch (IOException e) {
			throw e;
		} finally {
			reader.close();
		}
		
		return listGuardShifts;
	}

	private GuardShift parseShift(String line) {

		String[] splits = line.split(RECORD_SEPERATOR);
		int start = Integer.parseInt(splits[0]);
		int end = Integer.parseInt(splits[1]);

		return new GuardShift(start, end);
	}
}
