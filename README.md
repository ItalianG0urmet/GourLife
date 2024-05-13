### Glife Plugin

![Banner](https://images-ext-2.discordapp.net/external/K_Vj-_8FzSL7B57Ycey0crHLgwftXVdKUHPVUceQ26c/https/i.postimg.cc/yxBjZCXB/Glifebanner-dsgvgd.jpg?format=webp&width=2560&height=1180)

**Description:**

This Spigot plugin provides players with a unique gameplay experience by introducing a life system. Each player starts with three lives, losing one upon death and gaining one upon killing another player. The plugin offers customizable placeholders and configurations.

**Note:** When a player's life count reaches zero, they lose everything.

**Features:**

- **Revelation:** Reveals the position of all players approximately every 30 minutes. This feature can be disabled via configurations.

  ![Revelation](https://i.postimg.cc/SNcc1bzf/Screenshot-2024-03-13-alle-00-15-36.png)

**Commands:**

1. `/life check <player>` - See a player's life.
2. `/life give <player>` - Give a life to a player.
3. `/life reset <player>` - Reset player stats.
4. `/life set <player> <number>` - Set a player's lives.

**Configurations:**

```yaml
# Settings #
rivelation: true
timer_rivelation: 80 # in seconds
Join-messages: true
leave-messages: true
death-message: true
final-message: true
life-number: 3

# Messages #
prefix: "&7[&FServerName&7]&8 » "
no-player: "&cPlayer does not exist"
reset-life: "&e%player% is back in the game"
no-permission: "&cYou do not have the necessary permissions"
check-lifes: "&c%player%'s lives are %lifes%"
your-lifes: "&bYour lives are %lifes%"
give-life: "&bYou have donated 1 life"
recive-life: "&bYou have received 1 life from %player%"
not-enough-lifes: "&cYou do not have enough lives"
cant-find-player: "&cPlayer not found"
cant-send-to-yourself: "&cYou cannot do this to yourself"
rivelation_message: "%player% is at (%position_x%, %position_y%, %position_z%, %world%)"
title_rivelation: "&cRevelation"
subtitle_rivelation: "&c"
death: "&e%player% has died, now has &c❤ %lifes%"
final-death: "&e%player% has died completely"
join: "&e%player% has joined!"
leave: "&c%player% has left the server"
gained-life: "&bYou have gained 1 life"
set-life: "&bNew life; %new_life%"

# Help #
help-commands: "&c\n Usage: \n give: /life give <player> \n check: /life check <player>"
```

**Placeholders:**

- Life Placeholder: `%glife_lifes%`
- Revelation Timer: `%glife_revelation%`

![Placeholder](https://i.postimg.cc/NF8XDK6h/Screenshot-2024-03-14-alle-23-09-53.png)

**Permissions:**

- `glife.set`
- `glife.reload`
- `glife.reset`
