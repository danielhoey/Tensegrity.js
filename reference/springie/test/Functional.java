
package test;
import org.junit.*;
import org.junit.Assert.*;
import java.util.*;
import com.springie.elements.nodes.*;
import com.springie.elements.links.*;
import com.springie.elements.clazz.*;
import com.springie.geometry.*;

public class Functional {
  NodeTypeFactory ntf = new NodeTypeFactory();
  LinkTypeFactory ltf = new LinkTypeFactory();
  ArrayList<Node> nodes = new ArrayList<Node>();
  ArrayList<Link> links = new ArrayList<Link>();

  @Test public void threeStrutPrism() {
    // ltf.getNew((length, elasticity)
    LinkType strut = ltf.getNew(178, 0);
    LinkType short_chord = ltf.getNew(76, 10);
    //int long_chord_length = strut.length * strut.length - (int)(short_chord.length * short_chord.length / Math.sin(60));
    //int long_chord_length = (int)Math.sqrt(178.0*178.0 - (76*76 / Math.sin(60)));
    int long_chord_length = 225;
    LinkType long_chord = ltf.getNew(long_chord_length, 10);
   
    Clazz c1 = new Clazz(0x446644);

    Node n1 = node(0,0,0);
    Node n2 = node(0,76,0);
    Node n3 = node(66,38,0);
    links.add(new Link(n1, n2, short_chord, c1));
    links.add(new Link(n1, n3, short_chord, c1));
    links.add(new Link(n2, n3, short_chord, c1));

    Node n4 = node(0,0,long_chord_length);
    Node n5 = node(0,76,long_chord_length);
    Node n6 = node(66,38,long_chord_length);
    links.add(new Link(n4, n5, short_chord, c1));
    links.add(new Link(n4, n6, short_chord, c1));
    links.add(new Link(n5, n6, short_chord, c1));
    
    links.add(new Link(n1, n4, long_chord, c1));
    links.add(new Link(n2, n5, long_chord, c1));
    links.add(new Link(n3, n6, long_chord, c1));
    
    links.add(new Link(n1, n5, strut, c1));
    links.add(new Link(n2, n6, strut, c1));
    links.add(new Link(n3, n4, strut, c1));

    assertLength(short_chord.length, links.get(0).length());

    for (Link l : links) l.applyForce();
    for (Node n : nodes) n.travel();

    assertLength(short_chord.length, links.get(0).length());

  }

  private void assertLength(int a, int b) {
    // assert lengths withing 1.0 of each other
    Assert.assertEquals((double)a, (double)b, 1.0);
  }

  private Node node(int x, int y, int z) {
    Node n = new Node(p(x,y,z), 1, ntf);
    nodes.add(n);
    return n;
  }

  private Point3D p(int x, int y, int z) {
    return new Point3D(x, y, z);
  }
}
