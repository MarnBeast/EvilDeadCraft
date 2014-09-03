package net.evildead.mod.network;

import net.evildead.mod.EvilDead;
import net.evildead.mod.util.EDReference;
import net.evildead.mod.wherethefunstarts.Summon;
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
			
			DebugPrint(ctx);
			
			// BEGIN SUMMON (Server)
			if(message.text.equals(EDReference.MessageFlags.SUMMON_MESSAGE)) {
				Summon summon = new Summon(ctx.getServerHandler().playerEntity);
    			summon.begin();
			}
			
			// PLAY SOUND (Client)
			else if(message.text.startsWith(EDReference.MessageFlags.SOUND_MESSAGE)){
				playSound(message, ctx);
			}
			
			return null;
		}
		
		
		private void DebugPrint(MessageContext ctx){
			Side side = ctx.side;

			String msg = (ctx.side == Side.CLIENT) ? "CLIENT" : "SERVER";
			msg += " received";
			
			EntityPlayer player;
			if(ctx.side == Side.CLIENT){
				player = Minecraft.getMinecraft().thePlayer;
			}
			else {
				player = ctx.getServerHandler().playerEntity;
			}
			
			IChatComponent kandaChat = new ChatComponentText(msg);
			player.addChatMessage(kandaChat);
		}
		
		
		private void playSound(EDMessage message, MessageContext ctx){

			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			/* Get Sound Info */
			String[] soundInfo = message.text.split(EDReference.MessageFlags.SOUND_DELIMITER);
			String soundName = (soundInfo.length > 1) ? soundInfo[1] : "";
			float soundVolume = 1.0f;
			try{
				soundVolume = (soundInfo.length > 2) ? Float.parseFloat(soundInfo[2]) : soundVolume;
			} catch(NumberFormatException e) {
				// leave default
			}
			
			float soundPitch = 1.0f;
			try{
				soundPitch = (soundInfo.length > 3) ? Float.parseFloat(soundInfo[3]) : soundPitch;
			} catch(NumberFormatException e) {
				// leave default
			}

			int posX;
			try{
				posX = (soundInfo.length > 4) ? Integer.parseInt(soundInfo[4]) : Integer.MIN_VALUE;
			} catch(NumberFormatException e) {
				posX = Integer.MIN_VALUE;
			}

			int posY;
			try{
				posY = (soundInfo.length > 5) ? Integer.parseInt(soundInfo[5]) : Integer.MIN_VALUE;
			} catch(NumberFormatException e) {
				posY = Integer.MIN_VALUE;
			}

			int posZ;
			try{
				posZ = (soundInfo.length > 6) ? Integer.parseInt(soundInfo[6]) : Integer.MIN_VALUE;
			} catch(NumberFormatException e) {
				posZ = Integer.MIN_VALUE;
			}

			/* Start Playing Sound */
			if(posX != Integer.MIN_VALUE && posY != Integer.MIN_VALUE && posZ != Integer.MIN_VALUE) {
				player.worldObj.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, EvilDead.modid + ":" + soundName, soundVolume, soundPitch, false);
			}
			else {
				player.playSound(EvilDead.modid + ":" + soundName, soundVolume, soundPitch);
			}
		}
	}

}
