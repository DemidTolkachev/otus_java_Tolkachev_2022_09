package ru.otus.services;


import java.io.*;
import java.util.Properties;

public class UserAuthServiceImpl implements UserAuthService {
    public final String resource;

    public UserAuthServiceImpl(String resource) {
        this.resource = resource;
    }

    @Override
    public boolean authenticate(String login, String password) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resource).getFile());
        Properties props = new Properties();
        props.load(new FileInputStream(file));
        return props.getProperty("admin.login").equals(login) && props.getProperty("admin.password").equals(password);
    }

}
