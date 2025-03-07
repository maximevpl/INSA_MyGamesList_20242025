#Projet réalisé en 3 ème année à l'INSA de Rennes en spécialité informatique dans le cadre de l'option programmation mobile.

Objectif : Découverte du language kotlin et de la programmation mobile à l'aide de Android Studio avec la bibliothèque Jetpack Compose.

Séance 1 : Découverte du language kotlin.

Séance 2 : Découverte d'Android Studio et de la bibliothèque Jetpack Compose.
           Amélioration du fichier IGDB afin de récupérer tous les élements utiles dans les fichiers JSON.
           Création des fonctions GameListScreen et GameItem afin d'afficher la page d'acceuil, composée de plusieurs box qui détaillent un jeu, le tout affiché en colonnes. 
           
Séance 3 : Découverte et dévoloppement de la navigation entre les pages.
           Lors d'un clique sur un jeu, une autre page est ouverte avec le détail du jeu et il est possible de retourner sur la page d'acceuil à l'aide d'une flèche ou du bouton back.

Séance 4 : Développement du second écran qui détail un jeu : affichage des données contenues dans les fichiers JSON récupérées par des focntions composables.
           Création de la fonction GameDetailScreen et des fonctions de getteur pour récupérer les données.
           Création d'une liste déroulante pour afficher les logos des plateformes, 
           Bonus : mise en place d'un font noir afin que chaque photo aient la même forme (je n'ai pas réussi à centrer la liste sur la page lorsque le nombre de logo est restreint).

Séance 5 : Mise en place d'une barre de recherche dans l'AppBar de l'écran d'acceuil.
           Sauvegarde de la recherche lors d'un retour en arrière.
           Gestion des données entrées à l'aide de filtres et d'un message d'erreur si la recherche ne correspond à aucune réponse.
           Réduction de la barre de recherche à un icon dans l'AppBar, lors d'un clique sur l'icon, la barre de recherche s'affiche.
           Bonus : ajout d'une flèche "back" dans la barre de recherche qui vide la recherche si un texte à été écrit et qui ferme la barre de recherche si aucun texte n'est écrit.

Séance 6 : Ajout d'un widget "favoris" sur chaque box de la page principale et sur la page de détail de chaque jeu.
           Si un clique est effectué, l'icon change et ce changement est conservé lors de la naviguation.
