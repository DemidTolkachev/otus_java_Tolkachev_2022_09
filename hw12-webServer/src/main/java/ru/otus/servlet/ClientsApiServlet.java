package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.io.IOException;
import java.util.ArrayList;

public class ClientsApiServlet extends HttpServlet {
    private final DbServiceClientImpl dbServiceClient;

    public ClientsApiServlet(DbServiceClientImpl dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletInputStream in = req.getInputStream();
        String str = new String(in.readAllBytes());
        Gson gson = new Gson();
        ClientFields clientFields = gson.fromJson(str, ClientFields.class);
        ArrayList<Phone> PhoneList = new ArrayList<>();
        for (String phone : clientFields.phones) {
            PhoneList.add(new Phone(null, phone));
        }
        Client client = new Client(null, clientFields.clientName, new Address(null, clientFields.street), PhoneList);
        dbServiceClient.saveClient(client);
        resp.setStatus(201);
    }

}
