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
java -jar target/GitMining-1.0-SNAPSHOT-jar-with-dependencies.jar kind_of_analysis repository_information [GitHub_Token]
```
The kind_of_analysis can be **remote** or **local**. 
- For **remote** analysis, the user should inform the URL to 
a valid project on GitHub. In the case of private projects, the user needs to give a GitHub_Token. For instance:
```
java -jar target/GitMining-1.0-SNAPSHOT-jar-with-dependencies.jar remote https://github.com/voldemort/voldemort
```
- For **local** analysis, the user should inform a local path to a Git repository. For example:
```
java -jar target/GitMining-1.0-SNAPSHOT-jar-with-dependencies.jar "/media/gleiph/Gleiph dados/repositories/voldemort"
```

If you have any doubts or suggestions, please let me know by sending an email to gleiph@ice.ufjf.br. 

 
