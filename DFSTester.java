/***********************************************************************************************
*
*  class DFSTester contains a main method that reads in a graph, performs depth-first search
*  from a given source vertex, and prints out the shortest path to every vertex reachable
*  from the source.
*
* 
************************************************************************************************/

import java.io.*; // for FileNotFoundException
import java.util.*;  // For iterator interface

class DFSTester {

 public static void main (String[] args) {
 
    // Read in a file and build graph
    String inputFileName = args[0];
    
    Graph myGraph = null;
    
    try {
      myGraph = GraphMaker.makeGraph(inputFileName);
    }
    catch (FileNotFoundException e)
    {
      System.out.println("File " + inputFileName + " not found.");
      System.exit(1);
    }


    // Perform depth-first search
    DepthFirstSearch dfs;
    int sourceVertex = 0;
    if (args.length > 1)
      sourceVertex = Integer.parseInt(args[1]);
      
    dfs = new DepthFirstSearch(myGraph,sourceVertex);
    
    dfs.doDFS();
    
    // Print out DFS tree
    System.out.println("\nThe tree for the depth first search from " + sourceVertex + " is\n");
    System.out.println(dfs.dfsTreeToString());
    
    // Print paths to vertices (if any)
    System.out.println("\nPaths from source vertex along DFS tree edges:\n");
    for (int currentVertex = 0; currentVertex < myGraph.getNumberOfVertices(); currentVertex++)
      System.out.println(dfs.getPathFromSource(currentVertex));
    
    //Print out edge classes
    Iterator<Edge> edgeIterator = null;
    System.out.println("\nThe tree edges are:\n");
    edgeIterator = dfs.getTreeEdgeIterator();
    printEdgesInList (edgeIterator);     
    
    System.out.println("\nThe back edges are:\n");
    edgeIterator = dfs.getBackEdgeIterator();
    printEdgesInList(edgeIterator);
    
    System.out.println("\nThe forward edges are:\n");
    edgeIterator = dfs.getForwardEdgeIterator();
    printEdgesInList(edgeIterator);

    System.out.println("\nThe cross edges are:\n");
    edgeIterator = dfs.getCrossEdgeIterator();
    printEdgesInList(edgeIterator);

  }
  
  // A function to print out the edges in an edge list using an iterator
  private static void printEdgesInList (Iterator<Edge> edgeIterator) {
    
    Edge currentEdge;
    
    if (!edgeIterator.hasNext())
      System.out.println("None");
    else
      while (edgeIterator.hasNext()) {
        currentEdge = edgeIterator.next();
        System.out.println("(" + currentEdge.getStartVertex() + "," + currentEdge.getEndVertex() + ")");
      }
  }
}    
