package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.crm.model.Client;
import ru.otus.repository.AddressRepository;
import ru.otus.repository.ClientRepository;

import java.util.List;
import java.util.Random;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;

    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clientList = clientRepository.findAll();
        for (Client c : clientList) {
            if (c.getAddressId() != null) {
                c.setAddress(addressRepository.findById(c.getOAddressId()).get());
            }
        }
        return clientList;
    }

    @Override
    public Client findById(long id) {
        Client client;
        client = clientRepository.findById(id).get();
        if (client.getAddressId() != null) {
            client.setAddress(addressRepository.findById(client.getOAddressId()).get());
        }
        return client;
    }

    @Override
    public Client findByName(String name) {
        return clientRepository.findByName(name);
    }

    @Override
    public Client findRandom() {
        List<Client> clients = clientRepository.findAll();
        Random rnd = new Random();
        return clients.stream().skip(rnd.nextInt(clients.size() - 1)).findFirst().orElse(null);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
