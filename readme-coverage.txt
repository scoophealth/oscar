A coverage report can be generated using:

mvn clean test jacoco:report

The Jacoco maven plugin uses the coverage data in target/jacoco.exec to 
create a report in target/site.  Look at target/site/jacoco/index.html
