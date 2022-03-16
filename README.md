# Description du project Gestimo 
On souhaite créer une application web qui permet de gérer des locations des bien immobiliers.
Un propriétaire possède un ou plusieurs biens immobiliers (Villa, immeuble, appartement, studio)
Un locataire peut louer un bien immobilier sur une période donnée et paie un loyer qui est susceptible d’évoluer dans le temps. 
Chaque bien immobilier possède une adresse.
Une agence immobilier est chargé de mettre en relation les bien du propriétaire et un locataire créant ainsi un contrat de bail
Chaque client est défini par son code et son nom et son numéro de téléphone
Chaque mois le système effectue un appel de loyers dans le quelle chaque locataire reçoit une forme de quittance
## Exigences fonctionnelles
L’application doit permettre de :
1. Gérer les Locataires :
- Ajouter un Locataire
- Consulter tous les Locataires
- Consulter les Locataires par nom, code ou id.
2. Gérer les Biens Immobiliers :
- Ajouter un bien
- Consulter un bien
-Lister tous les bien immobilier par adresse
3. Gérer les baux :
- Ajouter des baux
- Modifier le montant du loyer
- Effectuer des appels de loyers
- Effectuer un paiement de loyer
- Consulter les paiements de loyers par locataires, immeuble, adresse
-Modifier  
## Exigences Techniques
1. Les données sont stockées dans une base de données MySQL
2. L’application se compose de trois couches :
- La couche DAO qui est basée sur Spring Data, JPA, Hibernate et JDBC.
- La couche Métier
- La couche Web basée sur MVC coté Serveur en utilisant Thymeleaf.
3. La sécurité est basée sur Spring Security d’authentification
## Architecture technique
![architecture](/assests/images/Architecture.PNG)
## Diagramme de classes des entités
![class diagram](/assests/images/classdiagram.PNG)


