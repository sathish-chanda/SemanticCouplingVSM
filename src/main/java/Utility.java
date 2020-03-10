import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.lang.*;

public class Utility {

    public static StanfordCoreNLP pipeline;

    public static void initializeStanfordCoreNLP() {
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props);
    }

    /**
     * find the Java class that match the given file name.
     *
     * @param filename
     * @param javaClasses
     * @return
     */
    public static JavaClass findDocumentByFilename(String filename, List<JavaClass> javaClasses) {
        for (JavaClass javaClass : javaClasses) {
            if (javaClass.getFileName().equals(filename)) {
                return javaClass;
            }
        }
        return new JavaClass();
    }

    /**
     * calculate the cosine similarity between the given Java file and a list of other Java files, the return
     * is a hashtable, key is the Java file name, value is its similarity with the target Java file.
     *
     * @param targetFileName
     * @param javaClasses
     * @return documentSimilarities
     */
    public static Hashtable<String, Double> getDocumentSimilarities(String targetFileName, List<JavaClass> javaClasses) {
        JavaClass target = findDocumentByFilename(targetFileName, javaClasses);
        Hashtable<String, Double> documentSimilarities = new Hashtable<>();
        for (JavaClass javaClass : javaClasses) {
            if (target.getFileName().equals(javaClass.getFileName())) continue;
            documentSimilarities.put(javaClass.getFileName(), target.calculateCosineSimilarity(javaClass));
        }
        return documentSimilarities;
    }

    /**
     * Rank a hashtable based on the value in descending order. The return should be a list of Entries.
     * Within each Entry, key is the Java file name, value is the similarity score. For example, the input is
     * {'a.java': 1.0, 'b.java': 2.0, 'c.java': 1.5}.
     * The output should be [{'b.java': 2.0}, {'c.java': 1.5}, {'a.java': 1.0}]
     *
     * @param documentSimilarities, the hashtable returned by getDocumentSimilarities
     * @return listOfEntries
     */
    
    /*
     * TESTING
     * 
     * Hashtable<String,Double> ht = new Hashtable<String,Double>();
       ht.put("a.java", 1.0);
       ht.put("b.java", 2.0);
       ht.put("c.java", 1.5);
       ht.put("d.java",100.0);
       ht.put("e.java",0.67);
       ht.put("f.java",0.78);
       System.out.println(Utility.rankDimilarDocuments(ht));
     */
    public static List<Entry<String, Double>> rankDimilarDocuments(Hashtable<String, Double> documentSimilarities) {
        List<Entry<String, Double>> listOfEntries = new LinkedList<Entry<String,Double> >(documentSimilarities.entrySet());
            Collections.sort(listOfEntries, new Comparator<Entry<String, Double> >() { 
            public int compare(Entry<String, Double> o1,  
                               Entry<String, Double> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            }
        }); 
        return listOfEntries;
    }

    /**
     * print the top k entries in a list of entries.
     *
     * @param rankedDocuments returned from rankDimilarDocuments()
     * @param k               the max number of Java files to be printed.
     */
    public static void printTopKSimilarDocuments(List<Entry<String, Double>> rankedDocuments, Integer k) {
        for (int i = 0; i < k; i++) {
            System.out.format("The top " + k + " Java class(es) ranked by semantic coupling is \n" +
                    rankedDocuments.get(i).getKey() + "\t" + "%.2f%n", rankedDocuments.get(i).getValue());
        }
    }

    /**
     * dump the dictionary to disk, two files are generated, idfs.txt and invertedIndex.txt
     *
     * @param dictionary
     */
    public static void dumpDictionaryToDisk(Dictionary dictionary) {
        try {
            PrintWriter out = new PrintWriter(new File("idfs.txt"));
            PrintWriter outIvertedIndex = new PrintWriter(new File("invertedIndex.txt"));
            for (Map.Entry<String, Double> entry : dictionary.getIdfs().entrySet()) {
                out.println(entry.getKey() + "\t=>\t" + entry.getValue().toString());
            }
            for (Map.Entry<String, Set<String>> entry : dictionary.getInvertedIndex().entrySet()) {
                outIvertedIndex.println(entry.getKey() + "\t=>\t" + entry.getValue().toString());
            }
            outIvertedIndex.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dump the target Java class content to disk.
     *
     * @param javaFileName
     * @param javaClasses
     */
    public static void dumpJavaClassToDisk(String javaFileName, List<JavaClass> javaClasses) {
        try {
            JavaClass javaCLass = findDocumentByFilename(javaFileName, javaClasses);
            PrintWriter out = new PrintWriter(new File("tf.txt"));
            PrintWriter outtfidf = new PrintWriter(new File("tfidf.txt"));
            for (Map.Entry<String, Double> entry : javaCLass.getTfs().entrySet())
                out.println(entry.getKey() + "\t=>\t" + entry.getValue().toString());
            for (Double entry : javaCLass.getTfIdfs()) {
                outtfidf.println(entry.toString());
            }
            outtfidf.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args)
    {
       
    }*/
}

