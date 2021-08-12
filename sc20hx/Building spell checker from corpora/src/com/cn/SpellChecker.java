package com.cn;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import com.cn.Sql;


public class SpellChecker {
	
	public static void main(String[] args) throws IOException {
		String goode = new SpellChecker().start("abc");
		System.out.println(goode);
	}
	
	private static final char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	// launch the database

	
	public String start(String input) throws IOException {
		//1.construct and build the language model
		String path = "big.txt";
		Map<String, Double> languModel = buildLanguageModel(path);
		Set<String> dictionary = languModel.keySet();
		List<String> guessWords = null;
		


		//2.aquire the words that user inputs
		while(input != null) {

			if("bye".equals(input))
				break;
			if(dictionary.contains(input)){
				return null;
			}
			Sql database_notepad=new Sql();
			String sql="select English from dic where English like'"+(input)+"'";
			try {
				ResultSet rs=database_notepad.rsexecuteQuery(sql);
				if(rs.next()) {
					String st=rs.getString("English");
					if (st.equals(input))
					return null;
					
				}
					
				}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();}

			

			long startTime = System.currentTimeMillis();
			
			//3.construct a lexicon set within the edit distance, and then delete the words that is not in the dictionary.
			
//			Set<String> wordsInEditDistance2 = buildEditDistance2Set(languModel, input);
//            wordsInEditDistance2.retainAll(dictionary);
			Set<String> wordsInEditDistance = buildEditDistance1Set(languModel, input);
            wordsInEditDistance.retainAll(dictionary);
            if(wordsInEditDistance.isEmpty()) {
                  wordsInEditDistance = buildEditDistance2Set(languModel, input);
                  wordsInEditDistance.retainAll(dictionary);
                  if (wordsInEditDistance.isEmpty()) {
                         return "false";
                  }
            }
			// 4.calculate all the possible probability
            // c - correct word we guess, w - wrong word user input in reality
            // argmax P(c|w) = argmax P(w|c) * P(c) / P(w)
            // we ignore P(w) here, because it's the same for all words
//          guessWords = guessRightWord(languModel, wordsInEditDistance);
//            System.out.printf("Do you want to input %s and Cost time: %.10f second(s)\n",
//                         guessWords.toString(), (System.currentTimeMillis() - startTime) / 1000D);
            guessWords = guessRightWord(languModel, wordsInEditDistance);
            if(guessWords.contains(input)){
            	return null;
			}
            return guessWords.toString();
		}
		if(guessWords.contains(input)){
			return null;
		}
		return guessWords.toString();
	}

	/**
	 * read from the corpora named big.txt, construct the model
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private Map<String, Double> buildLanguageModel(String path) throws IOException {
		Map<String, Double> languModel = new HashMap<String, Double>();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		//delete all the signals except alphabets in the document
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		String line;
		int totalCount = 0;
		while ((line = reader.readLine()) != null) {
			String[] words = line.split(" ");
			for(String word : words) {
				if(pattern.matcher(word).matches()) {
					word = word.toLowerCase();
					Double wordCount = languModel.get(word);
					if(wordCount == null) {
						languModel.put(word, 1D);
					} else {
						languModel.put(word, wordCount+1D);
					}
					totalCount++;
				}
			}
		}
		reader.close();
		
		for(Entry<String, Double> entry : languModel.entrySet())
			entry.setValue(entry.getValue() / totalCount);
		
		return languModel;
	}
	
	/**
	 * words collection of edit distance one
	 * @param languModel
	 * @param input
	 * @return
	 */
	private Set<String> buildEditDistance1Set(Map<String, Double> languModel,String input) {
		Set<String> wordsInEditDistance = new HashSet<String>();
		char[] characters = input.toCharArray();
		
		// delete letter[i]
		for(int i=0;i<input.length();i++) {
			wordsInEditDistance.add(input.substring(0,i) + input.substring(i+1));
		}
		// exchange letter[i] and letter[i+1]
		for(int i=0;i<input.length()-1;i++) {
			wordsInEditDistance.add(input.substring(0,i) + characters[i+1] 
					+ characters[i] + input.substring(i+2));
		}
		// exchange letter[i] to a-z
		for(int i=0;i<input.length();i++) {
			for(char c : alphabets) {
				wordsInEditDistance.add(input.substring(0,i) + c + input.substring(i+1));
			}
		}
		//  insert a new alphabet a-z
		for(int i=0;i<input.length()+1;i++){
			for(char c : alphabets) {
				wordsInEditDistance.add(input.substring(0,i) + c + input.substring(i));
			}
		}
		return wordsInEditDistance;
	}
	
	/**
	 * The collection of edit distance two, through editDistance1 function to get the collection of edit distance one,
	 * this collection then through editDistance1 function, to get the collection of edit distacne two.
	 * @param languModel
	 * @param input
	 * @return
	 */
	private Set<String> buildEditDistance2Set(Map<String, Double> languModel,String input) {
		Set<String> wordsInEditDistance1 = buildEditDistance1Set(languModel, input);
		Set<String> wordsInEditDistance2 = new HashSet<String>();
		for(String editDistance1 : wordsInEditDistance1) {
			//wordsInEditDistance2.addAll(buildEditDistance1Set(languModel, input));
			wordsInEditDistance2.addAll(buildEditDistance1Set(languModel, editDistance1));
			
		}
		wordsInEditDistance2.addAll(wordsInEditDistance1);
		return wordsInEditDistance2;
	}
	
	/**
	 * acquire the accurate words from the corpora
	 * @param languModel
	 * @param wordsInEditDistance
	 * @return
	 */
	private List<String> guessRightWord(final Map<String, Double> languModel,Set<String> wordsInEditDistance){
		List<String> words = new LinkedList<String>(wordsInEditDistance);
		// sort the words according to their probability in the dictionary list, the more probability, the more likely to appear.
		Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String word1, String word2) {
                  return languModel.get(word2).compareTo(languModel.get(word1));
            }
		});	
		return words.size() > 5 ? words.subList(0, 5) : words;
	}
}