#version 150

#define M_PI 3.1415926535897932384626433832795

#moj_import <fog.glsl>

const int cosmiccount = 10;
const int cosmicoutof = 101;
const float lightmix = 0.2f;

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float time;

uniform float yaw;
uniform float pitch;
uniform float externalScale;

uniform float opacity;

uniform mat2 cosmicuvs[cosmiccount];

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
in vec3 fPos;

out vec4 fragColor;

mat4 rotationMatrix(vec3 axis, float angle)
{
    axis = normalize(axis);
    float s = sin(angle);
    float c = cos(angle);
    float oc = 1.0 - c;

    return mat4(oc * axis.x * axis.x + c,           oc * axis.x * axis.y - axis.z * s,  oc * axis.z * axis.x + axis.y * s,  0.0,
    oc * axis.x * axis.y + axis.z * s,  oc * axis.y * axis.y + c,           oc * axis.y * axis.z - axis.x * s,  0.0,
    oc * axis.z * axis.x - axis.y * s,  oc * axis.y * axis.z + axis.x * s,  oc * axis.z * axis.z + c,           0.0,
    0.0,                                0.0,                                0.0,                                1.0);
}

void main (void)
{
    vec4 mask = texture(Sampler0, texCoord0.xy);

    float oneOverExternalScale = 1.0/externalScale;

    int uvtiles = 16;

    // 星空背景色 - 深紫蓝渐变
vec4 col = vec4(
0.5 + 0.5 * sin(fPos.x + time * 0.1), // 红
0.5 + 0.5 * sin(fPos.y + time * 0.2), // 绿
0.5 + 0.5 * sin(fPos.z + time * 0.3), // 蓝
1.0
);


float pulse = mod(time, 600) / 600.0; // 稍微放慢脉冲

    // 添加紫蓝渐变效果
    col.r += sin(pulse * M_PI * 1.5) * 0.08 + 0.12; // 紫色分量
    col.g += cos(pulse * M_PI * 2.0) * 0.06 + 0.08; // 绿色分量（少量）
    col.b += sin(pulse * M_PI * 1.8 + M_PI * 0.5) * 0.15 + 0.35; // 蓝色分量

    // 添加深度渐变效果
    float depth = length(fPos) / 10.0;
    vec3 deepSpace = vec3(0.02, 0.01, 0.08); // 更深的空间色彩
    vec3 nearSpace = vec3(0.08, 0.05, 0.25); // 较近的空间色彩
col.rgb = mix(deepSpace, col.rgb, clamp(1.0 - depth*0.5, 0.0, 1.0));
    // get ray from camera to fragment
    vec4 dir = normalize(vec4(-fPos, 0));

    // rotate the ray to show the right bit of the sphere for the angle
    float sb = sin(pitch);
    float cb = cos(pitch);
    dir = normalize(vec4(dir.x, dir.y * cb - dir.z * sb, dir.y * sb + dir.z * cb, 0));

    float sa = sin(-yaw);
    float ca = cos(-yaw);
    dir = normalize(vec4(dir.z * sa + dir.x * ca, dir.y, dir.z * ca - dir.x * sa, 0));

    vec4 ray;

    // draw the layers
    for (int i = 0; i < 16; i++) {
        int mult = 16 - i;

        // get semi-random stuff
        int j = i + 7;
        float rand1 = (j * j * 4321 + j * 8) * 2.0F;
        int k = j + 1;
        float rand2 = (k * k * k * 239 + k * 37) * 3.6F;
        float rand3 = rand1 * 347.4 + rand2 * 63.4;

        // random rotation matrix by random rotation around random axis
        vec3 axis = normalize(vec3(sin(rand1), sin(rand2), cos(rand3)));

        // apply
        ray = dir * rotationMatrix(axis, mod(rand3, 2 * M_PI));

        // calcuate the UVs from the final ray
        float rawu = 0.5 + (atan(ray.z, ray.x) / (2 * M_PI));
        float rawv = 0.5 + (asin(ray.y) / M_PI);

        // get UV scaled for layers and offset by time;
        float scale = mult * 0.5 + 2.75;
        float u = rawu * scale * externalScale;
        float v = (rawv + time * 0.0002 * oneOverExternalScale) * scale * 0.6 * externalScale;

        vec2 tex = vec2(u, v);

        // tile position of the current uv
        int tu = int(mod(floor(u * uvtiles), uvtiles));
        int tv = int(mod(floor(v * uvtiles), uvtiles));

        // get pseudorandom variants
        int position = ((171 * tu) + (489 * tv) + (303 * (i + 31)) + 17209) ^ 10;
        int symbol = int(mod(position, cosmicoutof));
        int rotation = int(mod(pow(tu, float(tv)) + tu + 3 + tv * i, 8));
        bool flip = false;
        if (rotation >= 4) {
            rotation -= 4;
            flip = true;
        }

        // if it's an icon, then add the colour!
        if (symbol >= 0 && symbol < cosmiccount) {

            vec2 cosmictex = vec2(1.0, 1.0);
            vec4 tcol = vec4(1.0, 0.0, 0.0, 1.0);

            // get uv within the tile
            float ru = clamp(mod(u, 1.0) * uvtiles - tu, 0.0, 1.0);
            float rv = clamp(mod(v, 1.0) * uvtiles - tv, 0.0, 1.0);

            if (flip) {
                ru = 1.0 - ru;
            }

            float oru = ru;
            float orv = rv;

            // rotate uvs if necessary
            if (rotation == 1) {
                oru = 1.0 - rv;
                orv = ru;
            } else if (rotation == 2) {
                oru = 1.0 - ru;
                orv = 1.0 - rv;
            } else if (rotation == 3) {
                oru = rv;
                orv = 1.0 - ru;
            }

            // get the iicon uvs for the tile
            float umin = cosmicuvs[symbol][0][0];
            float umax = cosmicuvs[symbol][1][0];
            float vmin = cosmicuvs[symbol][0][1];
            float vmax = cosmicuvs[symbol][1][1];

            // interpolate based on tile uvs
            cosmictex.x = umin * (1.0 - oru) + umax * oru;
            cosmictex.y = vmin * (1.0 - orv) + vmax * orv;

            tcol = texture(Sampler0, cosmictex);

            // set the alpha, blending out at the bunched ends
            float a = tcol.r * (0.5 + (1.0 / mult) * 1.0) * (1.0 - smoothstep(0.15, 0.48, abs(rawv - 0.5)));

            // 星星颜色 - 更真实的星空色彩
            float starType = mod(rand1 * rand2, 100.0);
            vec3 starColor;

float hue = mod(rand1*0.17 + rand2*0.23 + time*0.05, 1.0);
starColor = vec3(
0.5 + 0.5*cos(6.2831*hue + 0.0),
0.5 + 0.5*cos(6.2831*hue + 2.094),
0.5 + 0.5*cos(6.2831*hue + 4.188)
);
starColor *= sin(time*0.003 + rand1*0.1)*0.2 + 0.8; // 闪烁
col.rgb += starColor * a * 0.8;

            // 添加闪烁效果
            float twinkle = sin(time * 0.003 + rand1 * 0.1) * 0.2 + 0.8;
            starColor *= twinkle;

            // 根据距离调整亮度
            float distanceFade = 1.0 - float(i) / 20.0;
            starColor *= distanceFade;

            // mix the colours
            col = col + vec4(starColor, 1.0) * a;
        }
    }

    // apply lighting
    vec3 shade = vertexColor.rgb * (lightmix) + vec3(1.0 - lightmix, 1.0 - lightmix, 1.0 - lightmix);
    col.rgb *= shade;

    // apply mask
    col.a *= mask.r * opacity;

    col = clamp(col, 0.0, 1.0);

    fragColor = linear_fog(col * ColorModulator, vertexDistance, FogStart, FogEnd, FogColor);
}