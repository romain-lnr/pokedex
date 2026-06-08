# PokéDex Android
Application Android qui affiche une liste de Pokémon récupérée depuis l'API publique PokéAPI, avec un écran de détail, une recherche, un filtre par type et un système de favoris.
Projet réalisé en Kotlin avec Android Studio.

# Fonctionnalités
## Requises

Écran liste : les Pokémon sont chargés depuis l'API (nom + image/sprite), affichés dans une liste défilante (RecyclerView).
Écran détail : au clic sur un Pokémon, ouverture d'un second écran montrant son image agrandie, ses type(s) et ses statistiques (HP, Attaque), avec un bouton de retour.
Navigation entre les deux écrans via Intent (l'id du Pokémon est passé à l'écran détail).
Gestion d'erreurs : si le réseau est absent, un message clair est affiché au lieu d'un crash.

## Bonus

Recherche par nom : filtre la liste en temps réel pendant la frappe.
Filtre par type : menu déroulant (Feu, Eau, Plante…) qui ne montre que les Pokémon du type choisi.
Favoris : étoile sur chaque Pokémon, sauvegardés en local (ils persistent après fermeture de l'app) + case « Favoris seulement ».
Écran de démarrage animé : titre « PokéDex » qui apparaît en fondu et zoom au lancement.

# Comment lancer le projet

Ouvrir le projet dans Android Studio (File > Open puis sélectionner le dossier du projet).
Synchroniser Gradle : Aller dans <File> (Sync Project with Gradle Files) ou sur « Sync Now » si la barre apparaît.
Choisir un appareil : un émulateur (Android 7.0 ou plus) ou un téléphone physique en mode développeur.
Vérifier la connexion Internet de l'appareil (l'application a besoin du réseau pour appeler l'API).
Lancer avec le bouton ▶ (Run 'app').


⚠️ Une connexion Internet est nécessaire : l'application récupère toutes ses données depuis PokéAPI, rien n'est codé en dur.


# Endpoints utilisés

Liste des Pokémon : GET /api/v2/pokemon?limit=20
Détail d'un Pokémon : GET /api/v2/pokemon/{id}
Pokémon par type : GET /api/v2/type/{type}


# Structure du projet
app/src/main/
├── java/com/example/pokedex/
│   ├── SplashActivity.kt      # Écran de démarrage animé (lancé en premier)
│   ├── MainActivity.kt        # Écran liste : API, recherche, filtre, favoris
│   ├── DetailActivity.kt      # Écran détail : types + statistiques
│   ├── PokemonAdapter.kt      # Adapter du RecyclerView (lignes + étoile favori)
│   ├── Pokemon.kt             # data class (liste, détail, type)
│   ├── PokeApiService.kt      # Interface Retrofit (appels API)
│   ├── RetrofitClient.kt      # Configuration de Retrofit
│   └── FavoritesManager.kt    # Lecture/écriture des favoris (SharedPreferences)
└── res/layout/
    ├── activity_splash.xml
    ├── activity_main.xml
    ├── activity_detail.xml
    └── item_pokemon.xml       # Une ligne de la liste

# Gestion des erreurs

En cas d'échec (pas de réseau), l'écran liste affiche un message au lieu de planter.
Un timeout court (5 s) est configuré pour ne pas faire attendre l'utilisateur indéfiniment.