package com.leaf.framework.jdbc.converter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class ResultSetConverterallows to convert an object
 * Of type ResultSetin an object of type BufferResultSet
 *
 */


//TODO : make converting parametrik for partial getting of result set..
// use first index and last index parameter..
public class PreparedResultSetConverter {

	
	public PreparedResultSet convert(ResultSet rs) throws Exception {

		Object[][] tabResultSet = null;
		String[] columnNames = null;

		ResultSetMetaData metaData = rs.getMetaData();

		// Get number of Columns
		int nbcolonnes = metaData.getColumnCount();

		// Get list of Rows
		List<Object> rows = new ArrayList<Object>();

		while (rs.next()) {
			Object[] row = new Object[nbcolonnes];

			for (int col = 0; col < nbcolonnes; col++) {
				try {
					row[col] = rs.getObject(col + 1);
				}
				 catch (Exception e) {
					row[col] = "";
				}
			}

			rows.add(row);
		}

		// Get the column names and cache them.
		columnNames = new String[nbcolonnes];

		for (int i = 0; i < nbcolonnes; i++) {

			String columnName;
            
			try{
                columnName = metaData.getColumnLabel(i + 1); // use getColumnLabel to support column aliases (select name as 'firstName' from ...)
                if (columnName == null || columnName.trim().length()==0)
                    columnName = metaData.getColumnName(i + 1); // if getColumnLabel is not supported, use getColumnName instead
            }catch(Exception e){
                columnName = metaData.getColumnName(i + 1); // if getColumnLabel is not implemented, use getColumnName instead
            }
            columnNames[i] = columnName;
		}

		tabResultSet = new Object[rows.size()][nbcolonnes];

		for (int i = 0; i < rows.size(); i++) {
			tabResultSet[i] = (Object[]) rows.get(i);
		}

		PreparedResultSet buffer = new PreparedResultSet();
		buffer.setColumnNames(columnNames);
		buffer.setResultSet(tabResultSet);
		buffer.setWarnings(rs.getWarnings());

		convertMetaData(buffer, columnNames, metaData);

		return buffer;
	}

	protected void convertMetaData(PreparedResultSet buffer, String[] columnNames, ResultSetMetaData metaData) throws SQLException {

		int colCount = columnNames.length;

		PreparedResultSetMetaData rsmd = new PreparedResultSetMetaData();

		String[] colClassName = new String[colCount];
		String[] colTypeName = new String[colCount];

		int[] colType = new int[colCount];
		int[] colMaxLength = new int[colCount];
		int[] colPrecision = new int[colCount];
		int[] colScale = new int[colCount];

		boolean[] colSigned = new boolean[colCount];

		for (int i = 0; i < colCount; i++) {
			
			colType[i] = metaData.getColumnType(i + 1);
			colTypeName[i] = metaData.getColumnTypeName(i + 1);
			colMaxLength[i] = metaData.getColumnDisplaySize(i + 1);
			colScale[i] = metaData.getScale(i + 1);

			try {

				colPrecision[i] = metaData.getPrecision(i + 1);
				colSigned[i] = metaData.isSigned(i + 1);
				colClassName[i] = metaData.getColumnClassName(i + 1);
				
			}
			 catch (Exception e) {
				 // same databases does not implement isSigned..
			}
		}

		rsmd.setColName(columnNames);
		rsmd.setColType(colType);
		rsmd.setColTypeName(colTypeName);
		rsmd.setColDisplaySize(colMaxLength);
		rsmd.setColPrecision(colPrecision);
		rsmd.setColScale(colScale);
		rsmd.setColSigned(colSigned);
		rsmd.setColClassName(colClassName);

		buffer.setMetaData(rsmd);
	}
}
