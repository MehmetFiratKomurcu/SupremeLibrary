/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author RECEP
 */
public class BookRecommender {

    private dataSet[] data;
    private ArrayList<finalResult> resultsOfCosineValues = new ArrayList<finalResult>();
    
    public dataSet[] getData() {
        return data;
    }

    public ArrayList<finalResult> getResultsOfCosineValues() {
        return resultsOfCosineValues;
    }

    public BookRecommender() throws SQLException {
        readFromDatabase();
        cosineCorrelation();
        sortFinalResults();
        resultsOfCosineValues.forEach(x -> System.out.println("UserId: " + x.user_id + " Coseine Value: " + x.cosineValue));
    }

    public class finalResult {
        public int user_id;
        public double cosineValue;
    }

    public class dataSet {
        public int user_id;
        public ArrayList<Integer> book_id = new ArrayList<Integer>();
        public ArrayList<Integer> rating = new ArrayList<Integer>();
    }

    public void sortFinalResults() {
        for (int i = 0; i < resultsOfCosineValues.size(); i++) {
            for (int j = i; j < resultsOfCosineValues.size(); j++) {
                if (resultsOfCosineValues.get(i).cosineValue < resultsOfCosineValues.get(j).cosineValue) {
                    finalResult temp = new finalResult();
                    temp = resultsOfCosineValues.get(i);
                    resultsOfCosineValues.set(i, resultsOfCosineValues.get(j));
                    resultsOfCosineValues.set(j, temp);
                }
            }
        }
    }

    public void readFromDatabase() throws SQLException {
        try {
            YazLab1 yazlab = new YazLab1();
            Connection conn = DriverManager.getConnection(yazlab.getHost(), yazlab.getUName(), yazlab.getUPass());
            Statement stmt = conn.createStatement();
            String queryForCount = "SELECT count(distinct(ratings.user_id)) as countUserId FROM bx_book_ratings as ratings, bx_books as books,"
                    + "bx_users as users WHERE ratings.isbn = books.isbn AND users.user_id = ratings.user_id";
            ResultSet rs = stmt.executeQuery(queryForCount);
            rs.next();
            int dataSize = rs.getInt("countUserId");
//            System.out.println(dataSize);
            data = new dataSet[dataSize];
//            System.out.println(dataSize);

            String query = "SELECT ratings.user_id,books.book_id,ratings.book_rating FROM bx_book_ratings as ratings, bx_books as books, "
                    + "bx_users as users WHERE ratings.isbn = books.isbn AND users.user_id = ratings.user_id ORDER BY ratings.user_id DESC";
            System.out.println(query);
            rs = stmt.executeQuery(query);
            int userCounter = -1;
            int tempUserId = -1;
            while (rs.next()) {
                boolean dataFlag;
                if (tempUserId != rs.getInt("ratings.user_id")) {
                    userCounter++;
                    dataFlag = true;
//                    System.out.println(userCounter);
                } else {
                    dataFlag = false;
                }
                if (dataFlag == true) {
                    data[userCounter] = new dataSet();
                }
                data[userCounter].user_id = rs.getInt("ratings.user_id");
                data[userCounter].book_id.add(rs.getInt("books.book_id"));
                data[userCounter].rating.add(rs.getInt("ratings.book_rating"));
                tempUserId = rs.getInt("ratings.user_id");
//                System.out.println(rs.getInt("ratings.user_id") + "," + rs.getInt("books.book_id") + "," + rs.getInt("ratings.book_rating"));
            }
//            for (int i = 0; i < data.length; i++) {
//                for (int j = 0; j < data[i].book_id.size(); j++) {
//                    System.out.println(data[i].user_id + "," + data[i].book_id.get(j) + "," + data[i].rating.get(j));
//                }
//            }
            rs.close();
            conn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BookRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cosineCorrelation() {
        int loggedInUserIdIndex = -1;
        double squareRootSumOfSquaresofFirstUser = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i].user_id == LogInScreen.getUserId()) {
                loggedInUserIdIndex = i;
            }
        }
        for (int j = 0; j < data[loggedInUserIdIndex].rating.size(); j++) {
            squareRootSumOfSquaresofFirstUser += Math.pow(data[loggedInUserIdIndex].rating.get(j), 2);
        }
        squareRootSumOfSquaresofFirstUser = Math.sqrt(squareRootSumOfSquaresofFirstUser);
        for (int i = 0; i < data.length; i++) {
            int shareSum = 0;
            double denominatorSum = 0;
            double squareRootSumOfSquaresofSecondUser = 0;
            if (i == loggedInUserIdIndex) {
                continue;
            }
            for (int j = 0; j < data[i].book_id.size(); j++) {
                if (data[loggedInUserIdIndex].book_id.indexOf(data[i].book_id.get(j)) != -1) {
                    int firstBookIdIndex = data[loggedInUserIdIndex].book_id.indexOf(data[i].book_id.get(j));
                    shareSum += data[loggedInUserIdIndex].rating.get(firstBookIdIndex) * data[i].rating.get(j);
                }
                squareRootSumOfSquaresofSecondUser += Math.pow(data[i].rating.get(j), 2);
            }
            squareRootSumOfSquaresofSecondUser = Math.sqrt(squareRootSumOfSquaresofSecondUser);
            denominatorSum = squareRootSumOfSquaresofFirstUser * squareRootSumOfSquaresofSecondUser;
            double cosineValue = shareSum / denominatorSum;
            if (!Double.isNaN(cosineValue) && cosineValue != 0.0) {
                finalResult tempFinal = new finalResult();
                tempFinal.user_id = data[i].user_id;
                tempFinal.cosineValue = cosineValue;
                resultsOfCosineValues.add(tempFinal);
//                System.out.println(data[i].user_id + "," + cosineValue);
            }
            data[i].book_id.removeAll(data[loggedInUserIdIndex].book_id);
        }
    }
}
