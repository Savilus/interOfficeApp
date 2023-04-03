//package io.mkolodziejczyk92.views.clients;
//
//import com.vaadin.collaborationengine.CollaborationAvatarGroup;
//import com.vaadin.collaborationengine.CollaborationBinder;
//import com.vaadin.collaborationengine.UserInfo;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.grid.GridVariant;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.notification.Notification.Position;
//import com.vaadin.flow.component.notification.NotificationVariant;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.splitlayout.SplitLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.ValidationException;
//import com.vaadin.flow.router.BeforeEnterEvent;
//import com.vaadin.flow.router.BeforeEnterObserver;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.router.RouteAlias;
//import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
//import io.mkolodziejczyk92.data.entity.Client;
//import io.mkolodziejczyk92.data.service.ClientService;
//import io.mkolodziejczyk92.views.MainLayout;
//import jakarta.annotation.security.PermitAll;
//import java.util.Optional;
//import java.util.UUID;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.orm.ObjectOptimisticLockingFailureException;
//
////@PageTitle("Clients")
////@Route(value = "clients", layout = MainLayout.class)
////@RouteAlias(value = "", layout = MainLayout.class)
////@PermitAll
//public class ClientsView extends Div {
//
//    private final String CLIENT_ID = "clientID";
//    private final String CLIENT_EDIT_ROUTE_TEMPLATE = "Clients/%s/edit";
//
//    private final Grid<Client> grid = new Grid<>(Client.class, false);
//
//    CollaborationAvatarGroup avatarGroup;
//
//    private TextField firstName;
//    private TextField lastName;
//    private TextField phoneNumber;
//    private TextField email;
//    private TextField address;
//    private TextField orders;
//    private TextField contracts;
//
//    private final Button cancel = new Button("Cancel");
//    private final Button save = new Button("Save");
//
//    private final CollaborationBinder<Client> binder;
//
//    private Client client;
//
//    private final ClientService clientService;
//
//    public ClientsView(ClientService clientService) {
//        this.clientService = clientService;
//        addClassNames("clients-view");
//
//        // UserInfo is used by Collaboration Engine and is used to share details
//        // of users to each other to able collaboration. Replace this with
//        // information about the actual user that is logged, providing a user
//        // identifier, and the user's real name. You can also provide the users
//        // avatar by passing an url to the image as a third parameter, or by
//        // configuring an `ImageProvider` to `avatarGroup`.
//        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");
//
//        // Create UI
////        SplitLayout splitLayout = new SplitLayout();
//
//        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
//        avatarGroup.getStyle().set("visibility", "hidden");
//
////        createGridLayout(splitLayout);
////        createEditorLayout(splitLayout);
//
//        add(grid);
//
//        // Configure Grid
//        grid.addColumn("firstName").setAutoWidth(true);
//        grid.addColumn("lastName").setAutoWidth(true);
//        grid.addColumn("phoneNumber").setAutoWidth(true);
//        grid.addColumn("email").setAutoWidth(true);
//
//        grid.setItems(query -> clientService.list(
//                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
//                .stream());
//        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
//
//        // when a row is selected or deselected, populate form
//        grid.asSingleSelect().addValueChangeListener(event -> {
//            if (event.getValue() != null) {
//                UI.getCurrent().navigate(String.format(CLIENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
//            } else {
//                clearForm();
//                UI.getCurrent().navigate(ClientsView.class);
//            }
//        });
//
//        // Configure Form
//        binder = new CollaborationBinder<>(Client.class, userInfo);
//
//        // Bind fields. This is where you'd define e.g. validation rules
//
//        binder.bindInstanceFields(this);
//
//        cancel.addClickListener(e -> {
//            clearForm();
//            refreshGrid();
//        });
//
//        save.addClickListener(e -> {
//            try {
//                if (this.client == null) {
//                    this.client = new Client();
//                }
//                binder.writeBean(this.client);
//                clientService.update(this.client);
//                clearForm();
//                refreshGrid();
//                Notification.show("Data updated");
//                UI.getCurrent().navigate(ClientsView.class);
//            } catch (ObjectOptimisticLockingFailureException exception) {
//                Notification n = Notification.show(
//                        "Error updating the data. Somebody else has updated the record while you were making changes.");
//                n.setPosition(Position.MIDDLE);
//                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
//            } catch (ValidationException validationException) {
//                Notification.show("Failed to update the data. Check again that all values are valid");
//            }
//        });
//    }
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        Optional<Long> clientId = event.getRouteParameters().get(CLIENT_ID).map(Long::parseLong);
//        if (clientId.isPresent()) {
//            Optional<Client> clientFromBackend = clientService.get(clientId.get());
//            if (clientFromBackend.isPresent()) {
//                populateForm(clientFromBackend.get());
//            } else {
//                Notification.show(String.format("The requested client was not found, ID = %d", clientId.get()), 3000,
//                        Notification.Position.BOTTOM_START);
//                // when a row is selected but the data is no longer available,
//                // refresh grid
//                refreshGrid();
//                event.forwardTo(ClientsView.class);
//            }
//        }
//    }
//
//    private void createEditorLayout(SplitLayout splitLayout) {
//        Div editorLayoutDiv = new Div();
//        editorLayoutDiv.setClassName("editor-layout");
//
//        Div editorDiv = new Div();
//        editorDiv.setClassName("editor");
//        editorLayoutDiv.add(editorDiv);
//
//        FormLayout formLayout = new FormLayout();
//        firstName = new TextField("First Name");
//        lastName = new TextField("Last Name");
//        phoneNumber = new TextField("Phone Number");
//        email = new TextField("Email");
//        address = new TextField("Address");
//        orders = new TextField("Orders");
//        contracts = new TextField("Contracts");
//        formLayout.add(firstName, lastName, phoneNumber, email, address, orders, contracts);
//
//        editorDiv.add(avatarGroup, formLayout);
//        createButtonLayout(editorLayoutDiv);
//
//        splitLayout.addToSecondary(editorLayoutDiv);
//    }
//
//    private void createButtonLayout(Div editorLayoutDiv) {
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        buttonLayout.setClassName("button-layout");
//        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        buttonLayout.add(save, cancel);
//        editorLayoutDiv.add(buttonLayout);
//    }
//
//    private void createGridLayout(SplitLayout splitLayout) {
//        Div wrapper = new Div();
//        wrapper.setClassName("grid-wrapper");
//        splitLayout.addToPrimary(wrapper);
//        wrapper.add(grid);
//    }
//
//    private void refreshGrid() {
//        grid.select(null);
//        grid.getDataProvider().refreshAll();
//    }
//
//    private void clearForm() {
//        populateForm(null);
//    }
//
//    private void populateForm(Client value) {
//        this.client = value;
//        String topic = null;
//        if (this.client != null && this.client.getId() != null) {
//            topic = "client/" + this.client.getId();
//            avatarGroup.getStyle().set("visibility", "visible");
//        } else {
//            avatarGroup.getStyle().set("visibility", "hidden");
//        }
//        binder.setTopic(topic, () -> this.client);
//        avatarGroup.setTopic(topic);
//
//    }
//}
