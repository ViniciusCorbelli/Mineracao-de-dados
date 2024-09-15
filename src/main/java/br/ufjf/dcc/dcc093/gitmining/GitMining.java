package br.ufjf.dcc.dcc093.gitmining;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.ufjf.dcc.dcc093.gitmining.cli.CLIExecute;
import br.ufjf.dcc.dcc093.gitmining.cli.CLIExecution;
import br.ufjf.dcc.dcc093.gitmining.git.cli.GitCLI;
import br.ufjf.dcc.dcc093.gitmining.model.Commit;
import br.ufjf.dcc.dcc093.gitmining.model.File;

public class GitMining {

    private static final Set<String> IGNORED_EXTENSIONS = Set.of(
            "md", "yml", "yaml", "json", "xml", "properties", "csv",
            "log", "tmp", "bak", "swp", "class", "jar", "war", "ear",
            "sqlite", "db", "pem", "crt", "p12", "key", "classpath",
            "jsp", "prefs", "txt", "sql", "tag", "jmx", "html", "js",
            "springBeans", "iml", "cmd", "springjavaformatconfig", "lock",
            "editorconfig", "markdown", "dtd"
    );

    private static final Set<String> IGNORED_FILES = Set.of(
            "README", ".gitignore", "pom.xml", "build.gradle", "package.json",
            "LICENSE", "gradlew", "Makefile", ".springBeans"
    );

    private static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : filePath.substring(lastDotIndex + 1).toLowerCase();
    }

    private static String getFileName(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf('/');
        return (lastSlashIndex == -1) ? filePath : filePath.substring(lastSlashIndex + 1);
    }

    private static boolean isIgnoredFile(String filePath) {
        String extension = getFileExtension(filePath);
        String fileName = getFileName(filePath);

        return IGNORED_EXTENSIONS.contains(extension) || IGNORED_FILES.contains(fileName);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Please inform the repository path.");
            return;
        }
        System.out.println("Programa em execucao, por favor aguarde.");
        CLIExecute executer = new CLIExecute(args[0]);
        GitCLI gitCLI = new GitCLI(args[0]);

        List<Commit> commits = gitCLI.log();

        try (FileWriter csvWriter = new FileWriter("commits.csv")) {
            csvWriter.append("Commit Hash,Autor,Nome,Inserções,Deleções,Diferença de Porcentagem\n");

            char[] loading = {'\\','-','|','/','|'};
            int loadIndex = 0;
            for (Commit commit : commits) {
                System.out.print(loading[loadIndex]);
                loadIndex++;
                if (loadIndex >= loading.length) {
                    loadIndex = 0;
                }

                List<File> files = gitCLI.show(commit.getHash());

                analysingFiles(commit, files, executer, csvWriter);

                System.out.print("\r");
                System.out.print("                    ");
                System.out.print("\r");
                System.out.flush();
            }

            System.out.println("Arquivo criado com sucesso: commits.csv");
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo: " + e.getMessage());
        }
    }

    private static void analysingFiles(Commit commit, List<File> files, CLIExecute executer, FileWriter csvWriter) throws IOException {
        for (File file : files) {
            if (isIgnoredFile(file.getPath())) {
                continue;
            }

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
                        String line = (commit.getHash() != null ? commit.getHash() : "") +
                                '|' +
                                (commit.getCommitterEmail() != null ? commit.getCommitterEmail() : "") +
                                '|' +
                                (commit.getCommitterName() != null ? commit.getCommitterName() : "") +
                                '|' +
                                insertions +
                                '|' +
                                deletions +
                                '|' +
                                String.format("%.2f", percentageDifference) +
                                '\n';

                        line = line.replace(',','.');
                        line = line.replace('|',',');
                        csvWriter.append(line);
                        break;
                    }
                }
            }
        }
    }

}
