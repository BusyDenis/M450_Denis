package ch.tbz.m450;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AddressbookApplicationTest {

    @Autowired
    private AddressService addressService;

    @Test
    void saveAndLoadWithRealDatabase() {
        addressService.save(new Address(1, "Max", "Muster", "", new Date()));
        addressService.save(new Address(2, "Paul", "Aebi", "", new Date()));

        List<Address> all = addressService.getAll();

        assertEquals(2, all.size());
        assertEquals("Aebi", all.get(0).getLastname());
    }
}
