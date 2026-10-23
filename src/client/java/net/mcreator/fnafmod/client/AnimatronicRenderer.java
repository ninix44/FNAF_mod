package net.mcreator.fnafmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.mcreator.fnafmod.FnafMod;
import net.mcreator.fnafmod.client.bedrock.BedrockAnimation;
import net.mcreator.fnafmod.client.bedrock.BedrockAssets;
import net.mcreator.fnafmod.client.bedrock.BedrockDraw;
import net.mcreator.fnafmod.client.bedrock.BedrockGeometry;
import net.mcreator.fnafmod.client.bedrock.BoneTransform;
import net.mcreator.fnafmod.client.bedrock.ClipPlayer;
import net.mcreator.fnafmod.entity.AnimatronicEntity;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class AnimatronicRenderer extends EntityRenderer<AnimatronicEntity, AnimatronicRenderer.State> {
	private final Map<AnimatronicEntity, ClipPlayer> players = new WeakHashMap<>();

	public static class State extends EntityRenderState {
		float bodyRot;
		float death;
		boolean hurt;
		String modelKey;
		Identifier texture;
		Map<String, BoneTransform> pose = Map.of();
	}

	public AnimatronicRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.6f;
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(AnimatronicEntity animatronic, State state, float partialTick) {
		super.extractRenderState(animatronic, state, partialTick);
		state.bodyRot = Mth.rotLerp(partialTick, animatronic.yBodyRotO, animatronic.yBodyRot);
		state.hurt = animatronic.hurtTime > 0 || animatronic.deathTime > 0;
		state.death = animatronic.deathTime > 0 ? (animatronic.deathTime + partialTick - 1) / 20f : 0;
		state.modelKey = animatronic.modelKey();
		state.texture = FnafMod.id(animatronic.getTextureVariant());

		ClipPlayer player = players.computeIfAbsent(animatronic, a -> new ClipPlayer());
		double now = (animatronic.tickCount + partialTick) / 20.0;

		Map<String, BedrockAnimation> clips = BedrockAssets.animations(state.modelKey);
		Map.Entry<String, BedrockAnimation> selected = resolveClip(animatronic, clips);

		if (selected != null) {
			player.play(selected.getKey(), selected.getValue(), now, 0.25);
			state.pose = player.pose(now);
		} else {
			state.pose = Map.of();
		}
	}

	private static Map.Entry<String, BedrockAnimation> resolveClip(AnimatronicEntity animatronic, Map<String, BedrockAnimation> clips) {
		if (clips == null || clips.isEmpty()) return null;

		String explicit = animatronic.getAnimationState();
		if ("sit".equalsIgnoreCase(explicit)) {
			Map.Entry<String, BedrockAnimation> found = findMatching(clips, "sit");
			if (found != null) return found;
		}

		boolean isAggressive = animatronic.getTarget() != null;
		boolean isMoving = animatronic.walkAnimation.speed() > 0.05f;

		if (isAggressive && isMoving) {
			Map.Entry<String, BedrockAnimation> found = findMatching(clips, "chase", "sprint", "aggression", "run", "walk");
			if (found != null) return found;
		} else if (isMoving) {
			Map.Entry<String, BedrockAnimation> found = findMatching(clips, "walk");
			if (found != null) return found;
		} else {
			Map.Entry<String, BedrockAnimation> found = findMatching(clips, "idle", "pose", "day");
			if (found != null) return found;
		}

		return clips.entrySet().stream().findFirst().orElse(null);
	}

	private static Map.Entry<String, BedrockAnimation> findMatching(Map<String, BedrockAnimation> clips, String... patterns) {
		for (String pat : patterns) {
			for (Map.Entry<String, BedrockAnimation> entry : clips.entrySet()) {
				String key = entry.getKey().toLowerCase();
				if (key.contains(pat)) {
					return entry;
				}
			}
		}
		return null;
	}

	@Override
	public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - state.bodyRot));
		if (state.death > 0) {
			poseStack.mulPose(Axis.ZP.rotationDegrees(Math.min(1, Mth.sqrt(state.death * 1.6f)) * 90));
		}

		BedrockGeometry geometry = BedrockAssets.geometry(state.modelKey);
		BedrockDraw.submit(geometry, state.pose, state.texture, poseStack, collector, state.lightCoords,
			OverlayTexture.pack(OverlayTexture.u(0), OverlayTexture.v(state.hurt)));

		poseStack.popPose();
		super.submit(state, poseStack, collector, camera);
	}
}
