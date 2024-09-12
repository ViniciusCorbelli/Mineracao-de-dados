package br.ufjf.dcc.dcc093.gitmining;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufjf.dcc.dcc093.gitmining.cli.CLIExecute;
import br.ufjf.dcc.dcc093.gitmining.cli.CLIExecution;
import br.ufjf.dcc.dcc093.gitmining.git.cli.GitCLI;
import br.ufjf.dcc.dcc093.gitmining.model.Commit;
import br.ufjf.dcc.dcc093.gitmining.model.File;

public class GitMining {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Please inform the repository path.");
            return;
        }

        CLIExecute executer = new CLIExecute(args[0]);
        GitCLI gitCLI = new GitCLI(args[0]);

        List<Commit> commits = gitCLI.log();

        try (FileWriter csvWriter = new FileWriter("commits.csv")) {
            csvWriter.append("Commit Hash|Autor|Nome|Inserções|Deleções|Diferença de Porcentagem\n");

            for (Commit commit : commits) {
                List<File> files = gitCLI.show(commit.getHash());

                for (File file : files) {
                    CLIExecution cat = executer.execute("git show --stat " + commit.getHash() + " " + file.getPath());
                    List<String> output = cat.getOutput();

                    if (!output.isEmpty()) {
                        String lastLine = output.get(output.size() - 1);

                        Pattern patternInsertions = Pattern.compile("(\\d+) insertions?\\(\\+\\)");
                        Pattern patternDeletions = Pattern.compile("(\\d+) deletions?\\(-\\)");

                        Matcher matcherInsertions = patternInsertions.matcher(lastLine);
                        Matcher matcherDeletions = patternDeletions.matcher(lastLine);

                        int insertions = 0;
                        int deletions = 0;

                        if (matcherInsertions.find()) {
                            insertions = Integer.parseInt(matcherInsertions.group(1));
                        }

                        if (matcherDeletions.find()) {
                            deletions = Integer.parseInt(matcherDeletions.group(1));
                        }

                        int totalChanges = insertions + deletions;
                        if (totalChanges > 0) {
                            double percentageDifference = Math.abs(insertions - deletions) / (double) totalChanges * 100;

                            if (percentageDifference <= 10) {
                                DecimalFormat df = new DecimalFormat("#.00");
                                csvWriter.append(commit.getHash()).append('|')
                                        .append(commit.getCommitterEmail()).append('|')
                                        .append(commit.getCommitterName()).append('|')
                                        .append(String.valueOf(insertions)).append('|')
                                        .append(String.valueOf(deletions)).append('|')
                                        .append(df.format(percentageDifference)).append('\n');
                                break;
                            }
                        }
                    }
                }
            }

            System.out.println("Arquivo CSV criado com sucesso: commits.csv");
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo CSV: " + e.getMessage());
        }
    }
}
