package ru.otus.crm.webmodel;

import java.util.List;

public record WebClient(String name, String street, List<String> phones) {
}

