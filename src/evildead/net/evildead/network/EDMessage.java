package net.evildead.network;

import net.evildead.mod.EvilDead;
import net.evildead.util.EDReference;
import net.evildead.wherethefunstarts.Summon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EDMessage implements IMessage{

	private String text;
	
	public EDMessage() {}
	
	public EDMessage(String text) {
		this.text = text;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		text = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, text);
	}
	
	
	
	public static class Handler implements IMessageHandler<EDMessage, IMessage> {

		@Override
		public IMessage onMessage(EDMessage message, MessageContext ctx) {

			// BEGIN SUMMON (Server)
			if(message.text.equals(EDReference.MessageFlags.SUMMON_MESSAGE)) {
				DebugPrint(ctx);
				Summon summon = new Summon(ctx.getServerHandler().playerEntity);
    			summon.begin();
			}
			
			// PLAY SOUND (Client)
			else if(message.text.startsWith(EDReference.MessageFlags.SOUND_MESSAGE)){

				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				ChunkCoordinates origin = player.getPlayerCoordinates();
		        
				/* Get Sound Name */
				String soundName = message.text.substring(EDReference.MessageFlags.SOUND_MESSAGE.length());
				/* Start Playing Sound */
				player.playSound(EvilDead.modid + ":" + soundName, 1.0f, 1.0f);
			}
			
			
			
			
			return null;
		}
		
		
		private void DebugPrint(MessageContext ctx){
			Side side = ctx.side;

			String msg = (ctx.side == Side.CLIENT) ? "CLIENT" : "SERVER";
			msg += " received";
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			
			IChatComponent kandaChat = new ChatComponentText(msg);
			player.addChatMessage(kandaChat);
		}
		
	}

}
