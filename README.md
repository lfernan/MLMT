# Mutantes - Mercado Libre

Proyecto evaluativo para Mercado Libre

## Descripción
La aplicación fue realizada con 
  - JDK 1.8
  - Spring Boot
  - Gradle
  - Firestore (https://firebase.google.com)
  - Lombok
  - Cloud AWS EC2
  - JUnit

### Serivicio
    EL endpoint tiene provisto un explorador de HAL Browser 
```shell
http://ec2-18-219-28-180.us-east-2.compute.amazonaws.com:8080/
```
> POST HTTP METHOD /mutant
```shell
Ejemplo JSON de peticion
{
  "dna": ["TCGT","AGTC","TTAT","TGAG"]
}
```
> GET HTTP METHOD /stats
```shell
Ejemplo JSON de respuesta
{
  "ratio":1.0,
  "count_mutant_dna":1,
  "count_human_dna":1
}
```
**Las respuestas del servicio pueden ser**

  - 200 El adn es mutante
  - 403 EL adn no es mutante
  - 501 La secuencia de adn es incorrecta o error del servicio
  - 400 Los parametros de entrada son incorrectos

## Test

https://mlmt-lfernan.s3.us-east-2.amazonaws.com/reports/jacoco/jacocoHtml/index.html

https://mlmt-lfernan.s3.us-east-2.amazonaws.com/reports/tests/test/index.html

## Autor

* **Leandro Fernández**
