#version 450
#extension GL_EXT_debug_printf : enable

// Workgroup size: 256 invocations per workgroup (= threads per block in CUDA)
layout(local_size_x = 256) in;

// Buffer bindings (like kernel arguments)
layout(set = 0, binding = 0) buffer Input { uint data[]; };
layout(set = 0, binding = 1) buffer Output { uint result; };

// Push constant: small uniform data (like a kernel parameter)
layout(push_constant) uniform PushConstants { uint n; }; // number of elements

void main() {
    uint idx = gl_GlobalInvocationID.x;
    if (idx < n) {
        data[gl_GlobalInvocationID.x] += 1;
    }

    barrier();

    if (idx == 0) {
        for(int i = 0; i < n; i++) {
            result += data[i];
        }

        // debugPrintfEXT("day00.comp: result is %i", result);
    }
}