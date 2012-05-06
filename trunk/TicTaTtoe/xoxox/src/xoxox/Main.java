package xoxox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author xkotula
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        Board board = new Board();

        board.load(new BufferedReader(new InputStreamReader(System.in)));

//        board.diags();

//        board.move();

        board.print();
        

    }

}
