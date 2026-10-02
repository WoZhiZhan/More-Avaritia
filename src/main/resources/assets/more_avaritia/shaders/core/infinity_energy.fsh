#version 150

uniform float time;

in vec4 vertexColor;
in vec2 vUv;
in float vDist;
in vec3 localPosition;
in vec3 viewDirection;

out vec4 fragColor;

float hash12(vec2 p) {
    p = fract(p * vec2(127.1, 311.7));
    p += dot(p, p + 37.19);
    return fract(p.x * p.y);
}

vec3 rainbow(float h) {
    vec3 c = vec3(
            0.5 + 0.5 * cos(6.28318 * h),
            0.5 + 0.5 * cos(6.28318 * h + 2.0944),
            0.5 + 0.5 * cos(6.28318 * h + 4.1888));
    return c * c * (3.0 - 2.0 * c);
}

float starLayer(vec2 uv, float density, float size, float t, float salt) {
    vec2 gv = uv * density;
    vec2 id = floor(gv);
    vec2 f = fract(gv) - 0.5;

    float h = hash12(id + salt);
    float h2 = hash12(id + 3.71 + salt);
    float h3 = hash12(id + 9.13 + salt);

    vec2 off = (vec2(h2, h3) - 0.5) * 0.72;
    float d = length(f - off);
    float tw = clamp(0.35 + 0.65 * sin(t * (0.8 + h * 4.0) + h * 63.0), 0.0, 1.0);

    float star = (1.0 - smoothstep(0.0, size, d)) * step(0.70, h) * tw;
    float bright = (1.0 - smoothstep(0.0, size * 2.6, d)) * step(0.955, h) * (0.6 + 0.4 * tw);
    return star + bright * 0.8;
}

void main() {
    vec3 normal = normalize(localPosition);
    float t = time * 0.05;

    // 无接缝的抛物面映射，把球面展开成星点平面
    vec2 plane = normal.xz / (1.0 + abs(normal.y));

    float rim = pow(1.0 - abs(dot(normal, normalize(viewDirection))), 2.0);

    float hue = fract(t * 0.02 + normal.x * 0.22 + normal.y * 0.18 + normal.z * 0.15);
    vec3 col = rainbow(hue) * (0.32 + rim * 1.2);
    col += vec3(1.0) * rim * 0.3;

    float stars = starLayer(plane, 7.0, 0.14, t, 3.0);
    stars += starLayer(plane * 1.7 + 11.0, 12.0, 0.2, t * 1.2, 9.0) * 0.6;
    stars += starLayer(plane * 2.6 - 7.0, 18.0, 0.24, t * 0.9, 15.0) * 0.4;
    col += rainbow(fract(hue + 0.35)) * stars * 1.7;
    col += vec3(1.0) * stars * stars * 0.5;

    float alpha = clamp(0.72 + rim * 0.35 + stars * 0.7, 0.0, 1.0);
    fragColor = vec4(col, alpha);
}
