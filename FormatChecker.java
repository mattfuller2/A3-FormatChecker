import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Evaluates the format of one or more input files, as specified in the command-line arguments
 *
 * @author Matt Fuller
 */
public class FormatChecker {
	private static int rows, cols;

	/**
	 * Usage: $ java FormatChecker file1 [file2 ... fileN]
	 * 
	 * @param args $ java FormatChecker file1 [file2 ... fileN]
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Usage: java ProcessText file1 [file2 ...]");
			System.exit(1);
		}
		else {
			for (String file : args) {
				boolean valid = true;
				File filename = new File(file);
				String invalidInfo = "";
				try {
					Scanner fileScan = new Scanner(filename);

					int rowCOUNT = 0;
					int gridDimCOUNT = 0;
					while (fileScan.hasNextLine()) {
						// Reads in each line of the file
						String line = fileScan.nextLine().trim();

						try {
							// create an additional Scanner to break the current line into
							// individual tokens separated by whitespace
							Scanner lineScan = new Scanner(line);

							// The file is invalid if there are more rows of data than specified
							try {
								if (rowCOUNT > rows && lineScan.hasNextDouble()) {
									valid = false;
									invalidInfo = "The first row of the file contains more integers than specified.";
									break;
								}

								int colCOUNT = 0;
								// loops until the line has no other tokens to read
								while (lineScan.hasNext()) {

									// Checks if first line is valid and sets the grid's dimensions
									// if it is
									if (rowCOUNT == 0) {
										colCOUNT++;
										if (colCOUNT == 1) {
											rows = lineScan.nextInt();
											gridDimCOUNT++;
										}
										else if (colCOUNT == 2) {
											cols = lineScan.nextInt();
											gridDimCOUNT++;
										}
										else {
											if (lineScan.nextInt() > 0) {
												valid = false;
												gridDimCOUNT++;
												invalidInfo = "The first row of the file contains more integers than specified.";
												break;
											}
										}
									}
									else if (rowCOUNT <= rows && colCOUNT < cols) {
										colCOUNT++;
										Double.parseDouble(lineScan.next());
									}
									else {
										colCOUNT++;
										// The file is invalid if there are more rows of data than
										// specified
										if (colCOUNT > cols) {
											valid = false;
											invalidInfo = "There are more columns of data than specified.";
											break;
										}
										Double.parseDouble(lineScan.next());
									}
								}
								lineScan.close();
							}
							// The file is invalid if the first line values in the file are not
							// integers
							catch (InputMismatchException m) {
								valid = false;
								invalidInfo = m.toString();
								break;
							}
						}
						// The file is invalid if there is a value in the file that is not a number
						catch (NumberFormatException n) {
							valid = false;
							invalidInfo = n.toString();
							break;
						}

						// The file is invalid if the first line does not provide valid grid
						// dimensions
						if (rowCOUNT == 0) {
							if (gridDimCOUNT != 2) {
								valid = false;
								break;
							}
						}
						rowCOUNT++;
					}

					// If the file is invalid, prints out why the file is invalid
					if (valid == false) {
						System.out.println(file);
						System.out.println(invalidInfo);
						System.out.println("INVALID");
						System.out.println();
					}
					else {
						System.out.println(file);
						System.out.println("VALID");
						System.out.println();
					}
					fileScan.close();
				}
				catch (FileNotFoundException e1) {
					System.out.println(filename);
					System.out.println(e1.toString());
					System.out.println("INVALID");
					System.out.println();
				}
			}
		}
	}
}
