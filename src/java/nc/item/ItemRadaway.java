package nc.item;

import nc.NuclearCraft;
import nc.player.PlayerRads;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRadaway extends ItemNC {
	
	public ItemRadaway(String nam, String... lines) {
		super("", nam, lines);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		PlayerRads playerRads = PlayerRads.get(player);
		--stack.stackSize;
		playerRads.removeRads(0.01F*NuclearCraft.radawayPercent*NuclearCraft.maxRads);
		return stack;
	}
	
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.drink;
	}
	
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 10;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		PlayerRads playerRads = PlayerRads.get(player);
		if (player.canEat(true)) {
			if (playerRads.currentRads() >= 0.01F*NuclearCraft.radawayPercent*NuclearCraft.maxRads || (playerRads.currentRads() > 0F && player.isSneaking())) {
				player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
			}
		}
		return stack;
    }
}
