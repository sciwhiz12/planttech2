package net.kaneka.planttech2.packets;

import net.kaneka.planttech2.PlantTechMain;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PlantTech2PacketHandler
{
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(PlantTechMain.MODID, "main_channel"))
	        .clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	public static final void register()
	{
		int i = 0;
		INSTANCE.registerMessage(i++, ButtonPressMessage.class, ButtonPressMessage::encode, ButtonPressMessage::decode, ButtonPressMessage.ButtonPressMessageHandler::handle);
		INSTANCE.registerMessage(i++, CropConfigChangeMessage.class, CropConfigChangeMessage::encode, CropConfigChangeMessage::decode, CropConfigChangeMessage.CropConfigChangeHandler::handle);
	}

	public static void sendToServer(Object msg)
	{
		INSTANCE.sendToServer(msg);
	}

	public static void sendTo(Object msg, EntityPlayerMP player)
	{
		if (!(player instanceof FakePlayer))
		{
			INSTANCE.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}
}
