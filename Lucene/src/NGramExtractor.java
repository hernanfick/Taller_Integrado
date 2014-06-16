/*
    Copyright (C) 2013  Kyle Williams <kwilliams@psu.edu>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

/* This is a general class for extracting NGrams from a string of text implemented using Lucene libraries */

public class NGramExtractor {

    Analyzer analyzer;

    String text;
    Boolean stopWords;
    int minLength;
    int maxLength;

    //We store ngrams, unique ngrams, and the frequencies of the ngrams
    LinkedList<String> nGrams;
    LinkedList<String> uniqueNGrams;
    HashMap<String, Integer> nGramFreqs;

    /**
     * Default constructor.
     * Initializes the ngram extractor
     */
    public NGramExtractor() {

    }

    /**
     * Extracts NGrams from a String of text.
     * Can handle ngrams of any length and also perform stop word removal before extraction
     * @param text the text that the ngrams should be extracted from
     * @param minLength the minimum length of the ngrams
     * @param maxLength the maximum length of the ngrams
     * @param stopWords whether or not stopwords should be removed before extraction
     */
    public void extract(String text, int minLength, int maxLength, Boolean stopWords) throws FileNotFoundException, IOException {

        this.text = text;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.stopWords = stopWords;

        nGrams = new LinkedList<String>();
        uniqueNGrams = new LinkedList<String>();
        nGramFreqs = new HashMap<String, Integer>();

        /* If the minLength and maxLength are both 1, then we want unigrams
         * Make use of a StopAnalyzer when stopwords should be removed
         * Make use of a SimpleAnalyzer when stop words should be included
         */
        if ((minLength == 4) && (maxLength == 4)) {
            if (this.stopWords) {
                analyzer = new SpanishAnalyzer(Version.LUCENE_36);
            }
            else {
                analyzer = new SpanishAnalyzer(Version.LUCENE_36);
            }
        }
        else { //Bigger than unigrams so use ShingleAnalyzerWrapper. Once again, different analyzers depending on stop word removal
            if (this.stopWords) {
                analyzer = new  SpanishAnalyzer(Version.LUCENE_36); //This is a hack to use Lucene 2.4 since in 2.4 position increments weren't preserved by default. Using a later version puts underscores (_) in the place of removed stop words.
            }
            else {
                analyzer = new SpanishAnalyzer(Version.LUCENE_36);
            }
        }

        //Code to process and extract the ngrams
        TokenStream tokenStream = analyzer.tokenStream("text", new StringReader(this.text));
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        int tokenCount = 0;
        
        while (tokenStream.incrementToken()) {
            
            int startOffset = offsetAttribute.startOffset();
            int endOffset = offsetAttribute.endOffset();
            String termToken = charTermAttribute.toString(); //The actual token term
            nGrams.add(termToken); //Add all ngrams to the ngram LinkedList
            
        }

        //Store unique nGrams and frequencies in hash tables
        for (String nGram : nGrams) {
            if (nGramFreqs.containsKey(nGram)) {
                nGramFreqs.put(nGram, nGramFreqs.get(nGram)+1);
            }
            else {
                nGramFreqs.put(nGram, 1);
                uniqueNGrams.add(nGram);
            }
        }

    }

    /**
     * Returns the frequency of an ngram
     * @param ngram the ngram whose frequency should be returned
     * @return the frequency of the specified ngram
     */
    public int getNGramFrequency(String ngram) {
        return nGramFreqs.get(ngram);
    }

    /**
     * Returns all ngrams
     * @return all ngrams
     */
    public LinkedList<String> getNGrams() {
        return nGrams;
    }

    /**
     * Returns unique ngrams
     * @return the unique ngrams
     */
    public LinkedList<String> getUniqueNGrams() {
        return uniqueNGrams;
    }

}
