
/************************************************************************************
*
*  The class DepthFirstSearch contains methods to perform depth-first search
*   on a graph from a given source vertex, and to produce the shortest path 
*   (ignoring weights) from the source to any vertex.
*  It contains an inner class VertexData that maintains its distance from
*   the source vertex and its parent in the BFS tree
*
* @author Pallavi Tilloo
*
************************************************************************************/

import java.util.*;

class DepthFirstSearch {

  // An enumeration class for colors
  enum Color {
    WHITE, // color for vertices that has not yet been discovered by DFS
    GRAY, // color for verrtices that has been discovered but not fully processed
    BLACK; // color for vertices that have been fully processed
  }

  EdgeList treeEdges, backEdges, fwdEdges, crossEdges;

  // An inner class for vertex data
  class VertexData {

    private Color color;
    private int vertexIndex, parentIndex; // Index of parent vertex in DFS tree
    private double distance; // Distance to source vertex in DFS tree
    private int startTime, // Capture the start time of processing of the vertex
        endTime; // Capture the end time of processing of the vertex

    // Constructor creates a vertex with parent initialized to -1 (no parent),
    // distance initialized to infinity, color initialized to white
    // and start time and end time initialized to current datetime
    public VertexData(int vertexIndex) {
      this.vertexIndex = vertexIndex;
      parentIndex = -1; // no parent
      distance = Double.POSITIVE_INFINITY;
      color = Color.WHITE;
      startTime = 0;
      endTime = 0;
    }

    // get and set methods
    public int getVertexIndex() {
      return vertexIndex;
    }

    public int getParentIndex() {
      return parentIndex;
    }

    public double getDistance() {
      return distance;
    }

    public Color getColor() {
      return color;
    }

    public int getStartTime() {
      return startTime;
    }

    public int getEndTime() {
      return endTime;
    }

    public void setParent(int parentIndex) {
      this.parentIndex = parentIndex;
    }

    public void setDistance(double distance) {
      this.distance = distance;
    }

    public void setColor(Color color) {
      this.color = color;
    }

    public void setStartTime(int startTime) {
      this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
      this.endTime = endTime;
    }
  } // inner class Vertex data

  private Graph theGraph;
  private int sourceIndex; // Index of source vertex
  private VertexData[] vertexData;

  // Constructor sets graph and index of source vertex
  public DepthFirstSearch(Graph theGraph, int sourceIndex) {
    this.theGraph = theGraph;
    this.sourceIndex = sourceIndex;

    treeEdges = new EdgeList();
    backEdges = new EdgeList();
    fwdEdges = new EdgeList();
    crossEdges = new EdgeList();

    vertexData = new VertexData[theGraph.getNumberOfVertices()];

    for (int i = 0; i < theGraph.getNumberOfVertices(); i++)
      vertexData[i] = new VertexData(i);
  }

  // Constructor sets graph and default source index of 0
  public DepthFirstSearch(Graph theGraph) {
    this(theGraph, 0);
  }

  public void doDFS() {

    ArrayStack<Integer> stack = new ArrayStack<Integer>(theGraph.getNumberOfVertices());

    int currentVertexIndex, // current vertex being processed;
        adjacentVertexIndex, // a vertex adjacent to current vertex
        noOfVertices;

    noOfVertices = theGraph.getNumberOfVertices();

    Edge currentEdge = null;
    int currentTime = 0; // Start Time stamp at 0

    // Initialize stack with source vertex
    stack.push(sourceIndex);
    vertexData[sourceIndex].setColor(Color.GRAY);
    vertexData[sourceIndex].setDistance(0);
    vertexData[sourceIndex].setStartTime(currentTime);

    while (!stack.isEmpty()) {

      currentVertexIndex = stack.peek(); // To get the current value on top of Stack, we don't need to pop the value

      Iterator<Edge> edgeIterator = theGraph.getVertexEdgeIterator(currentVertexIndex);

      // Check the vertices that are adjacent to current vertex
      while (edgeIterator.hasNext()) {
        currentEdge = edgeIterator.next();
        adjacentVertexIndex = currentEdge.getEndVertex();

        if (vertexData[adjacentVertexIndex].getColor() == Color.WHITE) {

          // Vertex has not yet been discovered

          stack.push(adjacentVertexIndex);
          vertexData[adjacentVertexIndex].setColor(Color.GRAY);
          vertexData[adjacentVertexIndex].setDistance(vertexData[currentVertexIndex].getDistance() + 1);
          vertexData[adjacentVertexIndex].setStartTime(++currentTime);
          currentVertexIndex = adjacentVertexIndex; // Go to the adjacent vertex
          edgeIterator = theGraph.getVertexEdgeIterator(currentVertexIndex);
        }
      }

      // All elements until the depth have been discovered. Now, pop element from
      // stack and process it
      currentVertexIndex = stack.pop();
      if (currentVertexIndex != sourceIndex) {
        vertexData[currentVertexIndex].setParent(stack.peek()); // Parent of current element is the next element
      } // on top of the stack
      vertexData[currentVertexIndex].setColor(Color.BLACK);
      vertexData[currentVertexIndex].setEndTime(++currentTime);

    }

    // Setting the edges as Tree/Back/Forward/Cross
    boolean ifEdgeClassified = false;
    currentVertexIndex = sourceIndex;

    while (currentVertexIndex < noOfVertices) // Iterate over all vertices
    {
      if (vertexData[currentVertexIndex].getColor() != Color.WHITE) // Take only the DFS Tree vertices
      {
        Iterator<Edge> edgeIterator = theGraph.getVertexEdgeIterator(currentVertexIndex);

        while (edgeIterator.hasNext()) {
          currentEdge = edgeIterator.next();
          adjacentVertexIndex = currentEdge.getEndVertex();
          if (vertexData[adjacentVertexIndex].getParentIndex() == currentVertexIndex) {
            // currentEdge.setAsTreeEdge();
            treeEdges.addEdge(currentEdge);
            ifEdgeClassified = true;
          } else {
            // Find back edges
            int parent = vertexData[currentVertexIndex].getParentIndex();
            while (adjacentVertexIndex != parent && parent != -1) {
              parent = vertexData[parent].getParentIndex();
            }
            if (adjacentVertexIndex == parent || currentVertexIndex == adjacentVertexIndex) {
              backEdges.addEdge(currentEdge);
              ifEdgeClassified = true;
            }

            // Find forward edges
            parent = adjacentVertexIndex;
            while (currentVertexIndex != parent && parent != -1) {
              parent = vertexData[parent].getParentIndex();
            }
            if (currentVertexIndex == parent && currentVertexIndex != adjacentVertexIndex) {
              fwdEdges.addEdge(currentEdge);
              ifEdgeClassified = true;
            }

            // Find cross Edges
            if (!ifEdgeClassified)
              crossEdges.addEdge(currentEdge);
          } // End of else
          ifEdgeClassified = false;
        } // End of inner while loop
      } //End of IF 
      currentVertexIndex++;
    }  // End of outer while
  }

