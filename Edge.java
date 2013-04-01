/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shaanan Cohney
 */
public class Edge {

    Friend A;
    Friend B;

    public Edge(Friend A, Friend B) {
        this.A = A;
        this.B = B;
    }

    public boolean equals(Object other){
      if (other == null) return false;
      if (other == this) return true;
      if (!(other instanceof Edge))return false;
      Edge otherEdge = (Edge)other;
      return (A == otherEdge.A && B == otherEdge.B) 
        || (A == otherEdge.B && B == otherEdge.A);
    }
}
