package dev.polaris_light.abysssonata.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class ModPayloads {
    private ModPayloads() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(
                DamageTypeScrollPayload.TYPE,
                DamageTypeScrollPayload.STREAM_CODEC,
                DamageTypeScrollPayload::handle
        );
    }
}
