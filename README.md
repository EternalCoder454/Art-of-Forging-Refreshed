# Art of Forging

An addon for **Secrets of Forging: Revelations**, which is itself an addon for **Tetra**, created by
[AceTheEldritchKing](https://github.com/AceTheEldritchKing) and MindFaer. Adds many new and unique
modules, from rapiers to maces and even pop culture weapons.

```
Tetra  <-  Secrets of Forging: Revelations  <-  Art of Forging
```

This fork is the **Minecraft 26.1.2 NeoForge port** and nothing more. The mod is Ace's and
MindFaer's work. Port by EternalHell.

## Credit and permission

**The mod is by AceTheEldritchKing and MindFaer.** Ace gave permission on Discord on 2026-08-18 for
this and for Secrets of Forging: Revelations to be ported and included in Tetra Refreshed, asking
that they stay separate projects included in the mod rather than flattened into it, and that he be
credited as a contributor as well as here.

The conversation is recorded verbatim in [dev-permission.md](dev-permission.md).

Credit EternalHell for the 26.1.2 port only, never as author.

## State

**It compiles and it builds. Nothing has been run.** No world has been loaded with it and no item
has been crafted or held. Read [PORT-STATUS.md](PORT-STATUS.md) for what changed and what is known
to be broken, and work through [PLAYTESTING.md](PLAYTESTING.md) before calling a build good.

Requires **Tetra Refreshed 6.13.0**, **Secrets of Forging: Revelations 1.3.5**,
**Mutil Refreshed 7.0.0-pre.0** and **Curios 15.0.0** or later. All of them except Curios ship
inside Tetra Refreshed.

## Building

Gradle 9.7.0, ModDevGradle 2.0.144, Java 25, NeoForge 26.1.2.95.

Tetra and mutil come from mavenLocal, so the order matters:

```bash
cd "../Mutil Refreshed"                && ./gradlew.bat publishToMavenLocal
cd "../Secrets-Of-Forging-Revelations" && ./gradlew.bat build publishToMavenLocal
cd "../Art-of-Forging-Refreshed"       && ./gradlew.bat build publishToMavenLocal
cd "../Tetra Refreshed"                && ./gradlew.bat build
```

## Terms

There is no licence file in this repository. `gradle.properties` declares MIT, and that is what the
built jar reports. Permission for this port is recorded above and in
[dev-permission.md](dev-permission.md). Read it before publishing anything.
