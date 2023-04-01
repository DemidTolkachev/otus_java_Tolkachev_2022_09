package ru.otus.crm.model;

import lombok.*;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("client")
public class Client implements Cloneable {

    @Id
    @NonNull
    private Long id;
    private String name;

    private String addressId;
    @MappedCollection(idColumn = "id")
    private Address address;

    @MappedCollection(idColumn = "client_id", keyColumn = "id")
    private List<Phone> phones;

    public String getOAddressId() {
        if (address != null) {
            return address.getId();
        }
        return null;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            this.addressId = getOAddressId();
        }
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        if (address != null) {
            this.addressId = getOAddressId();
        }
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.address, this.phones);
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + ", address=" + address + ", phones=" + phones + '}';
    }
}
