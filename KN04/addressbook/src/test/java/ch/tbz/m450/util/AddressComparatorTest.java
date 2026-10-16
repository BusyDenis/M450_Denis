package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressComparatorTest {

    private AddressComparator comparator;
    private Address maxMuster;
    private Address annaMuster;
    private Address paulAebi;

    @BeforeEach
    void setUp() {
        comparator = new AddressComparator();
        maxMuster = new Address(1, "Max", "Muster", "079 111 11 11", new Date(3000));
        annaMuster = new Address(2, "Anna", "Muster", "079 333 33 33", new Date(2000));
        paulAebi = new Address(3, "Paul", "Aebi", "079 222 22 22", new Date(1000));
    }

    @Test
    void sameAddressIsEqual() {
        assertEquals(0, comparator.compare(maxMuster, maxMuster));
    }

    @Test
    void compareByLastname() {
        assertTrue(comparator.compare(paulAebi, maxMuster) < 0);
        assertTrue(comparator.compare(maxMuster, paulAebi) > 0);
    }

    @Test
    void sameLastnameComparesFirstname() {
        assertTrue(comparator.compare(annaMuster, maxMuster) < 0);
    }

    @Test
    void sameNameComparesRegistrationDate() {
        Address older = new Address(4, "Max", "Muster", "", new Date(1000));

        assertTrue(comparator.compare(older, maxMuster) < 0);
    }

    @Test
    void nullValuesAreSortedLast() {
        Address withoutName = new Address(5, null, null, null, null);

        assertTrue(comparator.compare(maxMuster, withoutName) < 0);
        assertTrue(comparator.compare(withoutName, maxMuster) > 0);
        assertEquals(0, comparator.compare(withoutName, new Address(6, null, null, null, null)));
    }

    @Test
    void sortListByDefault() {
        List<Address> addresses = new ArrayList<>(List.of(maxMuster, paulAebi, annaMuster));

        addresses.sort(comparator);

        assertEquals(List.of(paulAebi, annaMuster, maxMuster), addresses);
    }

    @Test
    void sortByFirstname() {
        List<Address> addresses = new ArrayList<>(List.of(maxMuster, paulAebi, annaMuster));

        addresses.sort(new AddressComparator(SortField.FIRSTNAME));

        assertEquals(List.of(annaMuster, maxMuster, paulAebi), addresses);
    }

    @Test
    void sortByPhonenumber() {
        List<Address> addresses = new ArrayList<>(List.of(annaMuster, paulAebi, maxMuster));

        addresses.sort(new AddressComparator(SortField.PHONENUMBER));

        assertEquals(List.of(maxMuster, paulAebi, annaMuster), addresses);
    }

    @Test
    void sortByRegistrationDate() {
        List<Address> addresses = new ArrayList<>(List.of(maxMuster, annaMuster, paulAebi));

        addresses.sort(new AddressComparator(SortField.REGISTRATION_DATE));

        assertEquals(List.of(paulAebi, annaMuster, maxMuster), addresses);
    }

    @Test
    void sortById() {
        List<Address> addresses = new ArrayList<>(List.of(paulAebi, maxMuster, annaMuster));

        addresses.sort(new AddressComparator(SortField.ID));

        assertEquals(List.of(maxMuster, annaMuster, paulAebi), addresses);
    }

    @Test
    void sortByMultipleFields() {
        Address annaMeier = new Address(7, "Anna", "Meier", "", new Date(500));
        List<Address> addresses = new ArrayList<>(List.of(maxMuster, annaMuster, annaMeier));

        addresses.sort(new AddressComparator(SortField.FIRSTNAME, SortField.LASTNAME));

        assertEquals(List.of(annaMeier, annaMuster, maxMuster), addresses);
    }
}
