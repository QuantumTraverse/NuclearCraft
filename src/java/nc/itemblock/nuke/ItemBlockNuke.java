package nc.itemblock.nuke;

import nc.itemblock.ItemBlockNC;
import nc.player.PlayerRads;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockNuke extends ItemBlockNC {

	public ItemBlockNuke(Block block) {
		super(block, "A cruel joke, a fun time, or just a big hole.", "Nuff said.");
	}
	
	public void onUpdate(ItemStack stack, World world, Entity player, int num, boolean bool) {
		if (stack != null) {
			if (player instanceof EntityPlayer) {
				PlayerRads playerRads = PlayerRads.get((EntityPlayer) player);
				playerRads.addRads(1F*stack.stackSize);
			}
		}
		super.onUpdate(stack, world, player, num, bool);
	}
}