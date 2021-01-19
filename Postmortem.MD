# Postmortem Loggbok
# 1. Inledning
Jag har programmerat en loggbok i Java.
# 1.1 Syfte/Mål
Syftet och målet med arbetet var att loggboken är att de centrala delarna ska fungera.
Loggbokens centrala delar är att kunna skriva anteckningar med författare, datum och att detta finns kopplat till en databas.
Jag har gjort mer än så.
# 1.2 Arbetssätt och genomförande
Jag började med att göra ett litet klassdiagram och tänka igenom vilka klasser jag ska börja med.
Sedan började jag skriva koden. Jag började med att programmera de centrala delarna i loggboken.
Jag kommenterade också en del i koden för att komma ihåg vad jag har gjort och ska göra härnäst.
När det centrala var klart så började jag skriva fler funktioner som inte är nödvändiga men för att göra loggboken bättre och mer användbar.
En del av programmet skrev jag i enskilda klasser även fast jag kunde och använt mvc i mina privata projekt innan.
Sedan när läraren sa att vi skulle använda MVC så skrev jag om allt till MVC så att en controller kontrollerar modellen och designen och att koden är separerad.
Att använda mvc gör så att det blir mer organiserat att arbeta tycker jag.
Bland annat så har jag gjort en DBManager som kan användas i vilket Java program som helst för framtida databasprogram.
Loggboken har bland annat dessa funktioner:
<br>
Skapa ny logg
<br>
Redigera logg
<br>
Folk kan inte redigera samma log samtidigt (förutom admin)
<br>
Ta bort logg från db (krävs admin)
<br>
Printa ut alla loggar från databas (med ID, Author, Editors, Body tags)
<br>
Printa ut all logghistorik från databas (med ID, Log ID, Author, Editor, Body, Created At, Last Edited, Type Of Change)
<br>
Ta bort alla loggar från databas (krävs admin)
<br>
Ta bort all logg historik (krävs admin)
<br>
Filtrera loggar utifrån author eller body nyckelord
<br>
Spara loggar som finns i textrutan till fil.
<br>
Printa loggar från fil.
<br>
Logga ut från loggboken. Då kan man logga in/registrera sig
<br>
Säkerhetsloggar med login loggar med tags ID, Username, Admin, Success och Date & time
<br>
Ta bort alla säkerhetsloggar (krävs admin)
<br>
Admin system så att bara admins kan ta bort loggar osv
(admin för en viss user kan förnuvarande bara ändras till true i databasen icke i programmet)
<br>
Login system med username och password, password hashas med sha256 så att det
inte kan crackas lätt, men man kan kolla att det stämmer. Den kollar om input pass hashen stämmer med den i databasen.
<br>
Man kan även logga in som anonym
<br>
Registreringssystem, username och password hashas och läggs in i db
<br>
Om mysql servern är nere så stängs programmet
<br>
Om mysql databas inte finns så skapar programmet
den med alla tables så att användaren slipper.
<br>
Med mera orkar inte skriva ut fler funktioner
# 1.3 Bakgrund
Jag har använt min dator med IntelliJ som är ett programmeringsverktyg där man skriver sin programmeringskod.
Jag har använt mysql connector jar fil som är ett Java API för mysql. Denna fil gör databashantering möjlig från Java.
# 1.4 Positiva erfarenheter
Allt har gått bra och jag har fått med ganska mycket funktioner i programmet.
Dock så skulle jag kunna göra programmet mycket mer avancerad med fler och förbättrade funktioner om jag skulle lagt med tid på programmet hemma.
Filtreringen av loggar skulle jag kunna förbättra så att den känner av stor bokstav eller inte, samt fler keywords.
Jag skulle kunna lagt till så att man kan redigera i loggar samtidigt (som google drive) via en Socket och det vet
jag ungefär hur jag skulle göra eftersom jag gjort liknande förut, men jag valde att inte göra det på grund av tidsbrist.
Jag skulle också kunna lägga till så att man kan skydda loggar med lösenord men jag hann inte det på grund av tidsbrist. 
# 1.5 Negativa erfarenheter
Nej nog inga.
# 1.6 Sammanfattning
Jag tycker att det har gått bra med arbetet. Jag har inte stött på några stora problem och jag har fått med allt som skulle vara med i arbetet plus ganska mycket mer.
Jag skrev några förbättringsmöjligheter under positiva erfarenheter.
I framtiden så gör jag gärna något större eget projekt, spelar ingen större roll vad, men gärna något liknande
som detta eller något med nätverk.
