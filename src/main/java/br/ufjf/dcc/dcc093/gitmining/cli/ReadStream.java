/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufjf.dcc.dcc093.gitmining.cli;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gleiph
 */
public class ReadStream implements Runnable {

    /**
     * @return the output
     */
    public List<String> getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(List<String> output) {
        this.output = output;
    }
    InputStream is;
    Thread thread;      
    private List<String> output;
    public ReadStream(InputStream is) {
        
        this.is = is;
        this.output = new ArrayList<>();
    }       
    public void start () {
        thread = new Thread (this);
        thread.start ();
    }       
    public void run () {
        try {
            InputStreamReader isr = new InputStreamReader (is);
            BufferedReader br = new BufferedReader (isr);   
            while (true) {
                String s = br.readLine ();
                if (s == null) break;
                getOutput().add(s);
            }
            is.close ();    
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
    }
}
