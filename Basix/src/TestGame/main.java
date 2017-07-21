package TestGame;

import EngineCore.BasixEngine;
import EngineCore.IBasixLogic;
 
public class main {
 
    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IBasixLogic gameLogic = new TestGameLogic();
            BasixEngine gameEng = new BasixEngine("Make a fucking quad you little bitch", 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.out.println("System exiting");
            System.exit(-1);

        }
    }
}