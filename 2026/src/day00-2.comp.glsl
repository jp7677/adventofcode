#version 450
#extension GL_EXT_debug_printf : enable

struct Light {
    ivec3 position;
    uint intensity;
};

layout(local_size_x = 256) in;

layout(set = 0, binding = 0) buffer Input { Light data[]; };
layout(set = 0, binding = 1) buffer Output { uint result; };
layout(push_constant) uniform PushConstants { uint n; }; // number of elements

void main() {
    uint idx = gl_GlobalInvocationID.x;

    if (idx < n) {
        //        debugPrintfEXT("day00-2.comp[%i]: input is (%v3i), %i ", idx, data[idx].position, data[idx].intensity);
        uint i = data[idx].intensity;
        data[idx].position = data[idx].position.zxy;
        data[idx].position *= ivec3(i, i, i);
    }

    barrier();

    if (idx == 0) {
        for (int i = 0; i < n; i++) {
            result += data[i].position.x;
        }
        //        debugPrintfEXT("day00-2.comp[%i]: result is %i", idx, result);
    }
}