package ru.otus.services;

import java.io.IOException;

public interface UserAuthService {
    boolean authenticate(String login, String password) throws IOException;
}
