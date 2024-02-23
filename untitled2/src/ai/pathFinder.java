package ai;

import main.GamePanel;

import java.util.ArrayList;

public class pathFinder {
    GamePanel gp;
    Node [][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node start,goal,current;
    boolean goalReached = false;
    int step = 0;

    public pathFinder(GamePanel gp){
        this.gp = gp;
        instantiateNodes();
    }
    public void instantiateNodes(){
        node = new Node[gp.maxWorldcol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col<gp.maxWorldcol && row<gp.maxWorldRow){
            node[col][row] = new Node(col,row);
            col++;
            if (col == gp.maxWorldcol){
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes(){
        int col = 0;
        int row = 0;

        while (col<gp.maxWorldcol && row<gp.maxWorldRow){
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;

            col++;
            if (col == gp.maxWorldcol){
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    public void setNode(int startCol,int startRow, int goalCol, int goalRow){
        resetNodes();

        start = node[startCol][startRow];
        current = start;
        goal = node[goalCol][goalRow];
        openList.add(current);

        int col = 0;
        int row = 0;

        while (col<gp.maxWorldcol && row<gp.maxWorldRow){

            int tileNum = gp.tileManeger.mapTileNum[gp.currentMap][col][row];
            if (gp.tileManeger.tiles[tileNum].collision){
                node[col][row].solid = true;
            }
            for (int i =0; i<gp.iTile[1].length; i++)
            {
                if (gp.iTile[gp.currentMap][i]!= null && gp.iTile[gp.currentMap][i].destructible){
                    int itcol = gp.iTile[gp.currentMap][i].WorldX/gp.tileSize;
                    int itrow = gp.iTile[gp.currentMap][i].WorldY/gp.tileSize;
                    node[itcol][itrow].solid = true;
                }
            }
            getCost(node[col][row]);
            col++;
            if (col == gp.maxWorldcol){
                col = 0;
                row++;
            }
        }
    }

    public void getCost(Node node){
        //G cost
        int xDistance = Math.abs(node.col-start.col);
        int yDistance = Math.abs(node.row-start.row);
        node.gCost = xDistance+yDistance;
        //H cost
        xDistance = Math.abs(node.col-goal.col);
        yDistance = Math.abs(node.row-goal.row);
        node.hCost = xDistance + yDistance;
        //F cost
        node.fCost = node.gCost+node.hCost;
    }
    public boolean search(){
        while (!goalReached && step<500){
            int col = current.col;
            int row = current.row;
             current.checked = true;
             openList.remove(current);

             if (row-1>=0){
                 openNode(node[col][row-1]);
             }
            if (col-1>=0){
                openNode(node[col-1][row]);
            }
            if (row+1<gp.maxWorldRow){
                openNode(node[col][row+1]);
            }
            if (col+1<gp.maxWorldcol){
                openNode(node[col+1][row]);
            }

            int bestIndex = 0;
            int bestfCost = 999;
             for (int i =0; i<openList.size(); i++){
                 if (openList.get(i).fCost<bestfCost)
                 {
                     bestIndex = i;
                     bestfCost = openList.get(i).fCost;
                 }
                 else if (openList.get(i).fCost==bestfCost){
                     if (openList.get(i).gCost< openList.get(bestIndex).gCost){
                         bestIndex = i;
                     }
                 }
             }
             if (openList.size()==0){
                 break;
             }

             current = openList.get(bestIndex);
             if (current == goal){
                 goalReached = true;
                 trackThePath();
             }
             step++;
        }
        return goalReached;
    }

    public void trackThePath() {
        Node current = goal;
        while (current!=start){
            pathList.add(0,current);
            current = current.parent;
        }
    }

    public void openNode(Node node){
        if (!node.open && !node.checked && !node.solid){
            node.open = true;
            node.parent = current;
            openList.add(node);
        }
    }
}
