package org.example;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ReadFile {

    public static void readFile(Map<String, String> map, String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            String temp = "";
            String[] temps;
            Set<String> tempSet;
            boolean flag = true;
            int index = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (flag) {
                    index = Integer.parseInt(line.trim());
                    flag = false;
                } else {
                    if (line.trim().equals("/")) {
                        flag = true;
                        map.put("" + index, npl(temp));
                        temp = "";
                    } else {
                        temp = temp + " " + line.trim().toLowerCase();
                    }
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String npl(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = pipeline.processToCoreDocument(text);
        StringBuilder result = new StringBuilder();
        String temp;
        for (CoreLabel tok : document.tokens()) {
            temp = tok.lemma();
            if (!Contants.STOP_WORDS.contains(temp)) {
                result.append(temp.toLowerCase()).append(" ");
            }
        }
        return result.toString().trim();
    }
}
