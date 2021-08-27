package netpayTest;

import java.sql.*;
import java.util.ArrayList;

/*
 * This class is responsible for handling the specific database functions for the
 * creation of an adjacency list, as well as querying.
 */
class DbInteract {
  private String mysqlUrl;
  private String user;
  private String pass;
  private String dbName;
  private int autoincrement = 1;

  public DbInteract(String mysqlUrl, String user, String pass, String dbName) {
    this.mysqlUrl = mysqlUrl;
    this.user = user;
    this.pass = pass;
    this.dbName = dbName;
  }

  private boolean createDatabase() {
    try (Connection con = DriverManager.getConnection(mysqlUrl, user, pass); Statement st = con.createStatement();) {
      String sql = "CREATE DATABASE IF NOT EXISTS " + dbName + ";";
      st.executeUpdate(sql);
      System.out.println("SCHEMA CREATED SUCCESSFULLY");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean createTables() {
    try (Connection con = DriverManager.getConnection(mysqlUrl + dbName, user, pass);
        Statement st = con.createStatement();) {

      String sql = "DROP TABLE IF EXISTS Directories;";
      st.addBatch(sql);
      sql = "DROP TABLE IF EXISTS Files;";
      st.addBatch(sql);
      sql = "CREATE TABLE Directories (" + "DirectoryID int PRIMARY KEY NOT NULL," + "Name varchar(255),"
          + "FullPath varchar(255)," + "ParentID int references Directories(DirectoryID));";
      st.addBatch(sql);
      sql = "CREATE TABLE Files (" + "FileID int PRIMARY KEY NOT NULL," + "Name varchar(255),"
          + "FullPath varchar(255)," + "ParentID int references Directories(DirectoryID));";
      st.addBatch(sql);
      st.executeBatch();
      System.out.println("TABLES CREATED SUCCESSFULLY");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void setup() {
    createDatabase();
    createTables();
  }

  public void populateDatabase(String path) {
    Loader ld = new Loader(path);
    ld.loadFile();
    Node<String> root = ld.buildTree();
    try (Connection con = DriverManager.getConnection(mysqlUrl + dbName, user, pass);
        Statement st = con.createStatement();) {
      dfs(root, "", 0, st);
      st.executeBatch();
      System.out.println("DATA ENTERED SUCCESSFULLY");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void dfs(Node<String> node, String fullpath, int parentID, Statement st) {
    if (fullpath == "") {
      fullpath = node.getData();
    } else {
      fullpath = fullpath + "/" + node.getData();
    }

    try {

      if (node.getParent() == null) {
        st.addBatch("INSERT INTO directories VALUES (" + autoincrement + ", '" + node.getData() + "', '" + fullpath
            + "', NULL);");

      } else if (node.getChildren().size() == 0) {
        st.addBatch("INSERT INTO files VALUES (" + autoincrement + ", '" + node.getData() + "', '" + fullpath + "', "
            + parentID + ");");
      } else {
        st.addBatch("INSERT INTO directories VALUES (" + autoincrement + ", '" + node.getData() + "', '" + fullpath
            + "', " + parentID + ");");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    parentID = autoincrement;
    autoincrement++;

    // base case
    for (int i = 0; i < node.getChildren().size(); i++) {
      dfs(node.getChildren().get(i), fullpath, parentID, st);
    }
  }

  public String[] search(String value) {
    try (Connection con = DriverManager.getConnection(mysqlUrl + dbName, user, pass);
        Statement st = con.createStatement();) {
      ArrayList<String> data = new ArrayList<String>();
      ResultSet rs = st.executeQuery("SELECT fullpath FROM directories WHERE fullpath LIKE '%" + value + "%';");
      while (rs.next()) {
        data.add(rs.getString("fullpath"));
      }
      rs = st.executeQuery("SELECT fullpath FROM files WHERE fullpath LIKE '%" + value + "%';");
      while (rs.next()) {
        data.add(rs.getString("fullpath"));
      }

      return data.toArray(new String[data.size()]);

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

}