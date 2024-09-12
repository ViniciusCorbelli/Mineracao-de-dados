/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufjf.dcc.dcc093.gitmining.model;

public class File {

    private final String path;

    public File(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "File{" + this.path + '}';
    }

    public String getPath() {
        return this.path;
    }
}
