import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

import java.io.*;
import java.util.*;

import static javax.lang.model.SourceVersion.isIdentifier;

public class JavaClass {
    private String content;
    private String fileName;
    private List<String> terms;

    public Hashtable<String, Double> getTfs() {
        return tfs;
    }

    private Hashtable<String, Double> tfs = new Hashtable<>();
    private List<Double> tfIdfs = new ArrayList<>();


    public JavaClass(String fileName, File file) {
        this.fileName = fileName;
        this.content = readDocument(file);
        preprocessDocument();
        this.tfs = calculateTfs(terms);
    }

    public JavaClass() {
    }

    public void setTfIdfs(List<Double> tfIdfs) {
        this.tfIdfs = tfIdfs;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getTerms() {
        return terms;
    }

    public List<Double> getTfIdfs() {
        return tfIdfs;
    }

    /**
     * read the content in a Java file into a String and store it to the variable *content*.
     * @param file . A file Object
     * @return content. A string storing the content of the Java file.
     */
    public String readDocument(File file) {
    	
        StringBuilder contents = new StringBuilder();
        BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        String st; 
        try {
			while ((st = br.readLine()) != null) {
			        contents.append(st); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return contents.toString();
    }

    /**
     * Preprocess the Java class file and store the processed tokens in the variable *terms*.
     */
    public void preprocessDocument() {
        List<String> tokens = tokenizeAndStemDocument();
        terms = removePunctuation(tokens);
    }

    /**
     * Tokenize the *content* of the Java file, return a list of tokens.
     * It contains two three steps:
     * 1. split the content into tokens, this is already done.
     * 2. do the Lemmatization on the tokens
     * 3. deal with the CamelCase.
     * @return tokens
     */
    private List<String> tokenizeAndStemDocument() {

        List<String> tokens = new ArrayList<>();
        CoreDocument coreDocument = new CoreDocument(content);
        // annotate document for tokenization
        Utility.pipeline.annotate(coreDocument);
        for (CoreSentence sentense: coreDocument.sentences()){
            for (CoreLabel token: sentense.tokens()){
                String tokenLemma = null; //do the Lemmatization and get the lemma of the token.
                List<String> tokenSplits = splitIdentifiers(tokenLemma); // deal with the CamelCases.
                tokens.addAll(tokenSplits);
            }
        }
        return tokens;
    }


    /**
     * This method take a string as input and return a list of tokens after splitting the CamelCase
     * For example, given the String,
     * 1. CamelCase, it should return "Camel" and "Case".
     * 2. camelCase, return "camel" and "Case".
     * 3. MAXNumber, return "MAX" and "Number".
     * 4. top1Results. return "top", "1" and "Results"
     * @param tokenLemma
     * @return a list of tokens after spliting the camel case
     */
    private List<String> splitIdentifiers(String tokenLemma){
        List<String> splitTokens = new ArrayList<>();
        return splitTokens;
    }


    /**
     *  Loop the tokens in the Java file and return a token list without the punctuations.
     * @param tokens
     * @return tokensWithoutPunctuation
     */
    private List<String> removePunctuation(List<String> tokens){
        List<String> tokensWithoutPunctuation = new ArrayList<>();
        for (String token: tokens){
            if (isIdentifier(token)){
                tokensWithoutPunctuation.add(token);
            }
        }
        return tokensWithoutPunctuation;
    }

    /**
     * Calculate TFs of the Java class. The TF should be store in the variable *tfs*. The key is the term, the value is
     * the frequency of this term. The method need to process the tokens in the variable *terms*. For example, the
     * variable *terms* is [â€œforâ€�, â€œiâ€�, â€œiâ€�,â€œiâ€�, â€œprintfâ€�, â€œhelloâ€�] ,
     * the output should be a hashtable with the value {â€œforâ€�:1, â€œiâ€�:3, â€œprintfâ€�:1, â€œhelloâ€�:1}
     *
     * @param terms a list of terms in the Java file.
     * @return tfs.
     */
    private Hashtable<String, Double> calculateTfs( List<String> terms) {
        Hashtable<String, Double> tfs = new Hashtable<>();
        return tfs;
    }

    /**
     * This method is used for calculating the TF.IDF representation of the Java file.
     * It accepts a dictionary which contains the IDF information.
     * And it returns results as list of TF-IDF values for terms in the same order as dictionary.getIdfs()
     * @param dictionary
     * @return tfIdfs. list of TF-IDF values for terms
     */
    public List<Double>  calculateTfIdfs(Dictionary dictionary) {
        List<Double> tfIdfs = new ArrayList<>();
        return tfIdfs;
    }

    /**
     * Do the calculation of the cosine similarity between this document and the given document.
     * @param query
     * @return the cosine similarity between the TFIDF representation of this Java file and that of the given Java file.
     */
    public double calculateCosineSimilarity(JavaClass query) {
        double cosineSimilarity = 0;
        return cosineSimilarity;
    }
    /*public static void main(String[] args)
    {
    }*/
}
