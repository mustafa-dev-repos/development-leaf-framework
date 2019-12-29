package com.leaf.framework.jdbc.converter;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class PreparedResultSetMetaData implements ResultSetMetaData, Serializable {

	private static final long serialVersionUID = 6969457855975975942L;

    private String[] colName = null;

	private String[] colClassName = null;

    private int[] colType = null;

    private String[] colTypeName = null;
    
	private int[] colMaxLength = null;
    
    private int[] colPrecision = null;

    private int[] colScale = null;

    private boolean[] colSigned = null;

    public void setColName(String[] colName) {
        this.colName = colName;
    }

    public void setColType(int[] _columnType) {
    	this.colType = _columnType;
    }

    public void setColTypeName(String[] colTypeName) {
    	this.colTypeName = colTypeName;
    }

    public void setColDisplaySize(int[] colMaxLength) {
    	this.colMaxLength = colMaxLength;
    }

    public void setColPrecision(int[] colPrecision) {
    	this.colPrecision = colPrecision;
    }

    public void setColScale(int[] colScale) {
        this.colScale = colScale;
    }

    public void setColSigned(boolean[] colSigned) {
    	this.colSigned = colSigned;
    }

    public void setColClassName(String[] colClassName) {
        this.colClassName = colClassName;
    }

    public boolean isReadOnly(int _i) throws SQLException {
        return false;
    }

    public int getColumnCount() throws SQLException {
        return colName.length;
    }

    public String getColumnName(int i) throws SQLException {
        return colName[i - 1];
    }

    public int getColumnType(int i) throws SQLException {
        return colType[i - 1];
    }

    public String getTableName(int i) throws SQLException {
        return (String) null;
    }

    public String getCatalogName(int i) throws SQLException {
        return (String) null;
    }

    public String getColumnClassName(int i) throws SQLException {
        return colClassName[i - 1];
    }

    public int getColumnDisplaySize(int i) throws SQLException {
        return colMaxLength[i - 1];
    }

    public String getColumnLabel(int i) throws SQLException {
        return (String) null;
    }

    public String getColumnTypeName(int i) throws SQLException {
        return colTypeName[i - 1];
    }

    public int getPrecision(int i) throws SQLException {
        return colPrecision[i - 1];
    }

    public int getScale(int i) throws SQLException {
        return colScale[i - 1];
    }

    
    
    public String getSchemaName(int i) throws SQLException {
        return (String) null;
    }

    public boolean isAutoIncrement(int i) throws SQLException {
        return false;
    }

    public boolean isCaseSensitive(int i) throws SQLException {
        return false;
    }

    public boolean isCurrency(int i) throws SQLException {
        return false;
    }

    public boolean isDefinitelyWritable(int i) throws SQLException {
        return false;
    }

    public int isNullable(int i) throws SQLException {
        return 0;
    }

    public boolean isSearchable(int i) throws SQLException {
        return false;
    }
    
    public boolean isWritable(int i) throws SQLException {
        return false;
    }
    
    public boolean isSigned(int i) throws SQLException {
        return colSigned[i - 1];
    }
    public String[] getColNameList() {
        return colName;
    }

    public int[] getColTypeList() {
        return colType;
    }

    public String[] getColTypeNameList() {
        return colTypeName;
    }

    public int[] getColDisplaySizeList() {
        return colMaxLength;
    }

    public int[] getColPrecisionList() {
        return colPrecision;
    }

    public int[] getColScaleList() {
        return colScale;
    }

    public boolean[] getColSignedList() {
        return colSigned;
    }

    public String[] getColClassNameList() {
        return colClassName;
    }

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
