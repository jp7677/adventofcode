#pragma once

#include "aoc_private.h"

namespace util {

inline vk::PhysicalDevice SelectPhysicalDevice(const std::vector<vk::PhysicalDevice>& physicalDevices) {
    if (const auto deviceIndexEnv = std::getenv("DEVICE_INDEX")) {
        if (const auto index = std::stoul(deviceIndexEnv); index < physicalDevices.size()) {
            WARN("Using device index: " << index);
            return physicalDevices[index];
        }
    }

    return physicalDevices.front();
}

inline u_int32_t findComputeFamily(const vk::PhysicalDevice& physicalDevice) {
    auto queueFamilies = physicalDevice.getQueueFamilyProperties2();
    auto computeFlagPred = [](const auto& p) { return static_cast<bool>(p.queueFamilyProperties.queueFlags & vk::QueueFlags::BitsType::eCompute); };
    auto queueFamiliesIt = std::ranges::find_if(queueFamilies.begin(), queueFamilies.end(), computeFlagPred);
    REQUIRE(queueFamiliesIt != queueFamilies.end());
    return static_cast<u_int32_t>(std::distance(queueFamilies.begin(), queueFamiliesIt));
}

inline void createBuffer(vk::PhysicalDevice physicalDevice, vk::Device device, vk::Buffer& inputBuffer, u_int32_t bufferSize, vk::DeviceMemory& inputMemory) {
    inputBuffer = device.createBuffer({.size = bufferSize, .usage = vk::BufferUsageFlagBits::eStorageBuffer});

    auto memReqs = device.getBufferMemoryRequirements2({.buffer = inputBuffer});
    auto memProps = physicalDevice.getMemoryProperties2();

    // Find host-visible memory type
    auto propertyFlags = vk::MemoryPropertyFlags::BitsType::eHostVisible | vk::MemoryPropertyFlags::BitsType::eHostCoherent;
    auto memoryTypeIndex = 0u;
    for (auto i = 0u; i < memProps.memoryProperties.memoryTypeCount; i++) { // TODO: std::ranges::find_if ?
        if (memReqs.memoryRequirements.memoryTypeBits & (1 << i) && memProps.memoryProperties.memoryTypes[i].propertyFlags & propertyFlags) {
            memoryTypeIndex = i;
            break;
        }
    }

    inputMemory = device.allocateMemory({.allocationSize = memReqs.memoryRequirements.size, .memoryTypeIndex = memoryTypeIndex});
    device.bindBufferMemory2({{.buffer = inputBuffer, .memory = inputMemory}});
}

inline std::vector<uint32_t> getShaderCode(const std::string_view name) {
    auto shaderPathEnv = std::getenv("SHADER_PATH");
    REQUIRE(shaderPathEnv);
    auto shaderPath = std::string(shaderPathEnv);
    REQUIRE_FALSE(shaderPath.empty());
    if (shaderPath.ends_with('/'))
        shaderPath.pop_back();

    auto filePath = shaderPath + "/" + std::string(name) + ".comp.spv";
    WARN("Loading shader: " << std::filesystem::current_path().c_str() << "/" << filePath);

    auto file = std::ifstream(filePath, std::ios::ate | std::ios::binary);
    REQUIRE_FALSE(file.fail());

    auto fileSize = file.tellg();
    auto spirvCode = std::vector<uint32_t>(fileSize / sizeof(uint32_t));
    file.seekg(0);
    file.read(reinterpret_cast<char*>(spirvCode.data()), fileSize);
    return spirvCode;
}

inline std::vector<std::string> readInput(const std::string_view name) {
    auto dataPathEnv = std::getenv("DATA_PATH");
    REQUIRE(dataPathEnv);
    auto dataPath = std::string(dataPathEnv);
    REQUIRE_FALSE(dataPath.empty());
    if (dataPath.ends_with('/'))
        dataPath.pop_back();

    auto filePath = dataPath + "/" + std::string(name) + "-input.txt";
    WARN("Loading input data: " << std::filesystem::current_path().c_str() << "/" << filePath);
    std::ifstream file(filePath);
    REQUIRE(file.is_open());

    auto input = std::vector<std::string>();
    std::string line;
    while (std::getline(file, line))
        input.push_back(line);

    file.close();

    return input;
}

} // namespace util