import java.util.stream.IntStream;

public class Data {
    static String[] TEAMS = {"Arsenal", "Manchester City", "Liverpool", "Manchester United",
            "Chelsea", "Tottenham", "Everton", "Leicester", "Wolverhampton", "Watford", "West Ham", "Bournemouth",
            "Crystal Palace", "Burnley", "Newcastle United", "Southampton", "Brighton", "Cardiff", "Fulham", "Huddersfield"};
    static String[][] PLAYERS = {
            {"Leno", "Bellerin", "Sokratis", "Koscielny", "Kolasinac",
                    "Torreira", "Ramsey", "Xhaka", "Mkhitaryan", "Lacazette", "Aubameyang"},
            {"Ederson", "Walker", "Kompany", "Laporte", "Mendy",
                    "Fernandinho", "David Silva", "De Bruyne", "Sane", "Sterling", "Aguero"},
            {"Alisson", "Alexander-Arnold", "Lovren", "van Dijk", "Robertson",
                    "Fabinho", "Milner", "Keita", "Mane", "Salah", "Firmino"},
            {"De Gea", "Young", "Bailly", "Lindelof", "Shaw",
                    "Herrera", "Matic", "Pogba", "Lingard", "Rashford", "Lukaku"},
            {"Kepa", "Azpilicueta", "Rudiger", "Luiz", "Alonso",
                    "Kante", "Jorginho", "Barkley", "Willian", "Hazard", "Higuain"},
            {"Lloris", "Trippier", "Alderweireld", "Vertonghen", "Rose",
                    "Dier", "Sissoko", "Eriksen", "Alli", "Son", "Kane"},
            {"Pickford", "Coleman", "Keane", "Zouma", "Digne",
                    "Gueye", "Gomes", "Richarlison", "Bernard", "Sigurdsson", "Calvert-Lewin"},
            {"Schmeichel", "Pereira", "Evans", "Maguire", "Chillwell",
                    "Ndidi", "Tielemans", "Maddison", "Gray", "Barnes", "Vardy"},
            {"Rui Patricio", "Doherty", "Bennett", "Coady", "Boly",
                    "Jonny", "Dendoncker", "Neves", "Moutinho", "Jota", "Jimenez"},
            {"Foster", "Janmaat", "Cathcart", "Kabasele", "Holebas",
                    "Doucoure", "Capoue", "Hughes", "Pereyra", "Deulofeu", "Deeney"},
            {"Fabianski", "Zabaleta", "Diop", "Ogbonna", "Cresswell",
                    "Rice", "Snodgrass", "Noble", "Lanzini", "Felipe Anderson", "Arnautovic"},
            {"Boruc", "Clyne", "Mepham", "Ake", "Smith",
                    "Brooks", "Surman", "Lerma", "Fraser", "King", "Wilson"},
            {"Guaita", "Wan-Bissaka", "Tomkins", "Dann", "van Aanholt",
                    "Milivojevic", "Kouyate", "Schlupp", "Townsend", "Batshuayi", "Zaha"},
            {"Heaton", "Bardsley", "Tarkowski", "Mee", "Taylor",
                    "Gudmundsson", "Westwood", "Cork", "Brady", "Wood", "Barnes"},
            {"Dubravka", "Yedlin", "Schar", "Lascelles", "Dummett",
                    "Ritchie", "Yueng", "Hayden", "Perez", "Almiron", "Rondon"},
            {"Gunn", "Valery", "Bednarek", "Stephens", "Vestergaard",
                    "Targett", "Ward-Prowse", "Romeu", "Hojbjerg", "Redmond", "Ings"},
            {"Ryan", "Montoya", "Duffy", "Dunk", "Bernardo",
                    "Stephens", "Gros", "Propper", "Knockaert", "Jahanbakhsh", "Murray"},
            {"Etheridge", "Peltier", "Morrison", "Manga", "Bennett",
                    "Gunnarsson", "Arter", "Murphy", "Hoilett", "Camarasa", "Niasse"},
            {"Rico", "Odoi", "Nordtveit", "Ream", "Bryan",
                    "McDonald", "Seri", "Babel", "Sessegnon", "Cairney", "Mitrovic"},
            {"Lossl", "Hadergjonaj", "Jorgensen", "Schindler", "Durm",
                    "Hogg", "Bacuna", "Mooy", "Puncheon", "Pritchard", "Mounie"}
    };
    static int[][] OVERALL = {
            {8, 7, 8, 7, 7, 8, 8, 7, 7, 9, 10},
            {9, 8, 8, 9, 7, 9, 9, 10, 9, 9, 10},
            {9, 7, 7, 10, 8, 9, 7, 8, 9, 10, 9},
            {10, 7, 7, 7, 7, 8, 8, 10, 8, 8, 9},
            {8, 8, 7, 8, 8, 10, 8, 7, 8, 10, 9},
            {9, 7, 9, 9, 7, 7, 7, 9, 8, 9, 10},
            {9, 7, 7, 7, 7, 8, 7, 7, 7, 8, 6},
            {8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 7},
            {8, 6, 5, 5, 6, 7, 7, 7, 8, 7, 8},
            {7, 7, 6, 7, 7, 7, 7, 7, 7, 7, 7},
            {8, 6, 7, 7, 6, 6, 7, 6, 7, 8, 7},
            {6, 7, 4, 7, 6, 6, 6, 7, 7, 7, 7},
            {7, 7, 7, 6, 6, 7, 6, 6, 7, 7, 7},
            {7, 4, 7, 7, 6, 7, 6, 6, 6, 7, 7},
            {7, 6, 6, 7, 6, 7, 7, 5, 6, 7, 7},
            {6, 3, 6, 6, 7, 5, 7, 7, 7, 6, 6},
            {7, 7, 7, 7, 6, 6, 7, 7, 6, 7, 7},
            {6, 4, 6, 6, 5, 5, 6, 5, 6, 6, 5},
            {8, 4, 6, 5, 6, 6, 6, 7, 6, 7, 7},
            {6, 6, 6, 7, 6, 6, 5, 7, 5, 6, 6},
    };

