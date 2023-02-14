# OK Praktikum DatabaseBrowser

Ein Datenbankbrowser, der sich mit einer Mysql Datenbank verbinden, ihren Inhalt anzeigen und bearbeiten kann. <br>
Um den Entwicklungsprozess in kleinere Arbeitsschritte zu unterteilen und einen Überblick über Inhalt und Anforderungen dieser zu erhalten,
wurden diese Arbeitsschritte unter Tickets (weiter läufig Schritte genannt) mithilfe eines internen Ticketsystems verfasst.

## Schritt 1
### Verbindung zur Datenbank erstellen und aufbauen

Das Programm soll den Nutzer nach den Verbindungsdaten für die Datenbank-Server (Serveradresse, Port, Benutzer und Passwort) und diese nutzen, um eine Verbindung aufzubauen.
Bei erfolgreicher Verbindung soll das Programm die vorhandenen Datenbanken in einer Liste anzeigen.
Ein Statustext zeigt bei erfolgloser Verbindung dies an und soll bei Bedarf mehr Information zum Fehler anzeigen.

Akzeptanzkriterien:
 - Eingaben des Benutzers werden validiert
 - Datenbank Verbindung wird hergestellt und bei Erfolg oder Fehlschlag im Status angezeigt
 - Bei Fehler soll die Statusanzeige beim Anklicken den Fehler anzeigen
 
 ### *Abgeschlossen mit Commit [36e0643](https://github.com/ShiruSan/OK.Praktikum-DatabaseBrowser/commit/36e0643)* :white_check_mark:
 
 ## Schritt 2
 ### Tabellen einer Datenbank anzeigen
 
Nachdem die Datenbanken aufgelistet wurden, soll das Programm nach Auswahl einer Datenbank die Tabellen, die sich darin befinden, aufzeigen.
Auch hier nach Anzeige der Tabellen sollen die Daten innerhalb einer Tabelle bei Auswahl angezeigt werden.

Akzeptanzkriterien:
 - Label ändert sich von Datenbanken zu Tabellen
 - anklickbarer Label zum Verlassen der Tabellenansicht wird hinzugefügt
 - Die Tabellen der Datenbank werden angezeigt
 
  ### *Abgeschlossen mit Commit [dd84da8](https://github.com/ShiruSan/OK.Praktikum-DatabaseBrowser/commit/dd84da8)* :white_check_mark:
 
 ## Schritt 3
 ### Tabellen Struktur und Inhalt auslesen
 
 Das Programm muss bevor es die Daten korrekt auslesen kann die Struktur der Tabelle kennen, um die unterschiedlichen Datentypen richtig anzuzeigen.
 Eine Recherche zu den SQL-Befehlen und ihrer Implementation muss für diese Aufgabe gemacht werden.

Akzeptanzkriterien:
 - Auslesung der Tabellenstruktur und Umwandlung in ein programm-internes Objekt (eigene Klasse)
 - Aufbau des UI für die Tabellendarstellung
 - ggf. Datenumwandlung und anschließende Darstellung der Tabellendaten
 
 ### *Abgeschlossen mit Commit [7f993a4](https://github.com/ShiruSan/OK.Praktikum-DatabaseBrowser/commit/7f993a4)* :white_check_mark:
 
 ## Schritt 4
 ### Benutzerrechte auslesen
 
 Damit das Programm weiß, welche Aktionen der eingeloggte Benutzer durchführen kann, muss das Programm die Rechte des Benutzers auslesen und in einem eigenen Objekt speichern.
 Für die SQL-Befehle, die einem diese Informationen geben, müssen Recherchen durchgeführt werden und anschließend implementiert werden.

Akzeptanzkriterien:
 - eigener Manager für die Verwaltung und Überprüfung der Rechte
 - objektorientierte Klassen zur bessern Veranschaulichung der Rechte und Nutzer
 - eigene Ansicht für die Auflistung der Rechte
 
 ## Schritt 5
 ### Datenbankeintrag auswählen und näher darstellen
 
 Nachdem das Programm die Daten als auch Berechtigungen des Benutzers ansehen kann, soll das Programm nach Auswahl eines Tabelleneintrags diese genauer darstellen, da sie in der normalen Tabelle nur in ihrer String-Form dargestellt werden.

Akzeptanzkriterien:
 - Ausbau der programm-internen "Table"-Klasse
 - Dialog für die Anzeige der Daten in ihrem ursprünglichen Format
 
 ## Schritt 6
 ### Datenbankeintrag bearbeiten
 
 Nach Auswahl soll das Programm nun in der Lage sein neben der Darstellung auch eine Bearbeitung der Daten zu ermöglichen unter Beachtung der Benutzerrechte.

Akzeptanzkriterien:
 - Dialogfeld aus dem letzten Schritt soll eine Bearbeitungsansicht erhalten
 - Blockierung der Bearbeitungssicht bei fehlender Rechte
