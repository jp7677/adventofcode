#include "aoc_private.h"
#include "util.h"

TEST_CASE("day00") {
    auto data = util::readInput("day00") //
        | std::views::transform([](auto& s) { return std::stoul(s); })
        | std::ranges::to<std::vector<uint32_t>>();

    INFO("Create instance");

    const auto validationLayer = std::string("VK_LAYER_KHRONOS_validation");
    REQUIRE_FALSE(vk::enumerateInstanceExtensionProperties(validationLayer).empty());
    std::vector layers = {validationLayer.c_str()};
    auto appInfo = vk::ApplicationInfo{.pApplicationName = "aoc2026", .apiVersion = vk::ApiVersion14};
    constexpr auto levels = std::array<const char*, 2>({"error", "info"}); // info for shader printf
    constexpr auto enabled = static_cast<vk::Bool32>(vk::True);
    constexpr auto disabled = static_cast<vk::Bool32>(vk::False);
    auto layerSettings = std::array<vk::LayerSettingEXT, 4>{{
        {.pLayerName = validationLayer.c_str(), .pSettingName = "report_flags", .type = vk::LayerSettingTypeEXT::eString, .valueCount = levels.size(), .pValues = levels.data()},
        {.pLayerName = validationLayer.c_str(), .pSettingName = "validate_best_practices", .type = vk::LayerSettingTypeEXT::eBool32, .valueCount = 1, .pValues = &enabled},
        {.pLayerName = validationLayer.c_str(), .pSettingName = "printf_enable", .type = vk::LayerSettingTypeEXT::eBool32, .valueCount = 1, .pValues = &enabled},
        {.pLayerName = validationLayer.c_str(), .pSettingName = "check_shaders_caching", .type = vk::LayerSettingTypeEXT::eBool32, .valueCount = 1, .pValues = &disabled},
    }};
    auto instanceCreateInfo = vk::StructureChain<vk::InstanceCreateInfo, vk::LayerSettingsCreateInfoEXT>{
        {.pApplicationInfo = &appInfo, .enabledLayerCount = static_cast<uint32_t>(layers.size()), .ppEnabledLayerNames = layers.data()},
        {.settingCount = layerSettings.size(), .pSettings = layerSettings.data()},
    };
    auto instance = vk::createInstance(instanceCreateInfo.get<>());

    INFO("Select physical device");

    auto physicalDevices = instance.enumeratePhysicalDevices();
    REQUIRE(!physicalDevices.empty());
    auto physicalDevice = util::SelectPhysicalDevice(physicalDevices);
    auto props = physicalDevice.getProperties2();
    WARN("Using Vulkan GPU: " << props.properties.deviceName);

    INFO("Create logical device and queue");

    auto computeFamily = util::findComputeFamily(physicalDevice);
    auto queuePriority = 1.0f;
    auto deviceQueueCreateInfo = std::array<vk::DeviceQueueCreateInfo, 1>{{{.queueFamilyIndex = computeFamily, .queueCount = 1, .pQueuePriorities = &queuePriority}}};
    auto deviceCreateInfo = vk::StructureChain<vk::DeviceCreateInfo, vk::PhysicalDeviceSynchronization2Features>{
        {.queueCreateInfoCount = deviceQueueCreateInfo.size(), .pQueueCreateInfos = deviceQueueCreateInfo.data()},
        {.synchronization2 = vk::True},
    };
    auto device = physicalDevice.createDevice(deviceCreateInfo.get<>());
    auto computeQueue = device.getQueue2({.queueFamilyIndex = computeFamily});

    INFO("Allocate input buffers (host-visible memory, slower)");

    glm::uint32 N = data.size(); // number of input elements
    auto inputSize = N * sizeof(glm::uint32);
    vk::Buffer inputBuffer;
    vk::DeviceMemory inputMemory;
    util::createBuffer(physicalDevice, device, inputBuffer, inputSize, inputMemory);

    auto outputBufferSize = sizeof(glm::uint32);
    vk::Buffer outputBuffer;
    vk::DeviceMemory outputMemory;
    util::createBuffer(physicalDevice, device, outputBuffer, sizeof(glm::uint32), outputMemory);

    INFO("Fill input buffers");

    auto input = static_cast<glm::uint32*>(device.mapMemory2({.memory = inputMemory, .size = inputSize}));
    std::memcpy(input, data.data(), inputSize);
    device.unmapMemory2({.memory = inputMemory});

    INFO("Create compute pipeline");

    // Create shader
    auto spirvCode = util::getShaderCode("day00");
    auto shaderModule = device.createShaderModule({.codeSize = spirvCode.size() * sizeof(uint32_t), .pCode = spirvCode.data()});

    // Create descriptor set layout (tells Vulkan about the buffer bindings)
    auto bindings = std::array<vk::DescriptorSetLayoutBinding, 2>{{
        {.binding = 0, .descriptorType = vk::DescriptorType::eStorageBuffer, .descriptorCount = 1, .stageFlags = vk::ShaderStageFlagBits::eCompute},
        {.binding = 1, .descriptorType = vk::DescriptorType::eStorageBuffer, .descriptorCount = 1, .stageFlags = vk::ShaderStageFlagBits::eCompute},
    }};
    auto descLayout = device.createDescriptorSetLayout({.bindingCount = bindings.size(), .pBindings = bindings.data()});
    auto descLayouts = std::array<vk::DescriptorSetLayout, 1>{{descLayout}};

    // Push constant range
    auto pushConstants = std::array<vk::PushConstantRange, 1>{{{.stageFlags = vk::ShaderStageFlagBits::eCompute, .size = sizeof(N)}}};

    // Pipeline layout
    auto pipelineCreateInfo = vk::PipelineLayoutCreateInfo{
        .setLayoutCount = descLayouts.size(),
        .pSetLayouts = descLayouts.data(),
        .pushConstantRangeCount = pushConstants.size(),
        .pPushConstantRanges = pushConstants.data(),
    };
    auto pipelineLayout = device.createPipelineLayout(pipelineCreateInfo);

    // Compute pipeline
    auto pipelineShaderStageCreateInfo = vk::PipelineShaderStageCreateInfo{
        .stage = vk::ShaderStageFlagBits::eCompute,
        .module = shaderModule,
        .pName = "main",
    };
    auto pipelines = device.createComputePipelines({}, {{.stage = pipelineShaderStageCreateInfo, .layout = pipelineLayout}});
    REQUIRE_FALSE(pipelines->empty());
    auto pipeline = pipelines->front();

    INFO("Bind buffers to shader (Descriptor Set)");

    auto descriptorCount = std::ranges::fold_left(bindings, 0u, [](auto acc, auto& b) { return acc + b.descriptorCount; });
    auto poolSize = std::array<vk::DescriptorPoolSize, 1>{{{.type = vk::DescriptorType::eStorageBuffer, .descriptorCount = descriptorCount}}};
    auto descPool = device.createDescriptorPool({.maxSets = 1, .poolSizeCount = poolSize.size(), .pPoolSizes = poolSize.data()});

    auto descSets = device.allocateDescriptorSets({.descriptorPool = descPool, .descriptorSetCount = descLayouts.size(), .pSetLayouts = descLayouts.data()});
    REQUIRE_FALSE(descSets.empty());
    auto descSet = descSets.front();

    // Write buffer references into the descriptor set
    auto bufInfos = std::array<vk::DescriptorBufferInfo, 2>{{
        {.buffer = inputBuffer, .offset = 0, .range = inputSize},
        {.buffer = outputBuffer, .offset = 0, .range = outputBufferSize},
    }};
    auto writes = std::array<vk::WriteDescriptorSet, 2>{{
        {
            .sType = vk::StructureType::eWriteDescriptorSet,
            .dstSet = descSet,
            .dstBinding = 0,
            .descriptorCount = 1,
            .descriptorType = vk::DescriptorType::eStorageBuffer,
            .pBufferInfo = &bufInfos[0],
        },
        {
            .sType = vk::StructureType::eWriteDescriptorSet,
            .dstSet = descSet,
            .dstBinding = 1,
            .descriptorCount = 1,
            .descriptorType = vk::DescriptorType::eStorageBuffer,
            .pBufferInfo = &bufInfos[1],
        },
    }};
    device.updateDescriptorSets(writes.size(), writes.data(), 0, nullptr);

    INFO("Record and Submit Command Buffer");

    auto cmdPool = device.createCommandPool({.queueFamilyIndex = computeFamily});
    auto cmdBuffers = device.allocateCommandBuffers({.commandPool = cmdPool, .level = vk::CommandBufferLevel::ePrimary, .commandBufferCount = 1});
    REQUIRE_FALSE(cmdBuffers.empty());
    auto cmdBuf = cmdBuffers.front();

    cmdBuf.begin({.flags = vk::CommandBufferUsageFlagBits::eOneTimeSubmit});
    cmdBuf.bindPipeline(vk::PipelineBindPoint::eCompute, pipeline);
    cmdBuf.bindDescriptorSets2({.stageFlags = vk::ShaderStageFlagBits::eCompute, .layout = pipelineLayout, .descriptorSetCount = 1, .pDescriptorSets = &descSet});
    cmdBuf.pushConstants2({.layout = pipelineLayout, .stageFlags = vk::ShaderStageFlagBits::eCompute, .size = sizeof(N), .pValues = &N});
    cmdBuf.dispatch((N + 255) / 256, 1, 1); // launch shader
    cmdBuf.end();

    auto fence = device.createFence({});
    auto commandBufferSubmitInfo = vk::CommandBufferSubmitInfo{.commandBuffer = cmdBuf};
    computeQueue.submit2({{.commandBufferInfoCount = 1, .pCommandBufferInfos = &commandBufferSubmitInfo}}, fence);
    REQUIRE(device.waitForFences(1, &fence, vk::True, std::numeric_limits<uint64_t>::max()) == vk::Result::eSuccess);

    INFO("Get and validate output");

    auto result = static_cast<glm::uint32*>(device.mapMemory2({.memory = outputMemory, .size = outputBufferSize}));
    REQUIRE(*result == 15u);
    device.unmapMemory2({.memory = outputMemory});

    INFO("Cleanup");

    device.destroyFence(fence);
    device.destroyCommandPool(cmdPool);
    device.destroyPipeline(pipeline);
    device.destroyPipelineLayout(pipelineLayout);
    device.destroyDescriptorPool(descPool);
    device.destroyDescriptorSetLayout(descLayout);
    device.destroyShaderModule(shaderModule);
    device.destroyBuffer(inputBuffer);
    device.freeMemory(inputMemory);
    device.destroyBuffer(outputBuffer);
    device.freeMemory(outputMemory);
    device.destroy();
    instance.destroy();
}