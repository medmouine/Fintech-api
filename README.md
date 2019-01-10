[![Build Status](https://travis-ci.com/GLO4002UL/projet2018-eq3.svg?token=osUKSXp2YedetsxPAmp3&branch=master)](https://travis-ci.com/GLO4002UL/projet2018-eq3)

# Hot-dog Mast3rs - Team 3

| Name                | GitHub username   | ULaval email                    | NI          |
| ------------------- | ----------------- | -------------------------------:| ----------- |
| Jules Caron         | Caron300          | jules.caron.1@ulaval.ca         | 111 158 104 |
| Loup Labelle        | RagingCub         | loup.labelle.1@ulaval.ca        | 111 043 945 |
| Benjamin Matte-Jean | benjaminMatteJean | benjamin.matte-jean.1@ulaval.ca | 111 152 584 |
| Stéphanie Mercier   | Stephmercier      | stephanie.mercier.4@ulaval.ca   | 111 042 162 |
| Mohamed Mouine      | medmouine         | mohamed.mouine.2@ulaval.ca      | 111 159 279 | 
| Sunny Pelletier     | pelletier197      | sunny.pelletier.1@ulaval.ca     | 111 156 686 |
| Olivier Précourt    | OlivierPrecourt   | olivier.precourt.1@ulaval.ca    | 111 154 571 |
| Philippe Turcotte   | PhilTurcotte      | philippeTurcotte.4@ulaval.ca    | 111 160 625 |

## Completed stories
For now, we have completed the six first stories, which are :
- COOU - Ouvrir un compte avec des crédits
- TXAC - Effectuer un achat
- TXVE - Effectuer une vente
- TXFR - Appliquer des frais de transaction
- TXHO - Refuser les transactions en dehors des heures d'ouverture
- RTHI - Lister l'historique des transactions

## Modules structure
There are three [maven modules](https://maven.apache.org/guides/mini/guide-multiple-modules.html) in this project : 

 * stocks-api : An API that allows getting the value of stocks. It is provided as an "external service", so it **cannot be modified**. It also cannot be directly referred to in java either, its REST API must be used. See the documentation of this project through the stories for its use.
 * trading-api : The developed project.
 * application : Allows to start the 2 APIs simultaneously.

## External dependencies and special Permissions
### Springboot
All projects are built upon Springboot framework instead of Jersey. We made sure that both applications (Trading and Stocks API) were running on the same ports as expected. Since StocksAPI was already on springboot, it's still on port `8080` and TradingAPI runs on port `8181`.

### Lombok
This project uses [Lombok](https://projectlombok.org/) to generate code dynamically (Getters, Setters, Equals and Hashcode, Constructor, etc.). You might need to install a plugin so that Lombok functions will be recognized by your IDE, because those functions are generated at runtime, so they are not visible for the IDE.

## Travis Integration

This project is integrated with travis. See the build status badge at the top of this README.

## Starting the project

The two servers (`StocksServer` or `TradingServer`) can be started individually or simultaneously.

There are three classes that can be executed in intelliJ or Eclipse for that: `StocksServer`, `TradingServer` or `ApplicationServer`.
None of the `main()` require arguments.

The server can also be run through maven (**this is what will be used to grade the project**) : 

```bash
mvn clean install
mvn exec:java -pl application
```