  // Returns the path from the source to a given vertex along BFS tree edges
  public String getPathFromSource(int vertexIndex) {
    if (vertexData[vertexIndex].getDistance() == Double.POSITIVE_INFINITY)
      return ("No path from source vertex to vertex " + vertexIndex + ".");
    else
      return ("(" + getPathFromSourceHelper(vertexIndex) + ")");
  }

  // Returns the sequence of vertices along the path
  public String getPathFromSourceHelper(int vertexIndex) {
    if (vertexData[vertexIndex].getParentIndex() == -1)
      // at source vertex
      return Integer.toString(vertexIndex);
    else
      return getPathFromSourceHelper(vertexData[vertexIndex].getParentIndex()) + " " + Integer.toString(vertexIndex);
  }

  /*
   * Creates a string of the depth-first search tree in parenthisized format This
   * format is (sourceVertex (child[1] subtree) (child[2] subtree)...(child[n]
   * subtree))
   */
  public String dfsTreeToString() {
    return dfsTreeToStringHelper(sourceIndex, vertexData[sourceIndex].getStartTime(),
        vertexData[sourceIndex].getEndTime());
  }

  // Builds the string for the subtree rooted at the given vertex
  public String dfsTreeToStringHelper(int vertexIndex, int startTime, int endTime) {
    StringBuilder treeString = new StringBuilder();
    treeString.append("( [" + vertexIndex + "," + startTime + "," + endTime + "] ");

    Iterator<Edge> adjacentEdgeIterator = theGraph.getVertexEdgeIterator(vertexIndex);

    while (adjacentEdgeIterator.hasNext()) {
      Edge adjacentEdge = adjacentEdgeIterator.next();
      int adjacentVertexIndex = adjacentEdge.getEndVertex();
      if (vertexData[adjacentVertexIndex].getParentIndex() == vertexIndex)
        treeString.append(dfsTreeToStringHelper(adjacentVertexIndex, vertexData[adjacentVertexIndex].getStartTime(),
            vertexData[adjacentVertexIndex].getEndTime()));
    }

    treeString.append(") ");
    return treeString.toString();
  }

  // Gets all tree edges
  public Iterator<Edge> getTreeEdgeIterator() {
    Iterator<Edge> treeEdgeIterator = treeEdges.getEdgeListIterator();
    return treeEdgeIterator;
  }

  // Gets all Backward tree edges
  public Iterator<Edge> getBackEdgeIterator() {
    Iterator<Edge> backEdgeIterator = backEdges.getEdgeListIterator();
    return backEdgeIterator;
  }

  // Gets all Forward tree edges
  public Iterator<Edge> getForwardEdgeIterator() {
    Iterator<Edge> fwdEdgeIterator = fwdEdges.getEdgeListIterator();
    return fwdEdgeIterator;
  }

  // Gets all Cross tree edges
  public Iterator<Edge> getCrossEdgeIterator() {
    Iterator<Edge> crossEdgeIterator = crossEdges.getEdgeListIterator();
    return crossEdgeIterator;
  }

}
