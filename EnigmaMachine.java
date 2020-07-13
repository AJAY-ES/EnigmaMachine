package sad;

import java.util.*;
/**
 * Enigma machine 
 * Same to German Enigma machine difference is that plugboard is generated w.r.to date.
 * Its same Encoder and Decoder
 * This will read 3 inputs:Input string (sentence),3 RotorKey and date(dd only)
 * This will return Output string 
 * */
class EnigmaMachine {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Code:");
		String input = sc.nextLine();
		String rotorKey = null;
		do {
			System.out.println("Enter the 3 Code:");
			rotorKey = sc.nextLine();
		} while (rotorKey.length() != 3);
		System.out.println("Enter the date:");
		int date = sc.nextInt();
		char[][] plugBoard = plugBoardGenerater(date);
		int rotateCount = 0;
		for (int i = 0; i < input.length(); i++) {
			char inputChar = input.charAt(i);
			char outputChar = ' ';
			if (inputChar == ' ') {
				outputChar = inputChar;
			} else {
				inputChar = getPlugBoardOut(plugBoard, inputChar);
				rotateCount++;
				outputChar = sendToRotors(inputChar, rotateCount, rotorKey.charAt(2), rotorKey.charAt(1),
						rotorKey.charAt(0));
				outputChar = getPlugBoardOut(plugBoard, outputChar);
			}
			System.out.print(outputChar);
		}
	}

	public static void seeRotorsAlignment(char[][] a) {
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(a[i][j] + "\t");
			}
			System.out.println("\n");
		}
	}

	public static char[][] setRotors(char set3, char set2, char set1) {
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String r3 = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
		String r2 = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
		String r1 = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
		String R = "ABCDEFGDIJKGMKMIEBFTCVVJAT";

		String res1 = rotate(r1, checkByIndex(r1, checkByLetter(alpha, set1)));
		String res2 = rotate(r2, checkByIndex(r2, checkByLetter(alpha, set2)));
		String res3 = rotate(r3, checkByIndex(r3, checkByLetter(alpha, set3)));
		char[][] a = new char[26][8];
		String alpha1 = rotate(alpha, alpha.charAt(checkByLetter(r1, res1.charAt(0))));
		String alpha2 = rotate(alpha, alpha.charAt(checkByLetter(r2, res2.charAt(0))));
		String alpha3 = rotate(alpha, alpha.charAt(checkByLetter(r3, res3.charAt(0))));
		for (int i = 0; i < 26; i++) {
			int j = 0;
			a[i][j++] = R.charAt(i);
			a[i][j++] = alpha3.charAt(i);
			a[i][j++] = res3.charAt(i);
			a[i][j++] = alpha2.charAt(i);
			a[i][j++] = res2.charAt(i);
			a[i][j++] = alpha1.charAt(i);
			a[i][j++] = res1.charAt(i);
			a[i][j++] = alpha.charAt(i);
		}
		return a;
	}

	public static char sendToRotors(char y, int count, char set3, char set2, char set1) {
		int c1 = (count % 26);
		int c2 = (count / 26) % 26;
		int c3 = (count / 676) % 26;
		char[][] a = setRotors((char) (set3 + c3), (char) (set2 + c2), (char) (set1 + c1));
		int index = -1;
		int j = 7;
		boolean moveRight = true;
		while ((j <= 7) && (j >= 0)) {
			if (moveRight) {
				if (j % 2 != 0)
					index = findIndex(a, j, y, -1);
				else
					y = a[index][j];
			} else {
				if ((j + 1) % 2 != 0)
					index = findIndex(a, j, y, -1);
				else
					y = a[index][j];
			}
			if (j == 0) {
				moveRight = false;
				y = a[index][j];
				index = findIndex(a, j, y, index);
			}
			j = (moveRight) ? j - 1 : j + 1;
		}
		return y;
	}

	public static int findIndex(char[][] a, int j, char ch, int index) {
		int i = 0;
		for (i = 0; i < 26; i++) {
			if ((a[i][j] == ch) && (index != i))
				break;
		}
		return i;
	}

	public static String rotate(String str, char ch) {
		String res = "";
		for (int i = 0; i < 26; i++) {
			if ((str.charAt(i) == ch)) {
				while (i < str.length()) {
					res = res + str.charAt(i++);
				}
				i = 0;
				while ((str.charAt(i) != ch)) {
					res = res + str.charAt(i++);
				}
				i = 30;
			}
		}
		return res;
	}

	public static int checkByLetter(String str, char in) {
		int index = -1;
		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) == in)) {
				index = i;
			}
		}
		return index;
	}

	public static char checkByIndex(String str, int index) {
		return str.charAt(index);
	}

	public static char[][] plugBoardGenerater(int day) {
		char[][] plugboard = new char[10][2];
		int count = (day % 26);
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		ArrayList<Character> chars = new ArrayList<Character>();
		for (char c : alpha.toCharArray()) {
			chars.add(c);
		}
		for (int i = 0; i < 10; i++) {
			plugboard[i][0] = chars.get(0);
			plugboard[i][1] = chars.get(count % chars.size());			 
			chars.remove(count % chars.size());
			chars.remove(0);
		}		 
		return plugboard;
	}

	public static char getPlugBoardOut(char[][] plugBoard, char input) {
		char output = ' ';
		for (int i = 0; i < 10; i++) {
			if (plugBoard[i][0] == input) {
				output = plugBoard[i][1];
				break;
			} else if (plugBoard[i][1] == input) {
				output = plugBoard[i][0];
				break;
			} else
				output = input;
		}

		return output;
	}
}