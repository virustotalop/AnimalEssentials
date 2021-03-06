# AnimalEssentials
All the essentials for your animals.

----
- [AnimalEssentials](https://github.com/JustRamon/AnimalEssentials) is licensed under GPLv2
- [fanciful by mkremins](https://github.com/mkremins/fanciful) in licensed under the MIT license
- [gson by  Google](https://github.com/google/gson) is licensed under the Apache v2 license

The licenses for these projects can be found in the directories by their respective authors.

----

### To-Do

1. Add WG support to prevent people from spawning animals in areas that are not their's.
2. Animal Protect
3. Money System
4. (/ae clone (Clones the right-clicked animal))
5. /ae language \<list|language\> (Shows a list of available languages/lists available languages)
6. A permission to bypass the restriction to only tp/kill/name own animals.
7. Make plugin version independant.
8. https://www.reddit.com/r/admincraft/comments/3jfvbj/pet\_protection\_plugin\_to\_rule\_them\_all\_i\_hope\_so/

### Done
1. /ae \<home|sethome|listhomes|delhome|edithome\> ([AnimalTeleport](https://github.com/JustRamon/AnimalTeleport) functionality with new look and commands.)
2. /ae reload (Reload the configuration file.)
3. /ae help (Show the help menu.)
4. /ae name \<name\> (Name the right-clicked animal.)
5. /ae tp \<home|player\> (Teleport the right-clicked animal to a home or player.)
6. /ae find \<name\> (Find all animals by name.)
7. /ae kill (Kills the right-clicked animal.)
8. Config option to only show own animals with /ae find
9. /ae heal
10. /ae owner (Shows the owner of the animal.)
11. Invincibility when teleporting to player
12. /ae tame (Tames the the right-clicked animal.)
13. Replace "a(n)" with the correct thing
14. /ae spawn (With GUI.)
15. Make multiple players able to issue the same command at a time (List!)
16. Particles for /ae kill | /ae spawn | /ae name | /ae tame
17. Ability to click the coordinates in chat, after issuing /ae find <name>, to teleport to the animal.
18. Update Checker
19. /aetp \<player\> \<world\> \<x\> \<y\> \<z\> \<keyword\> (Teleport command used to have find teleport permissions)
20. Define amount of kills (eg. /ae kill 6)

-----

### Bugs
1. Commands which are both for client and console don't output messages to console
2. Setting the amount of /ae kill to 0 enables unlimited killing
3. Reissuing a command within the timeframe of the first issued command cancels the reissued command too early

### Fixed
1. A player is a null mob instead of a player.
2. Space names cannot be found or named
3. Teleporting to animals (ae find) in a different world teleports the player into the world he is currently in and not the one of the animal.
4. /ae name is not case-sensitive
5. Stacktrace when clicking outside of inventory
6. Multiple /ae commands can be issued at the same time
7. /ae tp does not work correctly with multiple players.
8. Fix cancelling of all tasks of the plugin (aka only cancel the task of the current command)

---
