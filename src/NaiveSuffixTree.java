import java.util.ArrayList;

public class NaiveSuffixTree
{
  public class Node
  {
    int count = 1;
    ArrayList<Node> children;
    int stringStart = 0;
    int stringEnd = 0;

    public Node()
    {
      children = new ArrayList<Node>();
    }

    public Node(int start, int end)
    {
      this.stringStart = start;
      this.stringEnd = end;
      children = new ArrayList<Node>();
    }

    public void addChild(Node node)
    {
      children.add(node);
    }
  }

  private Node root;
  public String input;

  public NaiveSuffixTree()
  {
    root = new Node();
    root.count --;
  }

  public Node createTree(String input)
  {
    this.input = input.concat("$");
    for (int i = 0; i < this.input.length(); i++)
    {
      process(i, this.input.length(), root);
      System.out.println("TREE-----------------" + i);
      printTree(root);
    }
    return root;
  }

  // I'm at a node
  public void process(int start, int end, Node node)
  {
    node.count++;
    for (int i = 0; i < node.children.size(); i++)
    {
      if (input.substring(start, end).startsWith(
          input.substring(node.children.get(i).stringStart,
              node.children.get(i).stringEnd)))
      {
        process(start + (node.children.get(i).stringEnd - node.children.get(i).stringStart), end,
            node.children.get(i));
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
    node.addChild(new Node(start, end));
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
    Node newNode = new Node(start + match, end);
    Node newParent = new Node(start, start + match);
    child.stringStart += match;
    parent.children.remove(child);
    parent.children.add(newParent);
    newParent.children.add(newNode);
    newParent.children.add(child);
    newParent.count = child.count + 1;
  }

  public void printTree(Node node)
  {
    System.out.println();
    System.out.println(node);
    System.out.println(node.count);
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

  public static void main(String[] args)
  {
    NaiveSuffixTree myTree = new NaiveSuffixTree();
    Node root = myTree.createTree("GGCTGGA");
    // myTree.printTree(root);
  }
}
