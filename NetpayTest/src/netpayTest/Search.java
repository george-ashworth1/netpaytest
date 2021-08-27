package netpayTest;

import java.util.Scanner;

/*
 * Queries the database against user input, which is looped.
 */
public class Search {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    DbInteract db = new DbInteract(DatabaseCredentials.MYSQL_URL, DatabaseCredentials.USER,
        DatabaseCredentials.PASS, DatabaseCredentials.DB_NAME);
    while (true) {
      System.out.print("Search: ");
      String value = sc.nextLine();
      String[] data = db.search(value);
      for (int i = 0; i < data.length; i++) {
        System.out.println(data[i]);
      }
    }

  }

}
