package org.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Contants {
    public static final Set<String> STOP_WORDS = new HashSet<String>(
            Arrays.asList(
                    "",
                    "a",
                    "is",
                    "the",
                    "of",
                    "all",
                    "to",
                    "can",
                    "be",
                    "as",
                    "once",
                    "for",
                    "at",
                    "am",
                    "are",
                    "has",
                    "have",
                    "had",
                    "up",
                    "his",
                    "her",
                    "in",
                    "on",
                    "no",
                    "we",
                    "do",
                    "by",
                    "or",
                    "and",
                    "not"
            )
    );

    public static final String PATH_FILE_DOCUMENT = "doc.txt";

    public static final String PATH_FILE_QUERY = "query.txt";
}
