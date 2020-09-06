package com.example.fivecontacts.main.model;

import android.widget.Switch;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String nome;
    String login;
    String senha,email;
    ArrayList<Contato> contatos;


    public User(String nome, String login, String password,String email)
    {
        this.nome = nome;
        this.login=login;//added
        this.senha=password;//added
        this.email=email;//added
        contatos=new ArrayList<Contato>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getLogin()
    {
        return login;
    }

    public String getSenha()
    {
        return senha;
    }

    public String getEmail()
    {
        return email;
    }

    public ArrayList<Contato> getContatos() {
        return contatos;
    }

}
