package net.mcreator.fnafmod.client.bedrock;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.mcreator.fnafmod.FnafMod;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public final class BedrockAssets {
	private static final Map<String, BedrockGeometry> GEOMETRY = new HashMap<>();
	private static final Map<String, Map<String, BedrockAnimation>> ANIMATIONS = new HashMap<>();

	private BedrockAssets() {}

	public static BedrockGeometry geometry(String name) {
		return GEOMETRY.computeIfAbsent(name, n -> {
			try {
				JsonObject obj = read("geo/" + n + ".geo.json");
				if (obj != null) return BedrockGeometry.parse(n, obj);
			} catch (Exception e) {
				System.err.println("[FnafMod] Missing bedrock geometry for: " + n);
			}
			return null;
		});
	}

	public static Map<String, BedrockAnimation> animations(String name) {
		return ANIMATIONS.computeIfAbsent(name, n -> {
			try {
				JsonObject json = read("animations/" + n + ".animation.json");
				if (json != null && json.has("animations")) {
					Map<String, BedrockAnimation> clips = new HashMap<>();
					JsonObject root = json.getAsJsonObject("animations");
					for (String clip : root.keySet()) clips.put(clip, BedrockAnimation.parse(clip, root.getAsJsonObject(clip)));
					return Map.copyOf(clips);
				}
			} catch (Exception e) {
				System.err.println("[FnafMod] Missing bedrock animations for: " + n);
			}
			return Map.of();
		});
	}

	public static void clear() {
		GEOMETRY.clear();
		ANIMATIONS.clear();
	}

	private static JsonObject read(String path) {
		Identifier id = FnafMod.id(path);
		var res = Minecraft.getInstance().getResourceManager().getResource(id);
		if (res.isEmpty()) return null;
		try (Reader reader = res.get().openAsReader()) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception e) {
			return null;
		}
	}
}
