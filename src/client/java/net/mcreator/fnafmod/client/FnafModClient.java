package net.mcreator.fnafmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.mcreator.fnafmod.FnafMod;

public class FnafModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(FnafMod.FREDDY_FAZBEAR, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.BONNIE_BUNNY, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.CHICA_CHICKEN, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.FOXY_PIRATE, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.GOLDEN_FREDDY, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.ENDO_SKELETON, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.PUPPET, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.SPRINGTRAP, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.SPRING_BONNIE, AnimatronicRenderer::new);

		EntityRendererRegistry.register(FnafMod.TOY_FREDDY, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.TOY_BONNIE, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.TOY_CHICA, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.MANGLE, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.BALLOON_BOY, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.WITHERED_FREDDY, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.WITHERED_BONNIE, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.WITHERED_CHICA, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.WITHERED_FOXY, AnimatronicRenderer::new);
		EntityRendererRegistry.register(FnafMod.ENDO_02, AnimatronicRenderer::new);
	}
}
