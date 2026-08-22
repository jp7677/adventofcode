#include "aoc_private.h"
#include "util.h"

namespace aoc {

class Vulkan {
  public:
    Vulkan() {
        // Create instance

        const auto validation = std::string("VK_LAYER_KHRONOS_validation");
        if (vk::enumerateInstanceExtensionProperties(validation).empty())
            throw std::domain_error("VK_LAYER_KHRONOS_validation not found");

        auto appInfo = vk::ApplicationInfo{.pApplicationName = "aoc2026", .apiVersion = vk::ApiVersion14};
        std::vector layers = {validation.c_str()};
        constexpr auto levels = std::array<const char*, 2>({"error", "info"}); // info for shader printf
        constexpr auto enabled = static_cast<vk::Bool32>(vk::True);
        constexpr auto disabled = static_cast<vk::Bool32>(vk::False);
        auto layerSettings = std::array<vk::LayerSettingEXT, 4>{{
            {.pLayerName = validation.c_str(), .pSettingName = "report_flags", .type = vk::LayerSettingTypeEXT::eString, .valueCount = levels.size(), .pValues = levels.data()},
            {.pLayerName = validation.c_str(), .pSettingName = "validate_best_practices", .type = vk::LayerSettingTypeEXT::eBool32, .valueCount = 1, .pValues = &enabled},
            {.pLayerName = validation.c_str(), .pSettingName = "printf_enable", .type = vk::LayerSettingTypeEXT::eBool32, .valueCount = 1, .pValues = &enabled},
            {.pLayerName = validation.c_str(), .pSettingName = "check_shaders_caching", .type = vk::LayerSettingTypeEXT::eBool32, .valueCount = 1, .pValues = &disabled},
        }};
        auto instanceCreateInfo = vk::StructureChain<vk::InstanceCreateInfo, vk::LayerSettingsCreateInfoEXT>{
            {.pApplicationInfo = &appInfo, .enabledLayerCount = static_cast<uint32_t>(layers.size()), .ppEnabledLayerNames = layers.data()},
            {.settingCount = layerSettings.size(), .pSettings = layerSettings.data()},
        };
        instance_ = vk::createInstance(instanceCreateInfo.get<>());

        // Select physical device

        auto physicalDevices = instance_.enumeratePhysicalDevices();
        if (physicalDevices.empty())
            throw std::domain_error("No physical device found");

        physicalDevice_ = SelectPhysicalDevice(physicalDevices);
        auto props = physicalDevice_.getProperties2();
        std::cout << "Using Vulkan GPU: " << props.properties.deviceName << std::endl;

        // Create logical device and queue

        auto queueFamilies = physicalDevice_.getQueueFamilyProperties2();
        auto computeFlagPred = [](const auto& p) { return static_cast<bool>(p.queueFamilyProperties.queueFlags & vk::QueueFlags::BitsType::eCompute); };
        auto queueFamiliesIt = std::ranges::find_if(queueFamilies.begin(), queueFamilies.end(), computeFlagPred);
        if (queueFamiliesIt == queueFamilies.end())
            throw std::domain_error("Compute family not found");

        computeFamily_ = static_cast<u_int32_t>(std::distance(queueFamilies.begin(), queueFamiliesIt));

        auto queuePriority = 1.0f;
        auto deviceQueueCreateInfo = std::array<vk::DeviceQueueCreateInfo, 1>{{{.queueFamilyIndex = computeFamily_, .queueCount = 1, .pQueuePriorities = &queuePriority}}};
        auto deviceCreateInfo = vk::StructureChain<vk::DeviceCreateInfo, vk::PhysicalDeviceSynchronization2Features>{
            {.queueCreateInfoCount = deviceQueueCreateInfo.size(), .pQueueCreateInfos = deviceQueueCreateInfo.data()},
            {.synchronization2 = vk::True},
        };
        device_ = physicalDevice_.createDevice(deviceCreateInfo.get<>());
        computeQueue_ = device_.getQueue2({.queueFamilyIndex = computeFamily_});
    }

