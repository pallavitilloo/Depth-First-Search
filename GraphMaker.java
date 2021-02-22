/************************************************************************************************
*
*  The class GraphMaker has a static method makeGraph that reads data on a weighted 
*   directed graph from a file and creates a graph object.  The file must be organized
*   as follows:
*
*  The first line contains the number of vertices in the graph.  The vertices will
*   be numbered from 0 to numberOfVertices-1.
*  The remaining lines contain data on directed edges.  Each line will have a 
*   beginning and ending vertex index (integers from 0 to numberOfVertices-1) 
*   and the weight of the edge (a double value).
*  The method does not check for errors in the file.
*
*  @author James W Benham
*  November 19,2018
*
**************************************************************************************************/
import java.util.*;  // for class Scanner
import java.io.*;    // for class File

class GraphMaker {
  public static Graph makeGraph(String fileName) throws FileNotFoundException {

    File graphFile = new File(fileName);
    Scanner graphFileScanner = new Scanner(graphFile); 
  
    // Create Graph object with the specified number of vertices 
    int numberOfVertices = graphFileScanner.nextInt();
    Graph theGraph = new Graph(numberOfVertices);
  
    int startVertex, endVertex;
    double weight;
  
    // Create edges
    while(graphFileScanner.hasNextInt()) {
      startVertex = graphFileScanner.nextInt();
      endVertex = graphFileScanner.nextInt();
      weight = graphFileScanner.nextDouble();
      theGraph.addEdge(startVertex,endVertex,weight);
    } // loop
  
    graphFileScanner.close();
    return theGraph;
  } //makeGraph
  
} // class GraphMaker
    