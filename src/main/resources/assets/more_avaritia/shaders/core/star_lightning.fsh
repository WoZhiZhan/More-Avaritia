#version 150

#define PI 3.14159265359
#define TAU 6.28318530718

uniform float time;
uniform float StarSpeed;
uniform float opacity;

in vec4 vertexColor;
in vec3 localPosition;

out vec4 fragColor;

float hash12(vec2 point) {
    point = fract(point * vec2(127.1, 311.7));
    point += dot(point, point + 37.19);
    return fract(point.x * point.y);
}

vec3 rainbow(float hue) {
    vec3 c = vec3(
            0.5 + 0.5 * cos(6.28318 * hue),
            0.5 + 0.5 * cos(6.28318 * hue + 2.0944),
            0.5 + 0.5 * cos(6.28318 * hue + 4.1888));
    return c * c * (3.0 - 2.0 * c);
}

vec2 hash22(vec2 point) {
    float seed = hash12(point);
    return vec2(seed, hash12(point + seed + 17.13));
}

float starryNoise2(vec2 point) {
    vec2 cell = floor(point);
    vec2 local = fract(point);
    local = local * local * (3.0 - 2.0 * local);
    return mix(
            mix(hash12(cell), hash12(cell + vec2(1.0, 0.0)), local.x),
            mix(hash12(cell + vec2(0.0, 1.0)), hash12(cell + vec2(1.0, 1.0)), local.x),
            local.y);
}

float fbm(vec2 point) {
    float value = 0.0;
    float amplitude = 0.5;
    for (int i = 0; i < 4; i++) {
        value += starryNoise2(point) * amplitude;
        point = point * 2.03 + vec2(3.7, -2.1);
        amplitude *= 0.5;
    }
    return value;
}

vec3 movingStars(vec2 point, float scale, float threshold, float phase, float salt) {
    vec2 scaled = point * vec2(scale, scale * 1.55);
    vec2 cell = floor(scaled);
    vec2 local = fract(scaled) - 0.5;
    vec2 base = (hash22(cell + salt) - 0.5) * 0.72;
    float seed = hash12(cell + salt * 1.73);

    // 星点只在自己的网格内做连续摆动，不会跨格瞬移。
    base.y += sin(phase * (0.72 + seed * 0.55) + seed * 70.0) * 0.12;
    base.x += cos(phase * (0.45 + seed * 0.35) + seed * 43.0) * 0.035;
    vec2 delta = local - base;

    float visible = smoothstep(threshold, min(threshold + 0.12, 0.999), seed);
    float d2 = dot(delta, delta);
    float core = exp(-d2 / 0.00082);
    float halo = exp(-d2 / 0.010) * 0.32;
    float ray = (exp(-abs(delta.x) * 135.0) * exp(-abs(delta.y) * 7.0)
               + exp(-abs(delta.y) * 135.0) * exp(-abs(delta.x) * 7.0))
               * smoothstep(0.78, 0.98, seed) * 0.18;
    float twinkle = 0.72 + 0.28 * sin(phase * (1.1 + seed * 1.8) + seed * 93.0);

    // 每颗星独立彩虹色
    vec3 temperature = rainbow(fract(seed * 0.67 + phase * 0.05));
    return temperature * visible * (core * 0.85 + halo * 0.90 + ray * 0.80) * twinkle;
}

float verticalNebula(vec2 point, float phase, float salt) {
    float broad = fbm(point * vec2(2.4, 5.6) + vec2(salt, phase * 0.030));
    float fine = fbm(point * vec2(7.0, 19.0) + vec2(-salt * 0.7, -phase * 0.016));
    float cloud = smoothstep(0.42, 0.80, broad * 0.68 + fine * 0.32);
    float filament = 0.5 + 0.5 * sin(point.y * 15.0 + sin(point.x * 6.0) * 2.2 - phase * 0.20);
    return cloud * (0.30 + filament * 0.70);
}

