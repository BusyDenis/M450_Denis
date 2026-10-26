package ch.tbz.lb2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO A5 (TDD): Diese Tests sind fertig und sind die Anforderungen. Im Moment sind sie ROT.
//  Implementiere PasswordValidator.isValid() in src/main/java, bis alle GRÜN sind.
//  Red -> Green -> Refactor. Die Tests NICHT ändern.
class A5_TddPasswordTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @Test
    void validPassword() {
        assertTrue(validator.isValid("Sommer2026"));
    }

    @Test
    void nullIsInvalid() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void sevenCharsIsTooShort() {
        assertFalse(validator.isValid("Abc1234"));
    }

    @Test
    void eightCharsIsValid() {
        assertTrue(validator.isValid("Abcdef12"));
    }

    @Test
    void needsDigit() {
        assertFalse(validator.isValid("Sommerzeit"));
    }

    @Test
    void needsUppercase() {
        assertFalse(validator.isValid("sommer2026"));
    }

    @Test
    void noSpacesAllowed() {
        assertFalse(validator.isValid("Sommer 2026"));
    }
}
