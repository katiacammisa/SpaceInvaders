package edu.austral.prog2_2018c2;


    import java.io.*;
    import java.util.ArrayList;


public class TextFile {

        public static final String FILENAME = "D:\\AleGG\\SpaceInvaders\\Scores.txt";


        public void run(String player, int score) {

            BufferedWriter bw = null;
            BufferedReader br;
            FileWriter fw = null;
            FileReader fr;
            PrintWriter pw;
            ArrayList <String> scoring = new ArrayList<String>();
                scoring.add(player + " " + score);

            try {

                fw = new FileWriter(FILENAME);
                bw = new BufferedWriter(fw);
                fr = new FileReader(FILENAME);
                br = new BufferedReader(fr);
                pw = new PrintWriter(bw);
                for(int n = 0; n <= scoring.size(); n++){
                    pw.write(scoring.get(n));
                    String[] spliteado = scoring.get(n).split(":");
                }

                System.out.println("Done");

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (bw != null)
                        bw.close();

                    if (fw != null)
                        fw.close();

                } catch (IOException ex) {

                    ex.printStackTrace();

                }

            }

        }
}

