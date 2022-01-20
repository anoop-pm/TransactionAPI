From openjdk:8
copy ./target/TransactionValidapi-0.0.1-SNAPSHOT.jar TransactionValidapi-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","TransactionValidapi-0.0.1-SNAPSHOT.jar"]