# `Distributed Systems`

## Healcthcheck
```
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
## RTT (Round Trip Time) UDP
``` 
die Packetumlaufzeit wird nur auf Worker-Seite berechnet, in dem die Localtime von Packetsendung (send()) und die Localtime Packetempfang (recive()) definiert wird,
um das Differenz zwischen beiden Localtime zu liefern, das in dem Fall als RTT gilt.
```

## `Ping & RTT`:

| ***Worker-Anzahl*** | Ping | RTT  |
|---------------------|------|------|
| ***1***             | 1 ms | 1 ms |
| ***2***             | 1 ms | 1 ms |
| ***3***             | 1 ms | 1 ms |
| ***4***             | 1 ms | 2 ms |
| ***5***             | 1 ms | 2 ms |
| ***6***             | 2 ms | 3 ms |
| ***7***             | 2 ms | 3 ms |
| ***8***             | 2 ms | 3 ms |
| ***9***             | 2 ms | 3 ms |
| ***10***            | 2 ms | 4 ms |

#### Bemerkung:
- ping is a tool using the icmp protocol. icmp sits on layer 3, the network layer, of the OSImodel.
- ping is much faster then UDP.

## RTT (Round Trip Time) TCP
```
Hier soll genau So wie bei UDP die Packetumlaufzeit auf der Workerseite von dem Send-Zeitpunkt bis zum Empfang-Zeitpunkt berechnet werden. 
Senden mit OutputStream und Empfangen wie z.B. Ack mit Inputstream ist bei TCP zu realisieren. Sobald Ack auf der Workerseite von TCP-Server ankommt, wird das
Differenz zwischen beide Ereignisse berechnet. Selbstverständlich soll  unter anderem lange Wartezeiten auf Ack aufgrund des TCP-Transportmechanismusses 
erwartet werden, deswegen würde es Sinn ergeben, wenn so implenetiert wird, dass auf lange Paketumlaufzeiten angemssen reagiert und diese korrekt interpretiert wird. 
```
## Wer ist schneller ***TCP*** oder ***UDP***
```
Laut der Ergebnisse der jeweiligen RTT unserer Implementation ist UDP deutlich schneller als TCP,
zumal UDP auf Acknowlagement nicht wartet, wobei TCP eine Bestätigung(Ack) unbedingt als Rückantwort geben muss. 
```
## HTTP
``` 
Hier soll die Kommunikation zur Datenübertragung irgendwie als TCP wegen HTTP-Methoden(POST,GET,etc...) weitergeführt werden.
Daher sind wir auf die Idee gekommen, die Ergebnisse aus einer UDP-Kommunikation intern als TCP woanders(nämlich CLientHandlerTCp) 
zu übertragen, um diese Daten sinnvoll weiter zu bearbeiten. Anschließend werden diese an TCPServer geschickt.  
In TCPServer werden diese Daten empfangen und wie angekündigt mittels händig implementierende Http-POST in einem selbst erstellten 
Webbrwoser ausgegeben.   

Darüber hinaus ist in unserem Programm möglich mittels Postman tool Daten als POST zu schciken, die in Datenbank postgreSQL abgelegt werden.
Der TCPServer ist in der Lage, wie von uns in der Aufgabestellung verlangt ist, POST und GET Anfragen voneinander zu unterscheiden.            
```

## Database
```
Als Datenbank haben wir uns für postgreSQL entschieden, da eine Datei wie JSON oder CSV als Datenbank zu benutzen sehr schmerzhaft  
aufgrund des selbst Kümmerns der zu persistierenden Daten und des Formats der Dateien sein kann. 

Die Implementierung von Insert in die Datenbank befindet sich momentan in TCPSerever.
```

## RPC
```
Nach dem Ausführen der thirft-Datei, in der sich zwei Funktionen befinden, wurde zwei (InvalidOperation - MatrixMultiplikationService)
Java Classen Schnitstellen automatisch erzeugt, diese sollen sowhol bei der Controller-Seite als auch bei der Worker-Seite enthalten sein.
Dieser zwei Funktionen der Thirft-Datei sind in der Klasse RpcController auf der Worker-Seite implementiert, somit ermöglichen wir durch Schnittstellen
den Zugriff bzw. den Aufruf vom Controller auf diese Funktionen(Worker) anhand TTransport und Tprotocol.
In der ersten Funktion wird Matrix-Multiplikatuion durchgeführt(Ob die Berechnung erfolgreich, erfolglos oder falsch durchgeführt,
wird explizit dementsprechend dem Contrroller diesen Status mitgeteilt) und in der zweiten wird die resultierte Zelle der Ergebnis-Matrix in der Datenbank 
abgelegt, nachdem wie angekundigt diesen Zelleninhalt per HTTP POST-Methode umgelenkt wird. 
```

## MQTT
```
Zum Austausch der Nachrichten zwischen Worker haben wir uns für MQTT Message-oriented Middleware(MoM) entschieden.  
Das angewendete Software zur Verbindung mit dem Backend für broker ist Mosquitto. Dies haben wir in docker-compose.yml file 
heruntergeladen.Worker sind in der Lage zu publischen und zu subscriben. Unter einer Bedingung, dass Topic und Broker der jeweiligen Worker 
identisch sein soll, um die benötigte Nachricht entsprechend dem dafür interessierenden Worker weiterzuleiten,
also die Worker sind sozusagen dazu verpflichtet, sich beim Broker zu registrieren. 
```