/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufjf.dcc.dcc093.gitmining.git.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufjf.dcc.dcc093.gitmining.cli.CLIExecute;
import br.ufjf.dcc.dcc093.gitmining.cli.CLIExecution;
import br.ufjf.dcc.dcc093.gitmining.model.Commit;
import br.ufjf.dcc.dcc093.gitmining.model.File;

public class GitCLI {

    private CLIExecute cliExecute;

    public GitCLI(String directory) {
        this.cliExecute = new CLIExecute(directory);
    }

    public List<Commit> log() throws IOException {
        String command = "git log --all --pretty=\"format:'%H''%cn''%ce''%s'\"";

        List<Commit> commits = new ArrayList<>();
        CLIExecution execute = cliExecute.execute(command);

        if (!execute.getError().isEmpty()) {
            throw new RuntimeException("The path does not have a Git Repository.");
        }

        for (String line : execute.getOutput()) {
            int hashBegin = line.indexOf("'");
            int hashEnd = line.indexOf("'", hashBegin + 1);
            String hash = line.substring(hashBegin + 1, hashEnd);

            int commiterNameBegin = line.indexOf("'", hashEnd + 1);
            int commiterNameEnd = line.indexOf("'", commiterNameBegin + 1);
            String commiterName = line.substring(commiterNameBegin + 1, commiterNameEnd);

            int commiterEmailBegin = line.indexOf("'", commiterNameEnd + 1);
            int commiterEmailEnd = line.indexOf("'", commiterEmailBegin + 1);
            String commiterEmail = line.substring(commiterEmailBegin + 1, commiterEmailEnd);

            int messageBegin = line.indexOf("'", commiterEmailEnd + 1);
            int messageEnd = line.lastIndexOf("'");
            String message = line.substring(messageBegin + 1, messageEnd);

            Commit commit = new Commit(hash, commiterName, commiterEmail, message);
            commits.add(commit);
        }
        return commits;
    }

    public List<File> show(String hash) throws IOException {
        String parentHashCommand = "git log --pretty=%P -n 1 " + hash;

        CLIExecution parentHashExecution = cliExecute.execute(parentHashCommand);

        String parentHashes = "";
        for (String line : parentHashExecution.getOutput()) {
            parentHashes = line;
        }

        String command = "git diff --name-only --diff-filter=AM " + parentHashes + " " + hash;

        List<File> files = new ArrayList<>();

        CLIExecution execute = cliExecute.execute(command);

        for (String line : execute.getOutput()) {
            File file = new File(line);
            files.add(file);
        }
        return files;
    }

}
