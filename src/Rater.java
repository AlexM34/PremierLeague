import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.stream.IntStream;

class Rater {
    private static final Random random = new Random();

    static final float[] homeRatings = new float[11];
    static final float[] awayRatings = new float[11];
    private static final Footballer[] goalscorers = new Footballer[20];
    private static final Footballer[] assistmakers = new Footballer[20];
    static Queue<Footballer> yellows = new LinkedList<>();
    static final Queue<Footballer> reds = new LinkedList<>();

    static void kickoff(final Club home, final Club away) {
        for (final Footballer f : home.getFootballers()) {
            if (f.getPosition() == Position.GK) {
                f.changeCondition(13 + random.nextInt(2));
            }
            else f.changeCondition(11 + random.nextInt(3));
            // TODO: Short bench solution with fatigue
        }

        for (final Footballer f : away.getFootballers()) {
            if (f.getPosition() == Position.GK) {
                f.changeCondition(13 + random.nextInt(2));
            }
            else f.changeCondition(11 + random.nextInt(3));
        }

        IntStream.range(0, 11).forEach(player -> {
            homeRatings[player] = 6;
            awayRatings[player] = 6;
        });

        IntStream.range(0, 20).forEach(player -> {
            goalscorers[player] = null;
            assistmakers[player] = null;
        });

        yellows = new LinkedList<>();
    }
    static void updateRatings(final int scale) {
        final int home = random.nextInt(5) + scale * 3 + 1;
        final int away = random.nextInt(5) - scale * 3 + 1;

        for (int i = 0; i < home; i++) {
            final int player = random.nextInt(11);
            if (Match.bookings[0][player] < 2) homeRatings[player] += 0.1;
        }

        for (int i = 0; i > home; i--) {
            final int player = random.nextInt(11);
            if (Match.bookings[0][player] < 2) homeRatings[player] -= 0.1;
        }

        for (int i = 0; i < away; i++) {
            final int player = random.nextInt(11);
            if (Match.bookings[1][player] < 2) awayRatings[player] += 0.1;
        }

        for (int i = 0; i > away; i--) {
            final int player = random.nextInt(11);
            if (Match.bookings[1][player] < 2) awayRatings[player] -= 0.1;
        }
    }

    static void goal(final int minute, final int homeGoals, final int awayGoals, final boolean isHome) {
        int scoring = 30;
        int assisting = 200;
        Footballer goalscorer = null;
        Footballer assistmaker = null;
        final Footballer[] squad = isHome ? Match.homeSquad : Match.awaySquad;

        for (int player = 0; player < 11; player++) {
            if (Match.bookings[isHome ? 0 : 1][player] < 2) {
                scoring += scoringChance(squad[player]);
                assisting += assistingChance(squad[player]);
            }
        }

        int r = random.nextInt(scoring);
        for (int player = 0; player < 11; player++) {
            r -= (Match.bookings[isHome ? 0 : 1][player] < 2 ? scoringChance(squad[player]) : 0);
            if (r < 0) {
                goalscorer = squad[player];

                float rating = 1.25f;
                if (goalscorer.getPosition().getAttackingDuty() < 3) rating += 0.5f;
                else if (goalscorer.getPosition().getAttackingDuty() < 5) rating += 0.25f;

                (isHome ? homeRatings : awayRatings)[player] += rating;
                break;
            }
        }

        if (goalscorer == null) {
            // TODO: Red card for the goalkeeper
            int footballer = ownGoal(isHome ? Match.awaySquad : Match.homeSquad);
            if (Match.bookings[isHome ? 0 : 1][footballer] >= 2) footballer = 0;
            if (PremierLeague.matchFlag) System.out.println(minute + "' " + "Own goal scored by " + footballer + ". " + homeGoals + "-" + awayGoals);
        }
        else {
            r = random.nextInt(assisting);
            for (int player = 0; player < 11; player++) {
                r -= (Match.bookings[isHome ? 0 : 1][player] < 2 ? assistingChance(squad[player]) : 0);
                if (r < 0) {
                    assistmaker = squad[player];
                    if (assistmaker.equals(goalscorer)) {
                        assistmaker = null;
                        (isHome ? homeRatings : awayRatings)[player] += 0.25f;
                    } else {
                        float rating = 1;
                        if (assistmaker.getPosition().getAttackingDuty() < 3) rating += 0.5f;
                        else if (assistmaker.getPosition().getAttackingDuty() < 5) rating += 0.25f;

                        (isHome ? homeRatings : awayRatings)[player] += rating;
                    }

                    break;
                }
            }

            if (PremierLeague.matchFlag) System.out.println(minute + "' " + goalscorer.getName() +
                    (assistmaker != null ? " scores after a pass from " + assistmaker.getName()
                            : " scores after a solo run") + ". " + homeGoals + "-" + awayGoals);
        }

        goalscorers[homeGoals + awayGoals - 1] = goalscorer;
        assistmakers[homeGoals + awayGoals - 1] = assistmaker;
    }

    private static int ownGoal(final Footballer[] footballers) {
        if (random.nextInt(10) < 2) return 0;
        if (random.nextInt(5) < 3) return 1 + random.nextInt(4);
        if (random.nextInt(10) < 7) return 5 + random.nextInt(3);
        return 8 + random.nextInt(3);
    }

    private static int scoringChance(final Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return 0;
            case Defender:
                return footballer.getFinishing() + footballer.getOverall() * 2;
            case Midfielder:
                return footballer.getFinishing() * 3 + footballer.getOverall() * 4;
            case Forward:
                return footballer.getFinishing() * 7 + footballer.getOverall() * 10;
        }

