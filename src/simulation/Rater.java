package simulation;

import players.*;
import teams.Club;
import teams.League;

import java.util.*;

import static java.util.stream.Collectors.toMap;
import static simulation.Data.HOME_WINS;
import static simulation.Data.AWAY_WINS;
import static simulation.PremierLeague.matchFlag;

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
            // TODO: Short bench solution with fatigue
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

    static void goal(final int homeGoals, final int awayGoals, final boolean isHome) {
        int scoring = 10000;
        int assisting = 20000;
        Footballer goalscorer = null;
        Footballer assistmaker = null;
        final List<MatchStats> squad = isHome ? Match.homeSquad : Match.awaySquad;

        for (int player = 0; player < 11; player++) {
            if (squad.get(player).isRedCarded()) continue;
            scoring += scoringChance(squad.get(player).getFootballer());
            assisting += assistingChance(squad.get(player).getFootballer());
        }

        int r = random.nextInt(scoring);
        for (int player = 0; player < 11; player++) {
            if (squad.get(player).isRedCarded()) continue;
            r -= scoringChance(squad.get(player).getFootballer());
            if (r < 0) {
                goalscorer = squad.get(player).getFootballer();
                squad.get(player).addGoals();
                break;
            }
        }

        if (goalscorer == null) {
            int footballer = ownGoal();
            if (squad.get(footballer).isRedCarded()) footballer = 0;
            if (matchFlag) System.out.println(Match.minute + (Match.stoppage != 0 ? "+" + Match.stoppage : "") + "' " +
                    "Own goal scored by " + squad.get(footballer).getFootballer().getName() + ". " + homeGoals + "-" + awayGoals);
        }
        else {
            r = random.nextInt(assisting);
            for (int player = 0; player < 11; player++) {
                if (squad.get(player).isRedCarded()) continue;
                r -= assistingChance(squad.get(player).getFootballer());
                if (r < 0) {
                    assistmaker = squad.get(player).getFootballer();
                    if (assistmaker.equals(goalscorer)) {
                        assistmaker = null;
                        squad.get(player).changeRating(0.25f);
                    } else squad.get(player).addAssists();

                    break;
                }
            }

            if (matchFlag) System.out.println(Match.minute + (Match.stoppage != 0 ? "+" + Match.stoppage : "") + "' " +
                    goalscorer.getName() + (assistmaker != null ? " scores after a pass from " + assistmaker.getName()
                            : " scores after a solo run") + ". " + homeGoals + "-" + awayGoals);
        }
    }

    private static int ownGoal() {
        if (random.nextInt(10) < 2) return 0;
        if (random.nextInt(5) < 3) return 1 + random.nextInt(4);
        if (random.nextInt(10) < 7) return 5 + random.nextInt(3);
        return 8 + random.nextInt(3);
    }

    private static int scoringChance(final Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return 1;
            case Defender:
                return footballer.getFinishing() * footballer.getOverall();
            case Midfielder:
                return footballer.getFinishing() * footballer.getOverall() * 2;
            case Forward:
                return footballer.getFinishing() * footballer.getOverall() * 3;
        }

        return 0;
    }

    private static int assistingChance(final Footballer footballer) {
        switch (footballer.getPosition().getRole()) {
            case Goalkeeper:
                return footballer.getVision() * 10;
            case Defender:
                return footballer.getVision() * footballer.getOverall();
            case Midfielder:
                return footballer.getVision() * footballer.getOverall() * 3;
            case Forward:
                return footballer.getVision() * footballer.getOverall() * 2;
        }

        return 0;
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

    public static void topPlayers(final Club[] clubs) {
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

        final Map<Footballer, Integer> sorted = contenders.entrySet().stream().sorted(
                Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));

        contenders.clear();

        for (int player = 0; player < 10; player++) {
            contenders.put(sorted.keySet().toArray(new Footballer[0])[player],
                    sorted.values().toArray(new Integer[0])[player]);
        }
    }
}
