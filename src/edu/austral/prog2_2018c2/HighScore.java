package edu.austral.prog2_2018c2;


    import java.io.*;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.List;


public class HighScore {

        public static final String FILENAME = "Scores.txt";

        public static void run(ScoreData newScoreData) {



            try {

                List<ScoreData> scoring = read();
                scoring.add(newScoreData);

                scoring.sort(new Comparator<ScoreData>() {
                    @Override
                    public int compare(ScoreData o1, ScoreData o2) {
                        return Integer.compare(o2.getScore(), o1.getScore());
                    }
                });

                write(scoring);

                System.out.println("Done");

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        public static List<ScoreData> read() throws IOException {
            FileReader fr = new FileReader(FILENAME);
            BufferedReader br = new BufferedReader(fr);

            List<ScoreData> scoring = new ArrayList<>();

            String thisLine;
            while ((thisLine = br.readLine()) != null) {
                scoring.add(new ScoreData(thisLine));
            }
            br.close();

            return scoring;
        }

        public static void write(List<ScoreData> scoring) throws IOException {
            FileWriter fw = new FileWriter(FILENAME);
            BufferedWriter bw = new BufferedWriter(fw);

            if(scoring.size() >= 10) {
                for (int i = 10; i < scoring.size(); i++) {
                    scoring.remove(i);
                }
            }

            for (int i = 0; i < scoring.size(); i++) {
                bw.write(scoring.get(i).serialize() + "\n");
            }
            bw.close();
        }

}