float verticalRibbon(vec2 point, float phase, float offset) {
    float center = 0.50 + offset
                 + sin(point.y * 8.0 + phase * 0.18) * 0.16
                 + sin(point.y * 23.0 - phase * 0.11) * 0.045;
    float distanceToRibbon = abs(fract(point.x) - center);
    float body = exp(-distanceToRibbon * distanceToRibbon / 0.026);
    float breakup = 0.35 + 0.65 * fbm(point * vec2(3.0, 9.0) + vec2(phase * 0.02, offset));
    return body * breakup;
}

void main() {
    float coverage = max(vertexColor.a, 0.85) * opacity;
    if (coverage < 0.002) discard;

    float phase = time * StarSpeed * 0.052;
    float height = localPosition.y * 0.0205;
    float side = localPosition.x * 0.050 + localPosition.z * 0.037;
    vec2 point = vec2(side * 0.82 + 0.5, height * 0.44);

    vec3 stars = movingStars(point, 7.0, 0.50, phase, 2.4);
    stars += movingStars(fract(point + vec2(0.23, phase * 0.06)), 13.0, 0.64,
                         phase * 0.86, 8.1) * 0.72;
    stars += movingStars(fract(point * vec2(1.0, 1.45) + vec2(0.41, phase * 0.10)),
                         22.0, 0.78, phase * 1.18, 15.7) * 0.52;
    stars += movingStars(fract(point * vec2(1.0, 2.10) + vec2(0.08, -phase * 0.07)),
                         35.0, 0.88, phase * 0.64, 24.3) * 0.32;

    float nebula = verticalNebula(point, phase, 4.7);
    float ribbonA = verticalRibbon(point, phase, -0.17);
    float ribbonB = verticalRibbon(fract(point + vec2(0.34, 0.11)), -phase * 0.74, 0.16) * 0.72;

    // 一道平滑的竖向星流亮面，提供清楚的运动方向。
    float sweepPosition = fract(height * 0.115 + phase * 0.46);
    float sweep = exp(-abs(sweepPosition - 0.5) * 20.0)
                * (0.45 + 0.55 * sin(side * 17.0 + phase * 0.35));

    float starEnergy = dot(stars, vec3(0.3333));
    float hueBase = fract(time * 0.03);

    // 多色星云：两个色相互相混合，颜色随位置变化，同一屏里同时出现多种颜色
    float mixNeb = 0.5 + 0.5 * sin(point.x * 3.1 + point.y * 2.3 + phase * 0.12);
    vec3 nebulaCol = mix(rainbow(hueBase + point.y * 0.22),
                         rainbow(hueBase + 0.42 + point.x * 0.30), mixNeb);
    vec3 ribbonACol = rainbow(hueBase + 0.12 + point.x * 0.55);
    vec3 ribbonBCol = rainbow(hueBase + 0.58 + point.x * 0.35);
    vec3 sweepCol = rainbow(hueBase + 0.25 + side * 0.08);

    vec3 color = vec3(0.010, 0.008, 0.024) * 0.90;
    color += nebulaCol * nebula * 0.70;
    color += ribbonACol * ribbonA * 0.85;
    color += ribbonBCol * ribbonB * 0.55;
    color += sweepCol * sweep * 0.25;
    color += stars * 1.10;

    // 提升饱和度但不过曝（晚上不刺眼、白天不发白）
    float lum = dot(color, vec3(0.299, 0.587, 0.114));
    color = clamp(mix(vec3(lum), color, 1.35), vec3(0.0), vec3(1.0));

    float detail = starEnergy * 1.00 + nebula * 0.12 + ribbonA * 0.45
                 + ribbonB * 0.28 + sweep * 0.14;
    float alpha = coverage * clamp(0.50 + detail * 0.75, 0.0, 1.0);
    if (alpha < 0.004) discard;

    fragColor = vec4(color, alpha);
}
