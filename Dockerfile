FROM java:8

RUN mkdir -p /manza/app/transaction

WORKDIR /manza/app/transaction

COPY . /manza/app/transaction

EXPOSE 8080

CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/transaction.jar"]
