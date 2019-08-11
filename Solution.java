import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * 
 */
public class Solution {

	// **** constants ****
	static final int 	Q	= 257;		// prime number: 101, 131, 257
	static final int	D	= 256;		// number of characters in alphabet (128 or 256)
	
	
	// **** global variables ****
	static int 			N	= 0;		// length of text
	static int 			M	= 0;		// length of pattern
	
	
	/*
	 * Search pattern in text using Rabin-Karp algorithm.
	 */
	static int search(String pattern, String text) {
		
		// ****  ****
		int count		= 0;			// count of matches
		int	mscMult		= 1;			// most significant character multiplier
		int	textHash	= 0;			// hash for the text
		int	patternHash	= 0;			// hash for the pattern
		
		// ???? ????
		System.out.println("search <<< D: " + D + " Q: " + Q);
				
		// **** compute mscMult = pow(D, M - 1) % Q ****
		for (int i = 0; i < (M - 1); i++)
			mscMult = (mscMult * D) % Q;
		
		// ???? ????
		System.out.println("search <<< mscMult: " + mscMult + "  pow(D, M - 1) % Q: " + (int)Math.pow((double)D, (double)(M - 1)) % Q);
		
		// **** calculate the hash of the pattern and the first text window ****
		for (int i = 0; i < M; i++) {
			patternHash = (D * patternHash + pattern.charAt(i)) % Q;
			textHash 	= (D * textHash    + text.charAt(i)   ) % Q;
		}
		
		// ???? ????
		System.out.println("search <<< patternHash: " + patternHash + " textHash: " + textHash);
		
		// **** slide the pattern over the text one character at a time ****
		for (int i = 0; i <= (N - M); i++) {
			
			// **** ****
			int j = 0;
			
			// **** check if hashes match ****
			if (patternHash == textHash) {
				
				// **** check if characters match ****
				for (j = 0; j < M; j++) {
					if (text.charAt(i + j) != pattern.charAt(j))
						break;
				}
				
				// **** check if all characters match (pattern found) ****
				if (j == M) {
					
					// ???? ????
					System.out.println("search <<< match found at i: " + i);
					
					// **** increment the count of matches ****
					count++;
				}
			}
			
			// **** compute the hash for the NEXT window of text
			//		remove leading digit and add trailing one ****
			if (i < (N - M)) {
				
				// **** compute the text hash ****
				textHash = (D * (textHash - text.charAt(i) * mscMult) + text.charAt(i + M)) % Q;
				
				// **** ****
				if (textHash < 0)
					textHash += Q;
			}
			
		}
				
		// **** count of matches ****
		return count;
	}
	
	
	/*
	 * Check if the text represents a file name.
	 * If so, replace the text with the contents of the file.
	 */
	static String checkAndLoadText(String text, String regex) throws IOException {
		
		// ???? ????
		System.out.println("checkAndLoadText <<<  text ==>" + text + "<==");
		System.out.println("checkAndLoadText <<< regex ==>" + regex + "<==");
		
		// **** ****
		final Pattern pattern = Pattern.compile(regex);
		
		// **** ****
		Matcher m = pattern.matcher(text);
			
		// **** ****
		if (m.find()) {
			
			// ???? ????
			System.out.println("checkAndLoadText <<< match found!!!");
			
			// **** ****
			text = new String(Files.readAllBytes(Paths.get(text)));
		} else {
			
			// ???? ????
			System.out.println("checkAndLoadText <<< no match");
		}

		// **** ****
		return text;
	}
	
	
	/*
	 * Test scaffolding
	 */
	public static void main(String[] args) throws IOException {

		// **** open the scanner ****
		Scanner sc = new Scanner(System.in);
		
		// **** prompt for the text ****
		System.out.print("main >>>    text: ");
		String text = sc.nextLine();
		
//		// ???? ????
//		text = "c:/temp/alice_full.txt";
		
		// **** check for a file name and if needed replace text with the contents of the specified file ****
		String regex = "^(c|C):/.*\\.txt$";
		text = checkAndLoadText(text, regex);

//		// ???? ????
//		text = "testing: this is the test text for this test";
		
//		// ???? display the text and numbers to display the index ????
//		System.out.println("main <<<     text ==>" + text + "<==");
//		System.out.print("main <<<             ");
//		for (int i = 0; i < text.length(); i++)
//			System.out.print(i % 10);
//		System.out.println();
		
		// **** prompt for the pattern ****
		System.out.print("main >>> pattern: ");
		String pattern = sc.nextLine();
		
//		// ???? ????
//		pattern = "test";
		
		System.out.println("main <<<  pattern ==>" + pattern + "<==");
		
		// **** close the scanner ****
		sc.close();
		
		// **** ****
		N = text.length();
		M = pattern.length();
		
		// ???? ????
		System.out.println("main <<<       N: " + N + " M: " + M);
		
		// **** check if the text is shorter than the pattern ****
		if (N < M) {
			System.err.println("main <<< UNEXPECTED N: " + N + " < M: " + M);
			System.exit(-1);
		}
		
		// **** search for the pattern in the text ****
		int count = search(pattern, text);
		System.out.println("main <<<   count: " + count);
	}

}
