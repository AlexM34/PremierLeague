package player;

import builders.FootballerBuilder;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static player.Position.CAM;
import static player.Position.CB;
import static player.Position.CM;
import static player.Position.GK;
import static player.Position.LM;
import static player.Position.LW;
import static player.Position.RB;
import static player.Position.RW;
import static player.Position.ST;

class FootballerTest {

    private Footballer footballer;

    @Test
    void changeOverall() {
        footballer = new FootballerBuilder().withOverall(83).withPotential(90).build();
        footballer.changeOverall(5);

        assertEquals(88, footballer.getOverall());
    }

    @Test
    void changeOverallPotentialReached() {
        footballer = new FootballerBuilder().withOverall(79).withPotential(81).build();
        footballer.changeOverall(4);

        assertEquals(81, footballer.getOverall());
    }

    @Test
    void changeOverallMax() {
        footballer = new FootballerBuilder().withOverall(55).withPotential(70).build();
        footballer.changeOverall(-18);

        assertEquals(50, footballer.getOverall());
    }

    @Test
    void changeOverallMin() {
        footballer = new FootballerBuilder().withOverall(94).withPotential(100).build();
        footballer.changeOverall(9);

        assertEquals(99, footballer.getOverall());
    }

    @Test
    void changePotential() {
        footballer = new FootballerBuilder().withOverall(80).withPotential(86).build();
        footballer.changePotential(3);

        assertEquals(89, footballer.getPotential());
    }

    @Test
    void changePotentialOverallReached() {
        footballer = new FootballerBuilder().withOverall(74).withPotential(75).build();
        footballer.changePotential(-2);

        assertEquals(74, footballer.getPotential());
    }

    @Test
    void changePotentialMax() {
        footballer = new FootballerBuilder().withOverall(93).withPotential(96).build();
        footballer.changePotential(5);

        assertEquals(99, footballer.getPotential());
    }

    @Test
    void changePotentialMin() {
        footballer = new FootballerBuilder().withOverall(44).withPotential(53).build();
        footballer.changePotential(-6);

        assertEquals(50, footballer.getPotential());
    }

    @Test
    void getValueProspect() {
        footballer = new FootballerBuilder().withOverall(70).withPotential(89)
                .withAge(18).withPosition(CM).build();

        assertEquals(16, footballer.getValue());
    }

    @Test
    void getValueYoungSuperstar() {
        footballer = new FootballerBuilder().withOverall(90).withPotential(94)
                .withAge(20).withPosition(ST).build();

        assertEquals(369, footballer.getValue());
    }

    @Test
    void getValuePrimeDefender() {
        footballer = new FootballerBuilder().withOverall(87).withPotential(90)
                .withAge(26).withPosition(CB).build();

        assertEquals(113, footballer.getValue());
    }

    @Test
    void getValuePrimeMidfielder() {
        footballer = new FootballerBuilder().withOverall(85).withPotential(88)
                .withAge(27).withPosition(LM).build();

        assertEquals(94, footballer.getValue());
    }

    @Test
    void getValuePrimeForward() {
        footballer = new FootballerBuilder().withOverall(88).withPotential(89)
                .withAge(28).withPosition(LW).build();

        assertEquals(101, footballer.getValue());
    }

    @Test
    void getValueGoalkeeperSuperstar() {
        footballer = new FootballerBuilder().withOverall(91).withPotential(91)
                .withAge(30).withPosition(GK).build();

        assertEquals(58, footballer.getValue());
    }

    @Test
    void getValueLegendPastPeak() {
        footballer = new FootballerBuilder().withOverall(93).withPotential(93)
                .withAge(32).withPosition(RW).build();

        assertEquals(97, footballer.getValue());
    }

    @Test
    void getValueOldSuperstar() {
        footballer = new FootballerBuilder().withOverall(86).withPotential(86)
                .withAge(35).withPosition(CM).build();

        assertEquals(34, footballer.getValue());
    }

    @Test
    void changeCondition() {
        footballer = new FootballerBuilder().build();
        footballer.changeCondition(-23);

        assertEquals(77, footballer.getCondition());
    }

    @Test
    void changeConditionMax() {
        footballer = new FootballerBuilder().build();
        footballer.changeCondition(6);

        assertEquals(100, footballer.getCondition());
    }

    @Test
    void changeConditionMin() {
        footballer = new FootballerBuilder().build();
        footballer.changeCondition(-53);

        assertEquals(50, footballer.getCondition());
    }

