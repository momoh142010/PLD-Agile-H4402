package fr.insa.lyon.pld.agile.algorithm;


import fr.insa.lyon.pld.agile.model.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;




/**
 * This is the first version of the KMeans, there's no kmeans++ initialization,
 * and no sorting when assigning the nodes.
 * This version is not used in the actual application.
 * 
 * @author challal
 */
public class KMeansV0 {
    
    public static List<Node> generateNodes(int nodesNb, int spaceHeight, int spaceWidth) {
        List<Node> nodes = new ArrayList<>();
        for(int i=0; i<nodesNb; ++i) {
            double lon = ( (Math.random()-0.5) * spaceWidth ) ;
            double lat = ( (Math.random()-0.5) * spaceHeight ) ;
            nodes.add(new Node (i, lon, lat));
        }
        
        return nodes;
    }
        
    public static int[] kMeans(List<Node> nodes, int clustersNb) {
        Point2D[] clustersCenters = new Point2D[clustersNb];    // the centers of the clusters
        int[] clusters = new int[nodes.size()];   // the clusters to which the points belong
        int[] clustersNodesNumber = new int[clustersNb];    // the number of points in the clusters
        
        for(int i=0; i<clustersNb; ++i) {
            int randomIndex = (int)(Math.random() * nodes.size());
            double lon = nodes.get(randomIndex).getLongitude(); // coord x
            double lat = nodes.get(randomIndex).getLatitude(); // coord y
            clustersCenters[i] = new Point2D.Double(lon, lat);
        }
        
        for(int t=0; t<5000; ++t) {    
            //! init the number of deliveries in each cluster
            for(int k=0; k<clustersNb; ++k) {
                clustersNodesNumber[k] = 0;
            }
                        
            //! assign the deliveries to the clusters
            for(int i=0; i<nodes.size(); ++i) {
                int cluster = 0;
                while( clustersNodesNumber[cluster] >= Math.ceil(1.0*nodes.size()/clustersNb) ) {
                    ++cluster;
                }
                
                for(int k=1; k<clustersNb; ++k) {
                    double deliveryLon = nodes.get(i).getLongitude();
                    double deliveryLat = nodes.get(i).getLatitude();
                    if(clustersCenters[k].distance(deliveryLon, deliveryLat) < clustersCenters[cluster].distance(deliveryLon, deliveryLat)
                            && clustersNodesNumber[k] < Math.ceil(nodes.size()/clustersNb)) {
                        cluster = k;
                    }
                }
                clusters[i] = cluster;
                clustersNodesNumber[cluster]++;
            }
            
            //! reevaluate the clusters centers
            for(int k=0; k<clustersNb; ++k) {
                double LonSum = 0; // Coordinate X
                double LatSum = 0; // Coordinate Y
                for(int i=0; i<nodes.size(); ++i) {
                    if(clusters[i] == k) {
                        LonSum += nodes.get(i).getLongitude();
                        LatSum += nodes.get(i).getLatitude();
                    }
                }
                
                if(clustersNodesNumber[k] != 0) {
                    clustersCenters[k].setLocation(LonSum / clustersNodesNumber[k], 
                                                   LatSum / clustersNodesNumber[k]);
                } else {
                    int randomIndex = (int)(Math.random() * nodes.size());
                    double lon = nodes.get(randomIndex).getLongitude();  // coord x
                    double lat = nodes.get(randomIndex).getLatitude();   // coord y
                    clustersCenters[k] = new Point2D.Double(lon, lat);
                }
            }
        }
        
        return clusters;
    }
    
    private static void test() {
        List<Node> nodes = generateNodes(12, 40, 40);
        int[] clusters = kMeans(nodes, 5);
        for(int i=0; i<clusters.length; ++i) {
            System.out.print(clusters[i] + " ; " ); 
            System.out.println(nodes.get(i).getLongitude() + " ; " +
                               nodes.get(i).getLatitude());
        }
    }

    public static void main(String[] args) {
        test();
    }

}
