package org.example;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ProbabilisticInformationRetrieval {

    private Map<String, Double> idfMap;
    private Map<String, Map<String, Double>> tfIdfMap;
    private Map<String, String> documents;

    public ProbabilisticInformationRetrieval() {
        this.idfMap = new HashMap<>();
        this.tfIdfMap = new HashMap<>();
        this.documents = new HashMap<>();
        ReadFile.readFile(this.documents, Contants.PATH_FILE_DOCUMENT);
        initIDF();
        calculateTFIDF();
    }

    private void initIDF() {
        int N = documents.size();
        for (Map.Entry<String, String> document : documents.entrySet()) {
            Set<String> uniqueWords = new HashSet<>(Arrays.asList(document.getValue().split(" ")));
            for (String word : uniqueWords) {
                if (!idfMap.containsKey(word)) {
                    int count = 0;
                    for (Map.Entry<String, String> doc : documents.entrySet()) {
                        String d = doc.getValue();
                        if (d.contains(word)) {
                            count++;
                        }
                    }
                    double idf = Math.log(N / count);
                    idfMap.put(word, idf);
                }
            }
        }
    }

    private Map<String, Double> getTermFrequency(String document) {
        Map<String, Double> tfMap = new HashMap<>();
        String[] words = document.split(" ");
        for (String word : words) {
            if (!tfMap.containsKey(word)) {
                tfMap.put(word, 1.0);
            } else {
                tfMap.put(word, tfMap.get(word) + 1);
            }
        }
        int totalWords = words.length;
        for (String key : tfMap.keySet()) {
            tfMap.put(key, tfMap.get(key) / totalWords);
        }
        return tfMap;
    }

    private void calculateTFIDF() {
        for (Map.Entry<String, String> document : documents.entrySet()) {
            String doc = document.getValue();
            Map<String, Double> tfMap = getTermFrequency(doc);
            Map<String, Double> tfidfMap = new HashMap<>();
            for (String word : tfMap.keySet()) {
                double tf = tfMap.get(word);
                double idf = idfMap.get(word);
                tfidfMap.put(word, tf * idf);
            }
            tfIdfMap.put(doc, tfidfMap);
        }
    }

    public List<String> search(String query) {
        Map<String, Double> tfMap = getTermFrequency(query);
        Map<String, Double> queryTfIdfMap = new HashMap<>();
        for (String word : tfMap.keySet()) {
            if (idfMap.containsKey(word)) {
                double tf = tfMap.get(word);
                double idf = idfMap.get(word);
                double tfidf = tf * idf;
                queryTfIdfMap.put(word, tfidf);
            }
        }
        List<String> results = new ArrayList<>();
        for (String doc : tfIdfMap.keySet()) {
            double score = 0.0;
            Map<String, Double> docTfIdfMap = tfIdfMap.get(doc);
            for (String word : queryTfIdfMap.keySet()) {
                if (docTfIdfMap.containsKey(word)) {
                    double docTfIdf = docTfIdfMap.get(word);
                    double queryTfIdf = queryTfIdfMap.get(word);
                    double pwc = (docTfIdf + 1) / (docTfIdfMap.size() + idfMap.size());
                    score *= (0.5 + 0.5 * pwc * queryTfIdf);
                }
            }
            results.add(doc + " - Score: " + score);
        }
        System.out.println("abc");
        return results;
    }


}

