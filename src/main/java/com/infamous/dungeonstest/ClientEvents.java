package com.infamous.dungeonstest;


import com.infamous.dungeonstest.client.Renderer.TornadoProjectileRenderer;
import com.infamous.dungeonstest.client.Renderer.WindcallerRenderer;
import com.infamous.dungeonstest.server.ModEntityTypes;
import com.infamous.dungeonstest.server.entities.TornadoProjetileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.infamous.dungeonstest.DungeonsTest.MODID;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {


    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WINDCALLER.get(),
                manager -> new WindcallerRenderer(manager));

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TORNADOPROJETILE.get(),
                manager -> new TornadoProjectileRenderer(manager));
    }


    /*@SubscribeEvent
    public static void onRenderNamePlateEvent(RenderNameplateEvent event){
        Entity entity = event.getEntity();
        IFormattableTextComponent copy = event.getContent().copy();
        StringBuilder enchantmentString = new StringBuilder();
        getEnchantableCapabilityLazy(entity).ifPresent(cap -> {
            if(cap.hasEnchantment()){
                enchantmentString.append(" (");
                enchantmentString.append(cap.getEnchantments().stream().map(mobEnchantment -> mobEnchantment.getRegistryName().getPath()).collect(Collectors.joining(", ")));
                enchantmentString.append(")");
                event.setResult(Event.Result.ALLOW);
            }
        });
        copy.append(enchantmentString.toString());
        event.setContent(copy);
    }*/
}