    @Test
    void changeBan() {
        footballer = new FootballerBuilder().build();
        footballer.changeBan(4);

        assertEquals(4, footballer.getBan());
    }

    @Test
    void changeBanMin() {
        footballer = new FootballerBuilder().build();
        footballer.changeBan(-1);

        assertEquals(0, footballer.getBan());
    }

    @Test
    void canPlay() {
        footballer = new FootballerBuilder().build();

        assertTrue(footballer.canPlay());
    }

    @Test
    void canPlayNullPosition() {
        footballer = new FootballerBuilder().withPosition(null).build();

        assertFalse(footballer.canPlay());
    }

    @Test
    void canPlayBadCondition() {
        footballer = new FootballerBuilder().build();
        footballer.changeCondition(-30);

        assertFalse(footballer.canPlay());
    }

    @Test
    void canPlayBanned() {
        footballer = new FootballerBuilder().build();
        footballer.changeBan(1);

        assertFalse(footballer.canPlay());
    }

    @Test
    void getScoringChanceGoalkeeper() {
        footballer = new FootballerBuilder().withPosition(GK).build();

        assertEquals(0, footballer.getScoringChance());
    }

    @Test
    void getScoringChanceDefenderBadFinishing() {
        footballer = new FootballerBuilder().withPosition(CB).withOverall(90).withFinishing(25).build();

        assertEquals(227, footballer.getScoringChance());
    }

    @Test
    void getScoringChanceDefenderOkayFinishing() {
        footballer = new FootballerBuilder().withPosition(RB).withOverall(80).withFinishing(60).build();

        assertEquals(258, footballer.getScoringChance());
    }

    @Test
    void getScoringChanceMidfielderOkayFinishing() {
        footballer = new FootballerBuilder().withPosition(CM).withOverall(51).withFinishing(70).build();

        assertEquals(222, footballer.getScoringChance());
    }

    @Test
    void getScoringChanceMidfielderGoodFinishing() {
        footballer = new FootballerBuilder().withPosition(CAM).withOverall(78).withFinishing(87).build();

        assertEquals(1172, footballer.getScoringChance());
    }

    @Test
    void getScoringChanceForwardOkayFinishing() {
        footballer = new FootballerBuilder().withPosition(RW).withOverall(86).withFinishing(73).build();

        assertEquals(1375, footballer.getScoringChance());
    }

    @Test
    void getScoringChanceForwardGoodFinishing() {
        footballer = new FootballerBuilder().withPosition(ST).withOverall(80).withFinishing(90).build();

        assertEquals(1750, footballer.getScoringChance());
    }

    @Test
    void getAssistChanceGoalkeeper() {
        footballer = new FootballerBuilder().withPosition(GK).withOverall(80).withVision(60).build();

        assertEquals(126, footballer.getAssistChance());
    }

    @Test
    void getAssistChanceDefenderBadVision() {
        footballer = new FootballerBuilder().withPosition(CB).withOverall(90).withVision(25).build();

        assertEquals(460, footballer.getAssistChance());
    }

    @Test
    void getAssistChanceDefenderOkayVision() {
        footballer = new FootballerBuilder().withPosition(RB).withOverall(80).withVision(60).build();

        assertEquals(750, footballer.getAssistChance());
    }

    @Test
    void getAssistChanceMidfielderOkayVision() {
        footballer = new FootballerBuilder().withPosition(CM).withOverall(51).withVision(70).build();

        assertEquals(1314, footballer.getAssistChance());
    }

    @Test
    void getAssistChanceMidfielderGoodVision() {
        footballer = new FootballerBuilder().withPosition(CAM).withOverall(78).withVision(87).build();

        assertEquals(4378, footballer.getAssistChance());
    }

    @Test
    void getAssistChanceForwardOkayVision() {
        footballer = new FootballerBuilder().withPosition(RW).withOverall(86).withVision(73).build();

        assertEquals(2512, footballer.getAssistChance());
    }

    @Test
    void getAssistChanceForwardGoodVision() {
        footballer = new FootballerBuilder().withPosition(ST).withOverall(80).withVision(90).build();

        assertEquals(3112, footballer.getAssistChance());
    }

    @Test
    void equalsVerifier() {
        EqualsVerifier.forClass(Footballer.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void toStringVerifier() {
        ToStringVerifier.forClass(Footballer.class).verify();
    }

}