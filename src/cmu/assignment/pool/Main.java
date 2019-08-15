package cmu.assignment.pool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		System.out.println("Starting LifeGuard removal process.");

		String outputLoc = "./OutputFiles/";
		String inputLoc = "./InputFiles/";
		File inputDir = new File(inputLoc);
		File[] listFiles = inputDir.listFiles();

		for (File file : listFiles) {
			if (file.isFile() && file.getName().endsWith(".in")) {

				System.out.println("Processing input file: " + file.getName());

				InputFileReader reader = new InputFileReader(file.getAbsolutePath());
				try {
					ArrayList<GuardShift> listShifts = reader.readInputRecords();
					RemoveLifeGuard removeLifeGuard = new RemoveLifeGuard(listShifts);
					Integer maxCoverage = removeLifeGuard.maxCoverageAfterRemoveOneLifeGuard();

					String outputFileName = file.getName().split(".in", 2)[0] + ".out";
					String outputFileLoc = outputLoc + outputFileName;
					File outputPath = new File(outputFileLoc);
					Files.write(outputPath.toPath(), maxCoverage.toString().getBytes());

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
