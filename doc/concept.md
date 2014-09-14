N-Puzzle Concept
==========

Database tables and fields:
-------------
*	State wordt opgeslagen in shared preferences.


Classes and methods:
-------------
* SplashActivity
	* splashscreen
* MainActivity 	
	* scherm met instellingen voor een nieuwe n-puzzle
	* heeft een horizontale gallery view van images die geselecteerd kunnen worden als puzzel
	* heeft een slider voor de moeilijkheidsgraad (1 tot 3)
	* heeft een button om het spel te starten met de aangewezen specificaties
	* ![Alt text](/start_sketch.jpg)
* GameActivity 	
* EndofgameActivity 
	* Popup met score en naam invoeren, slaat deze dan op in SQLite Database.
	* ![Alt text](/endgame_sketch.jpg)




Styleguide:
==========
Comments
-------------
Comments van één regel worden als volgt gedaan:
```
//dit is een comment
```
Comments van meerdere regels worden anders gedaan:
```
/*
 * Dit is een comment.
 * Met meerdere regels.
 */
```
Brackets
-------------
```
int voorbeeld(int a)
{
    return a;
}
```
