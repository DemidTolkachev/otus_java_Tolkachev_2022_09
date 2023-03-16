package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        List<String> fieldsToSelect = fieldsToString(entityClassMetaData.getAllFields());
        return "select " + String.join(", ", fieldsToSelect) + " from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        Field idField = entityClassMetaData.getIdField();

        List<String> fieldsToSelect = fieldsToString(entityClassMetaData.getAllFields());
        return "select " + String.join(", ", fieldsToSelect) + " from " + entityClassMetaData.getName() + " where " +
                idField.getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        List<String> fieldsToInsert = fieldsToString(entityClassMetaData.getFieldsWithoutId());
        return "insert into " + entityClassMetaData.getName() + "(" + String.join(",", fieldsToInsert) + ")" +
                " values(" + String.join(",", Collections.nCopies(fieldsToInsert.size(), "?")) + ")";
    }

    @Override
    public String getUpdateSql() {
        Field idField = entityClassMetaData.getIdField();
        List<String> fieldsToUpdate = fieldsToString(entityClassMetaData.getFieldsWithoutId());
        for (int i = 0; i < fieldsToUpdate.size(); i++) {
            String newVal = String.format("%s = ?", fieldsToUpdate.get(i));
            fieldsToUpdate.set(i, newVal);
        }
        return "update " + entityClassMetaData.getName() + " set " + String.join(",", fieldsToUpdate) + " where " +
                idField.getName() + " = ?";
    }

    private List<String> fieldsToString(List<Field> fields) {
        return fields.stream().map(e -> e.getName().toLowerCase()).collect(Collectors.toList());
    }

}
