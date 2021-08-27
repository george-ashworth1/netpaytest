package netpayTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Handles the reading of the input data and construction of tree.
 */
public class Loader {

  private String path;
  private String[] data;

  public Loader(String path) {
    this.path = path;
  }

  public void loadFile() {
    try {
      File input = new File(path);
      Scanner sc = new Scanner(input);
      ArrayList<String> data = new ArrayList<String>();
      while (sc.hasNextLine()) {
        data.add(sc.nextLine());
      }
      String[] dataArray = data.toArray(new String[data.size()]);
      sc.close();
      this.data = dataArray;
    } catch (FileNotFoundException e) {
      System.out.println("Error reading file.");
      e.printStackTrace();
    }
  }

  public Node<String> buildTree() {
    Node<String> root = new Node<String>(data[0].replace("\t", ""), null);
    Node<String> previousNode = root;
    for (int i = 1; i < data.length; i++) {
      // if previous item is less indented
      if (countTabs(data[i - 1]) < countTabs(data[i])) {
        Node<String> currentItem = new Node<String>(data[i].replace("\t", ""), previousNode);
        previousNode.addChild(currentItem);
        previousNode = currentItem;
        // if previous item is more indented
      } else if (countTabs(data[i - 1]) > countTabs(data[i])) {
        Node<String> parentNode = previousNode;
        for (int j = 0; j < countTabs(data[i - 1]) - countTabs(data[i]) + 1; j++) {
          parentNode = parentNode.getParent();
        }
        Node<String> currentItem = new Node<String>(data[i].replace("\t", ""), parentNode);
        parentNode.addChild(currentItem);
        previousNode = currentItem;
      } else {
        Node<String> currentItem = new Node<String>(data[i].replace("\t", ""), previousNode.getParent());
        previousNode.getParent().addChild(currentItem);
        previousNode = currentItem;
      }
    }

    return root;
  }

  private int countTabs(String str) {
    int tabCount = 0;
    for (char c : str.toCharArray()) {
      if (c == '\t') {
        tabCount++;
      }
    }

    return tabCount;
  }

}
