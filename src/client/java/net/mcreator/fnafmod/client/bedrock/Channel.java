package net.mcreator.fnafmod.client.bedrock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public interface Channel {
	Channel ZERO = new Constant(new Molang.Expr[]{Molang.constant(0)});
	Channel ONE = new Constant(new Molang.Expr[]{Molang.constant(1)});

	double[] get(Query query);

	int size();

	static Channel parse(JsonElement json) {
		if (json == null || json.isJsonNull()) return null;
		if (json.isJsonPrimitive()) return new Constant(new Molang.Expr[]{expr(json)});
		if (json.isJsonObject() && json.getAsJsonObject().has("vector")) {
			return parse(json.getAsJsonObject().get("vector"));
		}
		if (json.isJsonArray()) {
			JsonArray array = json.getAsJsonArray();
			Molang.Expr[] values = new Molang.Expr[array.size()];
			for (int i = 0; i < values.length; i++) values[i] = expr(array.get(i));
			return new Constant(values);
		}
		JsonObject object = json.getAsJsonObject();
		List<Map.Entry<String, JsonElement>> validEntries = new ArrayList<>();
		for (Map.Entry<String, JsonElement> e : object.entrySet()) {
			try {
				Double.parseDouble(e.getKey());
				validEntries.add(e);
			} catch (NumberFormatException ignored) {
			}
		}
		if (validEntries.isEmpty()) {
			return null;
		}
		return Keyframes.parse(validEntries);
	}

	private static Molang.Expr expr(JsonElement value) {
		if (value.getAsJsonPrimitive().isNumber()) return Molang.constant(value.getAsDouble());
		return Molang.parse(value.getAsString());
	}

	record Constant(Molang.Expr[] values) implements Channel {
		@Override
		public double[] get(Query query) {
			if (values.length >= 3) return new double[]{values[0].eval(query), values[1].eval(query), values[2].eval(query)};
			double v = values[0].eval(query);
			return new double[]{v, v, v};
		}

		@Override
		public int size() {
			return values.length;
		}
	}

	record KeyFrame(double time, Channel pre, Channel post, boolean smooth) {
		Channel prev() {
			return post;
		}

		Channel next() {
			return pre;
		}

		KeyFrame at(double newTime) {
			return new KeyFrame(newTime, pre, post, smooth);
		}
	}

	record Keyframes(double[] times, List<KeyFrame> frames) implements Channel {
		static Keyframes parse(List<Map.Entry<String, JsonElement>> entries) {
			entries.sort(Comparator.comparingDouble(e -> Double.parseDouble(e.getKey())));
			List<KeyFrame> frames = new ArrayList<>();
			List<Double> times = new ArrayList<>();
			for (Map.Entry<String, JsonElement> entry : entries) {
				double time = Double.parseDouble(entry.getKey());
				JsonElement value = entry.getValue();
				Channel pre, post;
				boolean smooth = false;
				if (value.isJsonObject() && (value.getAsJsonObject().has("pre") || value.getAsJsonObject().has("post"))) {
					JsonObject obj = value.getAsJsonObject();
					pre = obj.has("pre") ? Channel.parse(obj.get("pre")) : null;
					post = obj.has("post") ? Channel.parse(obj.get("post")) : null;
					if (pre == null) pre = post;
					if (post == null) post = pre;
					smooth = "catmullrom".equalsIgnoreCase(obj.has("lerp_mode") ? obj.get("lerp_mode").getAsString() : "");
				} else {
					pre = post = Channel.parse(value);
				}
				if (pre != null) {
					times.add(time);
					frames.add(new KeyFrame(time, pre, post, smooth));
				}
			}
			double[] timesArray = new double[times.size()];
			for (int i = 0; i < times.size(); i++) timesArray[i] = times.get(i);
			return new Keyframes(timesArray, frames);
		}

		@Override
		public double[] get(Query query) {
			if (frames.isEmpty()) return new double[]{0, 0, 0};
			double t = query.animTime;
			if (t <= times[0]) return frames.get(0).post().get(query);
			if (t >= times[times.length - 1]) return frames.get(frames.size() - 1).pre().get(query);
			int idx = 0;
			while (idx < times.length - 1 && times[idx + 1] < t) idx++;
			KeyFrame a = frames.get(idx), b = frames.get(idx + 1);
			double duration = b.time() - a.time();
			double k = duration <= 0 ? 0 : (t - a.time()) / duration;
			k = Math.max(0, Math.min(1, k));
			if (b.smooth()) k = k * k * (3 - 2 * k);
			double[] va = a.post().get(query);
			double[] vb = b.pre().get(query);
			int len = Math.max(va.length, vb.length);
			double[] out = new double[len];
			for (int i = 0; i < len; i++) {
				double da = i < va.length ? va[i] : 0;
				double db = i < vb.length ? vb[i] : 0;
				out[i] = da + (db - da) * k;
			}
			return out;
		}

		@Override
		public int size() {
			return frames.isEmpty() ? 0 : frames.get(0).post().size();
		}
	}
}
