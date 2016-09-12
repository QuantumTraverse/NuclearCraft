package nc.item;

import java.util.Random;

import nc.NuclearCraft;
import nc.player.PlayerRads;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemGeiger extends ItemNC {
	
	private Random rand = new Random();
	private double ticks = 0;
	private float lastRads = 0F;
	private double radsPerSecond = 0F;
	
	public ItemGeiger(String nam, String... lines) {
		super("", nam, lines);
	}
	
	public boolean isOnHotbar(ItemStack stack, EntityPlayer player) {
		for (int i = 0; i < 9; i++) {
			if (player.inventory.getStackInSlot(i) == stack) return true;
		}
		return false;
	}

	public void onUpdate(ItemStack stack, World world, Entity player, int num, boolean bool) {
		if (stack != null) {
			if (player instanceof EntityPlayer) {
				if (isOnHotbar(stack, (EntityPlayer) player)) {
					PlayerRads playerRads = PlayerRads.get((EntityPlayer) player);
					float rads = playerRads.currentRads();
					float radRate = playerRads.lastRadRate();
					if (ticks >= 100) {
						player.playSound("nc:geiger", 0.5F, 1.0F + (rand.nextFloat() - rand.nextFloat())/50F);
						//((EntityPlayer) player).addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Sound"));
						ticks = 0;
					} else {
						if (lastRads != 0F) ticks += rand.nextDouble()*(double)radRate;
						if (ticks < 0) ticks = 0;
					}
					radsPerSecond = Math.round(20D*(double)radRate);
					lastRads = rads;
					/*if (ticks >= 250000F/playerRads.currentRads()) {
						player.playSound("nc:geiger", 0.7F, 1.0F + (rand.nextFloat() - rand.nextFloat())/45F);
						ticks = rand.nextFloat() - 0.5;
					} else {
						ticks = ticks + rand.nextFloat();
					}*/
				}
			}
		}
		super.onUpdate(stack, world, player, num, bool);
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			PlayerRads playerRads = PlayerRads.get(player);
			
			/*if (player.isSneaking()) {
				playerRads.addRads(50000*rand.nextFloat());
				//playerRads.addAllRadX();
			}*/
			
			double radsPercent = 100D*playerRads.currentRads()/NuclearCraft.maxRads;
			EnumChatFormatting severity = radsPercent < 5 ? EnumChatFormatting.AQUA : radsPercent < 25 ? EnumChatFormatting.WHITE : radsPercent < 50 ? EnumChatFormatting.YELLOW : radsPercent < 75 ? EnumChatFormatting.RED : EnumChatFormatting.DARK_RED;
			player.addChatMessage(new ChatComponentText(severity + "Total Rad Count: " + playerRads.currentRadsInt() + " / " + NuclearCraft.maxRads + ", (" + (int) radsPercent + "%)"));
			player.addChatMessage(new ChatComponentText(severity + "Radiation Level: " + (int) Math.ceil(radsPerSecond) + " rad/s"));
			player.addChatMessage(new ChatComponentText(severity + "RadX Level: " + playerRads.currentRadXInt() + " / " + 1200*NuclearCraft.radXLifetime));
			//player.addChatMessage(new ChatComponentText(severity + "Ticks: " + (int) ticks));
		}
	    return stack;
	}
}
