package netpayTest;

import java.util.ArrayList;
import java.util.List;
/*
 * basic tree data structure
 */
public class Node<T> {
  private ArrayList<Node<T>> children;
  private T data;
  private Node<T> parent;
  
  public Node(T data, Node<T> parent) {
    this.data = data;
    this.parent = parent;
    this.children = new ArrayList<Node<T>>();
  }
  
  public ArrayList<Node<T>> getChildren() {
    return children;
  }
  public T getData() {
    return data;
  }
  public void addChild(Node<T> child) {
    children.add(child);
  }
  public Node<T> getParent() {
    return parent;
  }
}
