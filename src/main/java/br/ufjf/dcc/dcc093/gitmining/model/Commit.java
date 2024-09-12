/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufjf.dcc.dcc093.gitmining.model;

public class Commit {

    private final String hash;
    private final String committerName;
    private final String committerEmail;
    private final String message;

    public Commit(String hash, String committerName, String committerEmail, String message) {
        this.hash = hash;
        this.committerName = committerName;
        this.committerEmail = committerEmail;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Commit{" + "hash=" + hash + ", committerName=" + committerName + ", committerEmail=" + committerEmail + '}';
    }

    public String getHash() {
        return hash;
    }

    public String getCommitterName() {
        return committerName;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public String getMessage() {
        return message;
    }
}
