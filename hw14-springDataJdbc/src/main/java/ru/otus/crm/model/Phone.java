package ru.otus.crm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("phone")
@Data
@NoArgsConstructor
public class Phone {
    @Id
    @NonNull
    private String id;
    private String number;

    @PersistenceCreator
    public Phone(String id, String number) {
        this.id = (id == null) ? UUID.randomUUID().toString() : id;
        this.number = number;
    }

    @Override
    public Phone clone() {
        return new Phone(this.id, this.number);
    }

    @Override
    public String toString() {
        return "Phone{" + "id=" + id + ", number='" + number + '\'' + '}';
    }
}