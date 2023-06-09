#version 410 core
layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoordIn;

out vec2 texCoord;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
    texCoord = texCoordIn;
}