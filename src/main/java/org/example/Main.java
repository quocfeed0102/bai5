// N18DCC004 Hoang Nghia Quoc Anh
// N18DCCN163 Ho Mai Que
// N18DCCN166 Tran Anh Quoc

package org.example;

import java.util.*;

public class Main {
    private static final Map<String, String> queries = new HashMap<String, String>();


    public static void main(String[] args) {

        ProbabilisticInformationRetrieval model = new ProbabilisticInformationRetrieval();
        ReadFile.readFile(queries, Contants.PATH_FILE_QUERY);

        Scanner sc = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Nhap exit de thoat!");
            System.out.print("Nhap so thu tu cau query: ");
            choice = sc.nextLine();
            if (choice.equals("exit")) {
                break;
            }
            String query = queries.get(choice);
            List<String> results = model.search(query);
            System.out.println("Results for query: " + query);
            for (String result : results) {
                System.out.println("-" + result);
            }
            System.out.println("query: " + query);
        } while (true);
    }


}
