package nc.handler;

import nc.NuclearCraft;
import nc.player.PlayerRads;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RadHandler {
	private float radHealth(EntityPlayer player) {
		PlayerRads playerRads = PlayerRads.get(player);
		return Math.round(0.499999F + 20F - 20F*playerRads.currentRads()/NuclearCraft.maxRads);
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer && PlayerRads.get((EntityPlayer) event.entity) == null) {
			PlayerRads.register((EntityPlayer) event.entity);
		}
		if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(PlayerRads.EXT_PROP_NAME) == null) {
			event.entity.registerExtendedProperties(PlayerRads.EXT_PROP_NAME, new PlayerRads((EntityPlayer) event.entity));
		}
	}
	
	private void setEntityHealth(EntityLivingBase entity, float f) {
		entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double) f);
		if (entity.getHealth() > radHealth((EntityPlayer) entity)) entity.setHealth(f);
	}
	
	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		if ((event.entity instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) event.entity;
			PlayerRads playerRads = PlayerRads.get(player);
			playerRads.setRadRate(playerRads.currentRads() - playerRads.lastRads());
			setEntityHealth(player, radHealth((EntityPlayer) player));
			if (playerRads.currentRads() > 0.55F*NuclearCraft.maxRads) player.addPotionEffect(new PotionEffect(Potion.weakness.id, 40, 0)); else player.removePotionEffect(Potion.weakness.id);
			if (playerRads.currentRads() > 0.60F*NuclearCraft.maxRads) player.addPotionEffect(new PotionEffect(Potion.hunger.id, 40, 0)); else player.removePotionEffect(Potion.hunger.id);
			if (playerRads.currentRads() > 0.85F*NuclearCraft.maxRads) player.addPotionEffect(new PotionEffect(Potion.blindness.id, 40, 0)); else player.removePotionEffect(Potion.blindness.id);
			if (playerRads.currentRads() > 0.95F*NuclearCraft.maxRads) player.addPotionEffect(new PotionEffect(Potion.wither.id, 40, 2)); else player.removePotionEffect(Potion.wither.id);
			boolean wasAlreadyZero = playerRads.currentRadX() == 0F;
			playerRads.removeRadX(1F);
			if (playerRads.currentRadX() == 0F && !wasAlreadyZero) {
				if (!player.worldObj.isRemote) player.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "The effects of RadX wore off..."));
			}
			playerRads.setLastRads(playerRads.currentRads());
		}
	}
	
	/*@SubscribeEvent
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		PlayerRads playerRads = PlayerRads.get(player);
		playerRads.removeAllRads();
	}*/
	
	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if ((event.entity instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) event.entity;
			PlayerRads playerRads = PlayerRads.get(player);
			playerRads.removeAllRads();
			playerRads.removeAllRadX();
		}
	}
}