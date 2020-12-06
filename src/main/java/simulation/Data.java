package simulation;

import static java.nio.charset.StandardCharsets.UTF_8;

import league.LeagueManager;
import player.Footballer;
import player.Position;
import player.Resume;
import team.Club;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Data {

    private static final PrintStream STREAM = new PrintStream(new FileOutputStream(FileDescriptor.out));
    public static final Club[][] LEAGUES = LeagueManager.getAllLeagues();
    public static final Integer USER = -1;
    public static int userStyle;

    public static final String[] SURNAMES = {"Johnson", "Smith", "Rodriguez", "Traore", "Taylor",  "Pelle",  "Dembele",
            "Alba",  "Benitez", "Sterling",  "Martinez",  "Suarez",  "Jimenez",  "Muller",  "Gross",  "Cole"};

    public static final String[] NATIONS = {"France", "England", "Italy", "Spain", "Germany",  "Argentina",  "Brazil",
            "Portugal",  "Netherlands", "Belgium",  "Colombia",  "Uruguay",  "Russia",  "Nigeria",  "Ukraine",  "Austria"};

    public static final Footballer GOALKEEPER_1 = new Footballer(123455, "Goalkeeper 1", 18, "", 60, 60,
            "Dummy", 0, 0, Position.GK, 101, 20, 20, new Resume());

    public static final Footballer DEFENDER_1 = new Footballer(123456, "Defender 1", 18, "", 60, 60,
            "Dummy", 0, 0, Position.CB, 101, 20, 20, new Resume());

    public static final Footballer MIDFIELDER_1 = new Footballer(123457, "Midfielder 1", 18, "", 60, 60,
            "Dummy", 0, 0, Position.CM, 102, 20, 20, new Resume());

    public static final Footballer FORWARD_1 = new Footballer(123458, "Forward 1", 18, "", 60, 60,
            "Dummy", 0, 0, Position.ST, 103, 20, 20, new Resume());

    public static final Footballer DEFENDER_2 = new Footballer(123466, "Defender 2", 18, "", 60, 60,
            "Dummy", 0, 0, Position.CB, 104, 20, 20, new Resume());

    public static final Footballer MIDFIELDER_2 = new Footballer(123467, "Midfielder 2", 18, "", 60, 60,
            "Dummy", 0, 0, Position.CM, 105, 20, 20, new Resume());

    public static final Footballer FORWARD_2 = new Footballer(123468, "Forward 2", 18, "", 60, 60,
            "Dummy", 0, 0, Position.ST, 106, 20, 20, new Resume());

    private Data() {
    }

    static void extractData() {
        final String fileName = "data/data.csv";
        final File data = new File(fileName);
        try {
            for (final Club[] league : LEAGUES) {
                for (final Club club : league) {
                    final File file = new File("data/" + club.getName() + ".csv");
                    if (!file.delete() || !file.createNewFile()) throw new Exception("Unable to recreate file!");

                    final FileWriter write = new FileWriter(file, true);
                    final PrintWriter line = new PrintWriter(write);
                    final Scanner inputStream = new Scanner(data);
                    inputStream.nextLine();

                    while (inputStream.hasNext()) {
                        final String footballer = inputStream.nextLine();
                        final String[] values = footballer.replaceAll("\\s","").split(",");
                        if (club.getName().replaceAll("\\s","").equals(values[9])) {
                            line.printf(footballer + "\n");
                        }
                    }
                    inputStream.close();
                    line.close();
                }
            }
        } catch (final Exception e) {
            STREAM.println("Exception thrown while extracting data! " + e);
        }
    }

    static void buildSquads() {
        try {
            for (final Club[] league : LEAGUES) {
                for (final Club club : league) {
                    final String path = "src/main/resources/data/" + club.getName() + ".csv";
                    final File file = new File(path);
                    final Scanner scanner = new Scanner(file, UTF_8);

                    while (scanner.hasNextLine()) {
                        final String footballer = scanner.nextLine();
                        final String[] v = footballer.split("\"");
                        for (int i = 1; i < v.length; i += 2) {
                            v[i] = v[i].replace(",", "|");
                        }

                        final String[] values = String.join("", v).split(",");

                        if (values.length > 9 && club.getName().equals(values[9])) {
                            final int id = Integer.parseInt(values[0]);
                            final String name = values[2];
                            final int age = Integer.parseInt(values[4]);
                            final String nationality = values[8];
                            final int overall = Integer.parseInt(values[10]);
                            final int potential = Integer.parseInt(values[11]);
                            final long value = Long.parseLong(values[12]);
                            final long wage = Long.parseLong(values[13]);
                            final Position position = Position.valueOf(values[14].split("\\|")[0]);
                            final int number = Integer.parseInt(values[25]);
                            final int finishing = Integer.parseInt(values[45]);
                            final int vision = Integer.parseInt(values[67]);

                            final Footballer f = new Footballer(id, name, age, nationality, overall, potential,
                                    club.getName(), value, wage, position, number, finishing, vision, new Resume());

                            club.addFootballer(f);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            STREAM.println("Exception thrown while building squads! " + e);
        }
    }

    static void addDummies() {
        for (final Club[] league : LEAGUES) {
            for (final Club club : league) {
                club.addFootballer(GOALKEEPER_1);
                club.addFootballer(DEFENDER_1);
                club.addFootballer(MIDFIELDER_1);
                club.addFootballer(FORWARD_1);
                club.addFootballer(DEFENDER_2);
                club.addFootballer(MIDFIELDER_2);
                club.addFootballer(FORWARD_2);
            }
        }
    }
}
