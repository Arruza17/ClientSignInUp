package view.controllers;

import model.DwellingTableBean;
import exceptions.BussinessLogicException;
import interfaces.DwellingManager;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Dwelling;
import model.User;

/**
 * FXML Controller class
 *
 * @author Ander Arruza
 */
public class OwnerWindowController {

    private DwellingManager dwellingManager;
    @FXML
    private Label lblTitle;
    @FXML
    private ComboBox<String> cbDwellings;
    @FXML
    private DatePicker dpConstructionDate;
    @FXML
    private Spinner<Integer> spRating;
    @FXML
    private ImageView imgSearch;
    @FXML
    private ImageView imgCreateComment;
    @FXML
    private ImageView imgConfirmComment;
    @FXML
    private ImageView imgCancelComment;
    @FXML
    private ImageView imgDeleteDwelling;
    @FXML
    private ImageView imgPrint;
    @FXML
    private TableView<DwellingTableBean> tableDwelling;
    @FXML
    private TableColumn<DwellingTableBean, String> colAddress;
    @FXML
    private TableColumn<DwellingTableBean, Boolean> colWiFi;
    @FXML
    private TableColumn<DwellingTableBean, Double> colSquareMeters;
    @FXML
    private TableColumn<DwellingTableBean, Date> colConstructionDate;
    @FXML
    private TableColumn<DwellingTableBean, String> colRating;
    @FXML
    private TableColumn<DwellingTableBean, String> colMoreInfo;

    private static final Logger LOGGER = Logger.getLogger(DwellingController.class.getSimpleName());

    private Dwelling dwelling;

    private User user;

    private Stage stage;

    private final String SELECT_ALL_DWELLINGS = "Select all my dwellings";

    private final String SELECT_BY_MIN_CONSTRUCTION_DATE = "Select from min construction date";

    private final String SELECT_BY_MIN_RATING = "Select from min rating";

    private ObservableList<DwellingTableBean> dwellingsTableBean;

    /**
     * This method is used to initialize the stage
     *
     * @param root The parent object representing root node of the view graph
     */
    public void initStage(Parent root) {

        LOGGER.info("Initializing Owner/Guest-Window stage");
        //Creation of a new Scene
        Scene scene = new Scene(root);
        //Save the route of the .css file
        String css = this.getClass().getResource("/view/resources/styles/CSSLogin.css").toExternalForm();
        //Sets the .css to the Scene
        //scene.getStylesheets().add(css);
        //stage.getIcons().add(new Image("/view/resources/img/BluRoofLogo.png"));
        //Sets the scene to the stage
        stage.setScene(scene);
        //Sets the Title of the Window
        stage.setTitle("DwellingWindow");
        //Sets the window not resizable
        stage.setResizable(false);
        //Sets the column RATING & SQUARE METERS ro center-right
        colRating.setStyle("-fx-alignment: CENTER-RIGHT;");
        colSquareMeters.setStyle("-fx-alignment: CENTER-RIGHT;");
        //Sets the column More info to center
        colMoreInfo.setStyle("-fx-alignment: CENTER;");
        //Sets the datePicker and spinner to disabled
        dpConstructionDate.setDisable(true);
        spRating.setDisable(true);
        //Sets the confirm & cancel imgs to not clickable
        imgConfirmComment.setDisable(true);
        imgConfirmComment.setOpacity(0.25);
        imgCancelComment.setDisable(true);
        imgCancelComment.setOpacity(0.25);
        //Sets the the delete imgs to not clickable
        imgDeleteDwelling.setDisable(true);
        imgDeleteDwelling.setOpacity(0.25);
        //Add the combobox values
        ObservableList<String> optionsForCombo;
        optionsForCombo = FXCollections.observableArrayList(
                SELECT_ALL_DWELLINGS,
                SELECT_BY_MIN_CONSTRUCTION_DATE,
                SELECT_BY_MIN_RATING
        );
        cbDwellings.setItems(optionsForCombo);
        //Add listener to the rows selected
        tableDwelling.getSelectionModel().selectedItemProperty()
                .addListener(this::handleTableSelectionChanged);

        //if logged as an owner
        lblTitle.setText("My Dwellings");
        //Setting the cell value factories
        colAddress.setCellValueFactory(
                new PropertyValueFactory<>("address"));
        colWiFi.setCellValueFactory(
                new PropertyValueFactory<>("wifi"));
        colSquareMeters.setCellValueFactory(
                new PropertyValueFactory<>("squareMeters"));
        colConstructionDate.setCellValueFactory(
                new PropertyValueFactory<>("constructionDate"));
        colRating.setCellValueFactory(
                new PropertyValueFactory<>("rating"));
        colMoreInfo.setCellValueFactory(
                new PropertyValueFactory<>("moreInfo"));
        //SELECT THE FIRST COMBOBOX ITEM BY DEFAULT
        cbDwellings.getSelectionModel().selectFirst();
        //Load all the dwellings by default
        loadAllDwellings();
        //Shows the stage
        stage.show();

        LOGGER.info("Owner/Guest-Window Open");
    }

