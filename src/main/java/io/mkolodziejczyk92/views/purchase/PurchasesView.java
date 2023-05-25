package io.mkolodziejczyk92.views.purchase;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import io.mkolodziejczyk92.data.controllers.PurchasesViewController;
import io.mkolodziejczyk92.data.entity.Purchase;
import io.mkolodziejczyk92.utils.ComponentFactory;
import io.mkolodziejczyk92.views.MainLayout;
import jakarta.annotation.security.PermitAll;

import java.util.Objects;

import static io.mkolodziejczyk92.utils.ComponentFactory.*;

@PageTitle("Purchases")
@Route(value = "purchases", layout = MainLayout.class)
@PermitAll
public class PurchasesView extends Div implements HasUrlParameter<String> {

    private final PurchasesViewController purchasesViewController;
    private final PurchaseFilter purchaseFilter = new PurchaseFilter();
    private final PurchaseDataProvider purchaseDataProvider = new PurchaseDataProvider();
    private final ConfigurableFilterDataProvider<Purchase, Void, PurchaseFilter> filterDataProvider
            = purchaseDataProvider.withConfigurableFilter();
    private final Button newPurchase = createStandardButton("New Purchase");

    private final Grid<Purchase> grid = new Grid<>(Purchase.class, false);
    private  String clientIdFromUrl;
    private String supplierIdFromUrl;

    public PurchasesView(PurchasesViewController purchasesViewController) {
        this.purchasesViewController = purchasesViewController;


        grid.addColumn(purchase -> purchase.getClient().getFullName()).setHeader("Client");
        grid.addColumn(Purchase::getContractNumber).setHeader("Contract number");
        grid.addColumn(purchase -> purchase.getSupplier().getNameOfCompany()).setHeader("Supplier");
        grid.addColumn(Purchase::getNetAmount).setHeader("Net amount");
        grid.addColumn(purchase -> purchase.getCommodityType().getName()).setHeader("Commodity type");
        grid.addColumn(purchase -> purchase.getStatus().getStatusName()).setHeader("Status");
        grid.addColumn(Purchase::getSupplierPurchaseNumber).setHeader("Supplier purchase number");
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, purchase) -> {
                    if(!Objects.isNull(purchase.getComment()) && !purchase.getComment().isBlank()){
                        button.addThemeVariants(ButtonVariant.LUMO_ICON,
                                ButtonVariant.LUMO_PRIMARY);
                        button.addClickListener(e -> {
                            Dialog commentDialog = createCommentDialogLayout(purchase.getComment());
                            add(commentDialog);
                            commentDialog.open();
                        });
                    } else {
                        button.addThemeVariants(ButtonVariant.LUMO_ICON,
                                ButtonVariant.LUMO_CONTRAST);
                    }
                    button.setIcon(new Icon(VaadinIcon.COMMENT));
                })).setHeader("Comment");
        grid.getColumns().forEach(purchaseColumn -> purchaseColumn.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setItems(filterDataProvider);

        GridContextMenu<Purchase> menu = grid.addContextMenu();
        menu.addItem("Create Contract", event -> {
            if(event.getItem().isPresent()){
                purchasesViewController.createNewContractFromPurchase(event.getItem().get().getId());
            } else menu.close();
        });
        menu.add(new Hr());
        menu.addItem("Edit", event -> {
            if(event.getItem().isPresent()){
                purchasesViewController.editPurchaseInformation(event.getItem().get());
            } else menu.close();
        });
        menu.addItem("Delete", event -> {
            if(event.getItem().isPresent()){
                purchasesViewController.deletePurchase(event.getItem().get());
            } else menu.close();
        });

        add(createTopButtonLayout());
        add(createSearchLayout());
        add(grid);
    }

    private Component createSearchLayout() {
        HorizontalLayout searchLayout = new HorizontalLayout();

        TextField searchField = createTextFieldForSearchLayout
                ("Search by client, contract number, purchase number or supplier");
        searchField.addValueChangeListener(e -> {
            purchaseFilter.setSearchTerm(e.getValue());
            filterDataProvider.setFilter(purchaseFilter);
        });
        searchLayout.add(searchField);
        return searchLayout;
    }

    private Component createTopButtonLayout() {
        HorizontalLayout topButtonLayout = ComponentFactory.createTopButtonLayout();
        topButtonLayout.add(newPurchase);
        newPurchase.addClickListener( e -> purchasesViewController.createNewPurchaseForClient(clientIdFromUrl));
        return topButtonLayout;
    }

    private Dialog createCommentDialogLayout(String comment) {
        Dialog dialog = new Dialog();
        dialog.setModal(false);
        dialog.setDraggable(true);

        dialog.setHeaderTitle("Comment");
        dialog.add(comment);

        Button cancelButton = new Button("Close", event -> dialog.close());
        cancelButton.addClickShortcut(Key.ESCAPE);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton);
        return dialog;
    }


    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String urlParameter) {
        if(!urlParameter.isBlank()){
            char identificationChar = urlParameter.charAt(0);
            switch (identificationChar) {
                case 'c' -> {
                    String clientId = urlParameter.substring(1);
                    clientIdFromUrl = urlParameter;
                    grid.setItems(purchasesViewController.clientPurchases(Long.valueOf(clientId)));
                }
                case 's' -> {
                    String supplierId = urlParameter.substring(1);
                    supplierIdFromUrl = urlParameter;
                    grid.setItems(purchasesViewController.purchasesSentToSupplier(Long.valueOf(supplierId)));
                }
            }
        }
    }
}
