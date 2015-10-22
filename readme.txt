Version 1.0, la version avec UDP Client dans la Service.
Version 1.1,
 l.UDP Client devient dans la IntentService, il peut communiquer avec le Raspberry Pi m¨ºme si l¡¯Application Android est minimis¨¦e.

Version 1.2,
 1.Service et IntentService sont quasiment complets. On peut les arreter en utilisant le stopService.
 2.UDP envoi et recevoir toutes les 1 ms complet.

version release 1.0:
1. connection udp fini.
2. quand on perd de connection et on est en mode Myo, une alarme va lancer pour que l'utilisateur sache.
3. quand on perd de connection, le systeme va retourner vers l'interface de connect et sortir un toast qui indique la connection est perdue.

version test:
Essais sur les threads dans une seule service, il n¡¯a pas marche.

Version release 2.0,
update:
1. StandardControl et MyoControl devient dans la meme service IntentService.
2. Bug fixe.

Version release 2.1,
update:
Bug fixe
Ajouter le fonctionnement de Gravite.

version release 3.0, myo marche bien et bug fixe.
version release 3.2, correction et bug fixe.
version release 3.3, bug fixe.
version release 3.5, bug fixe et initialisation de reference en mode standard et changement de alert.mp3
version release  3.5.1, petite conrrection.
version release 3.5.2: commenter les fonctions.
version release 3.5.2: commenter les fonctions.
version release 3.6.1, update le fonctionnement du button stop.
version release 3.7, update texte qui affiche les r¨¦ponses de Raspberry Pi.
version final 3.8, ajouter l'interface de s¨¦lection de WiFi pour les deux applications.