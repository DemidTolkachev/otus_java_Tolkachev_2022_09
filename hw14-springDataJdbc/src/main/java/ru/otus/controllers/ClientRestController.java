package ru.otus.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.webmodel.WebClient;
import ru.otus.services.ClientService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return clientService.findById(id);
    }

    @GetMapping("/api/client")
    public Client getClientByName(@RequestParam(name = "name") String name) {
        return clientService.findByName(name);
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody WebClient webclient) {
        List<Phone> phones = new ArrayList<>();
        if (webclient.phones() != null) {
            for (String strPhone : webclient.phones()) {
                phones.add(new Phone(null, strPhone));
            }
        }
        Client client = new Client(null, webclient.name(), new Address(null, webclient.street()), phones);
        return clientService.save(client);
    }

}
