package org.example.sequential;

import java.util.Arrays;
import java.util.Random;

public class QLearning {
    private final double ALPHA=0.1;
    private final double GAMMA=0.9;
    private final double EPS=0.4;
    private final int MAX_EPOCH=90000;
    private final int GRID_SIZE=6;
    private final int ACTION_SIZE=4;
    private int [][] grid;
    //double precision mais consomme plus de ressources
    private double[][]qTable=new double[GRID_SIZE*GRID_SIZE][ACTION_SIZE];
    private int [][]actions;
    private int stateI,stateJ;

    public QLearning() {
        actions=new int[][]{
                {0,-1},//gauche
                {0,1},//droite
                {1,0},//bas
                {-1,0}//haut
        };
        grid=new int[][]{
                //Récompences
                {0,1,0,0,0,0},
                {0,0,0,0,-1,0},
                {0,0,0,0,0,0},
                {-1,-1,-1,-1,-1,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,1}
        };
    }

    private void resetState(){
        //Etat actuel d'agent
        stateI=2;
        stateJ=0;
    }

    private int chooseAction(double eps){
        Random rn=new Random();
        double bestQ=0;
        int act=0;
        if(rn.nextDouble()<EPS){
            //exploration
                act=rn.nextInt(ACTION_SIZE);
        }else {
            //exploitation : meilleur action à partir de mon état actuel
            int st=stateI*GRID_SIZE+stateJ;
            for (int i=0;i<ACTION_SIZE;i++){
                if(qTable[st][i]>bestQ){
                    bestQ=qTable[st][i];
                    act=i;
                }
            }
        }
        return act;
    }
    private int executeAction(int act){
        stateI=Math.max(0,Math.min(actions[act][0]+stateI,2));
        stateJ=Math.max(0,Math.min(actions[act][1]+stateI,2));
        return stateI*GRID_SIZE+stateJ;
    }
    private boolean finished(){
        return grid[stateI][stateJ]==1;
    }
    private void showResult(){

        System.out.println("***  q table ***");
        for(double []line:qTable){
            System.out.print("[");
            for (double qValue: line) {
                System.out.print(qValue+",");
            }
            System.out.println("]");
        }
        System.out.println("");
        resetState();
        while (!finished()){
            int act=chooseAction(0);
            System.out.println("State : "+(stateI*GRID_SIZE+stateJ)+" action : "+act);
            executeAction(act);
        }
        System.out.println("Final State : "+(stateI*GRID_SIZE+stateJ));
    }
    public void runQLearning(){
        int it=0;
        int act,act1;
        resetState();
        int currentState, nextState;
        while(it<MAX_EPOCH){
            resetState();
            while (!finished()) {
                currentState = stateI * GRID_SIZE + stateJ;
                act = chooseAction(0.3);
                nextState = executeAction(act);
                act1 = chooseAction(0);
                System.out.println(stateI + " " + stateJ + " " + grid[stateI][stateJ]);
                qTable[currentState][act] = qTable[currentState][act] + ALPHA * (grid[stateI][stateJ] + GAMMA * qTable[nextState][act1] - qTable[nextState][act]);
                it++;
            }
            showResult();
        }

    }
}
