package simulation;

import players.Competition;
import players.Footballer;
import players.MatchStats;
import players.Position;
import players.Statistics;
import teams.Club;
import teams.League;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static simulation.Data.AWAY_WINS;
import static simulation.Data.HOME_WINS;
import static simulation.Utils.sortMap;

class Rater {
    private static final Random random = new Random();

    private static MatchStats motmPlayer;
    private static float motmRating;
    static Map<Footballer, Integer> contenders;

    static void kickoff(final Club home, final Club away) {
        for (final Footballer f : home.getFootballers()) {
            if (f.getPosition() == Position.GK) {
                f.changeCondition(13 + random.nextInt(3));
            }
            else f.changeCondition(12 + random.nextInt(3));
        }

        for (final Footballer f : away.getFootballers()) {
            if (f.getPosition() == Position.GK) {
                f.changeCondition(13 + random.nextInt(3));
            }
            else f.changeCondition(12 + random.nextInt(3));
        }
    }
    static void updateRatings(final int scale) {
        final int home = random.nextInt(5) + scale * 3 + 1;
        final int away = random.nextInt(5) - scale * 3 + 1;

        for (int i = 0; i < home; i++) updateRating(Match.homeSquad, 0.1f);
        for (int i = 0; i > home; i--) updateRating(Match.homeSquad, -0.1f);
        for (int i = 0; i < away; i++) updateRating(Match.awaySquad, 0.1f);
        for (int i = 0; i > away; i--) updateRating(Match.awaySquad, -0.1f);
    }

    private static void updateRating(final List<MatchStats> squad, final float change) {
        final int player = random.nextInt(11);
        float overall = squad.get(player).getFootballer().getOverall() *
                squad.get(player).getFootballer().getOverall() / 6000;

        if (change < 0) overall = 1 / overall;
        squad.get(player).changeRating(change * overall);
    }

    static void finalWhistle(final Club home, final Club away, final int homeGoals, final int awayGoals) {
        if (awayGoals == 0) getCompetition(Match.homeSquad.get(0).getFootballer().getResume()
                .getSeason(), Match.competition).addCleanSheets(1);
        if (homeGoals == 0) getCompetition(Match.awaySquad.get(0).getFootballer().getResume()
                .getSeason(), Match.competition).addCleanSheets(1);

        motmPlayer = Match.homeSquad.get(0);
        motmRating = 0;

        for (int player = 0; player < 11; player++) {
            updateStats(Match.homeSquad.get(player));
            updateStats(Match.awaySquad.get(player));
        }

        getCompetition(motmPlayer.getFootballer().getResume().getSeason(), Match.competition).addMotmAwards(1);

        if (Match.competition == 0) updateLeague(home, away, homeGoals, awayGoals);
        updateForm(home, away, homeGoals, awayGoals);

        System.out.println(String.format("%s - %s %d:%d   --- %s %.2f", home.getName(),
                away.getName(), homeGoals, awayGoals, motmPlayer.getFootballer().getName(), motmRating));
    }

    private static void updateStats(final MatchStats matchStats) {
        if (matchStats.getRating() > motmRating) {
            motmPlayer = matchStats;
            motmRating = matchStats.getRating();
        }

        final Competition homeStats = getCompetition(matchStats.getFootballer().getResume().getSeason(), Match.competition);
        homeStats.addRating((int) matchStats.getRating() * 100, 1);
        homeStats.addMatches(1);
        homeStats.addGoals(matchStats.getGoals());
        homeStats.addAssists(matchStats.getAssists());
        if (matchStats.isYellowCarded()) homeStats.addYellowCards(1);
        if (matchStats.isRedCarded()) homeStats.addRedCards(1);
        matchStats.getFootballer().changeCondition(-15);
    }

    private static void updateLeague(final Club home, final Club away, final int homeGoals, final int awayGoals) {
        final League homeStats = home.getSeason().getLeague();
        final League awayStats = away.getSeason().getLeague();

        if (awayGoals == 0) homeStats.addCleanSheet();
        if (homeGoals == 0) awayStats.addCleanSheet();

        if (homeGoals > awayGoals) {
            HOME_WINS++;
            homeStats.addPoints(3);
            homeStats.addWin();
            awayStats.addLoss();
        } else if (homeGoals < awayGoals) {
            AWAY_WINS++;
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

    static Competition getCompetition(final Statistics season, final int type) {
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

    static void topPlayers(final Club[] clubs) {
        contenders = new HashMap<>();

        for (int i = 0; i < clubs.length; i++) {
            for (Footballer footballer : clubs[i].getFootballers()) {
                Statistics season = footballer.getResume().getSeason();
                int matches = season.getLeague().getMatches() + season.getCup().getMatches() * 2 +
                        season.getContinental().getMatches() * 5;
                float ratings = season.getLeague().getRating() * season.getLeague().getMatches() +
                        season.getCup().getRating() * season.getCup().getMatches() * 2 +
                        season.getContinental().getRating() * season.getContinental().getMatches() * 5;

                float performance = matches > 50 ? (ratings / ((float) matches)) : 0;

                if (i == 0) performance += 50;
                else if (i == 1) performance += 35;
                else if (i < 4) performance += 20;
                else if (i < 8) performance += 10;

                contenders.put(footballer, (int) performance);
            }
        }

        final Map<Footballer, Integer> sorted = sortMap(contenders);
        contenders.clear();

        for (int player = 0; player < 10; player++) {
            contenders.put(sorted.keySet().toArray(new Footballer[0])[player],
                    sorted.values().toArray(new Integer[0])[player]);
        }
    }
}
