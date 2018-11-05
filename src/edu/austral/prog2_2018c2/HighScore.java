package edu.austral.prog2_2018c2;


    import java.io.*;
    import java.util.ArrayList;


public class HighScore {

        public static final String FILENAME = "Scores.txt";
        private static ArrayList<String> scoring = new ArrayList<>();


        public void run() {

            BufferedWriter bw = null;
            BufferedReader br;
            FileWriter fw = null;
            FileReader fr;
            PrintWriter pw;


            try {
                fw = new FileWriter(FILENAME, true);
                bw = new BufferedWriter(fw);
                fr = new FileReader(FILENAME);
                br = new BufferedReader(fr);
                pw = new PrintWriter(bw);

                scoring = new ArrayList<String>();
                String thisLine;
                while((thisLine=br.readLine()) != null) {
                    scoring.add(thisLine);
                }
                scoring = ScoreData.creanding();
                for(int n = 0; n <= scoring.size(); n++){
                    pw.write(scoring.get(n) + "\n");
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

        public static ArrayList<String> getScoring(){
            return scoring;
        }
}

