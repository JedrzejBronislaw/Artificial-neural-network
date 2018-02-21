package application;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ColumnController{

	private TableView<NumberColumnItem> dataTable;
	private int predictColumnIndex;

	private ObservableList<NumberColumnItem> items;

	public int getPredictColumnIndex() {
		return predictColumnIndex;
	}
	public void setPredictColumnIndex(int predictColumnIndex) {
		items.get(predictColumnIndex).setPredict(true);
		this.predictColumnIndex = predictColumnIndex;
	}
	public NumberColumn getPredictColumn() {
		return items.get(predictColumnIndex).getNumberColumn();
	}

	/***
	 * Last column is a predict column;
	 * @return
	 */
	public List<NumberColumn> selectedColumn(){
		List<NumberColumn> list = new ArrayList<>();
		for(NumberColumnItem i : items)
			if(i.isSelected() && !i.isPredict())
				list.add(i.getNumberColumn());

		for(NumberColumnItem i : items)
			if(i.isPredict())
				list.add(i.getNumberColumn());

		return list;
	}

	public ColumnController(TableView<NumberColumnItem> dataTable, NumberColumn[] numberColumns) {
		this.dataTable = dataTable;

		items = records(numberColumns);
		dataTable.setItems(items);

		TableColumn<NumberColumnItem, String> col1 = new TableColumn<>("Name");
		col1.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<NumberColumnItem, Double> col2 = new TableColumn<>("Min");
		col2.setCellValueFactory(new PropertyValueFactory<>("min"));
		TableColumn<NumberColumnItem, Double> col3 = new TableColumn<>("Max");
		col3.setCellValueFactory(new PropertyValueFactory<>("max"));
		TableColumn<NumberColumnItem, Boolean> col4 = new TableColumn<>("Use");
		col4.setCellValueFactory(new PropertyValueFactory<>("selected"));
		col4.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer,ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(Integer param) {
				NumberColumnItem item = items.get(param);
				if(item.isPredict())
					item.setSelected(true);
		        return items.get(param).selectedProperty();
			}

		}));
		col4.setEditable(true);

		TableColumn<NumberColumnItem, Boolean> col5 = new TableColumn<>("Predict");
		col5.setCellValueFactory(new PropertyValueFactory<>("predict"));
		col5.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer,ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(Integer param) {
				NumberColumnItem item = items.get(param);
				if(item.isPredict()){
					if(!item.isSelected())
						item.setPredict(false);
					else {
						predictColumnIndex = param;
						for(NumberColumnItem i : items)
							if(i != item)
								i.setPredict(false);
					}
				} else {
					int c = 0;
					for(NumberColumnItem i : items)
						if(i.isPredict()) c++;
					if (c==0)
						item.setPredict(true);
				}
		        return item.predictProperty();
			}

		}));
		col5.setEditable(true);

		TableColumn<NumberColumnItem, Boolean> col6 = new TableColumn<>("Avg");
		TableColumn<NumberColumnItem, Boolean> col7 = new TableColumn<>("R");

		dataTable.setEditable(true);

		this.dataTable.getColumns().addAll(col5, col4, col1, col2, col3, col6, col7);

	}



	private ObservableList<NumberColumnItem> records(NumberColumn[] numberColumns) {
		int n = numberColumns.length;
		ObservableList<NumberColumnItem> list = FXCollections.observableArrayList();

		for(int i=0; i<n; i++){
			list.add(new NumberColumnItem(numberColumns[i]));
		}
		return list;
	}


}
