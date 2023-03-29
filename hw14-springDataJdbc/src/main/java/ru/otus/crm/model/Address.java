package ru.otus.crm.model;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("address")
public class Address {
    @Id
    @NonNull
    @Getter
    private final String id;
    @Getter
    private final String street;

    @PersistenceCreator
    public Address(String id, String street) {
        this.id = (id == null) ? UUID.randomUUID().toString() : id;
        this.street = street;
    }

    @Override
    public Address clone() {
        return new Address(this.id, this.street);
    }

    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", street='" + street + '\'' + '}';
    }

}