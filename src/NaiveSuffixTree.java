import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NaiveSuffixTree
{
  public class Node
  {
    int count = 1;
    ArrayList<Node> children;
    int stringStart = 0;
    int stringEnd = 0;
    int leaf = 0;
    ArrayList<Integer> leaves;

    public Node()
    {
      children = new ArrayList<Node>();
      leaves = new ArrayList<Integer>();
    }

    public Node(int start, int end)
    {
      this.stringStart = start;
      this.stringEnd = end;
      children = new ArrayList<Node>();
      leaves = new ArrayList<Integer>();
    }

    public Node(int start, int end, int leaf)
    {
      this.stringStart = start;
      this.stringEnd = end;
      children = new ArrayList<Node>();
      leaves = new ArrayList<Integer>();
      leaves.add(leaf);
      this.leaf = leaf;
    }

    public int length()
    {
      return stringEnd - stringStart;
    }

    public void addChild(Node node)
    {
      children.add(node);
    }
  }

  private Node root;
  public String input;
  private ArrayList<Node> nodes;

  public NaiveSuffixTree()
  {
    root = new Node();
    root.count--;
    nodes = new ArrayList<Node>();
  }

  int leaf = 1;

  public Node createTree(String input)
  {
    this.input = input;
    // this.input = input.concat("$");
    // this.input = " " + input;
    for (int i = 0; i < this.input.length(); i++)
    {
      process(i, this.input.length(), root);
      //System.out.println("TREE-----------------" + i);
      //printTree(root);
    }
    return root;
  }

  // I'm at a node
  public void process(int start, int end, Node node)
  {
    node.leaves.add(leaf);
    node.count++;
    for (int i = 0; i < node.children.size(); i++)
    {
      if (input.substring(start, end).startsWith(
          input.substring(node.children.get(i).stringStart,
              node.children.get(i).stringEnd)))
      {
        process(
            start
                + (node.children.get(i).stringEnd - node.children.get(i).stringStart),
            end, node.children.get(i));
        return;
      }
      int match = stringMatch(start, end, node.children.get(i).stringStart);
      if (match > 0)
      {
        splitTree(match, start, end, node, node.children.get(i));
        return;
      }
    }
    // no match
    Node newNode = new Node(start, end, leaf++);
    node.addChild(newNode);
    nodes.add(newNode);
  }

  public int stringMatch(int start, int end, int ostart)
  {
    for (int i = 0; i < end - start; i++)
    {
      if (input.charAt(start + i) != input.charAt(ostart + i))
      {
        return i;
      }
    }
    return -1;
  }

  public void splitTree(int match, int start, int end, Node parent, Node child)
  {
    Node newNode = new Node(start + match, end, leaf);
    Node newParent = new Node(start, start + match);
    newParent.leaves.addAll(child.leaves);
    newParent.leaves.add(leaf++);
    child.stringStart += match;
    parent.children.remove(child);
    parent.children.add(newParent);
    newParent.children.add(newNode);
    newParent.children.add(child);
    newParent.count = child.count + 1;
    nodes.add(newNode);
    nodes.add(newParent);
  }

  public void printTree(Node node)
  {
    System.out.println();
    System.out.println(node);
    System.out.println(node.count);
    System.out.println(node.leaves);
    System.out.println(node.leaf);
    System.out.println("String: "
        + input.substring(node.stringStart, node.stringEnd));
    System.out.println(node.stringStart + " " + (node.stringEnd - 1));
    for (int i = 0; i < node.children.size(); i++)
    {
      System.out.println(node.children.get(i));
    }
    for (int i = 0; i < node.children.size(); i++)
    {
      printTree(node.children.get(i));
    }
  }

  public boolean isLeftDiverse(Node node)
  {
    if (node.leaves.size() == 0 || node.leaves.get(0) == 1)
    {
      return true;
    }
    char temp = input.charAt(node.leaves.get(0) - 1 - 1);
    for (int i = 1; i < node.leaves.size(); i++)
    {
      if (temp != input.charAt(node.leaves.get(0) - 1))
      {
        return true;
      }
    }
    return false;
  }

  public ArrayList<Node> findMaxRepeats(Node node, int length, int repeats)
  {
    ArrayList<Node> returnArr = new ArrayList<Node>();
    if (node.length() >= length && node.count >= repeats && isLeftDiverse(node) == true)
    {
      returnArr.add(node);
    }
    for (Node child : node.children)
    {
      returnArr.addAll(findMaxRepeats(child, length, repeats));
    }
    return returnArr;
  }

  public ArrayList<Integer> findPosition(Node node)
  {
    ArrayList<Integer> returnArr = new ArrayList<Integer>();
    if (node.count == 1)
      returnArr.add(node.leaf);
    return node.leaves;
    /*
     * for (int i = 0; i < node.children.size(); i++) { if
     * (node.children.get(i).count > 1) {
     * returnArr.addAll(findPosition(node.children.get(i))); } else {
     * returnArr.add(node.children.get(i).leaf); } } return returnArr;
     */
  }

  public ArrayList<Integer> findSequence(String sequence, Node node)
  {
    if (sequence.length() == 0)
    {
      return findPosition(node);
    }

    for (int i = 0; i < node.children.size(); i++)
    {
      if (sequence.startsWith(input.substring(node.children.get(i).stringStart,
          node.children.get(i).stringEnd)))
      {
        return findSequence(
            sequence.substring(node.children.get(i).stringEnd
                - node.children.get(i).stringStart), node.children.get(i));
      }
      else if (input.substring(node.children.get(i).stringStart,
          node.children.get(i).stringEnd).startsWith(sequence))
      {
        System.out.println(input.substring(node.children.get(i).stringStart,
            node.children.get(i).stringEnd));
        return findPosition(node.children.get(i));
      }
    }
    return null;
  }

  public static void main(String[] args)
  {
    NaiveSuffixTree myTree = new NaiveSuffixTree();
    //String input = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATCGATCGTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTATCGATCGCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCATCGATCGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGATCGATCGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATCGATCGTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTATCGATCGCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCATCGATCGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGATCGATCGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATCGATCGTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTATCGATCGCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCATCGATCGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGATCGATCGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATCGATCGTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTATCGATCGCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCATCGATCGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGATCGATCG$";
    String input = "ATCGG$";
    Node root = myTree.createTree(input);
    // System.out.println(myTree.nodes);
    /*
     * Collections.sort(myTree.nodes, new Comparator<Node>() {
     * 
     * @Override public int compare(Node arg0, Node arg1) { // TODO
     * Auto-generated method stub if(arg0.stringEnd - arg1.stringEnd == 0)
     * return arg1.count - arg0.count; else return arg0.stringEnd -
     * arg1.stringEnd; } });
     */
   /* for (Node node : myTree.nodes)
    {
      System.out.println(node.stringEnd + " " + node.count);
    }*/
    //System.out.println(myTree.nodes);
    // System.out.println(myTree.findMaxRepeats(root, 3));
    myTree.printTree(root);
    for (Node node : myTree.findMaxRepeats(root, 1, 2))
    {
      System.out.println(input.substring(0, node.stringEnd));
      System.out.println();
    }
    // myTree.printTree(root);
    // Maximal Repeated sequences
  }
}
