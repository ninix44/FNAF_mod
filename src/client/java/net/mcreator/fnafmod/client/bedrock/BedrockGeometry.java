package net.mcreator.fnafmod.client.bedrock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public final class BedrockGeometry {
	public record Quad(float[] positions, float[] uvs, float nx, float ny, float nz) {}

	public interface QuadSink {
		void accept(Matrix4f pose, Matrix3f normal, Quad quad);
	}

	public static final class Bone {
		public final String name;
		public Bone parent;
		public final float x, y, z;
		public final float startRx, startRy, startRz;
		public final List<Quad> quads;
		public final List<Bone> children = new ArrayList<>();
		public final boolean neverRender;

		Bone(String name, float x, float y, float z, float startRx, float startRy, float startRz, List<Quad> quads, boolean neverRender) {
			this.name = name;
			this.x = x;
			this.y = y;
			this.z = z;
			this.startRx = startRx;
			this.startRy = startRy;
			this.startRz = startRz;
			this.quads = quads;
			this.neverRender = neverRender;
		}

		public void applyTransform(Matrix4f matrix, BoneTransform t, float[] extraRotation) {
			double tx = t == null ? 0 : t.tx, ty = t == null ? 0 : t.ty, tz = t == null ? 0 : t.tz;
			matrix.translate((float) (-tx / 16.0), (float) (ty / 16.0), (float) (tz / 16.0));
			matrix.translate(x / 16f, y / 16f, z / 16f);
			float rx = startRx - (t == null ? 0 : (float) Math.toRadians(t.rx));
			float ry = startRy - (t == null ? 0 : (float) Math.toRadians(t.ry));
			float rz = startRz + (t == null ? 0 : (float) Math.toRadians(t.rz));
			if (extraRotation != null) {
				rx += extraRotation[0];
				ry += extraRotation[1];
				rz += extraRotation[2];
			}
			if (rx != 0 || ry != 0 || rz != 0) matrix.rotateZYX(rz, ry, rx);
			if (t != null && (t.sx != 1 || t.sy != 1 || t.sz != 1)) matrix.scale((float) t.sx, (float) t.sy, (float) t.sz);
		}
	}

	private record PartInfo(String name, String parent, float[] pivot, float[] rotation, boolean neverRender, List<CubeInfo> cubes, List<PartInfo> children) {}

	private record CubeInfo(float[] origin, float[] size, JsonElement uv, float inflate, boolean mirror) {}

	private enum Face {
		DOWN(0, -1, 0), UP(0, 1, 0), NORTH(0, 0, -1), SOUTH(0, 0, 1), WEST(-1, 0, 0), EAST(1, 0, 0);

		final int ox, oy, oz;

		Face(int ox, int oy, int oz) {
			this.ox = ox;
			this.oy = oy;
			this.oz = oz;
		}

		String key() {
			return name().toLowerCase();
		}
	}

	public final String name;
	public final int textureWidth, textureHeight;
	public final Bone root;
	private final Map<String, Bone> bones = new LinkedHashMap<>();

	private BedrockGeometry(String name, int textureWidth, int textureHeight, PartInfo rootInfo) {
		this.name = name;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.root = bake(rootInfo, null);
		register(root);
	}

	private void register(Bone bone) {
		bones.put(bone.name, bone);
		for (Bone child : bone.children) register(child);
	}

	public Bone bone(String name) {
		return bones.get(name);
	}

	public Set<String> subtree(String name) {
		Set<String> out = new HashSet<>();
		ArrayDeque<Bone> todo = new ArrayDeque<>();
		if (bones.containsKey(name)) todo.add(bones.get(name));
		while (!todo.isEmpty()) {
			Bone b = todo.poll();
			out.add(b.name);
			todo.addAll(b.children);
		}
		return out;
	}

	public boolean hasBone(String name) {
		return bones.containsKey(name);
	}

	public Map<String, Bone> bones() {
		return Collections.unmodifiableMap(bones);
	}

	public boolean boneMatrix(String boneName, Map<String, BoneTransform> pose, Map<String, float[]> extra, Matrix4f base) {
		Bone bone = bones.get(boneName);
		if (bone == null) return false;
		List<Bone> chain = new ArrayList<>();
		for (Bone b = bone; b != null; b = b.parent) chain.add(b);
		for (int i = chain.size() - 1; i >= 0; i--) {
			Bone b = chain.get(i);
			b.applyTransform(base, pose.get(b.name), extra == null ? null : extra.get(b.name));
		}
		return true;
	}

	public void forEachBone(Map<String, BoneTransform> pose, Matrix4f base, BiConsumer<Bone, Matrix4f> sink) {
		visitBones(root, pose, new Matrix4f(base), sink);
	}

	private void visitBones(Bone bone, Map<String, BoneTransform> pose, Matrix4f parentMatrix, BiConsumer<Bone, Matrix4f> sink) {
		if (bone.neverRender) return;
		Matrix4f matrix = new Matrix4f(parentMatrix);
		bone.applyTransform(matrix, pose.get(bone.name), null);
		sink.accept(bone, matrix);
		for (Bone child : bone.children) visitBones(child, pose, matrix, sink);
	}

	public void forEachQuad(Map<String, BoneTransform> pose, Map<String, float[]> extra, Matrix4f base, Set<String> hidden, QuadSink sink) {
		renderBone(root, pose, extra, new Matrix4f(base), hidden, sink);
	}

	private void renderBone(Bone bone, Map<String, BoneTransform> pose, Map<String, float[]> extra, Matrix4f parentMatrix, Set<String> hidden, QuadSink sink) {
		if (bone.neverRender || (hidden != null && hidden.contains(bone.name))) return;
		Matrix4f matrix = new Matrix4f(parentMatrix);
		bone.applyTransform(matrix, pose.get(bone.name), extra == null ? null : extra.get(bone.name));
		if (!bone.quads.isEmpty()) {
			Matrix3f normal = new Matrix3f(matrix).invert().transpose();
			for (Quad quad : bone.quads) sink.accept(matrix, normal, quad);
		}
		for (Bone child : bone.children) renderBone(child, pose, extra, matrix, hidden, sink);
	}

	public static BedrockGeometry parse(String name, JsonObject json) {
		JsonObject geo = null;
		if (json.has("minecraft:geometry")) {
			JsonArray array = json.getAsJsonArray("minecraft:geometry");
			geo = array.get(0).getAsJsonObject();
		} else {
			for (String key : json.keySet()) {
				if (key.startsWith("geometry.")) {
					geo = json.getAsJsonObject(key);
					break;
				}
			}
		}
		if (geo == null) throw new IllegalArgumentException("No geometry found in " + name);
		JsonObject desc = geo.has("description") ? geo.getAsJsonObject("description") : geo;
		int tw = desc.has("texture_width") ? desc.get("texture_width").getAsInt() : 64;
		int th = desc.has("texture_height") ? desc.get("texture_height").getAsInt() : 64;
		JsonArray bones = geo.getAsJsonArray("bones");
		Map<String, PartInfo> parts = new LinkedHashMap<>();
		for (JsonElement e : bones) {
			PartInfo info = parsePart(e.getAsJsonObject());
			parts.put(info.name(), info);
		}
		List<PartInfo> roots = new ArrayList<>();
		for (PartInfo part : parts.values()) {
			if (part.parent() != null && parts.containsKey(part.parent())) {
				parts.get(part.parent()).children().add(part);
			} else {
				roots.add(part);
			}
		}
		PartInfo rootInfo;
		if (roots.size() == 1) {
			rootInfo = roots.get(0);
		} else {
			rootInfo = new PartInfo("__root__", null, new float[3], new float[3], false, List.of(), roots);
		}
		return new BedrockGeometry(name, tw, th, rootInfo);
	}

	private static PartInfo parsePart(JsonObject json) {
		String name = json.get("name").getAsString();
		String parent = json.has("parent") ? json.get("parent").getAsString() : null;
		float[] pivot = vec(json.get("pivot"));
		float[] rotation = vec(json.get("rotation"));
		boolean neverRender = json.has("neverRender") && json.get("neverRender").getAsBoolean();
		boolean mirror = json.has("mirror") && json.get("mirror").getAsBoolean();
		List<CubeInfo> cubes = new ArrayList<>();
		List<PartInfo> children = new ArrayList<>();
		int generated = 0;
		if (json.has("cubes")) {
			for (JsonElement element : json.getAsJsonArray("cubes")) {
				JsonObject c = element.getAsJsonObject();
				CubeInfo cube = new CubeInfo(vec(c.get("origin")), vec(c.get("size")), c.get("uv"),
					c.has("inflate") ? c.get("inflate").getAsFloat() : 0f,
					c.has("mirror") ? c.get("mirror").getAsBoolean() : mirror);
				if (c.has("rotation")) {
					children.add(new PartInfo(name + "_generated_" + generated++, name, vec(c.get("pivot")), vec(c.get("rotation")), false, List.of(cube), new ArrayList<>()));
				} else {
					cubes.add(cube);
				}
			}
		}
		return new PartInfo(name, parent, pivot, rotation, neverRender, cubes, children);
	}

	private static float[] vec(JsonElement element) {
		float[] out = new float[3];
		if (element == null || !element.isJsonArray()) return out;
		JsonArray array = element.getAsJsonArray();
		for (int i = 0; i < 3 && i < array.size(); i++) out[i] = array.get(i).getAsFloat();
		return out;
	}

	private Bone bake(PartInfo info, PartInfo parent) {
		List<Quad> quads = new ArrayList<>();
		for (CubeInfo cube : info.cubes()) bakeCube(cube, info, quads);
		float px = parent == null ? -info.pivot()[0] : -(info.pivot()[0] - parent.pivot()[0]);
		float py = parent == null ? info.pivot()[1] : info.pivot()[1] - parent.pivot()[1];
		float pz = parent == null ? info.pivot()[2] : info.pivot()[2] - parent.pivot()[2];
		Bone bone = new Bone(info.name(), px, py, pz,
			(float) -Math.toRadians(info.rotation()[0]), (float) -Math.toRadians(info.rotation()[1]), (float) Math.toRadians(info.rotation()[2]),
			quads, info.neverRender());
		for (PartInfo childInfo : info.children()) {
			Bone child = bake(childInfo, info);
			child.parent = bone;
			bone.children.add(child);
		}
		return bone;
	}

	private void bakeCube(CubeInfo cube, PartInfo owner, List<Quad> out) {
		float[] o = cube.origin(), s = cube.size(), p = owner.pivot();
		float ox = -(o[0] + s[0] - p[0]), oy = o[1] - p[1], oz = o[2] - p[2];
		float inflate = cube.inflate();
		if (s[0] == 0 || s[1] == 0 || s[2] == 0) inflate = Math.max(0.008f, inflate);
		float x1 = ox, y1 = oy, z1 = oz;
		float x2 = x1 + Math.max(s[0], 0.008f) + inflate;
		float y2 = y1 + Math.max(s[1], 0.008f) + inflate;
		float z2 = z1 + Math.max(s[2], 0.008f) + inflate;
		x1 -= inflate;
		y1 -= inflate;
		z1 -= inflate;
		float[] v1 = {x1, y1, z1}, v2 = {x2, y1, z1}, v3 = {x2, y2, z1}, v4 = {x1, y2, z1};
		float[] v5 = {x1, y1, z2}, v6 = {x1, y2, z2}, v7 = {x2, y2, z2}, v8 = {x2, y1, z2};
		boolean mirror = cube.mirror();
		if (s[0] != 0 && s[1] != 0) {
			addQuad(out, v3, v2, v1, v4, cube, Face.NORTH, mirror);
			addQuad(out, v6, v5, v8, v7, cube, Face.SOUTH, mirror);
		}
		if (s[2] != 0 && s[1] != 0) {
			addQuad(out, v4, v1, v5, v6, cube, mirror ? Face.EAST : Face.WEST, mirror);
			addQuad(out, v7, v8, v2, v3, cube, mirror ? Face.WEST : Face.EAST, mirror);
		}
		if (s[0] != 0 && s[2] != 0) {
			addQuad(out, v2, v8, v5, v1, cube, Face.DOWN, mirror);
			addQuad(out, v7, v3, v4, v6, cube, Face.UP, mirror);
		}
	}

	private void addQuad(List<Quad> out, float[] p1, float[] p2, float[] p3, float[] p4, CubeInfo cube, Face face, boolean mirror) {
		int[] uv = resolveUv(cube, face);
		if (uv == null) return;
		float u1 = uv[0] / (float) textureWidth, u2 = uv[2] / (float) textureWidth;
		float v1 = uv[1] / (float) textureHeight, v2 = uv[3] / (float) textureHeight;
		if (mirror) {
			float tmp = u1;
			u1 = u2;
			u2 = tmp;
		}
		float[] positions = {
			p1[0] / 16f, p1[1] / 16f, p1[2] / 16f,
			p2[0] / 16f, p2[1] / 16f, p2[2] / 16f,
			p3[0] / 16f, p3[1] / 16f, p3[2] / 16f,
			p4[0] / 16f, p4[1] / 16f, p4[2] / 16f
		};
		float[] uvs = {u1, v1, u1, v2, u2, v2, u2, v1};
		out.add(new Quad(positions, uvs, mirror ? -face.ox : face.ox, face.oy, face.oz));
	}

	private static int[] resolveUv(CubeInfo cube, Face face) {
		JsonElement uv = cube.uv();
		if (uv == null) return null;
		if (uv.isJsonArray()) {
			JsonArray arr = uv.getAsJsonArray();
			float u = arr.get(0).getAsFloat(), v = arr.get(1).getAsFloat();
			int w = (int) cube.size()[0], h = (int) cube.size()[1], d = (int) cube.size()[2];
			float[] r = switch (face) {
				case EAST -> new float[]{u, v + d, u + d, v + d + h};
				case WEST -> new float[]{u + d + w, v + d, u + d + w + d, v + d + h};
				case DOWN -> new float[]{u + d + w, v + d, u + d + w + w, v};
				case UP -> new float[]{u + d, v, u + d + w, v + d};
				case NORTH -> new float[]{u + d, v + d, u + d + w, v + d + h};
				case SOUTH -> new float[]{u + d + w + d, v + d, u + d + w + d + w, v + d + h};
			};
			return new int[]{(int) Math.ceil(r[0]), (int) Math.ceil(r[1]), (int) Math.ceil(r[2]), (int) Math.ceil(r[3])};
		}
		JsonObject faces = uv.getAsJsonObject();
		if (!faces.has(face.key())) return null;
		JsonObject f = faces.getAsJsonObject(face.key());
		JsonArray start = f.getAsJsonArray("uv");
		JsonArray size = f.getAsJsonArray("uv_size");
		int u = start.get(0).getAsInt(), v = start.get(1).getAsInt();
		return new int[]{u, v, u + size.get(0).getAsInt(), v + size.get(1).getAsInt()};
	}
}