        return 0;
    }

    private static int assistingChance(final Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return footballer.getVision();
            case Defender:
                return footballer.getVision() + footballer.getOverall() * 3;
            case Midfielder:
                return footballer.getVision() * 5 + footballer.getOverall() * 8;
            case Forward:
                return footballer.getVision() * 3 + footballer.getOverall() * 5;
        }

        return 0;
    }

    static void finalWhistle(final Club home, final Club away, final int homeGoals, final int awayGoals, final int type) {
        Footballer motmPlayer = Match.homeSquad[0];
        float motmRating = 0;
        if (awayGoals == 0) getCompetition(Match.homeSquad[0].getResume().getSeason(), type).addCleanSheets(1);
        if (homeGoals == 0) getCompetition(Match.awaySquad[0].getResume().getSeason(), type).addCleanSheets(1);

        for (int player = 0; player < 11; player++) {
            homeRatings[player] = homeRatings[player] < 10 ? homeRatings[player] : 10;
            homeRatings[player] = homeRatings[player] > 4 ? homeRatings[player] : 4;
            if (homeRatings[player] > motmRating) {
                motmPlayer = Match.homeSquad[player];
                motmRating = homeRatings[player];
            }

            final Competition homeStats = getCompetition(Match.homeSquad[player].getResume().getSeason(), type);
            homeStats.addRating((int) homeRatings[player] * 100, 1);
            homeStats.addMatches(1);
            Match.homeSquad[player].changeCondition(-15);

            awayRatings[player] = awayRatings[player] < 10 ? awayRatings[player] : 10;
            awayRatings[player] = awayRatings[player] > 4 ? awayRatings[player] : 4;
            if (awayRatings[player] > motmRating) {
                motmPlayer = Match.awaySquad[player];
                motmRating = awayRatings[player];
            }

            final Competition awayStats = getCompetition(Match.awaySquad[player].getResume().getSeason(), type);
            awayStats.addRating((int) awayRatings[player] * 100, 1);
            awayStats.addMatches(1);
            Match.awaySquad[player].changeCondition(-15);

            Data.RATINGS += homeRatings[player] + awayRatings[player];
        }

        getCompetition(motmPlayer.getResume().getSeason(), type).addMotmAwards(1);

        if (type == 0) updateLeague(home, away, homeGoals, awayGoals);
        updateForm(home, away, homeGoals, awayGoals);

        for (int goal = 0; goal < homeGoals + awayGoals; goal++) {
            if (goalscorers[goal] != null) getCompetition(goalscorers[goal].getResume().getSeason(), type).addGoals(1);
            if (assistmakers[goal] != null) getCompetition(assistmakers[goal].getResume().getSeason(), type).addAssists(1);
        }

        while (!yellows.isEmpty()) {
            getCompetition(yellows.remove().getResume().getSeason(), type).addYellowCards(1);
        }

        while (!reds.isEmpty()) {
            getCompetition(reds.remove().getResume().getSeason(), type).addRedCards(1);
        }

        System.out.println(String.format("%s - %s %d:%d   --- %s %.2f", home.getName(),
                away.getName(), homeGoals,
                awayGoals, motmPlayer.getName(), motmRating));
    }

    private static void updateLeague(final Club home, final Club away, final int homeGoals, final int awayGoals) {
        final League homeStats = home.getSeason().getLeague();
        final League awayStats = away.getSeason().getLeague();

        if (awayGoals == 0) homeStats.addCleanSheet();
        if (homeGoals == 0) awayStats.addCleanSheet();

        if (homeGoals > awayGoals) {
            Data.HOME_WINS++;
            homeStats.addPoints(3);
            homeStats.addWin();
            awayStats.addLoss();
        } else if (homeGoals < awayGoals) {
            Data.AWAY_WINS++;
            awayStats.addPoints(3);
            awayStats.addWin();
            homeStats.addLoss();
        } else {
            homeStats.addPoints(1);
            awayStats.addPoints(1);
            homeStats.addDraw();
            awayStats.addDraw();
        }

        homeStats.addMatch();
        awayStats.addMatch();
        homeStats.addScored(homeGoals);
        awayStats.addScored(awayGoals);
        homeStats.addConceded(awayGoals);
        awayStats.addConceded(homeGoals);
    }

    private static Competition getCompetition(final Statistics season, final int type) {
        switch (type) {
            case 0: return season.getLeague();
            case 1: return season.getCup();
            default: return season.getContinental();
        }
    }

    private static void updateForm(final Club home, final Club away, final int homeGoals, final int awayGoals) {
        final int homeForm = home.getSeason().getForm();
        final int awayForm = away.getSeason().getForm();

        if (homeGoals > awayGoals) {
            if (homeGoals - awayGoals > 2) {
                form(home, 1);
                form(away, -1);
            }

            if (homeForm < awayForm + 10) {
                if (homeForm < awayForm) {
                    form(home, 3);
                    form(away, -3);
                } else {
                    form(home, 1);
                    form(away, -1);
                }
            }
        } else if (homeGoals < awayGoals) {
            if (awayGoals - homeGoals > 2) {
                form(away, 1);
                form(home, -1);
            }

            if (awayForm < homeForm + 10) {
                if (awayForm < homeForm) {
                    form(away, 3);
                    form(home, -3);
                } else {
                    form(away, 1);
                    form(home, -1);
                }
            }
        } else {
            if (homeForm < awayForm) {
                form(home, 2);
                form(away, -2);
            } else if (awayForm < homeForm) {
                form(away, 2);
                form(home, -2);
            }
        }
    }
    private static void form(final Club team, final int change) {
        team.getSeason().changeForm(change);
    }
}
