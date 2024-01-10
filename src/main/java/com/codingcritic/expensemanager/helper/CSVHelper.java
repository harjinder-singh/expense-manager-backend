package com.codingcritic.expensemanager.helper;

import com.codingcritic.expensemanager.model.Transaction;
import com.codingcritic.expensemanager.model.TransactionType;
import com.codingcritic.expensemanager.model.TransactionSubType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "TransactionDate", "Description", "Amount", "TransactionType", "TransactionSubType" };

  public static boolean hasCSVFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  } 

  public static List<Transaction> csvToTransactions(InputStream is, String format) throws NumberFormatException, ParseException {
    try (
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withHeader(HEADERs));) {
                System.out.println("Inside");
      List<Transaction> transactions = new ArrayList<Transaction>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      SimpleDateFormat sdf = new SimpleDateFormat(format);
    
      for (CSVRecord csvRecord : csvRecords) {
        System.out.println("Inside for");
        System.out.println(csvRecord.get("Description"));
        System.out.println(csvRecord.get("TransactionType"));
        System.out.println(csvRecord.get("TransactionSubType"));
        System.out.println(csvRecord.get("TransactionDate"));
        Transaction transaction = new Transaction(
              csvRecord.get("Description"),
              Double.parseDouble(csvRecord.get("Amount")),
              TransactionType.valueOf(csvRecord.get("TransactionType")),
              TransactionSubType.valueOf(csvRecord.get("TransactionSubType")),
              sdf.parse(csvRecord.get("TransactionDate"))
            );
        System.out.println("Hello");
        transactions.add(transaction);
      }
      System.out.println(transactions);
      return transactions;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

}
