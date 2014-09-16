N-Puzzle Concept
==========



Classes and methods:
-------------
* SplashActivity
	* splashscreen
* MainActivity 	
	* scherm met instellingen voor een nieuwe n-puzzle
	* heeft een horizontale gallery view van images die geselecteerd kunnen worden als puzzel
	* heeft een slider voor de moeilijkheidsgraad (1 tot 3)
	* heeft een button om het spel te starten met de aangewezen specificaties
	* ![Alt text](start_sketch.jpg)
* GameActivity 	
	* bestaat uit een deel met n tiles (3x3. 4x4. 5x5)
	* 
	* heeft een slider voor de moeilijkheidsgraad (1 tot 3)
	* heeft een button om het spel te restarten
	* heeft een button naar het menu activity
	* ![Alt text](game_sketch.jpg)
* MenuActivity
	* geeft een popup over het scherm van GameActivity
	* in de popup zijn vier buttons
	* Button Afbeelding gaat naar de MainActivity en laat de gebruiker een nieuwe afbeelding kiezen
	* Button Makkelijker start een nieuw game met, indien mogelijk, de puzzel een niveau lager
	* Button Makkelijker start een nieuw game met, indien mogelijk, de puzzel een niveau hoger
	* Button Exit sluit het spel af
	* ![Alt text](menu_sketch.jpg)
* EndofgameActivity 
	* Popup met tekst, knop gaat naar MainActivity
	* ![Alt text](endgame_sketch.jpg)


Databases:
-------------
*	State wordt opgeslagen in shared preferences.


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
