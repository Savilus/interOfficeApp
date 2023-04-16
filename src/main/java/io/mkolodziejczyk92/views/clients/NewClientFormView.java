package io.mkolodziejczyk92.views.clients;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.mkolodziejczyk92.data.controllers.ClientAddNewFormController;
import io.mkolodziejczyk92.data.entity.Client;
import io.mkolodziejczyk92.data.enums.EClientType;
import io.mkolodziejczyk92.views.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("New Client")
@Route(value = "new-client", layout = MainLayout.class)
@PermitAll
public class NewClientFormView  extends Div {

    private final ClientAddNewFormController clientAddNewFormController;

    private final TextField firstName = new TextField("First Name");

    private final TextField lastName = new TextField("Last Name");
    private final TextField phoneNumber = new TextField("Phone Number");
    private final TextField email = new TextField("Email");
    private final TextField nip = new TextField("NIP");
    private final  ComboBox<EClientType> clientType = new ComboBox<>("Client Type");
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button back = new Button("Back");
    private final Binder<Client> binder = new Binder<>(Client.class);



    public NewClientFormView(ClientAddNewFormController clientAddNewFormController) {
        this.clientAddNewFormController = clientAddNewFormController;
        clientAddNewFormController.initView(this, binder);
        addClassName("new-client-view");

        createComboBox();
        add(createFormLayout());
        add(createButtonLayout());
        binder.bindInstanceFields(this);

        clientAddNewFormController.clearForm();
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout(firstName, lastName, phoneNumber, email, nip);
        formLayout.getStyle().set("padding-left", "30px");
        formLayout.getStyle().set("padding-right", "30px");
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        back.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttonLayout.add(save);
        buttonLayout.add(cancel);
        buttonLayout.add(back);
        buttonLayout.getStyle().set("padding-left", "30px");
        buttonLayout.getStyle().set("padding-top", "30px");

        cancel.addClickListener(e -> clientAddNewFormController.clearForm());
        save.addClickListener(e -> {
            clientAddNewFormController.saveNewClient(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " stored.");
            clientAddNewFormController.clearForm();
        });
        back.addClickListener(e -> UI.getCurrent().navigate(ClientsView.class));
        return buttonLayout;

    }

    private void createComboBox(){
        clientType.setItemLabelGenerator(EClientType::getType);
        clientType.setItems(EClientType.values());
        clientType.getStyle().set("padding-left", "30px");
        add(clientType);
    }
}
