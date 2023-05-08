package io.mkolodziejczyk92.data.controllers;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import io.mkolodziejczyk92.data.entity.Client;
import io.mkolodziejczyk92.data.entity.Purchase;
import io.mkolodziejczyk92.data.entity.Supplier;
import io.mkolodziejczyk92.data.service.ClientService;
import io.mkolodziejczyk92.data.service.ContractService;
import io.mkolodziejczyk92.data.service.PurchaseService;
import io.mkolodziejczyk92.data.service.SupplierService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PurchaseAddFormViewController {

    private final PurchaseService purchaseService;
    private final ClientService clientService;
    private final SupplierService supplierService;
    private final ContractService contractService;
    private  Binder<Purchase> binder;

    public PurchaseAddFormViewController(PurchaseService purchaseService, ClientService clientService, SupplierService supplierService, ContractService contractService) {
        this.purchaseService = purchaseService;
        this.clientService = clientService;
        this.supplierService = supplierService;
        this.contractService = contractService;
    }


    public void initBinder(Binder<Purchase> binder) {
        this.binder = binder;
    }

    public void returnToPurchases() {
        UI.getCurrent().navigate("purchases");
    }

    public void saveNewPurchase(Purchase purchase){
        purchaseService.save(purchase);
    }
    public void clearForm() {
        this.binder.setBean(new Purchase());
    }
    public List<Client> allClients() {
        return clientService.allClients();
    }

    public List<Supplier> allSuppliers() {
        return supplierService.allSuppliers();
    }
    public void updatePurchase(Purchase purchase) {
        purchaseService.update(purchase);
        Notification.show("Purchase updated.");
        UI.getCurrent().navigate("purchases");
    }
    public Purchase findPurchaseById(Long purchaseId) {
        return purchaseService.get(purchaseId).orElseThrow();
    }

    public Client findClientById(Long clientId){
        return clientService.get(clientId).orElseThrow();
    }

}
