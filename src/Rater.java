import java.util.List;
import java.util.Random;

class Rater {
    private static final Random random = new Random();

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

        for (int i = 0; i < home; i++) {
            final int player = random.nextInt(11);
            Match.homeSquad.get(player).changeRating(0.1f);
        }

        for (int i = 0; i > home; i--) {
            final int player = random.nextInt(11);
            Match.homeSquad.get(player).changeRating(-0.1f);
        }

        for (int i = 0; i < away; i++) {
            final int player = random.nextInt(11);
            Match.awaySquad.get(player).changeRating(0.1f);
        }

        for (int i = 0; i > away; i--) {
            final int player = random.nextInt(11);
            Match.awaySquad.get(player).changeRating(-0.1f);
        }
    }

    static void goal(final int homeGoals, final int awayGoals, final boolean isHome) {
        int scoring = 30;
        int assisting = 200;
        Footballer goalscorer = null;
        Footballer assistmaker = null;
        final List<MatchStats> squad = isHome ? Match.homeSquad : Match.awaySquad;

        for (int player = 0; player < 11; player++) {
            //TODO: Red-carded checks
            scoring += scoringChance(squad.get(player).getFootballer());
            assisting += assistingChance(squad.get(player).getFootballer());
        }

        int r = random.nextInt(scoring);
        for (int player = 0; player < 11; player++) {
            r -= scoringChance(squad.get(player).getFootballer());
            if (r < 0) {
                goalscorer = squad.get(player).getFootballer();
                squad.get(player).addGoals();
                break;
            }
        }

        if (goalscorer == null) {
            // TODO: Red card for the goalkeeper
            final int footballer = ownGoal();
            if (PremierLeague.matchFlag) System.out.println(Match.minute + (Match.stoppage != 0 ? "+" + Match.stoppage : "") + "' " +
                    "Own goal scored by " + squad.get(footballer) + ". " + homeGoals + "-" + awayGoals);
        }
        else {
            r = random.nextInt(assisting);
            for (int player = 0; player < 11; player++) {
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

            if (PremierLeague.matchFlag) System.out.println(Match.minute + (Match.stoppage != 0 ? "+" + Match.stoppage : "") + "' " +
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
        MatchStats motmPlayer = Match.homeSquad.get(0);
        float motmRating = 0;
        if (awayGoals == 0) getCompetition(Match.homeSquad.get(0).getFootballer().getResume().getSeason(), type).addCleanSheets(1);
        if (homeGoals == 0) getCompetition(Match.awaySquad.get(0).getFootballer().getResume().getSeason(), type).addCleanSheets(1);

        for (int player = 0; player < 11; player++) {
            if (Match.homeSquad.get(player).getRating() > motmRating) {
                motmPlayer = Match.homeSquad.get(player);
                motmRating = Match.homeSquad.get(player).getRating();
            }

            final Competition homeStats = getCompetition(Match.homeSquad.get(player).getFootballer().getResume().getSeason(), type);
            homeStats.addRating((int) Match.homeSquad.get(player).getRating() * 100, 1);
            homeStats.addMatches(1);
            Match.homeSquad.get(player).getFootballer().changeCondition(-15);

            if (Match.awaySquad.get(player).getRating() > motmRating) {
                motmPlayer = Match.awaySquad.get(player);
                motmRating = Match.awaySquad.get(player).getRating();
            }

            final Competition awayStats = getCompetition(Match.awaySquad.get(player).getFootballer().getResume().getSeason(), type);
            awayStats.addRating((int) Match.awaySquad.get(player).getRating() * 100, 1);
            awayStats.addMatches(1);
            Match.awaySquad.get(player).getFootballer().changeCondition(-15);

            Data.RATINGS += Match.homeSquad.get(player).getRating() + Match.awaySquad.get(player).getRating();
        }

        getCompetition(motmPlayer.getFootballer().getResume().getSeason(), type).addMotmAwards(1);

        if (type == 0) updateLeague(home, away, homeGoals, awayGoals);
        updateForm(home, away, homeGoals, awayGoals);

        System.out.println(String.format("%s - %s %d:%d   --- %s %.2f", home.getName(),
                away.getName(), homeGoals,
                awayGoals, motmPlayer.getFootballer().getName(), motmRating));
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
