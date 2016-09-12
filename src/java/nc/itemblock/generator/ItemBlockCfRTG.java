package nc.itemblock.generator;

import nc.NuclearCraft;
import nc.itemblock.ItemBlockNC;
import nc.player.PlayerRads;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockCfRTG extends ItemBlockNC {

	public ItemBlockCfRTG(Block block) {
		super(block, "Generates a constant stream of " + NuclearCraft.CfRTGRF + " RF/t");
	}
	
	public void onUpdate(ItemStack stack, World world, Entity player, int num, boolean bool) {
		if (stack != null) {
			if (player instanceof EntityPlayer) {
				PlayerRads playerRads = PlayerRads.get((EntityPlayer) player);
				playerRads.addRads(8F*stack.stackSize);
			}
		}
		super.onUpdate(stack, world, player, num, bool);
	}
}