# Deutsche Konfiguration für DiscordSRV Fabric

Diese Anleitung zeigt, wie Sie verschiedene Nachrichtentypen in unterschiedliche Discord-Channels senden können.

## Kanal-Struktur

Sie können mehrere Game-Channels erstellen, um verschiedene Nachrichtentypen zu trennen:

- **global**: Für Chat-Nachrichten und Achievements/Fortschritte
- **status**: Für Join/Leave-Nachrichten der Spieler
- **server**: Für Server-Start/Stop-Nachrichten
- **console**: Für Console-Ausgabe (separate Konfiguration)

## Beispiel-Konfiguration

Fügen Sie folgendes in Ihre `config.yaml` ein:

```yaml
channels:
  # Global Channel für Chat und Achievements
  global:
    channel-ids:
      - 123456789012345678  # Ihre Global Channel ID
    
    minecraft-to-discord:
      enabled: true
      format:
        webhook-username: "%player_prefix%%player_display_name%%player_suffix%"
        webhook-avatar-url: "%player_avatar_url%"
        content: "%message%"
    
    discord-to-minecraft:
      enabled: true
    
    join-messages:
      enabled: false  # Deaktiviert im global Channel
    
    leave-messages:
      enabled: false  # Deaktiviert im global Channel
    
    award-messages:
      enabled: true
      format:
        embeds:
          - author:
              name: "%player_name% hat den Fortschritt %award_display_name% erreicht!"
              icon-url: "%player_avatar_url%"
            description: "%award_description%"
            color: "%award_color_hex%"
    
    death-messages:
      enabled: true
      format:
        embeds:
          - author:
              name: "%message%"
              icon-url: "%player_avatar_url%"
            color: "#FF0000"
    
    start-message:
      enabled: false  # Deaktiviert im global Channel
    
    stop-message:
      enabled: false  # Deaktiviert im global Channel

  # Status Channel für Join/Leave
  status:
    channel-ids:
      - 234567890123456789  # Ihre Status Channel ID
    
    minecraft-to-discord:
      enabled: false  # Kein Chat in diesem Channel
    
    discord-to-minecraft:
      enabled: false  # Keine Discord-Nachrichten in MC
    
    join-messages:
      enabled: true
      format:
        embeds:
          - author:
              name: "%player_display_name% hat den Server betreten"
              icon-url: "%player_avatar_url%"
            color: "#55FF55"
    
    leave-messages:
      enabled: true
      format:
        embeds:
          - author:
              name: "%player_display_name% hat den Server verlassen"
              icon-url: "%player_avatar_url%"
            color: "#FF5555"
    
    award-messages:
      enabled: false
    
    death-messages:
      enabled: false
    
    start-message:
      enabled: false
    
    stop-message:
      enabled: false

  # Server Channel für Start/Stop
  server:
    channel-ids:
      - 345678901234567890  # Ihre Server Channel ID
    
    minecraft-to-discord:
      enabled: false
    
    discord-to-minecraft:
      enabled: false
    
    join-messages:
      enabled: false
    
    leave-messages:
      enabled: false
    
    award-messages:
      enabled: false
    
    death-messages:
      enabled: false
    
    start-message:
      enabled: true
      format:
        content: ":white_check_mark: **Server wurde gestartet**"
    
    stop-message:
      enabled: true
      format:
        content: ":octagonal_sign: **Server wurde heruntergefahren**"

  # Standard-Werte (werden verwendet wenn nicht überschrieben)
  default:
    # ... Standard-Konfiguration ...

# Console Channel (separate Konfiguration)
console:
  - channel:
      channel-id: 456789012345678901  # Ihre Console Channel ID
      use-thread: true
      thread:
        thread-name: "Console Log %date:'w'%"
    
    appender:
      output-mode: "ansi"
      line-format: "[%log_time:'HH:mm:ss'%] [%log_level%] %message%"

# Bot Präsenz/Aktivität
presence-updater:
  updater-rate-in-seconds: 90
  presences:
    - status: "online"
      activity: "Minecraft mit %playercount% Spielern"
  
  # Beim Server-Start
  use-starting-presence: true
  starting-presence:
    status: "do_not_disturb"
    activity: "Startet..."
  
  # Beim Server-Stop
  use-stopping-presence: true
  stopping-presence:
    status: "idle"
    activity: "Wird heruntergefahren..."
```

## Channel IDs herausfinden

1. Aktivieren Sie den Entwicklermodus in Discord (Benutzereinstellungen > Erweitert > Entwicklermodus)
2. Rechtsklick auf einen Channel > "ID kopieren"
3. Fügen Sie die ID in die Konfiguration ein

## Wichtige Hinweise

- Ersetzen Sie die Beispiel-IDs durch Ihre eigenen Discord-Channel-IDs
- Der Bot benötigt entsprechende Berechtigungen in jedem Channel
- Starten Sie den Server nach Änderungen neu oder verwenden Sie `/discordsrv reload`

## Weitere Platzhalter

### Für Spieler-Nachrichten:
- `%player_name%` - Spielername
- `%player_display_name%` - Anzeigename
- `%player_prefix%` - Prefix aus LuckPerms
- `%player_suffix%` - Suffix aus LuckPerms
- `%player_avatar_url%` - Avatar URL
- `%message%` - Nachrichteninhalt

### Für Server-Status:
- `%playercount%` - Aktuelle Spielerzahl
- `%playermax%` - Maximale Spielerzahl
- `%tps%` - Server TPS
- `%date%` - Aktuelles Datum/Zeit

Vollständige Liste: https://docs.discordsrv.com/placeholders

