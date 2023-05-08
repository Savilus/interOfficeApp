package io.mkolodziejczyk92.data.controllers;

import com.vaadin.flow.component.UI;
import io.mkolodziejczyk92.data.entity.Address;
import io.mkolodziejczyk92.data.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class AddressesViewController {

    private final AddressService addressService;

    public AddressesViewController(AddressService addressService) {
        this.addressService = addressService;
    }

    public List<Address> allAddresses() {
        return addressService.allAddresses();

    }
    public void createNewAddressFormForClient(String clientId) {
        if(Objects.isNull(clientId)){
            UI.getCurrent().navigate("new-address");
        } else {
            UI.getCurrent().navigate("new-address/" + clientId);
        }
    }
    public List<Address> clientAddresses(Long clientId) {
        return addressService.clientAddresses(clientId);
    }
}

