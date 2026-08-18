# Developer guide

Building this addon, and how it reaches into Tetra without Tetra knowing about it. For the port and
the permission position see [PORT-STATUS.md](PORT-STATUS.md) and
[dev-permission.md](dev-permission.md).

This is bundled inside Tetra Refreshed rather than installed alongside it, at Ace's request. It
stays its own mod with its own id, and Tetra embeds the jar with jarJar. Nothing about this project
lives in Tetra's source tree.

## Building

Gradle 9.7.0, ModDevGradle 2.0.144, Java 25, NeoForge 26.1.2.95.

Tetra and mutil come from mavenLocal, and Tetra then embeds this, so the order matters:

```bash
cd "../Mutil Refreshed"          && ./gradlew.bat publishToMavenLocal
cd "../Art-of-Forging-Refreshed" && ./gradlew.bat build publishToMavenLocal
cd "../Tetra Refreshed"          && ./gradlew.bat build
```

That last step is what embeds this jar into Tetra's. Skipping it leaves Tetra shipping the previous
build of this mod, which looks exactly like a change that did not take.

**This is circular on a clean machine.** This compiles against Tetra, and Tetra's jarJar task cannot
resolve a dependency that has never been published, so it fails before it builds anything. Break the
loop by publishing Tetra once with the jarJar lines commented out, then building normally from then
on.

**Do not also put this jar in the mods folder.** Tetra carries it, and two copies both register
`tetra:modular_artifact`, which fails to load.

## Where the content lives

It registers into Tetra's namespace rather than its own, which is why most of its data sits under
`data/tetra` and `assets/tetra`. That is how the original was written and it is load bearing:
Tetra's stores only read their own namespace.

| Path | What |
|---|---|
| `data/tetra/modules/artifact` | the casings, the internal and the attachment |
| `data/tetra/materials/reagent` | 8 reagents, a material category Tetra does not have |
| `data/tetra/materials/{metal,fibre,misc,socket}` | additions to Tetra's own categories |
| `data/tetra/schematics` | what can be crafted onto what, including onto Tetra's own items |
| `data/art_of_forging/curios/entities` | grants the player the charm slot |
| `assets/tetra/textures/gui/aof_glyph.png` | the glyphs its modules draw |
| `assets/tetra/textures/gui/aof_holo.png` | the holosphere icon |
| `assets/tetra/holosphere_entries` | the artifact's holosphere entry |

## Reaching into Tetra from this side

Tetra's source is untouched by this project. Everything this adds to Tetra's screens is done from
here, and that is the pattern to follow for anything new.

**The creative tab.** Tetra's holder for its own tab is private, so match on the id instead:

```java
ResourceKey.create(Registries.CREATIVE_MODE_TAB,
        Identifier.fromNamespaceAndPath(TetraMod.MOD_ID, "default"))
```

The artifact was in no creative tab at all before, not Tetra's and not this mod's own, which lists
thirty ingredients and not the item they are for. The only way to one was a command.

**The holosphere.** Ship `assets/tetra/holosphere_entries/<name>.json` with an `item`, an `icon` and
a `position`. Tetra's own take 0 to 7, Secrets of Forging took 3, and the artifact sits at 8.

The icon is read from a 256 by 256 sheet at y 218. Tetra's own sheet has that band full, so this
brings `aof_holo.png` with the artifact sprite placed at x 0.

**Charged abilities.** Tetra takes registrations now:

```java
ItemModularHandheld.registerAbility(SoulChargedEffect.instance);
```

This used to declare a mixin accessor for Tetra's private static ability array, append to a copy and
write the copy back from a hook on Tetra's static initialiser. Both mixins are gone.

**Stat bars** are added in java here, through `WorkbenchStatsGui.addBar` and `HoloStatsGui.addBar`.
Secrets of Forging ships its bars as data instead, which needs no client code, but it had to because
its java only ever added them to the workbench. This mod already calls both, so its bars reach both
screens and there is nothing to fix by converting them.

## What the bundle shares

Three jars now write into `data/tetra`, and a store merges entries with the same name rather than
letting the last one win, but only when a file says so:

```json
"replace": false
```

**Sixteen files are claimed by more than one jar**, mostly the socket modules and schematics that
all three extend. Every addon copy sets `replace` false so it merges into Tetra's, and `variants`
and `outcomes` both concatenate, so nothing is lost. A file that sets true throws away everything
loaded before it, which is what three of these used to do to each other.

Check it after touching anything under `data/tetra`:

```bash
cd "../Tetra Refreshed" && python tools/check-bundle-collisions.py
```

## Traps already hit

**Data directory names went singular.** `advancements`, `loot_tables` and `recipes` are
`advancement`, `loot_table` and `recipe`, tag directories likewise. None of this fails loudly. A
directory with the old name is simply not read, so the content is quietly absent.

**58 references to a namespace that does not exist** sit in this mod's Tetra: Enlarged and better
combat compatibility data. They are inert rather than broken.

Work through [PLAYTESTING.md](PLAYTESTING.md) before calling a build good.

## Repository rules

1. Minecraft 26.1.2, NeoForge only. Java 25.
2. The mod is AceTheEldritchKing's and MindFaer's. Credit EternalHell for the 26.1.2 port only,
   never as author.
3. `upstream` stays pointed at Ace's repository. Take future changes by rebasing onto it.
4. No licence file, but `gradle.properties` declares MIT. Permission is recorded in
   [dev-permission.md](dev-permission.md). Read it before publishing anything.
5. No em dash, no double hyphen in prose, no semicolon, in any document here.
