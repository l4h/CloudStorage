package com.gb.cs.common;

public class AuthMess implements Message {
    final private String login, password;
    final private CommandType command = CommandType.AUTH;
    private AuthStatus authOk;


    public AuthMess(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin(){
        return login;
    }

    public String[] getCred() {
        return new String[]{login, password};
    }

    public AuthMess setAuthOk(AuthStatus authOk) {
        this.authOk = authOk;
        return this;
    }

    public AuthStatus getAuthStatus(){
        return authOk;
    }

    @Override
    public CommandType getCommand() {
        return command;
    }
}
