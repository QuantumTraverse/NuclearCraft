package nc.itemblock.generator;

import nc.NuclearCraft;
import nc.itemblock.ItemBlockNC;
import nc.player.PlayerRads;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockWRTG extends ItemBlockNC {

	public ItemBlockWRTG(Block block) {
		super(block, "Generates a constant stream of " + NuclearCraft.WRTGRF + " RF/t");
	}
	
	public void onUpdate(ItemStack stack, World world, Entity player, int num, boolean bool) {
		if (stack != null) {
			if (player instanceof EntityPlayer) {
				PlayerRads playerRads = PlayerRads.get((EntityPlayer) player);
				playerRads.addRads(0.1F*stack.stackSize);
			}
		}
		super.onUpdate(stack, world, player, num, bool);
	}
}