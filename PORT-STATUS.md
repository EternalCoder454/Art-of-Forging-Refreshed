# Port status

Art of Forging by [AceTheEldritchKing](https://github.com/AceTheEldritchKing) and MindFaer, carried
from 1.20.1 Forge to 26.1.2 NeoForge.

**It is an addon for Secrets of Forging: Revelations**, which is itself an addon for Tetra:

```
Tetra  <-  Secrets of Forging: Revelations  <-  Art of Forging
```

That chain is declared, so NeoForge loads them in that order. It matters: this mod's schematics
target `polearm/head` and `polearm/handle`, slots that exist only because Secrets of Forging defines
them, and its data merges on top of Secrets of Forging's rather than beside it.

## State

**Bundled inside Tetra Refreshed with jarJar**, which is the shape Ace asked for. It stays its own
repository and its own mod id, publishes to mavenLocal, and NeoForge loads it from inside Tetra's
jar. Build order and the circular first build are in [DEV.md](DEV.md).

**The first launch failed** during mod construction, on a charged ability registered on the game bus
with no listener on it, which Forge ignored and NeoForge throws for. That is fixed and
`tools/check-bus-registrations.py` in Tetra now catches the shape of it. **The fixed build has not
been launched yet**, so whether it gets past construction is still unproven.

**Nothing has been played.** 698 compile errors down to zero, and the jar
packages, but no world has been loaded with it and no item has been crafted, held or swung. Treat
every claim below as "the code says so", not "it works".

Requires **Tetra Refreshed 6.13.0**, **Secrets of Forging: Revelations 1.3.5**,
**Mutil Refreshed 7.0.0-pre.0** and **Curios 15.0.0** or later.

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

**The loot modifiers work now, and none of them ever did.** They failed two different ways, both
older than this port.

Six named `art_of_forging:add_to_table`, a type nothing registers here or upstream. What they
describe, rolling a separate loot table and adding its results, is what NeoForge's own
`neoforge:add_table` does, and Tetra already uses it for its bastion scrolls, so they point at that
instead of at a class nobody wrote. All six tables they name exist.

Three used `add_item`, whose codec read a single `item` while every data file writes a list under
`items`. The codec reads the list now.

An entry in that list is data rather than a stack, and that is what makes it load at all. A loot
modifier is parsed during a datapack reload, and item components are unbound for the whole of one,
so building a stack while parsing fails with "Item art_of_forging:ancient_flail does not have
components yet". The codec reads an `id`, an optional `count` and an optional `components` patch,
and builds the stack in `doApply` instead, which runs when loot is rolled and components are long
since bound. Tetra's `OutcomeMaterial` defers the same way for the same reason.

It adds every stack listed rather than choosing between them. The author's own data settles that:
where he wanted a roll he used a separate table and the modifier that rolls one, which is six of
these nine.

**The nbt to components port is done.** 41 files and 54 item stacks, which is every advancement
icon, every advancement criterion and all eight scroll recipes. Every one of them had been dropped
on load with "No key id" or "Not a string", so none of them did anything at all.

Four shapes, each copied from a ported equivalent in Tetra rather than invented:

| where | was | now |
|---|---|---|
| advancement icon, a modular item | `item` and an nbt string | `id` and `minecraft:custom_data` |
| advancement icon, a scroll | `item` and snbt | `id` and `tetra:scroll_data` |
| advancement criterion | `item` and snbt | an `items` list and `predicates` |
| recipe result | `item` and an nbt object | `id` and `tetra:scroll_data` |

Scroll data used to live under `BlockEntityTag`, because a scroll was a block entity item. It is the
`tetra:scroll_data` component now, which is what Tetra's own scroll recipes and advancements read.

No file holds nbt any more.

**Every remaining `item` key is ported too.** 46 files and 115 references. All of them held an item
and no nbt, so the pass above never looked at them, and every one was still being dropped on load.

| where | was | now |
|---|---|---|
| a stack, under `result` or `display > icon` | an `item` | an `id` |
| an ingredient | an object holding `item` | a plain string |
| an ingredient naming a tag | an object holding `tag` | a string starting with a hash |
| an ingredient offering a choice | a list of objects | a list of strings |

An ingredient is a plain string now, so the object around it is gone rather than renamed. A stack
keeps its object because it can still carry a count and components. Tetra's own replacements read a
field genuinely called `item`, so those were left alone.

**The `global_loot_modifiers` index is gone.** A modifier is found by sitting in
`data/<namespace>/loot_modifiers` now, so the old index in `data/neoforge` was read as a modifier
itself and failed with "No key type". Tetra ships eight modifiers and no index.

**`tetratic` and `bettercombat` compatibility data is untouched.** Neither mod is in the test pack,
so those files were carried over as they were and have not been looked at.

**Nothing registers a greatsword.** Secrets of Forging ships 88 files of greatsword content, modules
for `greatsword/blade` and `greatsword/hilt`, and this mod's nano fused hone names those slots too.
No java in Tetra, Secrets of Forging or this mod registers a greatsword item, and none ever did in
this repository's history. All of it is inert until whatever registers that item is found, which is
most likely one of Ace's other mods or Tetra: Enlarged, the mod the `tetratic` data here is for.

**No play testing.** See [PLAYTESTING.md](PLAYTESTING.md).

**Five more faults the launch log named.** Everything above loaded, and these were what remained.

**Fourteen scroll advancements were dropped.** A scroll's data needs a `key` and an `intricate`
flag, and these icons carried only a material and a ribbon, so every one failed with "No key
intricate". The ribbon and material in each already matched that scroll's own recipe, so the icon
was meant to show that scroll. The key comes from the advancement's own criterion and the flag from
the recipe. The root watches for any scroll and names none, so its icon is a plain rolled scroll
rather than one carrying a key that would have to be invented.

**Two scroll recipes named a custom ingredient the old way.** The field is
`neoforge:ingredient_type`, not `type`, which is what Tetra's own gild scroll recipes write. Both
failed with "No key neoforge:ingredient_type".

**`minecraft:chain` is `minecraft:iron_chain` in 26.1.2.** Chains became a family with copper
variants, and the plain name went with it. One recipe, the demonic flail.

**The shockwave hammer animation named a frame that is not there.** Six frames exist, numbered 0 to
5, and both mcmeta files listed frame 6. That is an off by one in the upstream data, not the port.

## Repository rules

1. Minecraft 26.1.2, NeoForge only. Java 25.
2. The mod is AceTheEldritchKing's and MindFaer's. Credit EternalHell for the 26.1.2 port only,
   never as author.
3. `upstream` stays pointed at Ace's repository. Take future changes by rebasing onto it.
4. No licence file, but `gradle.properties` declares MIT. Permission for this port is recorded
   above. Read it before publishing anything.
5. Writing rules: run `python tools/check-writing-rules.py --rules`. Nothing else states them.
