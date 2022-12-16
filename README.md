# Distributed Systems
```
Healcthcheck: 
  
Die Worker schicken mittels UDP als erstes eine Nachricht(z.B. "Worker 1") an Controller und der Contoller empfängt das Packet und entpackt und 
schickt den selben Inhalt von der Nachricht zurück an diesen Worker, wenn das Packet bei der Controller Seite bereits angekommen ist und nicht leer ist,
dann somit weiß der Controller, dass dieser Worker betreibsbereit ist und gibt anschließend eine Ausgabe.
Natürlich haben wir uns um die Wartezeit aufgrund der UDP-Kommunikation gekümmert.
Nämlich alle vier Sekunden stellt der Controller sicher, ob dieses bestimmtes Packet vorhanden ist oder nicht und darauf basierend bewertet
er das ganze. 
Auf der Worker-Seite wird auf eine Rückantwrot von Controller gewartet, sobald es angekommen ist entpackt er es und vergleicht diesen Inhalt 
mit seinem, was er tatsächlich geschickt hat, falls die Inhalte gleich sind, dann weiß er Bescheid, dass er selbst wach bzw. betreibsbereit ist.
Also, in unserem Fall wird auf beiden Seiten Healthcheck überprüft und überwacht.

```

```

RTT(Round Trip Time): 

die Packetumlaufzeit wird nur auf Worker-Seite berechnet, in dem die Localtime von Packetsendung und die Localtime Packetempfang gecacht wird,
um das Differenz zwischen beiden Localtime zu liefern, das in dem Fall als RTT gilt.

```

```
HTTP:

Hier soll die Kommunikation zur Datenübertragung irgendwie als TCP wegen HTTP-Methoden(POST,GET,etc...) weitergeführt werden.
Daher sind wir auf die Idee gekommen, die Ergebnisse aus einer UDP-Kommunikation intern als TCP woanders(nämlich CLientHandlerTCp) 
zu übertragen, um diese Daten sinnvoll weiter zu bearbeiten. Anschließend werden diese an TCPServer geschickt.  
In TCPSErver werden diese Daten empfangen und wie angekündigt mittels händig implementierende Http-POST in einem selbst erstellten 
Webbrwoser ausgegeben.   

Darüber hinaus ist in unserem Programm möglich mittels Postman tool Daten als POST zu schciken, die in Datenbank postgreSQL abgelegt werden.
Der TCPServer ist in der Lage, wie von uns in der Aufgabestellung verlangt ist, POST und GET Anfragen voneinander zu unterscheiden.            
```

```
Database:

Als Datenbank haben wir uns für postgreSQL entschieden, da eine Datei wie JSON oder CSV als Datenbank zu benutzen sehr schmerzhaft  
aufgrund des selbst Kümmerns der zu persistierenden Daten und des Formats der Dateien sein kann. 

Die Implementierung von Insert in die Datenbank befindet sich momentan in TCPSerever.
```

```
RPC:

Nach dem Ausführen der thirft-Datei, in der sich zwei Funktionen befinden, wurde zwei (InvalidOperation - MatrixMultiplikationService)
Java Classen Schnitstellen automatisch erzeugt, diese sollen sowhol bei der Controller-Seite als auch bei der Worker-Seite enthalten sein.
Dieser zwei Funktionen der Thirft-Datei sind in der Klasse RpcController auf der Worker-Seite implementiert, somit ermöglichen wir durch Schnittstellen
den Zugriff bzw. den Aufruf vom Controller auf diese Funktionen(Worker) anhand TTransport und Tprotocol.
In der ersten Funktion wird Matrix-Multiplikatuion durchgeführt(Ob die Berechnung erfolgreich, erfolglos oder falsch durchgeführt,
wird explizit dementsprechend dem Contrroller diesen Status mitgeteilt) und in der zweiten wird die resultierte Zelle der Ergebnis-Matrix in der Datenbank 
abgelegt, nachdem wie angekundigt diesen Zelleninhalt per HTTP POST-Methode umgelenkt wird. 
```