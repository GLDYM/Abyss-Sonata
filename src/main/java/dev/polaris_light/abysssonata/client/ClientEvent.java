package dev.polaris_light.abysssonata.client;

import dev.polaris_light.abysssonata.registry.ModItems;
import dev.polaris_light.abysssonata.network.DamageTypeScrollPayload;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public final class ClientEvent {
    private ClientEvent() {
    }

    public static void registerConfigScreen(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (IConfigScreenFactory) (container, parent) -> new ConfigurationScreen(container, parent));
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent e) {
        CuriosRendererRegistry.register(ModItems.SAKURA_SPELL_BOOK.get(), SpellBookCurioRenderer::new);
        NeoForge.EVENT_BUS.addListener(ClientEvent::onMouseScroll);
        NeoForge.EVENT_BUS.addListener(ClientEvent::onMouseButton);
    }

    private static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        var player = Minecraft.getInstance().player;
        if (!isSneakingWithAbyssSonata() || event.getScrollDeltaY() == 0.0D) {
            return;
        }

        PacketDistributor.sendToServer(new DamageTypeScrollPayload(event.getScrollDeltaY() > 0.0D ? 1 : -1));
        event.setCanceled(true);
    }

    private static void onMouseButton(InputEvent.MouseButton.Pre event) {
        if (event.getButton() != GLFW.GLFW_MOUSE_BUTTON_MIDDLE || event.getAction() != GLFW.GLFW_PRESS
                || !isSneakingWithAbyssSonata()) {
            return;
        }

        PacketDistributor.sendToServer(new DamageTypeScrollPayload(0));
        event.setCanceled(true);
    }

    private static boolean isSneakingWithAbyssSonata() {
        var player = Minecraft.getInstance().player;
        return player != null && player.isShiftKeyDown()
                && (player.getMainHandItem().is(ModItems.ABYSS_SONATA.get())
                || player.getOffhandItem().is(ModItems.ABYSS_SONATA.get()));
    }
}
