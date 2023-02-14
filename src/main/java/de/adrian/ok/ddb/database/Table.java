package de.adrian.ok.ddb.database;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.mysql.cj.result.Field;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private Field[] columns;
    private List<Object[]> rows;

    public Table(ResultSet set) throws SQLException {
        ResultSetMetaData metaData = (ResultSetMetaData) set.getMetaData();
        columns = metaData.getFields();
        rows = new ArrayList<>();
        while (set.next()) {
            Object[] row = new Object[metaData.getColumnCount()];
            for(int i = 1; i <= row.length; i++) {
                row[i-1] = set.getObject(i);
            }
            rows.add(row);
        }
    }

    public ObservableList asObservableList() {
        ObservableList data = FXCollections.observableArrayList();
        for(Object[] row : rows) data.add(FXCollections.observableArrayList(row));
        return data;
    }

    public String getColumnName(int columnIndex) {
        return columns[columnIndex].getName();
    }

    public int getColumnLength() {
        return columns.length;
    }

    public int getRowCount() {
        return rows.size();
    }

}
