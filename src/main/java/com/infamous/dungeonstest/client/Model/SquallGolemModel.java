package com.infamous.dungeonstest.client.Model;


import com.infamous.dungeonstest.DungeonsTest;
import com.infamous.dungeonstest.server.entities.SquallGolemEntity;
import com.infamous.dungeonstest.server.entities.WindcallerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class SquallGolemModel extends AnimatedGeoModel {
	   
		@Override
		public ResourceLocation getAnimationFileLocation(Object entity) {
			return new ResourceLocation(DungeonsTest.MODID, "animations/squall_golem.animation.json");
		}

		@Override
		public ResourceLocation getModelLocation(Object entity) {
			return new ResourceLocation(DungeonsTest.MODID, "geo/squall_golem.geo.json");
		}

		@Override
		public ResourceLocation getTextureLocation(Object entity) {
			//ChorusGormandizerEntity entityIn = (ChorusGormandizerEntity) entity;
			return new ResourceLocation(DungeonsTest.MODID, "textures/entity/squall_golem.png");
		}

		@Override
		public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
			super.setLivingAnimations(entity, uniqueID, customPredicate);

			SquallGolemEntity entityIn = (SquallGolemEntity) entity;
			
			IBone head = this.getAnimationProcessor().getBone("head");

			EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
			if (extraData.headPitch != 0 || extraData.netHeadYaw != 0) {
				head.setRotationX(head.getRotationX() + (extraData.headPitch * ((float) Math.PI / 180F)));
				head.setRotationY(head.getRotationY() + (extraData.netHeadYaw * ((float) Math.PI / 180F)));
			}
		}

}

