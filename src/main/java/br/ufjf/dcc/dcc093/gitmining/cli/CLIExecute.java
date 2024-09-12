package br.ufjf.dcc.dcc093.gitmining.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLIExecute {

    private String directory;

    public CLIExecute(String directory) {
        this.directory = directory;
    }

    public CLIExecution execute(String command) throws IOException {

        CLIExecution execution = new CLIExecution();

        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec(command, null,
                new File(this.directory));

        String s;

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

        while ((s = stdInput.readLine()) != null) {
            execution.addOutput(s);
        }

        while ((s = stdError.readLine()) != null) {
            execution.addError(s);
        }

        return execution;
    }

    public CLIExecution execute(String[] command) throws IOException {

        CLIExecution execution = new CLIExecution();

        Runtime runtime = Runtime.getRuntime();
        Process exec = runtime.exec(command, null,
                new File(this.directory));

        String s;

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

        if (stdInput.ready()) {
            while ((s = stdInput.readLine()) != null) {
                execution.addOutput(s);
            }
        }

        if (stdError.ready()) {
            while ((s = stdError.readLine()) != null) {
                execution.addError(s);
            }
        }
        return execution;
    }

    public CLIExecution executeParallel(String command) throws IOException {

        try {
            CLIExecution execution = new CLIExecution();

            Runtime runtime = Runtime.getRuntime();
            Process exec = runtime.exec(command, null,
                    new File(this.directory));

            ReadStream in = new ReadStream(exec.getInputStream());
            ReadStream er = new ReadStream(exec.getErrorStream());
            in.start();
            er.start();
            exec.waitFor();

            execution.setError(er.getOutput());
            execution.setOutput(in.getOutput());
            return execution;
        } catch (InterruptedException ex) {
            return new CLIExecution();
        }
    }
}
