package com.leaf.framework.jdbc.converter;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baselib.exception.TechnicalException;
import com.leaf.framework.util.JdbcConvertUtil;

/**
 * 
 * The class BufferResultSet is an implementation of the interface ResultSet of
 * sql classes. It manipulates rows and columns to simulates a real java.sql.ResultSet.
 * 
 */

public class PreparedResultSet implements java.sql.ResultSet,
		java.io.Serializable {

	private static final long serialVersionUID = 8887170392505086007L;

	private static final String ERROR_COLUMN_NOT_FOUND = "Column [{0}] not found";

	private static final String ERROR_NOT_IMPLEMENTED_YET = "Method not implemented";

	private static final String ERROR_ROW_NOT_FOUND = "Row not found";

	private static final String ERROR_ROW_INCORRECT_LENGTH = "Row [{0}] have an incorrect length";

	private static final String ERROR_COLUMNS_INCORRECT_LENGTH = "The column have an incorrect length";

	private static final String ERROR_INVALID_ROW_NUMBER = "Use next method before getting results";

	private static final String ERROR_INVALID_COLUM_NUMBER = "Result set does not contain column number with [{0}]";

	private static final String ERROR_INCORRECT_LENGTH = "Column [{0}] have an incorrect length";

	private static final String ERROR_FILL_COLUMNS_NAMES = "You have to fill columns names before set datas in the BufferResultSet";

	private static final String CLASS_NAME = PreparedResultSet.class.getName();

	private static Log log = LogFactory.getLog(CLASS_NAME);

	private ResultSetMetaData resultMetaData = null;

	private String[] resultColNames = null;

	private Object[][] resultSetTable = null;

	private boolean[][] nullData = null;

	private int currentColumn = 0;

	private SQLWarning sqlWarnings = null;

	private boolean m_emptyStringIsNull = true;

	private int currentRowNum = -1;

	private Object[] columnArr;

	private int columnType;

	/**
	 * Constructor
	 */
	public PreparedResultSet() {
	}

	public void setColumnNames(String[] colNames) {
		resultColNames = colNames;
	}

	public String[] getColumnNames() {
		return resultColNames;
	}

	public void setNullData(boolean[][] mnullData) {
		nullData = mnullData;
	}

	public boolean[][] getNullData() {
		return nullData;
	}

	public void setResultSet(Object[][] _tabResultSet) throws Exception {
		if (resultColNames == null) {
			throw new Exception(ERROR_FILL_COLUMNS_NAMES);
		}

		int nbCol = resultColNames.length;

		for (int i = 0; i < _tabResultSet.length; i++) {
			if (_tabResultSet[i].length != nbCol) {
				String message = MessageFormat.format(ERROR_INCORRECT_LENGTH,
						new Object[] { new Integer(i) });
				throw new Exception(message);
			}
		}

		resultSetTable = _tabResultSet;
	}

	public void setEmptyStringIsNull(boolean _emptyStringIsNull) {
		m_emptyStringIsNull = _emptyStringIsNull;
	}

	public Object clone() {
		try {
			PreparedResultSet result = new PreparedResultSet();

			result.setColumnNames(resultColNames);
			result.setResultSet(resultSetTable);
			result.setMetaData(resultMetaData);
			result.setRow(currentRowNum);
			result.setNullData(nullData);

			return result;
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			return null;
		}
	}

	public PreparedResultSet cloneHeader() throws Exception {
		PreparedResultSet result = new PreparedResultSet();

		result.setColumnNames(getColumnNames());

		return result;
	}

	/**
	 * Create a new BufferResultSet and copy some lines of the current
	 * BufferResultSet
	 */
	public Object copyRows(int[] _rows) throws Exception {
		PreparedResultSet result = (PreparedResultSet) clone();

		Object[][] value = getRowsData(_rows);

		result.addRowsData(value);

		return result;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();

		for (int j = 0; j < getColumnCount(); j++) {
			result.append(resultColNames[j] + "\t");
		}

		result.append("\n");

		for (int i = 0; i < getRowCount(); i++) {
			Object[] row = resultSetTable[i];

			for (int j = 0; j < getColumnCount(); j++) {
				result.append(row[j] + "\t");
			}

			result.append("\n");
		}

		return result.toString();
	}

	public HashMap toHashMap() {
		HashMap result = new HashMap();

		try {
			HashMap row = new HashMap();

			for (int i = 0; i < getRowCount(); i++) {
				for (int j = 0; j < getColumnCount(); j++) {
					row.put(resultMetaData.getColumnName(j + 1),
							resultSetTable[i][j]);
				}

				result.put(new Integer(i), row);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * @return ArrayList - BufferResultSet content
	 */
	public ArrayList toArrayList() {
		ArrayList list = new ArrayList(getRowCount());

		// Column Headers
		list.add(resultColNames);

		// Data
		for (int i = 0; i < getRowCount(); i++) {
			list.add(resultSetTable[i]);
		}

		return list;
	}

	public void setMetaData(ResultSetMetaData _metaData) {
		resultMetaData = _metaData;
	}

	/**
	 * Send back a column position
	 */
	public int getColumnIndex(String colName) {
		for (int i = 0; i < getColumnCount(); i++) {
			if (resultColNames[i].equals(colName)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Send back the line number of the BufferResultSet
	 */
	public int getRowCount() {

		if (resultSetTable != null) {

			return resultSetTable.length;
		}

		return 0;
	}

	public int getColumnCount() {

		return resultColNames.length;
	}

	public Object[] getRowData(int _row) throws Exception {

		Object[] newRow = new Object[getColumnCount()];
		Object[] row = resultSetTable[_row];

		for (int i = 0; i < row.length; i++) {
			newRow[i] = row[i];
		}

		return newRow;
	}

	public Object[][] getRowsData(int[] _row) throws Exception {

		Object[][] result = new Object[_row.length][1];

		for (int i = 0; i < _row.length; i++) {
			result[i] = resultSetTable[_row[i]];
		}

		return result;
	}

	public Object[] allRows() throws Exception {

		return resultSetTable;
	}

	public void addRowData(Object[] row) throws Exception {

		increaseCapacity();

		if (row.length != resultColNames.length) {
			throw new Exception(ERROR_COLUMNS_INCORRECT_LENGTH);
		}

		resultSetTable[getRowCount() - 1] = row;
	}

	public void addRowsData(Object[][] row) throws Exception {

		increaseCapacity(row.length);

		for (int i = 0; i < row.length; i++) {

			if (row[i].length != resultColNames.length) {
				
				String message = MessageFormat.format(
						ERROR_ROW_INCORRECT_LENGTH, new Object[] { new Integer(
								i) });
				throw new Exception(message);
			}
		}

		for (int i = 0; i < row.length; i++) {
			resultSetTable[getRowCount() - row.length + i] = row[i];
		}
	}

	public void addColumn(String name, String value) throws Exception {

		resultColNames = addValueToRow(resultColNames, name);

		PreparedResultSetMetaData metaData = (PreparedResultSetMetaData) getMetaData();

		metaData.setColName(resultColNames);

		metaData.setColClassName(addValueToRow(metaData.getColClassNameList(),
				String.class.getName()));
		metaData.setColDisplaySize(addValueToRow(metaData
				.getColDisplaySizeList(), 255));
		metaData.setColPrecision(addValueToRow(metaData.getColPrecisionList(),
				0));
		metaData.setColScale(addValueToRow(metaData.getColScaleList(), 0));
		metaData
				.setColSigned(addValueToRow(metaData.getColSignedList(), false));
		metaData.setColType(addValueToRow(metaData.getColTypeList(),
				Types.VARCHAR));
		metaData.setColTypeName(addValueToRow(metaData.getColTypeNameList(),
				"VARCHAR2"));

		for (int i = 0; i < getRowCount(); i++) {

			resultSetTable[i] = addValueToRow(resultSetTable[i], value);
		}
	}

	public void updateColumn(String name, Object value) throws Exception {

		int index = getColumnIndex(name);

		if (index == -1) {

			String message = MessageFormat.format(ERROR_COLUMN_NOT_FOUND,
					new Object[] { name });
			throw new Exception(message);
		}

		for (int i = 0; i < getRowCount(); i++) {

			Object[] row = getRowData(i);

			row[index] = value;

			resultSetTable[i] = row;
		}
	}

	public void deleteColumn(String name) throws Exception {

		int index = getColumnIndex(name);

		if (index == -1) {

			String message = MessageFormat.format(ERROR_COLUMN_NOT_FOUND,
					new Object[] { name });
			throw new Exception(message);

		}

		for (int i = 0; i < getRowCount(); i++) {

			Object[] oldRow = getRowData(i);

			Object[] newRow = removeValueFromRow(oldRow, index);

			resultSetTable[i] = newRow;
		}

		resultColNames = removeValueFromRow(resultColNames, index);
	}

	public void updateElement(String name, int _rowIndex, Object value)
			throws Exception {

		int index = getColumnIndex(name);

		// check index
		if (index == -1) {
			String message = MessageFormat.format(ERROR_COLUMN_NOT_FOUND,
					new Object[] { name });
			throw new Exception(message);
		}

		if (_rowIndex >= resultSetTable.length) {
			throw new Exception(ERROR_ROW_NOT_FOUND);

		}

		Object[] row = resultSetTable[_rowIndex];

		row[index] = value;
	}

	public void updateRowData(int i, Object[] row) throws SQLException {
		resultSetTable[i] = row;
	}

	private void increaseCapacity() {
		if (resultSetTable == null) {
			resultSetTable = new Object[1][getColumnCount()];

			return;
		}

		int oldCapacity = getRowCount();
		Object[] oldData = resultSetTable;

		resultSetTable = new Object[oldCapacity + 1][getColumnCount() + 1];
		System.arraycopy(oldData, 0, resultSetTable, 0, oldCapacity);
	}

	private void increaseCapacity(int _rowNumber) {
		if (resultSetTable == null) {
			resultSetTable = new Object[_rowNumber][getColumnCount()];

			return;
		}

		int oldCapacity = getRowCount();
		Object[] oldData = resultSetTable;

		resultSetTable = new Object[oldCapacity + _rowNumber][getColumnCount() + 1];
		System.arraycopy(oldData, 0, resultSetTable, 0, oldCapacity);
	}

	private Object[] addValueToRow(Object[] oldRow, Object value) {
		int oldCapacity = oldRow.length;
		Object[] newRow = new Object[oldCapacity + 1];

		System.arraycopy(oldRow, 0, newRow, 0, oldCapacity);
		newRow[oldCapacity] = value;

		return newRow;
	}

	private String[] addValueToRow(String[] oldRow, String value) {
		int oldCapacity = oldRow.length;
		String[] newRow = new String[oldCapacity + 1];

		System.arraycopy(oldRow, 0, newRow, 0, oldCapacity);
		newRow[oldCapacity] = value;

		return newRow;
	}

	private boolean[] addValueToRow(boolean[] oldRow, boolean value) {
		int oldCapacity = oldRow.length;
		boolean[] newRow = new boolean[oldCapacity + 1];

		System.arraycopy(oldRow, 0, newRow, 0, oldCapacity);
		newRow[oldCapacity] = value;

		return newRow;
	}

	private int[] addValueToRow(int[] oldRow, int value) {
		int oldCapacity = oldRow.length;
		int[] newRow = new int[oldCapacity + 1];

		System.arraycopy(oldRow, 0, newRow, 0, oldCapacity);
		newRow[oldCapacity] = value;

		return newRow;
	}

	private Object[] removeValueFromRow(Object[] oldRow, int index) {
		// cr�e une nouvelle ligne avec un taille inf�rieur de 1
		Object[] newRow = new Object[oldRow.length - 1];

		if (index > 0) {
			System.arraycopy(oldRow, 0, newRow, 0, index);
		}

		if (index < oldRow.length) {
			System.arraycopy(oldRow, index + 1, newRow, index, oldRow.length
					- index - 1);
		}

		return newRow;
	}

	private String[] removeValueFromRow(String[] oldRow, int index) {

		String[] newRow = new String[oldRow.length - 1];

		if (index > 0) {
			System.arraycopy(oldRow, 0, newRow, 0, index);
		}

		if (index < oldRow.length) {
			System.arraycopy(oldRow, index + 1, newRow, index, oldRow.length
					- index - 1);
		}

		return newRow;
	}

	public byte[] getBytes(int int1) throws SQLException {

		return JdbcConvertUtil.convertToBytes(getObject(int1));
	}

	public byte[] getBytes(String columnName) throws SQLException {

		return getBytes(getColumnIndex(columnName) + 1);
	}

	public boolean next() throws SQLException {

		currentRowNum++;

		if (currentRowNum < getRowCount()) {
			return true;
		}

		return false;
	}

	public boolean previous() throws SQLException {

		if (currentRowNum > 0) {
			currentRowNum--;

			return true;
		} else {
			return false;
		}
	}

	public void close() throws SQLException {
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public boolean getBoolean(int rownum) throws SQLException {

		return JdbcConvertUtil.convertToBoolean(getObject(rownum));
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * ResultSet object
	 */
	public boolean getBoolean(String columnName) throws SQLException {
		return getBoolean(getColumnIndex(columnName) + 1);
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public long getLong(String columnName) throws SQLException {
		return getLong(getColumnIndex(columnName) + 1);
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public long getLong(int colnum) throws SQLException {

		return JdbcConvertUtil.convertToLong(getObject(colnum));
	}

	public Object getObject(String columnName) throws SQLException {
		return getObject(getColumnIndex(columnName) + 1);
	}

	public Object getObject(int colnum, java.util.Map _map2)
			throws SQLException {
		return getObject(colnum);
	}

	public Object getObject(int colnum) throws SQLException {

		currentColumn = colnum - 1;

		try {

			return resultSetTable[currentRowNum][currentColumn];
		} catch (ArrayIndexOutOfBoundsException e) {

			if (currentRowNum < 0) {

				String message = MessageFormat.format(ERROR_INVALID_ROW_NUMBER,
						new Object[] { currentRowNum });

				throw new TechnicalException(CLASS_NAME, message, e);

			} else if (resultSetTable != null
					&& currentColumn >= resultSetTable[currentRowNum].length) {

				String message = MessageFormat.format(
						ERROR_INVALID_COLUM_NUMBER,
						new Object[] { currentColumn + 1 });

				throw new TechnicalException(CLASS_NAME, message, e);
			}

			throw new TechnicalException(CLASS_NAME, e.getMessage(), e);
		} catch (Exception e) {

			throw new TechnicalException(CLASS_NAME, e.getMessage(), e);
		}

	}

	public Object getObject(String columnName, java.util.Map _map2)
			throws SQLException {
		return getObject(columnName);
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public java.sql.Date getDate(String columnName) throws SQLException {
		return getDate(getColumnIndex(columnName) + 1);
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public java.sql.Date getDate(int columnNumber) throws SQLException {

		return JdbcConvertUtil.convertToDate(getObject(columnNumber));
	}

	public java.sql.Time getTime(int columnNumber) throws SQLException {

		return JdbcConvertUtil.getTime(getObject(columnNumber));
	}

	public java.sql.Time getTime(String columnName) throws SQLException {
		return getTime(getColumnIndex(columnName) + 1);
	}

	public String getString(String columnName) throws SQLException {
		return getString(getColumnIndex(columnName) + 1);
	}

	public String getString(int columnNumber) throws SQLException {
		String result = null;

		result = JdbcConvertUtil.convertToString(getObject(columnNumber));

		if (!m_emptyStringIsNull && (result == null)) {
			result = "";
		}

		return result;
	}

	/**
	 * Moves the cursor to the first row in
	 */
	public boolean first() throws SQLException {
		currentRowNum = 0;

		return !(getRowCount() == 0);
	}

	/**
	 * Retrieves the number, types and properties of this <code>ResultSet</code>
	 * object's columns.
	 */
	public java.sql.ResultSetMetaData getMetaData() throws SQLException {
		return resultMetaData;
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public int getInt(int columnNumber) throws SQLException {

		return JdbcConvertUtil.convertToInt(getObject(columnNumber));
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public int getInt(String columnName) throws SQLException {
		return getInt(getColumnIndex(columnName) + 1);
	}

	public void deleteRow() throws SQLException {

		int oldCapacity = getRowCount();
		boolean deleteDone = false;

		Object[] oldData = resultSetTable;

		resultSetTable = new Object[oldCapacity - 1][getColumnCount()];

		for (int i = 0; i <= resultSetTable.length; i++) {
			if ((currentRowNum != i) && (!deleteDone)) {
				resultSetTable[i] = (Object[]) oldData[i];
			} else if (currentRowNum == i) {
				deleteDone = true;
			} else if (deleteDone) {
				resultSetTable[i - 1] = (Object[]) oldData[i];
			}
		}

		currentRowNum = 0;
	}

	public java.sql.SQLWarning getWarnings() throws SQLException {
		return sqlWarnings;
	}

	public double getDouble(int colnum) throws SQLException {

		return JdbcConvertUtil.convertToDouble(getObject(colnum));
	}

	public double getDouble(String columnName) throws SQLException {
		return getDouble(getColumnIndex(columnName) + 1);
	}

	public byte getByte(String columnName) throws SQLException {
		return getByte(getColumnIndex(columnName) + 1);
	}

	public byte getByte(int colnum) throws SQLException {

		currentColumn = colnum - 1;
		Object obj = resultSetTable[currentRowNum][currentColumn];

		return JdbcConvertUtil.convertToByte(obj);
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public float getFloat(int colnum) throws SQLException {

		return JdbcConvertUtil.convertToFloat(getObject(colnum));

	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public float getFloat(String columnName) throws SQLException {
		return getFloat(getColumnIndex(columnName) + 1);
	}

	public short getShort(String columnName) throws SQLException {
		return getShort(getColumnIndex(columnName) + 1);
	}

	public short getShort(int colnum) throws SQLException {

		return JdbcConvertUtil.convertToShort(getObject(colnum));
	}

	/**
	 * Moves the cursor to the last row in
	 */
	public boolean last() throws SQLException {

		currentRowNum = resultSetTable.length - 1;

		return !(getRowCount() == 0);
	}

	public java.io.InputStream getBinaryStream(String columnName)
			throws SQLException {

		currentColumn = getColumnIndex(columnName);

		return JdbcConvertUtil.convertToBinaryStream(getObject(currentColumn));
	}

	public java.io.InputStream getBinaryStream(int colnum) throws SQLException {

		return JdbcConvertUtil.convertToBinaryStream(getObject(colnum));
	}

	/**
	 * Moves the cursor to the end of
	 */

	public void afterLast() throws SQLException {
		currentRowNum = getRowCount();
	}

	public void beforeFirst() throws SQLException {
		currentRowNum = -1;
	}

	public void clearWarnings() throws SQLException {
		sqlWarnings = null;
	}

	public void setWarnings(SQLWarning _sqlWarnings) {
		sqlWarnings = _sqlWarnings;
	}

	public int findColumn(String columnName) throws SQLException {
		for (int i = 0; i < getColumnCount(); i++) {
			if (resultColNames[i].equals(columnName)) {
				return i + 1;
			}
		}

		String message = MessageFormat.format(ERROR_COLUMN_NOT_FOUND,
				new Object[] { columnName });
		throw new SQLException(message);
	}

	public java.math.BigDecimal getBigDecimal(int colnum, int colnum2)
			throws SQLException {
		return getBigDecimal(colnum);
	}

	public java.math.BigDecimal getBigDecimal(String columnName)
			throws SQLException {
		return getBigDecimal(getColumnIndex(columnName) + 1);
	}

	public java.math.BigDecimal getBigDecimal(String columnName, int _int2)
			throws SQLException {
		return getBigDecimal(getColumnIndex(columnName) + 1);
	}

	/**
	 * Retrieves the value of the designated column in the current row
	 */
	public java.math.BigDecimal getBigDecimal(int colnum) throws SQLException {

		return JdbcConvertUtil.convertToBigDecimal(getObject(colnum));
	}

	public Blob getBlob(int colnum) throws SQLException {

		try {
			Object o = resultSetTable[currentRowNum][colnum - 1];
			return (java.sql.Blob) o;
		} catch (Exception e) {
			throw new SQLException("Can't convert to Blob.");
		}

	}

	public Blob getBlob(String columnName) throws SQLException {
		return getBlob(getColumnIndex(columnName) + 1);
	}

	public java.sql.Clob getClob(int colnum) throws SQLException {
		try {
			Object o = resultSetTable[currentRowNum][colnum - 1];
			return (java.sql.Clob) o;
		} catch (Exception e) {
			throw new SQLException("Can't convert to Clob.");
		}
	}

	public java.sql.Clob getClob(String columnName) throws SQLException {
		return getClob(getColumnIndex(columnName));
	}

	/**
	 * Retrieves the current row number. The first row is number 1
	 */
	public int getRow() throws SQLException {
		return currentRowNum;
	}

	public void setRow(int rownum) throws SQLException {
		currentRowNum = rownum;
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		return getTimestamp(getColumnIndex(columnName) + 1);
	}

	public Timestamp getTimestamp(int colnum) throws SQLException {

		return JdbcConvertUtil.convertToTimestamp(getObject(colnum));
	}

	public boolean isAfterLast() throws SQLException {
		if ((resultSetTable.length > 0) && (currentRowNum < getRowCount())) {
			return false;
		}

		return true;
	}

	public boolean isFirst() throws SQLException {
		if (currentRowNum == 0) {
			return true;
		}

		return false;
	}

	public boolean isLast() throws SQLException {
		if (currentRowNum == (getRowCount() - 1)) {
			return true;
		}

		return false;
	}

	public boolean wasNull() throws SQLException {
		if (nullData == null) {
			return false;
		}

		boolean result = nullData[currentRowNum][currentColumn];
		return result;
	}

	public void sort(int colIndex, boolean order) throws Exception {
		
		int numLigne = getRow();

		columnArr = null;
		columnArr = new Object[getRowCount()];

		int firstPos = getRow();

		int i = 0;

		if (first()) {
			
			do {
				
				columnArr[i] = getObject(colIndex);
				i++;
				
			} while (next());

			setRow(firstPos);
			columnType = getMetaData().getColumnType(colIndex);
			quickSort(colIndex, 0, getRowCount() - 1, order);
			setRow(numLigne);
		}
	}

	private void swap(Object[] a, int i, int j) {
		
		Object tab;

		tab = a[i];
		a[i] = a[j];
		a[j] = tab;
	}

	private void quickSort(int colIndex, int left, int right,
			boolean order) throws Exception {
		int lo = left;
		int hi = right;
		Object mid = null;

		if (right > left) {
			int pos = (left + right) / 2;

			mid = columnArr[pos];

			while (lo <= hi) {
				
				/* find the first element */
				while ((lo < right)
						&& (compareValues(columnArr[lo], mid, columnType, order) < 0)) {
					++lo;
				}

				/* find an element that is smaller than or equal */
				while ((hi > left)
						&& (compareValues(columnArr[hi], mid, columnType, order) > 0)) {
					--hi;
				}

				// swap if the indexes have not crossed
				if (lo <= hi) {
					swapRows(lo, hi);
					swap(columnArr, lo, hi);
					++lo;
					--hi;
				}
			}

			/*
			 * If the right index has not reached the left side of array must
			 * now sort the left partition.
			 */
			if (left < hi) {
				quickSort(colIndex, left, hi, order);

				/*
				 * If the left index has not reached the right side of array
				 * must now sort the right partition.
				 */
			}

			if (lo < right) {
				quickSort(colIndex, lo, right, order);
			}
		}
	}

	private void swapRows(int i, int j) throws Exception {
		
		try {
			
			Object[] tampon = new Object[getColumnCount()];

			tampon = getRowData(i);
			updateRowData(i, getRowData(j));
			updateRowData(j, tampon);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private int compareValues(Object value1, Object value, int type, boolean order) throws SQLException {

		int result = -2;

		if ((null == value1) && (null == value)) {
			result = 0;
		} else if ((null == value1) && (null != value)) {
			result = -1;
		} else if ((null != value1) && (null == value)) {
			result = 1;
		} else {

			switch (type) {

			case Types.CHAR:
				result = ((String) value1).compareTo((String) value);
				break;

			case Types.VARCHAR:
				result = ((String) value1).compareTo((String) value);
				break;

			case Types.LONGVARCHAR:
				result = ((String) value1).compareTo((String) value);
				break;

			case Types.INTEGER:
				result = ((Integer) value1).compareTo((Integer) value);
				break;

			case Types.BIGINT:
				result = ((Long) value1).compareTo((Long) value);
				break;

			case Types.FLOAT:
				result = ((Float) value1).compareTo((Float) value);
				break;

			case Types.DOUBLE:
				result = ((Double) value1).compareTo((Double) value);
				break;

			case Types.TIMESTAMP:
				result = ((Timestamp) value1).compareTo((Timestamp) value);
				break;

			case Types.DATE:
				result = ((java.sql.Date) value1)
						.compareTo((java.sql.Date) value);

				break;

			case Types.NUMERIC:
				result = ((BigDecimal) value1).compareTo((BigDecimal) value);
				break;

			case Types.DECIMAL:
				result = ((BigDecimal) value1).compareTo((BigDecimal) value);
				break;

			case Types.BIT:
				result = (((Boolean) value1).toString())
						.compareTo(((Boolean) value).toString());
				break;

			default:
				break;
			}
		}

		if (order) {
			return result;
		} else {
			return -result;
		}
	}

	public java.sql.Date getDate(String columnName,
			java.util.Calendar calendar) throws SQLException {
		
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void cancelRowUpdates() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public Ref getRef(int colnum) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public Ref getRef(String columnName) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.sql.Date getDate(int colnum, java.util.Calendar calendar)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public int getType() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.sql.Time getTime(int colnum, java.util.Calendar calendar)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.sql.Time getTime(String columnName,
			java.util.Calendar calendar) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBytes(String columnName, byte[] byteArray)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBytes(int colnum, byte[] byteArray) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public boolean rowDeleted() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public boolean rowInserted() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void insertRow() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateRow() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public Array getArray(String columnName) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public Array getArray(int _int1) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateTime(String columnName, java.sql.Time time)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateTime(int colnum, java.sql.Time time) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.sql.Statement getStatement() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public Timestamp getTimestamp(int colnum, java.util.Calendar calendar)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public Timestamp getTimestamp(String columnName,
			java.util.Calendar calendar) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.io.InputStream getAsciiStream(int colnum) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.io.InputStream getAsciiStream(String columnName)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.io.Reader getCharacterStream(int colnum) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.io.Reader getCharacterStream(String columnName)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public boolean absolute(int colnum) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public int getConcurrency() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public String getCursorName() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public int getFetchDirection() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public int getFetchSize() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.io.InputStream getUnicodeStream(int colnum) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.io.InputStream getUnicodeStream(String columnName)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.net.URL getURL(int colnum) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public java.net.URL getURL(String columnName) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public boolean isBeforeFirst() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void moveToCurrentRow() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void moveToInsertRow() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void refreshRow() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public boolean relative(int colnum) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public boolean rowUpdated() throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void setFetchDirection(int direction) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void setFetchSize(int size) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateAsciiStream(String columnName,
			java.io.InputStream inputStream, int intValue) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateAsciiStream(int int1, java.io.InputStream inputStream,
			int int2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBigDecimal(String columnName,
			java.math.BigDecimal bigDecimal) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBigDecimal(int int1, java.math.BigDecimal bigDecimal2)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBinaryStream(String columnName,
			java.io.InputStream inputStream, int int3) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBinaryStream(int int1,
			java.io.InputStream inputStream2, int int3) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBoolean(String columnName, boolean boolean2)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBoolean(int int1, boolean boolean2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateByte(String columnName, byte byte2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateByte(int int1, byte byte2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateCharacterStream(int int1, java.io.Reader reader,
			int int3) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateCharacterStream(String columnName,
			java.io.Reader reader2, int int3) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateDate(String columnName, java.sql.Date date2)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateDate(int int1, java.sql.Date date2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateDouble(String columnName, double double2)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateDouble(int int1, double double2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateFloat(int int1, float float2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateFloat(String columnName, float float2)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateInt(int int1, int int2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateInt(String columnName, int int2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateLong(int int1, long long2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateLong(String columnName, long long2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateNull(String columnName) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateNull(int int1) throws SQLException { 
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateObject(String columnName, Object object2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateObject(int int1, Object object2, int int3) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateObject(int intVal, Object _object2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateObject(String columnName, Object _object2, int _int3)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateShort(int intVal, short _short2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateShort(String columnName, short _short2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateString(int intVal, String strVal) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateString(String columnName, String strVal) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateTimestamp(String columnName, Timestamp _timestamp2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateTimestamp(int intVal, Timestamp _timestamp2) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateRef(int colnum, Ref _x) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateRef(String columnName, Ref _x) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBlob(int colnum, Blob _x) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateBlob(String columnName, Blob _x) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateClob(int colnum, java.sql.Clob _x)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateClob(String columnName, java.sql.Clob _x)
			throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateArray(int colnum, Array _x) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public void updateArray(String columnName, Array _x) throws SQLException {
		throw new SQLException(ERROR_NOT_IMPLEMENTED_YET);
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Reader getNCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getNCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob getNClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob getNClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNString(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNString(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T getObject(int arg0, Class<T> arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T getObject(String arg0, Class<T> arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public RowId getRowId(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public RowId getRowId(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML getSQLXML(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML getSQLXML(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void updateAsciiStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateBlob(String arg0, InputStream arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateClob(String arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNClob(String arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateNString(String columnLabel, String nString) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
