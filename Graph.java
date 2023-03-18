/*********************************************************************************************
*   The class Graph represents a weighted directed graph using adjacency lists
************************************************************************************************/

import java.util.*;  // For Iterator interface

// Class for an (possibly weighted) edge in the graph
class Edge {
  private int startVertex, endVertex;
  private double weight;  
    
  // Constructor
  public Edge (int startVertex, int endVertex, double weight) {
    this.startVertex = startVertex;
    this.endVertex = endVertex;
    this.weight = weight;    
  }
    
  // get methods
  public int getStartVertex() { return startVertex; }
  public int getEndVertex()   { return endVertex; }
  public double getWeight()   { return weight;    }     
  
} // class Edge

// class for a list of edges, maintained as a linked list
class EdgeList {
  // The inner class EdgeNode contains a linked-list node with an edge
  class EdgeNode {
    private Edge edge;
    private EdgeNode next; 

    // Constructor for an edge node given an edge and a reference to another node
    public EdgeNode(Edge edge, EdgeNode next) {
      this.edge = edge;
      this.next = next;
    }
    
    // Constructor for an edge node that reference null given an edge
    public EdgeNode(Edge edge) {
      this.edge = edge;
      this.next = null;
    }
    
    // Constructor for an edge node given the data for an edge and a reference to the next node
    public EdgeNode(int startVertex, int endVertex, double weight, EdgeNode next) {
      this.edge = new Edge(startVertex, endVertex, weight);
      this.next = next;
    }
    
    // get methods
    public Edge getEdge()     { return edge; }
    public EdgeNode getNext() { return next; }
    
    //set method for next node
    public void setNext(EdgeNode next)  {this.next = next; }
  }  // inner class EdgeNode
 
  EdgeNode headNode;    // The first node (if any) in the list 

  // Constructor creates an empty list
  public EdgeList()
  {
    headNode = null;    
  }

  // Adds an edge at the beginning of the list
  public void addEdge (Edge edge)
  {
    EdgeNode newNode = new EdgeNode(edge,headNode);
    headNode = newNode;
  } 

  public class EdgeListIterator implements Iterator<Edge> {
  
    private EdgeNode currentEdgeNode;
    
    // Constructor for Iterator
    public EdgeListIterator() {
      currentEdgeNode = headNode;
    }
    
    public boolean hasNext () {
      return currentEdgeNode != null;
    }
    
    public Edge next() {
      Edge currentEdge = currentEdgeNode.getEdge();
      currentEdgeNode = currentEdgeNode.getNext();
      return currentEdge;
    }
  } // inner class EdgeListIterator
  
  public EdgeListIterator getEdgeListIterator() {
    return new EdgeListIterator();
  }
   
}  // class EdgeList

class Graph {
  
  private int numberOfVertices;
  private EdgeList[] vertexList;  // The array of vertices where each vertex is the starting vertex for a list of edges

  // Constructor to create a graph
  // Initially the graph is an array of vertices with an empty list of edges for each vertex
  // A separate GraphMaker class will handle adding edges taken from a file.
  public Graph(int numberOfVertices) {
    this.numberOfVertices = numberOfVertices;
    vertexList = new EdgeList[numberOfVertices];    
    
    for (int vertexIndex = 0; vertexIndex < numberOfVertices; vertexIndex++)
    {
        vertexList[vertexIndex] = new EdgeList();        
    }
       
  } // Graph constructor
  
  // Adds an edge with the given start vertex, end vertex and weight to the linked list for the start vertex
  public void addEdge (int startVertex, int endVertex, double weight) {
     vertexList[startVertex].addEdge(new Edge(startVertex, endVertex, weight));
  } 

  public int getNumberOfVertices() { return numberOfVertices; }
  
  // Returns an iterator over the edges adjacent to a given vertex
  public Iterator<Edge> getVertexEdgeIterator(int vertexIndex) {
    return vertexList[vertexIndex].getEdgeListIterator();  
  } 
  
  // An iterator to get all the edges in the graph
  public class AllEdgeIterator implements Iterator<Edge>
  {
    private int currentVertexIndex;
      // The index of the vertex whose edges are currently being iterated over
    private Iterator<Edge> currentVertexIterator;
      // Iterator over edges adjacent to current vertex
         
    // Constructor
    public AllEdgeIterator() {
      currentVertexIndex = 0;
      currentVertexIterator = vertexList[currentVertexIndex].getEdgeListIterator();
    }
    
    /* Returns true if there are more edges from the current vertex
       Otherwise, attempts to find another vertex with a non-empty list of adjacent edges
       If there is such a vertex, sets currentVertexIterator to iterate over its edges, and returns true
       Otherwise, returns false
       Note that this function has the side effect of resetting the currentVertexIterator if needed
    */
    public boolean hasNext() {
      if(currentVertexIterator.hasNext())
        return true;
      else {
        while (currentVertexIndex < numberOfVertices-1 && !currentVertexIterator.hasNext()) {
          currentVertexIndex++;
          currentVertexIterator = vertexList[currentVertexIndex].getEdgeListIterator();
        } // loop
        
        return currentVertexIterator.hasNext();
      } // else
    } //hasNext
   
    public Edge next() {
      return currentVertexIterator.next();
    }  
      
  } // inner class AllEdgeIterator
  
  public AllEdgeIterator getAllEdgeIterator() {
    return new AllEdgeIterator();
  }

} // class Graph
