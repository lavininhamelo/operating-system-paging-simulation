import java.util.Vector;
import java.util.Scanner;

import Software.Frame;
import Software.Page;

public class Teste {

  public static void main(String[] args) {
    Vector<Frame> list = new Vector<Frame>();
    list.add(new Frame(5));
    list.add(new Frame(2));
    list.add(new Frame(8));

    System.out.println(list.firstElement().getId());
  }

}