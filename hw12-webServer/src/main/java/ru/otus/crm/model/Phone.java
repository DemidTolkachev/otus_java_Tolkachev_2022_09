package ru.otus.crm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "phone")
public class Phone implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone() {
    }

    public Phone(String number) {
        this.number = number;
    }

    public Phone(Long id, String number) {
        if (id != null) {
            this.id = id;
        }
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
