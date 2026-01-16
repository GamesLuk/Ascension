# Servers Link Integration für DiscordSRV

## Übersicht

DiscordSRV wurde erweitert, um die **Servers Link** Mod zu unterstützen. Diese Integration ermöglicht es, Join- und Leave-Nachrichten nur dann an Discord zu senden, wenn Spieler wirklich das Netzwerk betreten oder verlassen - nicht bei Server-Transfers innerhalb des Netzwerks.

## Features

✅ **Transfer-Detection**: Erkennt automatisch, ob ein Spieler zwischen Servern transferiert wird  
✅ **Intelligente Nachrichten**: Join/Leave-Nachrichten werden nur bei echtem Netzwerk-Join/-Leave gesendet  
✅ **Optional**: Funktioniert mit und ohne Servers Link  
✅ **Keine Konfiguration nötig**: Automatische Erkennung und Aktivierung  

## Wie es funktioniert

### Ohne Servers Link
- Join-Nachricht wird gesendet, wenn ein Spieler auf den Server kommt
- Leave-Nachricht wird gesendet, wenn ein Spieler den Server verlässt

### Mit Servers Link
- Join-Nachricht wird **nur** gesendet, wenn ein Spieler von außerhalb ins Netzwerk kommt
- Leave-Nachricht wird **nur** gesendet, wenn ein Spieler das gesamte Netzwerk verlässt
- **Keine** Nachrichten bei Server-Transfers innerhalb des Netzwerks

## Installation

### Voraussetzungen
- DiscordSRV 3.0.0+ (diese Version)
- Servers Link 2.4.0+ (optional)
- Fabric 1.21.10

### Schritte

1. **Mit Servers Link** (empfohlen für Netzwerke):
   - Installiere Servers Link auf allen Servern
   - Installiere DiscordSRV auf allen Servern
   - Die Integration wird automatisch aktiviert

2. **Ohne Servers Link** (Standalone):
   - Installiere nur DiscordSRV
   - Normale Join/Leave-Nachrichten werden verwendet

## Beispiel-Szenario

### Server-Netzwerk: Hub, Survival-1, Survival-2

#### Mit Servers Link Integration:

1. **Spieler "Max" joined auf Hub** (von außen)
   → Discord: ✅ "Max joined" (echtes Network-Join)

2. **Spieler "Max" transferiert von Hub → Survival-1**
   → Discord: ❌ Keine Nachricht (Server-Transfer)

3. **Spieler "Max" transferiert von Survival-1 → Survival-2**
   → Discord: ❌ Keine Nachricht (Server-Transfer)

4. **Spieler "Max" disconnected auf Survival-2**
   → Discord: ✅ "Max left" (echtes Network-Leave)

#### Ohne Servers Link Integration:

Jeder Join/Leave würde eine Nachricht senden (4 Nachrichten statt 2).

## Technische Details

### Verwendete API

Die Integration nutzt die Servers Link API:

```java
// Prüfen ob Spieler beim Join von einem Transfer kommt
ServersLinkApi.getPreventConnect().contains(uuid)

// Prüfen ob Spieler beim Leave transferiert wird
ServersLinkApi.isPlayerBeingTransferred(uuid)
```

### Betroffene Module

- `FabricJoinModule`: Join-Nachrichten
- `FabricQuitModule`: Leave-Nachrichten

### Integration-Klasse

`ServersLinkIntegration.java` kapselt die gesamte API-Logik:
- Automatische Erkennung von Servers Link
- Sichere Fallback-Mechanismen
- Keine Abhängigkeitsprobleme bei Standalone-Betrieb

## Konfiguration

Keine spezielle Konfiguration erforderlich! Die Integration:

1. **Erkennt** automatisch ob Servers Link verfügbar ist
2. **Aktiviert** sich selbst wenn gefunden
3. **Fällt zurück** auf Standard-Verhalten wenn nicht gefunden

## Logs

Bei aktivierter Integration wird beim Start angezeigt:

```
[DiscordSRV] Servers Link gefunden - Transfer-Detection aktiviert
```

Ohne Servers Link:

```
[DiscordSRV] Servers Link nicht gefunden - Standard Join/Leave Nachrichten
```

## Build-Konfiguration

Die Servers Link Dependency ist als **optional** (`modCompileOnly`) konfiguriert:

```gradle
// In fabric/build.gradle
dependencies {
    // Servers Link (optional) - für Transfer-Detection
    modCompileOnly files('C:/Users/lukas/Documents/Programming_Programs/servers-link/build/libs/servers-link-2.4.0.jar')
}
```

Dies bedeutet:
- Kompiliert wird mit der API
- Laufzeit-Verfügbarkeit wird geprüft
- Keine harte Abhängigkeit

## Troubleshooting

### Integration wird nicht aktiviert

**Problem**: Servers Link ist installiert, aber Integration wird nicht erkannt

**Lösung**:
1. Überprüfe, dass Servers Link 2.4.0+ installiert ist
2. Überprüfe die Logs auf "Servers Link gefunden"
3. Stelle sicher, dass die API-Klasse verfügbar ist: `io.github.kgriff0n.api.ServersLinkApi`

### Doppelte Nachrichten

**Problem**: Join/Leave-Nachrichten werden bei Transfers trotzdem gesendet

**Lösung**:
1. Überprüfe, dass Servers Link korrekt konfiguriert ist
2. Überprüfe, dass alle Server im Netzwerk verbunden sind
3. Aktiviere Debug-Logs in beiden Mods

### ClassNotFoundException

**Problem**: `ClassNotFoundException: io.github.kgriff0n.api.ServersLinkApi`

**Lösung**: Dies ist normal wenn Servers Link nicht installiert ist. Die Mod fällt automatisch auf Standard-Verhalten zurück.

## Changelog

### Version 3.0.0-SNAPSHOT (2025-10-28)

- ✨ Servers Link Integration hinzugefügt
- ✨ Transfer-Detection für Join/Leave-Nachrichten
- ✨ Automatische Aktivierung bei Verfügbarkeit
- ✨ Optionale Dependency (keine harte Abhängigkeit)

## Links

- **Servers Link**: https://modrinth.com/mod/servers-link
- **DiscordSRV**: https://github.com/DiscordSRV/Ascension

## Lizenz

Diese Integration ist Teil von DiscordSRV und steht unter der GPLv3 Lizenz.

---

*Erstellt am: 2025-10-28*  
*Kompatibel mit: Fabric 1.21.10, Servers Link 2.4.0+*

