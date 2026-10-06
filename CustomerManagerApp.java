

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class CustomerManagerApp extends Application {

    // 2. ObservableList to hold customer records
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Manager");

        // --- Step 1: Input Form ---
        Label nameLabel = new Label("Name:");
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter full name");

        Label provinceLabel = new Label("Province:");
        ComboBox<String> provinceList = new ComboBox<>();
        provinceList.getItems().addAll(
            "Central", "Copperbelt", "Eastern", "Luapula", 
            "Lusaka", "Muchinga", "Northern", "North-Western", 
            "Southern", "Western"
        );
        provinceList.setPromptText("Select a Province");

        // Buttons
        Button addButton = new Button("_Add Customer"); // Mnemonic '_' enables Alt+A shortcut
        Button deleteButton = new Button("_Delete Selected"); // Mnemonic '_' enables Alt+D shortcut

        GridPane formLayout = new GridPane();
        formLayout.setHgap(10);
        formLayout.setVgap(10);
        formLayout.add(nameLabel, 0, 0);
        formLayout.add(nameInput, 1, 0);
        formLayout.add(provinceLabel, 0, 1);
        formLayout.add(provinceList, 1, 1);

        HBox buttonLayout = new HBox(10, addButton, deleteButton);

        // --- Step 3: TableView Setup ---
        TableView<Customer> tableView = new TableView<>(customerList);

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);

        TableColumn<Customer, String> provinceColumn = new TableColumn<>("Province");
        provinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        provinceColumn.setPrefWidth(150);

        tableView.getColumns().addAll(nameColumn, provinceColumn);

        // --- Step 4: Validation & Adding Logic ---
        addButton.setOnAction(e -> {
            String name = nameInput.getText().trim();
            String province = provinceList.getValue();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a customer name.");
                nameInput.requestFocus();
                return;
            }

            if (province == null || province.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a province.");
                provinceList.requestFocus();
                return;
            }

            // Add new customer and reset form fields
            customerList.add(new Customer(name, province));
            nameInput.clear();
            provinceList.setValue(null);
            nameInput.requestFocus();
        });

        // --- Step 5: Delete Confirmation Logic ---
        deleteButton.setOnAction(e -> {
            Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {
                showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a customer from the table to delete.");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Customer");
            confirmAlert.setContentText("Are you sure you want to delete " + selectedCustomer.getName() + "?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                customerList.remove(selectedCustomer);
            }
        });

        // Main Layout Structure
        VBox rootLayout = new VBox(15, formLayout, buttonLayout, tableView);
        rootLayout.setPadding(new Insets(15));

        Scene scene = new Scene(rootLayout, 420, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}