package eu.kaufko.paladin_spells.registry;

import eu.kaufko.paladin_spells.PaladinSpells;
import eu.kaufko.paladin_spells.spells.*;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PaladinSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, PaladinSpells.MODID);

    public static final RegistryObject<AbstractSpell> TAUNT_SPELL =
            SPELLS.register("taunt", TauntSpell::new);
    public static final RegistryObject<AbstractSpell> BULWARK_SPELL =
            SPELLS.register("bulwark", BulwarkSpell::new);
    public static final RegistryObject<AbstractSpell> SWORN_PROTECTOR_SPELL =
            SPELLS.register("sworn_protector", SwornProtectorSpell::new);
    public static final RegistryObject<AbstractSpell> BEDROCK_SKIN_SPELL =
            SPELLS.register("bedrock_skin", BedrockSkinSpell::new);
    public static final RegistryObject<AbstractSpell> RAM_SPELL =
            SPELLS.register("ram", RamSpell::new);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }
}