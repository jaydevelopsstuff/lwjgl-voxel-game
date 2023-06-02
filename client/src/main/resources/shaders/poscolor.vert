#version 410
layout (location = 0) in vec3 position;
layout(location = 1) in vec4 color;

out vec4 vertexColor;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    vertexColor = color;
}