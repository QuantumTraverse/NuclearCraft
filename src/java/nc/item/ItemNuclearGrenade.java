package nc.item;

import nc.entity.EntityNuclearGrenade;
import nc.player.PlayerRads;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNuclearGrenade extends ItemNC {

	public ItemNuclearGrenade(String nam, String... lines) {
		super("weapons", nam, lines);
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (!par3EntityPlayer.capabilities.isCreativeMode) {
			 --par1ItemStack.stackSize;
		}
		if (!par2World.isRemote) {
			par2World.spawnEntityInWorld(new EntityNuclearGrenade(par2World, par3EntityPlayer));
		} return par1ItemStack;
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
