package net.mcreator.fnafmod.client.bedrock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ClipPlayer {
	private Object key;
	private BedrockAnimation current;
	private double startedAt;
	private Map<String, BoneTransform> blendFrom = Map.of();
	private double blendStart, blendLength;

	public void play(Object newKey, BedrockAnimation clip, double now, double blendSeconds) {
		if (newKey != null && newKey.equals(key)) return;
		blendFrom = current == null ? Map.of() : pose(now);
		blendStart = now;
		blendLength = current == null ? 0 : blendSeconds;
		key = newKey;
		current = clip;
		startedAt = now;
	}

	public Object key() {
		return key;
	}

	public Map<String, BoneTransform> pose(double now) {
		if (current == null) return Map.of();
		Map<String, BoneTransform> target = current.pose(current.clipTime(now - startedAt));
		double k = blendLength <= 0 ? 1 : Math.min(1, (now - blendStart) / blendLength);
		if (k >= 1) return target;
		k = k * k * (3 - 2 * k);
		Set<String> bones = new HashSet<>(blendFrom.keySet());
		bones.addAll(target.keySet());
		Map<String, BoneTransform> out = new HashMap<>();
		for (String bone : bones) out.put(bone, lerp(blendFrom.get(bone), target.get(bone), k));
		return out;
	}

	private static BoneTransform lerp(BoneTransform a, BoneTransform b, double k) {
		BoneTransform zero = new BoneTransform();
		if (a == null) a = zero;
		if (b == null) b = zero;
		BoneTransform t = new BoneTransform();
		t.tx = a.tx + (b.tx - a.tx) * k;
		t.ty = a.ty + (b.ty - a.ty) * k;
		t.tz = a.tz + (b.tz - a.tz) * k;
		t.rx = a.rx + (b.rx - a.rx) * k;
		t.ry = a.ry + (b.ry - a.ry) * k;
		t.rz = a.rz + (b.rz - a.rz) * k;
		t.sx = a.sx + (b.sx - a.sx) * k;
		t.sy = a.sy + (b.sy - a.sy) * k;
		t.sz = a.sz + (b.sz - a.sz) * k;
		return t;
	}
}
