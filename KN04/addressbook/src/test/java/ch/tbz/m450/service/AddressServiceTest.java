package ch.tbz.m450.service;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    private AddressService addressService;
    private Address maxMuster;
    private Address paulAebi;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(addressRepository);
        maxMuster = new Address(1, "Max", "Muster", "079 111 11 11", new Date());
        paulAebi = new Address(2, "Paul", "Aebi", "079 222 22 22", new Date());
    }

    @Test
    void saveCallsRepository() {
        when(addressRepository.save(maxMuster)).thenReturn(maxMuster);

        Address saved = addressService.save(maxMuster);

        assertEquals(maxMuster, saved);
        verify(addressRepository).save(maxMuster);
    }

    @Test
    void getAllReturnsSortedList() {
        when(addressRepository.findAll()).thenReturn(List.of(maxMuster, paulAebi));

        List<Address> result = addressService.getAll();

        assertEquals(List.of(paulAebi, maxMuster), result);
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void getAllWithEmptyDatabase() {
        when(addressRepository.findAll()).thenReturn(List.of());

        assertTrue(addressService.getAll().isEmpty());
    }

    @Test
    void getAddressFound() {
        when(addressRepository.findById(1)).thenReturn(Optional.of(maxMuster));

        Optional<Address> result = addressService.getAddress(1);

        assertTrue(result.isPresent());
        assertEquals("Muster", result.get().getLastname());
    }

    @Test
    void getAddressNotFound() {
        when(addressRepository.findById(99)).thenReturn(Optional.empty());

        assertTrue(addressService.getAddress(99).isEmpty());
    }

    @Test
    void serviceDoesNotDeleteAnything() {
        addressService.getAddress(1);

        verify(addressRepository, never()).deleteById(anyInt());
    }
}
