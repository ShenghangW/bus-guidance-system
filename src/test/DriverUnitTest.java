import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
// Unit Test Cases for Driver Class
public class DriverUnitTest {
    // Unit Test Cases for Driver Class
    // D1 Valid driver ID should be accepted
    @Test
    void testD1_ValidDriverID_ShouldReturnTrue() {
        assertTrue(Driver.isValidDriverID("23@#abcdAB"));
    }

    // D1 Driver ID too short should be rejected
    @Test
    void testD1_TooShortID_ShouldReturnFalse() {
        assertFalse(Driver.isValidDriverID("23@#AB"));
    }

    // D1 First two chars not in range 2-9 should be rejected
    @Test
    void testD1_FirstTwoCharsNotInRange_ShouldReturnFalse() {
        assertFalse(Driver.isValidDriverID("01@#abcdAB"));
    }

    // D1 Last two chars lowercase should be rejected
    @Test
    void testD1_LastTwoCharsLowercase_ShouldReturnFalse() {
        assertFalse(Driver.isValidDriverID("23@#abcdab"));
    }

    // D1 Only one special character should be rejected
    @Test
    void testD1_OnlyOneSpecialChar_ShouldReturnFalse() {
        assertFalse(Driver.isValidDriverID("23@abcdeAB"));
    }

    // D2 Valid address should be accepted
    @Test
    void testD2_ValidAddress_ShouldReturnTrue() {
        assertTrue(Driver.isValidAddress("12|Main Street|Melbourne|VIC|Australia"));
    }

    // D2 Address missing one part should be rejected
    @Test
    void testD2_MissingPart_ShouldReturnFalse() {
        assertFalse(Driver.isValidAddress("12|Main Street|Melbourne|VIC"));
    }

    // D2 Address with empty part should be rejected
    @Test
    void testD2_EmptyPart_ShouldReturnFalse() {
        assertFalse(Driver.isValidAddress("12|Main Street||VIC|Australia"));
    }

    // D3 Valid birthdate should be accepted
    @Test
    void testD3_ValidBirthdate_ShouldReturnTrue() {
        assertTrue(Driver.isValidBirthdate("15-06-1990"));
    }

    // D3 Birthdate with slashes should be rejected
    @Test
    void testD3_WrongFormat_ShouldReturnFalse() {
        assertFalse(Driver.isValidBirthdate("15/06/1990"));
    }

    // D3 Birthdate with invalid month should be rejected
    @Test
    void testD3_InvalidMonth_ShouldReturnFalse() {
        assertFalse(Driver.isValidBirthdate("15-13-1990"));
    }

    // D4 Driver with less than 10 years can change license
    @Test
    void testD4_Under10Years_CanChangeLicense() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Light", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        assertTrue(driver.canUpdateLicense("Heavy"));
    }

    // D4 Driver with more than 10 years cannot change license
    @Test
    void testD4_Over10Years_CannotChangeLicense() {
        Driver driver = new Driver("23@#abcdAB", "John", 15,
                "Heavy", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        assertFalse(driver.canUpdateLicense("Light"));
    }

    // D4 Driver with exactly 10 years can change license
    @Test
    void testD4_Exactly10Years_CanChangeLicense() {
        Driver driver = new Driver("23@#abcdAB", "John", 10,
                "Light", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        assertTrue(driver.canUpdateLicense("Heavy"));
    }

    // D5 Updating allowed fields should return true
    @Test
    void testD5_UpdateAllowedFields_ShouldReturnTrue() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Light", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        assertTrue(driver.update("23@#abcdAB", "John", 6,
                "Heavy", "15|Queen St|Sydney|NSW|Australia", "15-06-1990"));
    }

    // D5 Trying to change driverID should return false
    @Test
    void testD5_ChangeDriverID_ShouldReturnFalse() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Light", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        assertFalse(driver.update("99@#abcdAB", "John", 6,
                "Heavy", "15|Queen St|Sydney|NSW|Australia", "15-06-1990"));
    }

    // D5 Trying to change name should return false
    @Test
    void testD5_ChangeName_ShouldReturnFalse() {
        Driver driver = new Driver("23@#abcdAB", "John", 5,
                "Light", "12|Main St|Melbourne|VIC|Australia", "15-06-1990");
        assertFalse(driver.update("23@#abcdAB", "Mike", 6,
                "Heavy", "15|Queen St|Sydney|NSW|Australia", "15-06-1990"));
    }
}