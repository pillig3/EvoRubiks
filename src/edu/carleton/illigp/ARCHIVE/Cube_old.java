package edu.carleton.illigp;

/**
 * Created by peterillig on 5/10/17.
 */
public class Cube_old {

    private int[] cube;
    public static final int[] SOLVED_CUBE = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};


    public Cube_old(int[] cube){
        this.cube = cube;
    }

    // initializes randomly shuffled cube
    public Cube_old(){
        cube = new int[54];
        for (int i = 0; i < cube.length; i++) {
            cube[i] = SOLVED_CUBE[i];
        }
        //do some number of random moves
    }

    public void R(){

    }
    public void Ri(){
        for (int i = 0; i < 3; i++) {
            R();
        }
    }

    public void L(){

    }
    public void Li(){
        for (int i = 0; i < 3; i++) {
            L();
        }
    }

    public void U(){

    }
    public void Ui(){
        for (int i = 0; i < 3; i++) {
            U();
        }
    }

    public void D(){

    }
    public void Di(){
        for (int i = 0; i < 3; i++) {
            D();
        }
    }

    public void F(){

    }
    public void Fi(){
        for (int i = 0; i < 3; i++) {
            F();
        }
    }

    public void B(){

    }
    public void Bi(){
        for (int i = 0; i < 3; i++) {
            B();
        }
    }

}
