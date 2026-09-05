project('aoc2026',
  ['cpp'],
  default_options: [
    'cpp_std=c++23',
    'warning_level=2'
  ],
  version : 'v0.0.1',
  meson_version : '>= 1.11.0')

cpp = meson.get_compiler('cpp')
compiler_args = []
link_args = []

add_project_arguments(cpp.get_supported_arguments(compiler_args), language: 'cpp')
add_project_link_arguments(cpp.get_supported_link_arguments(link_args), language: 'cpp')

vulkan_dep = dependency('Vulkan')
glm_dep = dependency('glm')

lib_catch2 = cpp.find_library('Catch2')
lib_catch2_main = cpp.find_library('Catch2Main')

glsllang = find_program('glslangValidator')

subdir('src')

