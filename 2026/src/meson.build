shaderSrc = files([
  'day00.comp.glsl',
])

src = files([
  'main.cpp',
  'day00.cpp',
])

shaders = []
foreach s : shaderSrc
  shaders += custom_target(
    s.full_path().underscorify(),
    command : [glsllang, '-V', '--target-env', 'vulkan1.4', '@INPUT@', '-o',  '@OUTPUT@'],
    input : s,
    output : '@BASENAME@.spv',
    build_subdir: 'shaders')
endforeach

exe = executable('aoc2026', [ src, shaders ],
  name_prefix         : '',
  dependencies        : [ vulkan_dep, glm_dep, lib_catch2, lib_catch2_main ],
  install             : true)
