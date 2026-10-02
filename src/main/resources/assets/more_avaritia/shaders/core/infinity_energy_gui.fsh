#version 150

uniform float IconTime;

in vec4 vertexColor;
in vec3 orbDirection;

out vec4 fragColor;

float hash12(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float noise2D(vec2 p) {
    vec2 cell = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash12(cell), hash12(cell + vec2(1.0, 0.0)), f.x),
               mix(hash12(cell + vec2(0.0, 1.0)), hash12(cell + vec2(1.0, 1.0)), f.x), f.y);
}

vec3 rainbow(float hue) {
    vec3 c = vec3(0.5 + 0.5 * cos(6.28318 * hue),
                  0.5 + 0.5 * cos(6.28318 * hue + 2.0944),
                  0.5 + 0.5 * cos(6.28318 * hue + 4.1888));
    return c * c * (3.0 - 2.0 * c);
}

float star(vec2 p, vec2 center, float size, float t) {
    vec2 d = p - center;
    float core = 1.0 - smoothstep(size * 0.2, size, length(d));
    float glow = 0.14 * exp(-dot(d, d) / (size * size * 6.0));
    return (core + glow) * (0.88 + 0.12 * sin(t * 0.7 + center.x * 7.0));
}

void main() {
    vec3 dir = normalize(orbDirection);
    float t = IconTime;
    float angle = -t * 0.16;
    vec2 p = vec2(dir.x * cos(angle) - dir.y * sin(angle),
                  dir.x * sin(angle) + dir.y * cos(angle));
    float cloud = noise2D(p * 2.7 + vec2(t * 0.035, -t * 0.02));
    float hue = fract(0.59 + t * 0.005 + p.x * 0.17 + p.y * 0.14);
    vec3 color = vec3(0.05, 0.03, 0.11) + rainbow(hue) * (0.16 + 0.08 * cloud);

    float lane = p.y + 0.35 * p.x + 0.08 * sin(p.x * 2.7 + t * 0.11);
    float galaxy = exp(-9.0 * lane * lane);
    color += rainbow(fract(hue + 0.28)) * galaxy * (0.24 + 0.13 * cloud);
    color += rainbow(fract(hue + 0.48)) * pow(1.0 - abs(dir.z), 2.0) * 0.18;
    float dust = lane + 0.04 * sin(p.x * 4.0 - t * 0.075);
    color *= 1.0 - 0.20 * exp(-90.0 * dust * dust);

    color += vec3(0.91, 0.85, 0.98) * star(p, vec2(-0.32, 0.25), 0.15, t) * 0.82;
    color += vec3(0.57, 0.78, 0.97) * star(p, vec2(0.40, -0.27), 0.13, t) * 0.64;
    color += vec3(0.84, 0.65, 0.88) * star(p, vec2(-0.06, -0.56), 0.09, t) * 0.38;

    vec2 grid = (p + 1.0) * 2.1;
    vec2 cell = floor(grid);
    vec2 offset = 0.2 + 0.6 * vec2(hash12(cell + 2.3), hash12(cell + 4.7));
    float specks = step(0.91, hash12(cell + 11.0))
                 * (1.0 - smoothstep(0.12, 0.38, length(fract(grid) - offset)));
    color += vec3(0.72, 0.75, 0.91) * specks * 0.24;

    fragColor = vec4(clamp(color * vertexColor.rgb, 0.0, 1.0), vertexColor.a);
}
