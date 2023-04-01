package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query("select * from client")
    List<Client> findAll();

    Client save(Client client);

    @Query("select * from client where id = :id")
    Optional<Client> findById(@Param("id") long id);

    @Query("select * from client where upper(name) = upper(:name)")
    Client findByName(@Param("name") String name);
}