package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;
import ru.otus.cachehw.MyCache;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        Map<Integer, Client> map = new HashMap<>();

// Работа с клиентом
        EntityClassMetaData entityClassMetaDataClient = new EntityClassMetaDataImpl<>(new Client());
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient,
                entityClassMetaDataClient); //реализация DataTemplate, универсальная
        MyCache<String, Client> cache = new MyCache<>();

// Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, cache);
        //dbServiceClient.saveClient(new Client("dbServiceFirst"));

        //var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        for (int i = 0; i < 3; i++) {
            map.put(i, dbServiceClient.saveClient(new Client("dbService" + i)));
        }
        //var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
        //.orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        //log.info("clientSecondSelected:{}", clientSecondSelected);
        for (int i = 0; i < 3; i++) {
            var cl = map.get(i);
            var clientSelected = dbServiceClient.getClient(cl.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + cl.getId()));
            log.info("clientSelected{}:{}", i, clientSelected);
        }

// Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData entityClassMetaDataManager = new EntityClassMetaDataImpl(new Manager());
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager =
                new DataTemplateJdbc<Manager>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager.getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure().dataSource(dataSource).locations("classpath:/db/migration").load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
