package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends CrudRepository<Address, String> {
    @Query("select * from address")
    List<Address> findAll();

    Address save(Address address);

    @Query("select * from address where id = :id")
    Optional<Address> findById(@Param("id") String id);

    @Query("select * from address where upper(street) = upper(:street)")
    Address findByName(@Param("street") String street);
}