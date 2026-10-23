package net.mcreator.fnafmod.client.bedrock;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record BedrockAnimation(String name, LoopMode loop, double length, Map<String, BoneAnimation> bones) {
	public enum LoopMode { ONCE, HOLD_ON_LAST_FRAME, LOOP }

	public record BoneAnimation(Channel rotation, Channel position, Channel scale) {
		void apply(Query query, BoneTransform target) {
			double[] r = rotation.get(query);
			double[] p = position.get(query);
			double[] s = scale.get(query);
			target.tx += p[0];
			target.ty += p[1];
			target.tz += p[2];
			target.rx += r[0];
			target.ry += r[1];
			target.rz += r[2];
			target.sx *= s[0];
			target.sy *= s[1];
			target.sz *= s[2];
		}
	}

	public double clipTime(double elapsed) {
		if (length <= 0) return elapsed;
		return loop == LoopMode.LOOP ? elapsed % length : Math.min(elapsed, length);
	}

	public boolean finished(double elapsed) {
		return loop != LoopMode.LOOP && elapsed >= length;
	}

	public Map<String, BoneTransform> pose(double time) {
		Query query = new Query();
		query.animTime = time;
		query.animLength = length;
		query.lifeTime = time;
		Map<String, BoneTransform> pose = new HashMap<>();
		for (Map.Entry<String, BoneAnimation> entry : bones.entrySet()) entry.getValue().apply(query, pose.computeIfAbsent(entry.getKey(), k -> new BoneTransform()));
		return pose;
	}

	public static BedrockAnimation parse(String name, JsonObject json) {
		LoopMode loop = LoopMode.ONCE;
		JsonElement loopJson = json.get("loop");
		if (loopJson != null && loopJson.isJsonPrimitive()) {
			if (loopJson.getAsJsonPrimitive().isBoolean()) loop = loopJson.getAsBoolean() ? LoopMode.LOOP : LoopMode.ONCE;
			else if ("hold_on_last_frame".equalsIgnoreCase(loopJson.getAsString())) loop = LoopMode.HOLD_ON_LAST_FRAME;
			else if ("true".equalsIgnoreCase(loopJson.getAsString())) loop = LoopMode.LOOP;
		}
		if (loop == LoopMode.ONCE && (name.contains("idle") || name.contains("walk") || name.contains("run") || name.contains("sprint") || name.contains("day") || name.contains("showtime"))) {
			loop = LoopMode.LOOP;
		}

		double length = json.has("animation_length") ? json.get("animation_length").getAsDouble() : 0;
		double maxTime = 0.0;
		Map<String, BoneAnimation> bones = new HashMap<>();
		if (json.has("bones")) {
			for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("bones").entrySet()) {
				JsonObject bone = entry.getValue().getAsJsonObject();
				for (String ch : List.of("rotation", "position", "scale")) {
					if (bone.has(ch) && bone.get(ch).isJsonObject()) {
						for (String k : bone.getAsJsonObject(ch).keySet()) {
							try {
								maxTime = Math.max(maxTime, Double.parseDouble(k));
							} catch (NumberFormatException ignored) {}
						}
					}
				}
				Channel rotation = Channel.parse(bone.get("rotation"));
				Channel position = Channel.parse(bone.get("position"));
				Channel scale = Channel.parse(bone.get("scale"));
				if (rotation == null) rotation = Channel.ZERO;
				if (position == null) position = Channel.ZERO;
				if (scale == null) scale = Channel.ONE;
				bones.put(entry.getKey(), new BoneAnimation(rotation, position, scale));
			}
		}
		if (length <= 0) {
			length = Math.max(1.0, maxTime);
		}
		return new BedrockAnimation(name, loop, length, Map.copyOf(bones));
	}
}
