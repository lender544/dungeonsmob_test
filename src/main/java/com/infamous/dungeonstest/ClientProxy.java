package com.infamous.dungeonstest;


import com.infamous.dungeonstest.client.Renderer.SquallGolemRenderer;
import com.infamous.dungeonstest.client.Renderer.TornadoProjectileRenderer;
import com.infamous.dungeonstest.client.Renderer.WindcallerRenderer;
import com.infamous.dungeonstest.server.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = DungeonsTest.MODID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {



    public void clientInit() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WINDCALLER.get(),
                manager -> new WindcallerRenderer(manager));

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TORNADOPROJETILE.get(),
                manager -> new TornadoProjectileRenderer(manager));


        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SQUALL_GOLEM.get(),
                manager -> new SquallGolemRenderer(manager));
    }




}