    @FXML
    private void handleChangeComponents(ActionEvent event) {
        switch (cbDwellings.getValue()) {
            case SELECT_ALL_DWELLINGS:
                dpConstructionDate.setDisable(true);
                spRating.setDisable(true);
                break;
            case SELECT_BY_MIN_CONSTRUCTION_DATE:
                dpConstructionDate.setDisable(false);
                spRating.setDisable(true);
                break;
            case SELECT_BY_MIN_RATING:
                dpConstructionDate.setDisable(true);
                spRating.setDisable(false);
                break;
            default:
                break;
        }

    }

    @FXML
    private void handleFilterSearch(MouseEvent event) {
        try {
            switch (cbDwellings.getValue()) {
                case SELECT_ALL_DWELLINGS:
                    loadAllDwellings();
                    break;
                case SELECT_BY_MIN_CONSTRUCTION_DATE:
                    if (dpConstructionDate.getValue() != null) {
                        Date date = Date.from(dpConstructionDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        List<Dwelling> ds = dwellingManager.findByDate(date);
                        System.out.println(ds.size());
                    } else {
                        //THROW NEW EXCEPTION OF FIELDS EMPTY
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Empty fields");
                        alert.setHeaderText("The construction date is null");
                        alert.setContentText("Try again");
                        alert.showAndWait();
                    }
                    break;
                case SELECT_BY_MIN_RATING:
                    List<Dwelling> ds = dwellingManager.findByRating(spRating.getValue());
                    tableDwelling.getItems().clear();
                    List<DwellingTableBean> dwellings = new ArrayList<>();
                    if (ds != null) {
                        if (ds.size() > 0) {
                            for (Dwelling d : ds) {
                                dwellings.add(new DwellingTableBean(d));
                            }
                            dwellingsTableBean = FXCollections.observableArrayList(dwellings);
                            tableDwelling.setItems(dwellingsTableBean);
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Search successful");
                            alert.setHeaderText("Dwellings have been found");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("No data");
                            alert.setHeaderText("No dwellings");
                            alert.setContentText("No dwellings found with " + spRating.getValue().toString() + " rating or more");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Empty fields");
                        alert.setHeaderText("ERRROR AAAA");
                        alert.setContentText("Try again");
                        alert.showAndWait();
                    }

                    break;
                default:
                    break;
            }

        } catch (BussinessLogicException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while searching");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    private void handleAddComment(MouseEvent event) {
    }

    @FXML
    private void handleConfirmComment(MouseEvent event) {
    }

    @FXML
    private void handleCancelComment(MouseEvent event) {
    }

    @FXML
    private void handleDeleteDwelling(MouseEvent event) {
        DwellingTableBean selectedDwelling = tableDwelling.getSelectionModel()
                .getSelectedItem();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you want to delete this dwelling?");
        alert.showAndWait();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                dwellingManager.remove(selectedDwelling.getId());
                dwellingsTableBean.remove(selectedDwelling);
                tableDwelling.refresh();
            } catch (BussinessLogicException ex) {
                Alert alert1 = new Alert(AlertType.ERROR);
                alert1.setTitle("AYUDA");
                alert1.setHeaderText("Error");
                alert1.setContentText(ex.getMessage());
                alert1.showAndWait();
            }
        } else {
            Alert alert3 = new Alert(AlertType.INFORMATION);
            alert3.setTitle("Dwelling not deleted");
            alert3.setHeaderText(null);
            alert3.setContentText(null);
            alert3.showAndWait();
        }
    }

    @FXML
    private void handlePrintDwellings(MouseEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("AYUDA");
        alert.setHeaderText("Boton de print");
        alert.setContentText("A");
        alert.showAndWait();
    }

    /**
     * Method to control if there's a item selected in the table
     *
     * @param observableValue
     * @param oldValue
     * @param newValue
     */
    private void handleTableSelectionChanged(ObservableValue observableValue, Object oldValue, Object newValue) {

        if (newValue != null) {
            imgDeleteDwelling.setDisable(false);
            imgDeleteDwelling.setOpacity(1);

        } else {
            imgDeleteDwelling.setDisable(true);
            imgDeleteDwelling.setOpacity(0.25);

        }
    }

    public void setStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void setDwellingManager(DwellingManager dwellingManager) {
        this.dwellingManager = dwellingManager;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void loadAllDwellings() {
        try {
            List<DwellingTableBean> dwellings = new ArrayList<>();
            List<Dwelling> allDwellings = dwellingManager.findAll();
            if (allDwellings.size() > 0) {
                for (Dwelling d : allDwellings) {
                    //String type = (d instanceof Flat) ? "Flat" : "Room";
                    dwellings.add(new DwellingTableBean(d));
                }
                ObservableList<DwellingTableBean> dwellingsTableBean = FXCollections.observableArrayList(dwellings);
                //The imgPrint will be disabled if there are not dwellings
                imgPrint.setDisable(false);
                imgPrint.setOpacity(1);
                //Add the items to the tableView
                tableDwelling.setItems(dwellingsTableBean);

            } else {
                //The imgPrint will be disabled if there are not dwellings
                imgPrint.setDisable(true);
                imgPrint.setOpacity(0.25);
            }
        } catch (BussinessLogicException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("AYUDA");
            alert.setHeaderText("Error");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

}
