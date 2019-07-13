# warehouse
Suppose you are part of a scrum team developing data warehouse for Bloomberg to analyze FX deals. One of customer stories is to import deals details from files into DB. The requested performance is to be able to import the file containing 100,000 records in less than 5 seconds.

# setup
* Setup Mongo DB in your local environment.
* build the project using maven command "mvn clean install"
you can use below command if you want to skip the test 
mvn clean install -DskipTests
* check target folder if it have the jar file
* use below command to start the application 
java -jar 
* in case you want to generate Docker image use below:
sudo mvn package
sudo mvn docker:build 
