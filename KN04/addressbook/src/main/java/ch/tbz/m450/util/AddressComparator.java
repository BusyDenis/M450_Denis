package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;

import java.util.Comparator;
import java.util.List;

public class AddressComparator implements Comparator<Address> {

    private final List<SortField> sortFields;

    public AddressComparator() {
        this(SortField.LASTNAME, SortField.FIRSTNAME, SortField.REGISTRATION_DATE);
    }

    public AddressComparator(SortField... sortFields) {
        this.sortFields = List.of(sortFields);
    }

    @Override
    public int compare(Address a1, Address a2) {
        for (SortField field : sortFields) {
            int result = compareBy(field, a1, a2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    private int compareBy(SortField field, Address a1, Address a2) {
        return switch (field) {
            case ID -> Integer.compare(a1.getId(), a2.getId());
            case FIRSTNAME -> compareValues(a1.getFirstname(), a2.getFirstname());
            case LASTNAME -> compareValues(a1.getLastname(), a2.getLastname());
            case PHONENUMBER -> compareValues(a1.getPhonenumber(), a2.getPhonenumber());
            case REGISTRATION_DATE -> compareValues(a1.getRegistrationDate(), a2.getRegistrationDate());
        };
    }

    private <T extends Comparable<T>> int compareValues(T value1, T value2) {
        if (value1 == null && value2 == null) {
            return 0;
        }
        if (value1 == null) {
            return 1;
        }
        if (value2 == null) {
            return -1;
        }
        return value1.compareTo(value2);
    }
}
