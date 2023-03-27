package ru.otus.servlet;

import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.services.TemplateProcessor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ClientsServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_ALL_CLIENTS = "allClients";

    private final TemplateProcessor templateProcessor;
    private final DbServiceClientImpl dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DbServiceClientImpl dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        var clientList = dbServiceClient.findAll();
        paramsMap.put(TEMPLATE_ATTR_ALL_CLIENTS, clientList);


        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

}
