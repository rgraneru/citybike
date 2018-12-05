# List city bikes and their availability

To run you need to generate a Client-Identifier at https://developer.oslobysykkel.no/clients
and update src/main/resources/application.properties with your Client-Identifier

To compile type: mvn clean install

To run, either run Application
or use maven: mvn spring-boot:run

Alternativly, instead of updating application.properties, you can add the property at runtime: 

When running Application, add --Identifier=YOUR_IDENTIFIER_HERE   
or
If you use maven, type mvn spring-boot:run -Dspring-boot.run.arguments=--Identifier=YOUR_IDENTIFIER_HERE 






