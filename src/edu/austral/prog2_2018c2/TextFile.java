package edu.austral.prog2_2018c2;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;


public class    TextFile {

    private String path;
    private boolean apend_to_file = false;
    Board board;

    public TextFile(String file_path){
        path = file_path;
    }

    public TextFile(String file_path, boolean append_value){
        path = file_path;
        apend_to_file = append_value;
    }

    public void writeToFile( String textLine ) throws IOException {
        FileWriter write = new FileWriter( path , apend_to_file);
        PrintWriter print_line = new PrintWriter( write );
        print_line.printf( "%s" + "%n" , textLine);
        print_line.close();

    }
public static void main(String[] args) throws IOException {
        TextFile data = new TextFile( "Holiwis" );
        data.writeToFile( "Hola buenos dias" );

    }

}
