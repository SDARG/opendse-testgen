Anleitung zum batch skript:
Wenn man das Skript einfach ohne Parameter ausführt, erscheint eine Hilfsübersicht wo alle möglichen Parameter und deren Funktion erklärt werden.
Das Skript ist dazu da um a) eine beliebige Anzahl an Optimiererkonfigurationen auszuführen und b) die Ergebnisse der verschiedenen Optimierer zu vergleichen und 2 abstrakte Qualitätsmaße zu berechnen (Indikator-/Epsilon Dominanz und Hypervolumen).

Dazu erwartet das Skript folgenden Dinge:
	1) die Optimiererkonfigurationen im xml Format(kann man z.B. über die Opt4J GUI erzeugen) derselben! Spezifiaktion in einem Verzeichnis SpecX in einem Unterordner configs.
	2) eine Opt4J Ausführungsdatei (Pfad dazu kann als Parameter angegeben werden oder der default: opt4j/bin/opt4j).
	Beispiel Verzeichnisse:
	-SpecX
		-configs
			-config1.xml
			-config2.xml
	-SpecY
		-configs
			-config1.xml
			-config2.xml
	-opt4j
		-bin
			-opt4j

Das Skript bekommt als zwingenden Parameter die Verzeichnisse SpecX, SpecY, SpecZ, ... übergeben, die ausgeführt werden sollen (jedes Verzeichnis davon muss einen configs Unterordner mit den xml configs haben).
Das Skript führt dann folgenden Dinge aus:
	1) Die Optimierungsläufe aller Konfigurationen. Es wird ein runs Verzeichnis erstellt wo log Dateien mit den Ergebnissen liegen.
	2) Die Analyse der Optimierungsergebnisse. Es wird ein results Verzeichnis erstellt wo log Dateien mit den Analyse Ergebnisse liegen.
	-> 1) und 2) können separat ausgeführt werden.
	Beispiel Verzeichnisse nach 1) und 2):
	-SpecX
		-configs
			-config1.xml
			-config2.xml
		-runs
			-run1.tsv
			-run2.tsv
			...
		-results
			-config1.STATS
			-config2.STATS

Für uns sind am Ende die .STATS Dateien in den results Ordnern interessant. Diese enthalten unter anderem den zeitlichen Verlauf der abstrakten Qualitätsmaße, was einen Aufschluss über den Verlauf der Lösungsqualität während der Optimierung liefert.
Diesen zeitlichen Verlauf kann man dann z.B. plotten und so verschiedene Optimierer Konfigurationen einfach miteinander vergleichen.