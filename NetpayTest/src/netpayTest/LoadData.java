package netpayTest;


/*
 * Loads data from file src/input.txt, and populates database with the data.
 */
public class LoadData {
  
  public static void main(String[] args) {
   DbInteract db = new DbInteract(DatabaseCredentials.MYSQL_URL, DatabaseCredentials.USER,
       DatabaseCredentials.PASS, DatabaseCredentials.DB_NAME);
   db.setup();
   db.populateDatabase("src/input.txt");
  }

}
