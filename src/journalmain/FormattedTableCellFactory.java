package journalmain;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import java.text.Format;

public class FormattedTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
    private TextAlignment alignment;
    private Format format;
    private Color cvet;

    public TextAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Color getCvet() {
        return cvet;
    }

    public void setCvet(Color color) {
        this.cvet = color;
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> p) {
        TableCell<S, T> cell = new TableCell<S, T>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                if (item == getItem()) {
                    return;
                }
                super.updateItem((T) item, empty);
                if (item == null) {
                    super.setText(null);
                    super.setGraphic(null);
                } else if (format != null) {
                    super.setText(format.format(item));
                } else if (item instanceof Node) {
                    super.setText(null);
                    super.setGraphic((Node) item);
                } else {
                    super.setText(item.toString());
                    super.setGraphic(null);
                }
                if (String.valueOf(item).equals("Не взято")) {
                    this.setStyle("-fx-text-fill:red");
                    setText(String.valueOf(item));
                } else if (String.valueOf(item).equals("Выполнено")) {
                    this.setStyle("-fx-text-fill:green");
                    setText(String.valueOf(item));
                } else if (String.valueOf(item).equals("В работе")) {
                    this.setStyle("-fx-text-fill:blue");
                    setText(String.valueOf(item));
                } else if (String.valueOf(item).equals("Отменено")) {
                    this.setStyle("-fx-text-fill:DARKVIOLET");
                    setText(String.valueOf(item));
                }
            }

        };
        cell.setTextAlignment(alignment);
        switch (alignment) {
            case CENTER:
                cell.setAlignment(Pos.CENTER);
                break;
            case RIGHT:
                cell.setAlignment(Pos.CENTER_RIGHT);
                break;
            default:
                cell.setAlignment(Pos.CENTER_LEFT);
                break;
        }
        return cell;
    }
}