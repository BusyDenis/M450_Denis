package ch.tbz.m450.controller;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressService addressService;

    @Test
    void createAddressReturns201() throws Exception {
        Address address = new Address(1, "Max", "Muster", "079 111 11 11", new Date());
        when(addressService.save(any(Address.class))).thenReturn(address);

        mockMvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"firstname\":\"Max\",\"lastname\":\"Muster\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastname").value("Muster"));
    }

    @Test
    void getAddressesReturnsList() throws Exception {
        when(addressService.getAll()).thenReturn(List.of(
                new Address(1, "Max", "Muster", "", new Date()),
                new Address(2, "Paul", "Aebi", "", new Date())));

        mockMvc.perform(get("/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].firstname").value("Paul"));
    }

    @Test
    void getAddressFound() throws Exception {
        when(addressService.getAddress(1)).thenReturn(Optional.of(new Address(1, "Max", "Muster", "", new Date())));

        mockMvc.perform(get("/address/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Max"));
    }

    @Test
    void getAddressNotFoundReturns404() throws Exception {
        when(addressService.getAddress(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/address/99"))
                .andExpect(status().isNotFound());
    }
}
