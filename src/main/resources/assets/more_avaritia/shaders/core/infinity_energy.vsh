#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 vertexColor;
out vec2 vUv;
out float vDist;
out vec3 localPosition;
out vec3 viewDirection;

void main() {
    vec4 view = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * view;
    vertexColor = Color;
    vUv = UV0;
    vDist = length(view.xyz);
    localPosition = Position;
    vec3 cameraObject = (inverse(ModelViewMat) * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
    viewDirection = cameraObject - Position;
}
