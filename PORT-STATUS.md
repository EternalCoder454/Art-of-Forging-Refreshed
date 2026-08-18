# Port status

Art of Forging, a Tetra addon by [AceTheEldritchKing](https://github.com/AceTheEldritchKing) and
MindFaer, carried from 1.20.1 Forge to 26.1.2 NeoForge.

## State

**Bundled inside Tetra Refreshed with jarJar**, which is the shape Ace asked for. It stays its own
repository and its own mod id, publishes to mavenLocal, and NeoForge loads it from inside Tetra's
jar. Build order and the circular first build are in [DEV.md](DEV.md).

**It compiles and it builds. Nothing has been run.** 698 compile errors down to zero, and the jar
packages, but no world has been loaded with it and no item has been crafted, held or swung. Treat
every claim below as "the code says so", not "it works".

Requires **Tetra Refreshed 6.13.0**, **Mutil Refreshed 7.0.0-pre.0** and **Curios 15.0.0** or later.

## Permission

Ace granted permission on Discord on 2026-08-18 for this and for Secrets of Forging, asking that
they stay separate projects included in Tetra rather than flattened into it, and that he be credited
as a contributor as well as in the README. The repository has no licence file, but
`gradle.properties` declares MIT, and that is what the built jar reports.

**MindFaer is a second author**, named in the original `mod_authors`, and is credited the same way.

## The port

### Capabilities became attachments

Six per player values, carnage, conquer, devouring, karma, soul charge and subjugation, were Forge
capabilities. Each had a data class, a provider, a `LazyOptional`, an entry in an attach event and
an entry in a register event. Capabilities are gone.

They are data attachments now, registered in `AoFAttachments` and read straight off the entity. The
provider classes survive as a single static `get` returning an `Optional`, which is what let the
eight files that read them change by one call each rather than being rewritten.

`AoFEvents` is deleted. All three of its handlers were capability plumbing.

**Its clone handler never worked.** It carried values across death like this:

```java
event.getOriginal().getCapability(...).ifPresent(oldStore -> {
    event.getOriginal().getCapability(...).ifPresent(newStore -> {
        newStore.copyFrom(oldStore);
```

Both lambdas read `getOriginal()`, so `newStore` and `oldStore` were the same object and the copy
did nothing. It also only ever handled devouring, with a `// Karma` comment and no body after it.
`copyOnDeath()` on the attachment does what that code was trying to do, for all six.

### Both mixins are gone

The mod added three charged abilities by declaring a mixin accessor for Tetra's private static
ability array, appending to a copy, and writing the copy back from a hook on Tetra's static
initialiser.

**Tetra takes registrations now.** `ItemModularHandheld.abilities` is a list rather than an array,
with `registerAbility`, so this calls it three times in common setup and carries no mixins at all.
That change is in Tetra Refreshed, not here.

### Networking

`SimpleChannel` and its message builder are gone. The two clientbound particle packets are payloads
now: records carrying their own type and stream codec, registered against a `PayloadRegistrar` on a
mod bus event, with the handler given at registration rather than living on the packet.

### The rest

* **The loader.** The mod loading context is gone, so the bus arrives as a constructor argument.
  `@EventBusSubscriber` has no `bus` element any more and is game bus only, so client setup is
  registered by hand.
* **Registration.** An item carries its registry id on its properties and only `DeferredRegister.Items`
  sets it, so every registration takes the properties it is handed rather than building its own.
  `RegistryObject` is `DeferredHolder`, and `@ObjectHolder` is gone, so the modular artifact's
  instance is set where it is registered.
* **Mob effects** are held as `Holder<MobEffect>`, so a deferred holder is passed directly and
  calling `get()` on it is the thing that breaks. `applyEffectTick` takes the server level and
  returns whether the effect is still active. `DIG_SPEED`, `DAMAGE_BOOST`, `DAMAGE_RESISTANCE`,
  `HARM` and `CONFUSION` are `HASTE`, `STRENGTH`, `RESISTANCE`, `INSTANT_DAMAGE` and `NAUSEA`.
* **`LivingHurtEvent` and `LivingAttackEvent` are gone.** `LivingIncomingDamageEvent` is the one
  that still carries `getSource`, `getAmount` and `setAmount`, which is what every use here wanted.
* **Tick events carry their phase in the type**, so `TickEvent.PlayerTickEvent` is
  `PlayerTickEvent.Post`.
* **Mob types are gone.** What a mob counts as is an entity type tag, so the undead check asks the
  registry holder.
* **Tooltips** are appended to a consumer and take a context rather than a level.
* **Loot modifiers** carry a priority, which is a constructor argument and a field in the json
  because `codecStart` writes it, and a modifier's codec is a `MapCodec`.
* **Curios** registers slots from data rather than by intermod message, so the charm slot the mod
  used to ask for is granted by `data/art_of_forging/curios/entities/player.json`.
* **Data directories went singular**, `advancements`, `loot_tables` and `recipes` became
  `advancement`, `loot_table` and `recipe`, tag directories likewise, and `data/forge` is
  `data/neoforge`. None of this fails loudly. A directory with the old name is simply not read.

### Deleted

* **`DummyItem` and `ScrollHelper`.** `DummyItem` was never registered, its registration is
  commented out in `AoFRegistry`, and it is written against `fillItemCategory` and
  `CreativeModeTab.TABS`, both gone for years. `ScrollHelper` existed only to serve it. Porting
  either would have been inventing a feature rather than carrying one across.

## Known problems

**The creative tab was never registered.** `AoFCreativeModeTab` has a `register` method that nothing
called, so none of the 30 items it lists had a tab to appear in. It is registered now, which is a
change in behaviour rather than a port, and is worth knowing when comparing against 1.20.1.

**The modular artifact was in no tab at all**, not Tetra's and not this mod's own, which lists the
ingredients and not the item they are for. It is in both now, and ninth in the holosphere. That is
also a change in behaviour rather than a port.

**Eleven shared data files were replacing rather than merging.** Tetra, Secrets of Forging and this
mod all ship a `data/tetra/modules/sword/socket.json` and two more like it, and every copy asked to
replace, so whichever loaded last discarded the others along with their variants. The copies here
set `"replace": false` now. `tools/check-bundle-collisions.py` in Tetra reports any path more than
one jar in the bundle claims.

**The loot modifiers do not match their own codec.** `AddItemModifier` reads a field named `item`,
a single item. All nine data files write `items`, a list. That mismatch predates this port and is
carried across unchanged, so the modifiers still will not parse. Fixing it means deciding whether
the codec should read a list or the data should name one item, which is a behaviour decision for
Ace rather than a port step.

**42 data files still contain an `nbt` field.** A result names its item as `id` with `components`
now, so scroll and schematic contents written as nbt strings will not apply. Secrets of Forging hit
the same thing.

**`tetratic` and `bettercombat` compatibility data is untouched.** Neither mod is in the test pack,
so those files were carried over as they were and have not been looked at.

**No play testing.** See [PLAYTESTING.md](PLAYTESTING.md).

## Repository rules

1. Minecraft 26.1.2, NeoForge only. Java 25.
2. The mod is AceTheEldritchKing's and MindFaer's. Credit EternalHell for the 26.1.2 port only,
   never as author.
3. `upstream` stays pointed at Ace's repository. Take future changes by rebasing onto it.
4. No licence file, but `gradle.properties` declares MIT. Permission for this port is recorded
   above. Read it before publishing anything.
5. No em dash, no double hyphen in prose, no semicolon, in any document here.
