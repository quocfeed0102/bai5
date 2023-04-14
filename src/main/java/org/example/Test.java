package org.example;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

class Test {
    public static void searchIndex(String indexDirectoryPath, String field, String searchString, int maxResults) throws IOException, ParseException {
        FSDirectory directory = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser(field, new StandardAnalyzer());
        Query query = queryParser.parse(searchString);
        TopDocs topDocs = indexSearcher.search(query, maxResults);
        ScoreDoc[] hits = topDocs.scoreDocs;
        System.out.println("Total Hits: " + topDocs.totalHits);
        for (ScoreDoc scoreDoc : hits) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("Score: " + scoreDoc.score + "\t" + "Id: " + document.get("id") + "\t" + "Title: " + document.get("title") + "\t" + "Content: " + document.get("content"));
        }
        indexReader.close();
        directory.close();
    }

    public static void main(String[] args) {
        try {
            String indexDirectoryPath = "P:\\baitap\\BaiTap5\\doc.txt";
            String field = "content";
            String searchString = "information retrieval";
            int maxResults = 10;
            searchIndex(indexDirectoryPath, field, searchString, maxResults);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}