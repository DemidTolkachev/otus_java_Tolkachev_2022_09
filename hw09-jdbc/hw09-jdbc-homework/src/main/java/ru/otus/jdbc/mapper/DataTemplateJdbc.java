package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.core.repository.DataTemplateException;

import java.sql.*;
import java.util.*;
import java.lang.reflect.Field;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createT(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        try {
            return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), rs -> {
                try {
                    List<T> result = new ArrayList();
                    while (rs.next()) {
                        result.add(createT(rs));
                    }
                    return result;
                } catch (SQLException e) {
                    throw new DataTemplateException(e);
                }
            }).get();
        } catch (NoSuchElementException nse) {
            return new ArrayList<>();
        }
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            List params = new ArrayList();
            for (Field field : (List<Field>) entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(object));
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            List params = new ArrayList();
            for (Field field : (List<Field>) entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(object));
            }
            params.add(entityClassMetaData.getIdField().get(object));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createT(ResultSet rs) {
        try {
            var constructor = entityClassMetaData.getConstructor();
            T t = (T) constructor.newInstance();
            for (Field field : (List<Field>) entityClassMetaData.getAllFields()) {
                field.setAccessible(true);
                field.set(t, rs.getObject(field.getName()));
            }
            return t;
        } catch (Exception e) {
            return null;
        }

    }
}
