package net.mcreator.fnafmod.client.bedrock;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Molang {
	@FunctionalInterface
	public interface Expr {
		double eval(Query query);
	}

	private final String src;
	private int pos;

	private Molang(String src) {
		this.src = src;
	}

	public static Expr constant(double value) {
		return query -> value;
	}

	public static Expr parse(String expression) {
		String fixed = expression.replaceAll("[+\\-*/%&^|?]+\\s*\\)", ")").trim();
		try {
			double value = Double.parseDouble(fixed);
			return constant(value);
		} catch (NumberFormatException ignored) {
		}
		Molang parser = new Molang(fixed.toLowerCase(Locale.ROOT));
		Expr expr = parser.expression();
		parser.skipSpaces();
		if (parser.pos < parser.src.length()) throw new IllegalArgumentException("Unexpected '" + parser.src.charAt(parser.pos) + "' in molang: " + expression);
		return expr;
	}

	private Expr expression() {
		Expr left = term();
		while (true) {
			if (eat('+')) {
				Expr a = left, b = term();
				left = q -> a.eval(q) + b.eval(q);
			} else if (eat('-')) {
				Expr a = left, b = term();
				left = q -> a.eval(q) - b.eval(q);
			} else {
				return left;
			}
		}
	}

	private Expr term() {
		Expr left = unary();
		while (true) {
			if (eat('*')) {
				Expr a = left, b = unary();
				left = q -> a.eval(q) * b.eval(q);
			} else if (eat('/')) {
				Expr a = left, b = unary();
				left = q -> a.eval(q) / b.eval(q);
			} else {
				return left;
			}
		}
	}

	private Expr unary() {
		if (eat('-')) {
			Expr inner = unary();
			return q -> -inner.eval(q);
		}
		if (eat('+')) return unary();
		return primary();
	}

	private Expr primary() {
		skipSpaces();
		if (eat('(')) {
			Expr inner = expression();
			expect(')');
			return inner;
		}
		int start = pos;
		if (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.')) {
			while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.' || src.charAt(pos) == 'e')) pos++;
			double value = Double.parseDouble(src.substring(start, pos));
			return constant(value);
		}
		while (pos < src.length() && (Character.isLetterOrDigit(src.charAt(pos)) || src.charAt(pos) == '_' || src.charAt(pos) == '.')) pos++;
		String name = src.substring(start, pos);
		if (name.isEmpty()) throw new IllegalArgumentException("Unexpected symbol in molang at " + pos + ": " + src);
		if (eat('(')) {
			List<Expr> args = new ArrayList<>();
			if (!eat(')')) {
				do {
					args.add(expression());
				} while (eat(','));
				expect(')');
			}
			return function(name, args);
		}
		return variable(name);
	}

	private static Expr variable(String name) {
		return switch (name) {
			case "query.anim_time", "q.anim_time" -> q -> q.animTime;
			case "query.anim_length", "q.anim_length", "query.anim_duration", "q.anim_duration" -> q -> q.animLength;
			case "query.life_time", "q.life_time" -> q -> q.lifeTime;
			case "math.pi" -> constant(Math.PI);
			default -> constant(0);
		};
	}

	private static Expr function(String name, List<Expr> a) {
		return switch (name) {
			case "math.sin" -> q -> Math.sin(Math.toRadians(a.get(0).eval(q)));
			case "math.cos" -> q -> Math.cos(Math.toRadians(a.get(0).eval(q)));
			case "math.abs" -> q -> Math.abs(a.get(0).eval(q));
			case "math.sqrt" -> q -> Math.sqrt(a.get(0).eval(q));
			case "math.floor" -> q -> Math.floor(a.get(0).eval(q));
			case "math.ceil" -> q -> Math.ceil(a.get(0).eval(q));
			case "math.round" -> q -> Math.round(a.get(0).eval(q));
			case "math.trunc" -> q -> (double) (long) a.get(0).eval(q);
			case "math.exp" -> q -> Math.exp(a.get(0).eval(q));
			case "math.ln" -> q -> Math.log(a.get(0).eval(q));
			case "math.pow" -> q -> Math.pow(a.get(0).eval(q), a.get(1).eval(q));
			case "math.min" -> q -> Math.min(a.get(0).eval(q), a.get(1).eval(q));
			case "math.max" -> q -> Math.max(a.get(0).eval(q), a.get(1).eval(q));
			case "math.mod" -> q -> a.get(0).eval(q) % a.get(1).eval(q);
			case "math.clamp" -> q -> Math.max(a.get(1).eval(q), Math.min(a.get(2).eval(q), a.get(0).eval(q)));
			case "math.lerp" -> q -> {
				double from = a.get(0).eval(q);
				return from + (a.get(1).eval(q) - from) * a.get(2).eval(q);
			};
			case "math.random" -> q -> {
				double min = a.get(0).eval(q);
				return min + Math.random() * (a.get(1).eval(q) - min);
			};
			default -> constant(0);
		};
	}

	private boolean eat(char c) {
		skipSpaces();
		if (pos < src.length() && src.charAt(pos) == c) {
			pos++;
			return true;
		}
		return false;
	}

	private void expect(char c) {
		if (!eat(c)) throw new IllegalArgumentException("Expected '" + c + "' in molang: " + src);
	}

	private void skipSpaces() {
		while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
	}
}
