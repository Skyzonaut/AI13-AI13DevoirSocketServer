### Application de messagerie serveur Java Socket

Cette application est la partie serveur d'une application de messagerie plus large composée de deux fichiers JAR exécutables et de packages associés :
- Côté Serveur
- Côté Client

L'application actuelle créera un Serveur Socket et attendra les connexions des clients via des Connexions Socket.
Ensuite, elle gérera les connexions entrantes et diffusera les messages des clients aux autres clients connectés.

### Lancement

Pour lancer le serveur, allez simplement dans le répertoire principal.
Puis exécutez cette commande :

```bash
java -jar DevoirSocket.jar
```

Le serveur sera lancé par défaut sur `localhost` sur le port `10810`.

Cependant, il est possible de sélectionner le port sur lequel nous voulons lancer le serveur.

Pour ce faire, ajoutez simplement le port en paramètre à la commande de lancement :

```bash
java -jar DevoirSocket.jar <port>
```

### Exceptions

Si un client se déconnecte volontairement en entrant la commande `exit`,
le serveur affichera un message sur sa sortie standard et diffusera aux clients que l'utilisateur s'est déconnecté.

Si un client se déconnecte de manière inappropriée (Ctrl-C / Ctrl-Q), le serveur capturera l'erreur de connexion et l'affichera, en plus de notifier la déconnexion aux autres clients.
