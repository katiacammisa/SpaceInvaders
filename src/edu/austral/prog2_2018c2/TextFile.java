package edu.austral.prog2_2018c2;


    import java.io.BufferedWriter;
    import java.io.FileWriter;
    import java.io.IOException;


public class TextFile {

        private static final String FILENAME = "D:\\AleGG\\SpaceInvaders\\Scores.txt";


        public void run(int score) {

            BufferedWriter bw = null;
            FileWriter fw = null;


            try {

                String content = Scanner.getString("Enter your name and your surname: ") + " " + score;

                fw = new FileWriter(FILENAME);
                bw = new BufferedWriter(fw);
                bw.write(content);

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

