package net.mcreator.fnafmod.client.bedrock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class BedrockDraw {
	private BedrockDraw() {}

	public static void submit(BedrockGeometry geometry, Map<String, BoneTransform> pose, Identifier texture, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay) {
		submit(geometry, pose, texture, poseStack, collector, light, overlay, null);
	}

	public static void submit(BedrockGeometry geometry, Map<String, BoneTransform> pose, Identifier texture, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, Set<String> hidden) {
		submit(geometry, pose, RenderTypes.entityCutout(texture, false), poseStack, collector, light, overlay, hidden);
	}

	public static void submit(BedrockGeometry geometry, Map<String, BoneTransform> pose, RenderType type, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, Set<String> hidden) {
		if (geometry == null) return;
		collector.submitCustomGeometry(poseStack, type, (stackPose, buffer) ->
			geometry.forEachQuad(pose, null, stackPose.pose(), hidden, (matrix, normal, quad) -> emit(buffer, matrix, normal, quad, light, overlay)));
	}

	private static void emit(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, BedrockGeometry.Quad quad, int light, int overlay) {
		Vector3f normal = normalMatrix.transform(new Vector3f(quad.nx(), quad.ny(), quad.nz())).normalize();
		float[] p = quad.positions();
		float[] uv = quad.uvs();
		Vector3f pos = new Vector3f();
		for (int i = 0; i < 4; i++) {
			matrix.transformPosition(p[i * 3], p[i * 3 + 1], p[i * 3 + 2], pos);
			buffer.addVertex(pos.x, pos.y, pos.z, -1, uv[i * 2], uv[i * 2 + 1], overlay, light, normal.x, normal.y, normal.z);
		}
	}
}