    void launchShader(void* inputData, size_t inputSize, void* outputData, size_t outputSize, std::string_view shader, std::string_view entrypoint, glm::uint32 n) const {
        // TODO: there is probably some reuse possible here

        INFO("Allocate input buffers (host-visible memory, slower)");

        vk::Buffer inputBuffer;
        vk::DeviceMemory inputMemory;
        createBuffer(physicalDevice_, device_, inputBuffer, inputSize, inputMemory);
        vk::Buffer outputBuffer;
        vk::DeviceMemory outputMemory;
        createBuffer(physicalDevice_, device_, outputBuffer, outputSize, outputMemory);

        INFO("Fill input buffers and reset output buffer");

        auto input = device_.mapMemory2({.memory = inputMemory, .size = inputSize});
        std::memcpy(input, inputData, inputSize);
        device_.unmapMemory2({.memory = inputMemory});

        auto output = device_.mapMemory2({.memory = outputMemory, .size = outputSize});
        std::memset(output, 0, outputSize);
        device_.unmapMemory2({.memory = outputMemory});

        INFO("Create compute pipeline");

        // Create shader
        auto spirvCode = util::getShaderCode(shader);
        auto shaderModule = device_.createShaderModule({.codeSize = spirvCode.size() * sizeof(uint32_t), .pCode = spirvCode.data()});

        // Create descriptor set layout (tells Vulkan about the buffer bindings)
        auto bindings = std::array<vk::DescriptorSetLayoutBinding, 2>{{
            {.binding = 0, .descriptorType = vk::DescriptorType::eStorageBuffer, .descriptorCount = 1, .stageFlags = vk::ShaderStageFlagBits::eCompute},
            {.binding = 1, .descriptorType = vk::DescriptorType::eStorageBuffer, .descriptorCount = 1, .stageFlags = vk::ShaderStageFlagBits::eCompute},
        }};
        auto descSetLayout = device_.createDescriptorSetLayout({.bindingCount = bindings.size(), .pBindings = bindings.data()});
        auto descSetLayouts = std::array<vk::DescriptorSetLayout, 1>{{descSetLayout}};

        // Push constant range
        auto pushConstants = std::array<vk::PushConstantRange, 1>{{{.stageFlags = vk::ShaderStageFlagBits::eCompute, .size = sizeof(n)}}};

        // Pipeline layout
        auto pipelineCreateInfo = vk::PipelineLayoutCreateInfo{
            .setLayoutCount = descSetLayouts.size(),
            .pSetLayouts = descSetLayouts.data(),
            .pushConstantRangeCount = pushConstants.size(),
            .pPushConstantRanges = pushConstants.data(),
        };
        auto pipelineLayout = device_.createPipelineLayout(pipelineCreateInfo);

        // Compute pipeline
        auto pipelineShaderStageCreateInfo = vk::PipelineShaderStageCreateInfo{
            .stage = vk::ShaderStageFlagBits::eCompute,
            .module = shaderModule,
            .pName = entrypoint.data(),
        };
        auto pipelines = device_.createComputePipelines({}, {{.stage = pipelineShaderStageCreateInfo, .layout = pipelineLayout}});
        REQUIRE_FALSE(pipelines->empty());
        auto pipeline = pipelines->front();

        INFO("Bind buffers to shader (Descriptor Set)");

        auto descriptorCount = std::ranges::fold_left(bindings, 0u, [](auto acc, auto& b) { return acc + b.descriptorCount; });
        auto poolSize = std::array<vk::DescriptorPoolSize, 1>{{{.type = vk::DescriptorType::eStorageBuffer, .descriptorCount = descriptorCount}}};
        auto descPool = device_.createDescriptorPool({.maxSets = 1, .poolSizeCount = poolSize.size(), .pPoolSizes = poolSize.data()});

        auto descSets = device_.allocateDescriptorSets({.descriptorPool = descPool, .descriptorSetCount = descSetLayouts.size(), .pSetLayouts = descSetLayouts.data()});
        REQUIRE_FALSE(descSets.empty());
        auto descSet = descSets.front();

        // Write buffer references into the descriptor set
        auto bufInfos = std::array<vk::DescriptorBufferInfo, 2>{{
            {.buffer = inputBuffer, .offset = 0, .range = inputSize},
            {.buffer = outputBuffer, .offset = 0, .range = outputSize},
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
        device_.updateDescriptorSets(writes.size(), writes.data(), 0, nullptr);

        INFO("Record and Submit Command Buffer");

        auto cmdPool = device_.createCommandPool({.queueFamilyIndex = computeFamily_});
        auto cmdBuffers = device_.allocateCommandBuffers({.commandPool = cmdPool, .level = vk::CommandBufferLevel::ePrimary, .commandBufferCount = 1});
        REQUIRE_FALSE(cmdBuffers.empty());
        auto cmdBuf = cmdBuffers.front();

        cmdBuf.begin({.flags = vk::CommandBufferUsageFlagBits::eOneTimeSubmit});
        cmdBuf.bindPipeline(vk::PipelineBindPoint::eCompute, pipeline);
        cmdBuf.bindDescriptorSets2({.stageFlags = vk::ShaderStageFlagBits::eCompute, .layout = pipelineLayout, .descriptorSetCount = 1, .pDescriptorSets = &descSet});
        cmdBuf.pushConstants2({.layout = pipelineLayout, .stageFlags = vk::ShaderStageFlagBits::eCompute, .size = sizeof(n), .pValues = &n});
        cmdBuf.dispatch((n + 255) / 256, 1, 1); // launch shader
        cmdBuf.end();

        auto fence = device_.createFence({});
        auto commandBufferSubmitInfo = vk::CommandBufferSubmitInfo{.commandBuffer = cmdBuf};
        computeQueue_.submit2({{.commandBufferInfoCount = 1, .pCommandBufferInfos = &commandBufferSubmitInfo}}, fence);
        REQUIRE(device_.waitForFences(1, &fence, vk::True, std::numeric_limits<uint64_t>::max()) == vk::Result::eSuccess);

        INFO("Get and validate output");

        output = static_cast<glm::uint32*>(device_.mapMemory2({.memory = outputMemory, .size = outputSize}));
        std::memcpy(outputData, output, outputSize);
        device_.unmapMemory2({.memory = outputMemory});

        INFO("Cleanup");

        device_.destroyFence(fence);
        device_.destroyCommandPool(cmdPool);
        device_.destroyPipeline(pipeline);
        device_.destroyPipelineLayout(pipelineLayout);
        device_.destroyDescriptorPool(descPool);
        device_.destroyDescriptorSetLayout(descSetLayout);
        device_.destroyShaderModule(shaderModule);
        device_.destroyBuffer(inputBuffer);
        device_.freeMemory(inputMemory);
        device_.destroyBuffer(outputBuffer);
        device_.freeMemory(outputMemory);
    }

    ~Vulkan() noexcept {
        device_.destroy();
        instance_.destroy();
    }

  private:
    static vk::PhysicalDevice SelectPhysicalDevice(const std::vector<vk::PhysicalDevice>& physicalDevices) {
        if (const auto deviceIndexEnv = std::getenv("DEVICE_INDEX")) {
            if (const auto index = std::stoul(deviceIndexEnv); index < physicalDevices.size()) {
                std::cout << "Using device index: " << index << std::endl;
                return physicalDevices[index];
            }
        }

        return physicalDevices.front();
    }

    static void createBuffer(vk::PhysicalDevice physicalDevice, vk::Device device, vk::Buffer& inputBuffer, u_int32_t bufferSize, vk::DeviceMemory& inputMemory) {
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

    vk::Instance instance_;
    u_int32_t computeFamily_;
    vk::PhysicalDevice physicalDevice_;
    vk::Device device_;
    vk::Queue computeQueue_;
};

struct Fixture {
    Vulkan vulkan;
};

} // namespace aoc