    // TODO: Add logos
    // TODO: Club stats
    // TODO: Age and effect on ratings
    // TODO: Goal and assist stats
    // TODO: Coach decisions
    // TODO: Add subs
    // TODO: Add positions
    // TODO: Add fatigue
    static int[] TITLES = {13, 5, 18, 20, 6, 2, 9, 1, 3, 0, 0, 0, 0, 2, 4, 0, 0, 0, 0, 3};
    static int[] POINTS = new int[20];
    static int[] GOALS_FOR = new int[20];
    static int[] GOALS_AGAINST = new int[20];
    static int[] GAMES = new int[20];
    static int[] WINS = new int[20];
    static int[] DRAWS = new int[20];
    static int[] LOSES = new int[20];
    static int[][] HOME = new int[38][10];
    static int[][] AWAY = new int[38][10];
    static int FANS = 7;
    static int[] FORM = new int[20];
    static float[][] RATINGS = new float[20][11];
    static int[][] GOALS = new int[20][11];
    static int[][] ASSISTS = new int[20][11];
    static int[] CLEAN_SHEETS = new int[20];

    static void prepare() {
        Data.POINTS = new int[20];
        Data.GOALS_FOR = new int[20];
        Data.GOALS_AGAINST = new int[20];
        Data.GAMES = new int[20];
        Data.WINS = new int[20];
        Data.DRAWS = new int[20];
        Data.LOSES = new int[20];
        Data.HOME = new int[38][10];
        Data.AWAY = new int[38][10];
        Data.FORM = new int[20];
        Data.RATINGS = new float[20][11];
        Data.GOALS = new int[20][11];
        Data.ASSISTS = new int[20][11];
        Data.CLEAN_SHEETS = new int[20];

        System.out.println("The Premier League begins!");
        for (int i = 0; i < 20; i++) {
            Data.FORM[i] = 10;
            System.out.println(String.format("%s %d", Data.TEAMS[i], IntStream.of(Data.OVERALL[i]).sum()));
        }

        System.out.println();
        PremierLeague.pause();
    }
}
