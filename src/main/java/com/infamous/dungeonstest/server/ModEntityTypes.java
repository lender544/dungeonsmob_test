package com.infamous.dungeonstest.server;

import com.infamous.dungeonstest.DungeonsTest;
import com.infamous.dungeonstest.server.entities.SquallGolemEntity;
import com.infamous.dungeonstest.server.entities.TornadoProjetileEntity;
import com.infamous.dungeonstest.server.entities.WindcallerEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeonstest.DungeonsTest.MODID;

@Mod.EventBusSubscriber(modid = DungeonsTest.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, DungeonsTest.MODID);


    public static final RegistryObject<EntityType<WindcallerEntity>> WINDCALLER = ENTITY_TYPES.register("windcaller", () ->
            EntityType.Builder.of(WindcallerEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(DungeonsTest.MODID+ ":windcaller")
    );

    public static final RegistryObject<EntityType<TornadoProjetileEntity>> TORNADOPROJETILE = ENTITY_TYPES.register("tornadoprojetile", () ->
            EntityType.Builder.<TornadoProjetileEntity>of(TornadoProjetileEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(DungeonsTest.MODID + ":tornadoprojetile")
    );

    public static final RegistryObject<EntityType<SquallGolemEntity>> SQUALL_GOLEM = ENTITY_TYPES.register("squall_golem", () ->
            EntityType.Builder.of(SquallGolemEntity::new, EntityClassification.MONSTER)
                    .sized(2.62F, 2.81F) // 42 px wide, 29px tall + 16px of height
                    .clientTrackingRange(10)
                    .build(DungeonsTest.MODID+ ":squall_golem")
    );

    @SubscribeEvent
    public static void initializeAttributes(EntityAttributeCreationEvent event) {
        event.put(WINDCALLER.get(), WindcallerEntity.windcaller().build());
        event.put(SQUALL_GOLEM.get(), SquallGolemEntity.squallGolem().build());

    }
}
