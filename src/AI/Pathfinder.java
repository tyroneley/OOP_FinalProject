package AI;

import java.util.ArrayList;

import entity.Entity;
import main.GamePanel;

public class Pathfinder {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public Pathfinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }
    
    // instantiates new nodes for each column and row
    public void instantiateNodes() {
        node = new Node[gp.maxScreenCol][gp.maxScreenRow];

        int col = 0;
        int row = 0;

        // creates node for each column and row, will be used for AI to find paths
        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            node[col][row] = new Node(col, row);
            col++;

            if (col == gp.maxScreenCol) {
                col = 0;
                row++;
            }
        }
    }

    // resets values of each node for new paths
    public void resetNodes() {
        int col = 0;
        int row = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxScreenCol) {
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    // sets the starting node, current node, and finish/goal node before computing a path
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {
        resetNodes();

        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            int tileNum = gp.tileM.mapTileNum[col][row];
            if (gp.tileM.tile[tileNum].collision) {
                node[col][row].solid = true;
            }

            getCost(node[col][row]);

            col++;
            if (col == gp.maxScreenCol) {
                col = 0;
                row++;
            }
        }
    }   
    
    // method to find which node has a higher or lower cost
    // g cost represents the node distance from the starting node
    // the h cost represents the node distance from the goal node
    // the f cost represents the node distance from the start node to the goal node
    // 1 g cost represents 1 node distance
    // the lower the cost the faster the path is to reach the end node
    public void getCost(Node node) {
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;
    }

    // searches for best path nodes
    public boolean search() {
        while (!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            // sets node to checked
            currentNode.checked = true;
            openList.remove(currentNode);

            // checks all sides of node/tile
            if (row - 1 >= 0) {
                openNode(node[col][row-1]);
            }

            if (col - 1 >= 0) {
                openNode(node[col-1][row]);
            }   

            if (row + 1 < gp.maxScreenRow) {
                openNode(node[col][row+1]);
            }

            if (col + 1 < gp.maxScreenCol) {
                openNode(node[col+1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            // finding the best node
            for (int i = 0; i < openList.size(); i++) {
                // smaller f cost/g cost means better node
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }
            
            // no more nodes
            if (openList.size() == 0) {
                break;
            }

            currentNode = openList.get(bestNodeIndex);
            
            // goal has been reached
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    // adds node to list of available nodes
    public void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    // tracks the current nodes/path
    public void trackThePath() {
        Node current = goalNode;

        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
