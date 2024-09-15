# GitMining

Project created to explore the GitHub API V3 and Git repositories.

Technologies:
- Java 17
- Maven

Steps to run the application:
- Clone the project
- Enter in the GitMining directory
- Build the project 
```
mvn clean install 
```
- Run the jar created in the target directory 
```
java -jar target/GitMining-1.0-SNAPSHOT-jar-with-dependencies.jar /path/local/repo
```
The user should inform a local path to a Git repository. For example:
```
java -jar target/GitMining-1.0-SNAPSHOT-jar-with-dependencies.jar C:\Users\Public\Downloads\repo\Mineracao-de-dados
```