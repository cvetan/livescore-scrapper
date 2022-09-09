# livescore-scrapper

generisano korišćenjem Luminus frejmvorka verzija "4.40"


### Opis
Pomoću Luminus frejmvorka i korišćenjem reitit swagger podrške sam napravio API za sportske rezultate

Na samom API-ju su izložene CRUD operacije za sportove i takmičenja, gde se praktično unose linkovi sa sajta https://www.flashscore.com

Iz sitemapa na samom sajtu se izvlače sportovi i takmičenja, ali neka popularnija takmičenja poput Engleske premier lige nisu u sitemap, pa se ona mogu manuelno dodati kroz API

Za ovako dodata takmičenja dva glavna endpointa su oni za tabele i rezultate i oni su praktično proxy između API-ja i sajta, a crawler je implementiran preko `etaoin` headless browsera 

Svaka API operacija je opisana na swaggeru, tako da verujem da ćete steći sliku kratkim pregledom specifikacije

Struktura baze podataka je implementirana u migracijama


## Zahtevi

Da bi se aplikacija pokrenula potrebno je sledeće
* leiningen
* mysql (mariadb) baza podataka
* geckodriver binary na sistemskoj putanji, jer je `etaoin` browser implementiran za firefox headless driver(ja sam na linuxu samo skinuo binary odavde https://github.com/mozilla/geckodriver/releases i postavio ga na user path)


## Pokretanje

Pre pokretanja potrebno je izvršiti migracije:

    lein run migrate
    
A zatim da bi se aplikacija pokrenula

    lein run -p 8000

Zatim na https://localhost:8000 može se videti Swagger UI i vršiti interakcija sa API-jem


