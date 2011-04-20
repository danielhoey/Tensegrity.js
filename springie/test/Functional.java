
package test;
import com.springie.elements.nodes.*;
import com.springie.elements.links.*;
import com.springie.elements.clazz.*;
import com.springie.geometry.*;
import org.junit.*;

public class Functional {

  @Test public void threeStrutPrism() {
    NodeTypeFactory ntf = new NodeTypeFactory();
    LinkTypeFactory ltf = new LinkTypeFactory();
    // ltf.getNew((length, elasticity)
    LinkType strut = ltf.getNew(178, 0);
    LinkType short_chord = ltf.getNew(76, 10);
    int long_chord_length = strut.length * strut.length - (int)(short_chord.length * short_chord.length / Math.sin(60));
    LinkType long_chord = ltf.getNew(long_chord_length, 10);
   
    Clazz c1 = new Clazz(0x446644);

    Node n1 = new Node(p(0,0,0), 1, ntf);
    Node n2 = new Node(p(0,76,0), 1, ntf);
    Node n3 = new Node(p(76,76,0), 1, ntf);
    Link l1_2 = new Link(n1, n2, short_chord, c1);
    Link l1_3 = new Link(n1, n3, short_chord, c1);
    Link l2_3 = new Link(n2, n3, short_chord, c1);
    Node n4 = new Node(p(0,0,long_chord_length), 1, ntf);
    Node n5 = new Node(p(0,76,long_chord_length), 1, ntf);
    Node n6 = new Node(p(76,76,long_chord_length), 1, ntf);
    Link l4_5 = new Link(n4, n5, short_chord, c1);
    Link l4_6 = new Link(n4, n6, short_chord, c1);
    Link l5_6 = new Link(n5, n6, short_chord, c1);
    
    Link l1_4 = new Link(n1, n4, long_chord, c1);
    Link l2_5 = new Link(n2, n5, long_chord, c1);
    Link l3_6 = new Link(n3, n6, long_chord, c1);
    
    Link l1_5 = new Link(n1, n5, strut, c1);
    Link l2_6 = new Link(n2, n6, strut, c1);
    Link l3_4 = new Link(n3, n4, strut, c1);
  }


  private Point3D p(int x, int y, int z) {
    return new Point3D(x, y, z);
  }
}
