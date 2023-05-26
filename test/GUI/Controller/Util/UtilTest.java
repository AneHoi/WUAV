package GUI.Controller.Util;

import BE.Case;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void tooOld() {
        //Triple A

        //Arrange
        Util util = new Util();
        Case testCase1 = new Case(1, "TestCase", "TestCase nr. 1", "TestPerson", 1, "TestTech", LocalDate.of(2022, 03, 14), LocalDate.of(2022,03,14), 500);
        Case testCase2 = new Case(2, "TestCase2", "TestCase nr. 2", "TestPerson 2", 2, "TestTech2", LocalDate.of(1995, 03, 14), LocalDate.of(1995, 03, 14), 500);

        //Act
        boolean actualValue1 = util.tooOld(testCase1);
        boolean actualValue2 = util.tooOld(testCase2);
        boolean expectedTestCase1 = false;
        boolean expectedTestCase2 = true;

        //Assert
        Assertions.assertEquals(expectedTestCase1, actualValue1);
        Assertions.assertEquals(expectedTestCase2, actualValue2);
    }
}