//import java.util.*;
//
//public class ProbabilisticInformationRetrieval {
//
//    private Map<String, Integer> docFreqs; // số lần xuất hiện của từ trong tài liệu
//    private Map<String, Map<Integer, Integer>> termFreqs; // số lần xuất hiện của từ trong từng tài liệu
//    private Map<Integer, Double> docLengths; // độ dài của từng tài liệu
//
//    private final double k1 = 1.2; // hệ số tối ưu cho tính toán tf
//    private final double k2 = 100; // hệ số tối ưu cho tính toán idf
//    private final double b = 0.75; // hệ số tối ưu cho tính toán độ dài tài liệu
//    private Map<String, String> documents = new HashMap<>();
//
//    public ProbabilisticInformationRetrieval() {
//        ReadFile.readFile(documents, Contants.PATH_FILE_DOCUMENT);
//        docFreqs = new HashMap<>();
//        termFreqs = new HashMap<>();
//        docLengths = new HashMap<>();
//
//        // tính toán số lần xuất hiện của từ trong tài liệu
//        for (int i = 1; i <= documents.size(); i++) {
//            String[] words = documents.get(i + "").split("\\s+");
//            Map<String, Integer> wordFreqs = new HashMap<>();
//            for (String word : words) {
//                if (!wordFreqs.containsKey(word)) {
//                    wordFreqs.put(word, 1);
//                } else {
//                    wordFreqs.put(word, wordFreqs.get(word) + 1);
//                }
//            }
//            for (String word : wordFreqs.keySet()) {
//                if (!docFreqs.containsKey(word)) {
//                    docFreqs.put(word, 1);
//                } else {
//                    docFreqs.put(word, docFreqs.get(word) + 1);
//                }
//                if (!termFreqs.containsKey(word)) {
//                    Map<Integer, Integer> docTermFreqs = new HashMap<>();
//                    docTermFreqs.put(i, wordFreqs.get(word));
//                    termFreqs.put(word, docTermFreqs);
//                } else {
//                    Map<Integer, Integer> docTermFreqs = termFreqs.get(word);
//                    docTermFreqs.put(i, wordFreqs.get(word));
//                    termFreqs.put(word, docTermFreqs);
//                }
//            }
//        }
//
//        // tính toán độ dài của từng tài liệu
//        for (int i = 1; i < documents.size(); i++) {
//            String[] words = documents.get(i + "").split("\\s+");
//            double length = 0;
//            for (String word : words) {
//                int tf = termFreqs.get(word).get(i);
//                double idf = Math.log((documents.size() - docFreqs.get(word) + 0.5) / (docFreqs.get(word) + 0.5));
//                length += ((tf * (k1 + 1)) / (tf + k1 * (1 - b + b * (words.length / getAvgDocLength())))) * (k2 + 1) * idf;
//            }
//            docLengths.put(i, length);
//        }
//    }
//
//    // tính toán độ tương đồng giữa truy vấn và tài liệu
//    public Map<Integer, Double> search(String query) {
//        String[] words =
//                query.split("\\s+");
//        Map<String, Integer> queryFreqs = new HashMap<>();
//        for (String word : words) {
//            if (!queryFreqs.containsKey(word)) {
//                queryFreqs.put(word, 1);
//            } else {
//                queryFreqs.put(word, queryFreqs.get(word) + 1);
//            }
//        }
//
//        Map<Integer, Double> scores = new HashMap<>();
//        for (int i = 0; i < docLengths.size(); i++) {
//            double score = 0;
//            for (String word : words) {
//                if (docFreqs.containsKey(word)) {
//                    if (termFreqs.containsKey(word)) {
//                        Map<Integer, Integer> docFreqs = termFreqs.get(word);
//                        if (docFreqs != null && i < docFreqs.size()) {
//                            int tf = 0;
//                            if (docFreqs.get(i) != null && docFreqs.get(word) != null && queryFreqs.get(word) != null) {
//                                tf = docFreqs.get(i);
//                            } else {
//                                continue;
//                            }
////                            int tf = termFreqs.get(word).get(i);
//                            double idf = Math.log((docLengths.size() - docFreqs.get(word) + 0.5) / (docFreqs.get(word) + 0.5));
//                            double qtf = queryFreqs.containsKey(word) ? queryFreqs.get(word) : 0;
//                            score += ((tf * (k1 + 1)) / (tf + k1 * (1 - b + b * (docLengths.get(i) / getAvgDocLength())))) * ((k2 + 1) * idf * qtf) / (k2 + qtf);
//                        }
//                    }
//                }
//            }
//            scores.put(i, score);
//        }
//        List<Map.Entry<Integer, Double>> list = new LinkedList<>(scores.entrySet());
//        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
//            @Override
//            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        });
//
//        for (Map.Entry<Integer, Double> entry : list) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }
//        return scores;
//    }
//
//    // tính toán độ dài trung bình của tất cả các tài liệu
//    private double getAvgDocLength() {
//        double totalLength = 0;
//        for (double length : docLengths.values()) {
//            totalLength += length;
//        }
//        return totalLength / docLengths.size();
//    }
//}