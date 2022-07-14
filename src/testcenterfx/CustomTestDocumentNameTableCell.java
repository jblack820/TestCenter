package testcenterfx;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 *
 * @author takacs.gergely
 */
public class CustomTestDocumentNameTableCell<S, T> extends TextFieldTableCell<S, T> {

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
        return forTableColumn(new DefaultStringConverter());
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final StringConverter<T> converter) {
        return new Callback<TableColumn<S, T>, TableCell<S, T>>() {
            @Override
            public TableCell<S, T> call(TableColumn<S, T> list) {
                final TextFieldTableCell<S, T> result = new TextFieldTableCell<S, T>(converter);

                final EventHandler<MouseEvent> enter = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if (null!=result.getText()) {
                            result.setStyle("-fx-background-color: #305294; -fx-alignment: CENTER-LEFT; -fx-text-fill: white");
                            ((Node) event.getTarget()).getScene().setCursor(Cursor.HAND);
                        }
                    }
                };

                final EventHandler<MouseEvent> exit = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        result.setStyle("-fx-background-color: transparent; -fx-alignment: CENTER-LEFT; -fx-text-fill: #ABAAAA");
                        ((Node) event.getTarget()).getScene().setCursor(Cursor.DEFAULT);
                    }
                };

                final EventHandler<MouseEvent> click = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Node node = ((Node) event.getTarget()).getParent();
                        TableCell tc = null;
                        if (isTableCell(node)) {
                            tc = (TableCell) node;
                            ProjectPageController.handleOpenTestDocumentRequest(tc.getText(), tc);
                        }
                    }
                };

                result.setOnMouseEntered(enter);
                result.setOnMouseExited(exit);
                result.setOnMouseClicked(click);
                return result;
            }
        };
    }

    private static boolean isTableCell(Node node) {
        return node.getClass().toString().equalsIgnoreCase("class javafx.scene.control.cell.TextFieldTableCell");
    }

}
