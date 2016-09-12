package nc.player;

import nc.NuclearCraft;
import nc.item.armour.ToughArmour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerRads implements IExtendedEntityProperties {

	public final static String EXT_PROP_NAME = "ExtendedPlayer";
	
	private final EntityPlayer player;
	
	private float /*currentRads, currentRadX,*/ maxRads;
	private float maxRadX = 1200F*NuclearCraft.radXLifetime;
	
	public static final int LASTRADS_WATCHER = 26;
	public static final int RADS_WATCHER = 27;
	public static final int RADX_WATCHER = 28;
	public static final int RADRATE_WATCHER = 29;
	
	public PlayerRads(EntityPlayer player) {
		this.player = player;
		// Start with min rads. Every player starts with the same amount (now done below).
		//this.currentRads = 0;
		this.maxRads = 1F*NuclearCraft.maxRads;
		this.player.getDataWatcher().addObject(LASTRADS_WATCHER, 0F);
		this.player.getDataWatcher().addObject(RADS_WATCHER, 0F);
		this.player.getDataWatcher().addObject(RADX_WATCHER, 0F);
		this.player.getDataWatcher().addObject(RADRATE_WATCHER, 0F);
	}
	
	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(PlayerRads.EXT_PROP_NAME, new PlayerRads(player));
	}
	
	public static final PlayerRads get(EntityPlayer player) {
		return (PlayerRads) player.getExtendedProperties(EXT_PROP_NAME);
	}
	
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		
		properties.setFloat("lastRads", this.player.getDataWatcher().getWatchableObjectFloat(LASTRADS_WATCHER));
		properties.setFloat("currentRads", this.player.getDataWatcher().getWatchableObjectFloat(RADS_WATCHER));
		properties.setFloat("currentRadX", this.player.getDataWatcher().getWatchableObjectFloat(RADX_WATCHER));
		properties.setFloat("currentRadRate", this.player.getDataWatcher().getWatchableObjectFloat(RADRATE_WATCHER));
		properties.setFloat("maxRads", this.maxRads);
		
		compound.setTag(EXT_PROP_NAME, properties);
	}
	
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		
		this.player.getDataWatcher().updateObject(LASTRADS_WATCHER, properties.getFloat("lastRads"));
		this.player.getDataWatcher().updateObject(RADS_WATCHER, properties.getFloat("currentRads"));
		this.player.getDataWatcher().updateObject(RADX_WATCHER, properties.getFloat("currentRadX"));
		this.player.getDataWatcher().updateObject(RADRATE_WATCHER, properties.getFloat("currentRadRate"));
		this.maxRads = properties.getFloat("maxRads");
	}
	
	public void init(Entity entity, World world) {}
	
	public void setRadRate(float rate) {
		this.player.getDataWatcher().updateObject(RADRATE_WATCHER, rate);
	}
	
	public float lastRadRate() {
		return this.player.getDataWatcher().getWatchableObjectFloat(RADRATE_WATCHER);
	}
	
	public void setLastRads(float rate) {
		this.player.getDataWatcher().updateObject(LASTRADS_WATCHER, rate);
	}
	
	public float lastRads() {
		return this.player.getDataWatcher().getWatchableObjectFloat(LASTRADS_WATCHER);
	}
	
	public boolean removeRads(float amount) {
		float rads = this.player.getDataWatcher().getWatchableObjectFloat(RADS_WATCHER);
		boolean below = amount >= rads;
		rads -= (amount < rads ? amount : rads);
		this.player.getDataWatcher().updateObject(RADS_WATCHER, rads);
		return below;
	}
	
	public boolean addRads(float amount) {
		float rads = this.player.getDataWatcher().getWatchableObjectFloat(RADS_WATCHER);
		float radX = this.player.getDataWatcher().getWatchableObjectFloat(RADX_WATCHER);
		
		float protection = 1F;
		if (this.player.getEquipmentInSlot(1) != null) if (this.player.getEquipmentInSlot(1).getItem() instanceof ToughArmour) protection *= 1.5F;
		if (this.player.getEquipmentInSlot(2) != null) if (this.player.getEquipmentInSlot(2).getItem() instanceof ToughArmour) protection *= 2F;
		if (this.player.getEquipmentInSlot(3) != null) if (this.player.getEquipmentInSlot(3).getItem() instanceof ToughArmour) protection *= 2F;
		if (this.player.getEquipmentInSlot(4) != null) if (this.player.getEquipmentInSlot(4).getItem() instanceof ToughArmour) protection *= 1.5F;
		
		float added = amount/protection;
		float added2 = radX > 0 ? added/((float) NuclearCraft.radXEffectiveness) : added;
		boolean over = added2 + rads > this.maxRads;
		rads = (added2 + rads <= this.maxRads ? added2 + rads : this.maxRads);
		this.player.getDataWatcher().updateObject(RADS_WATCHER, rads);
		return over;
	}
	
	public void removeAllRads() {
		float rads = this.player.getDataWatcher().getWatchableObjectFloat(RADS_WATCHER);
		rads = 0F;
		this.player.getDataWatcher().updateObject(RADS_WATCHER, rads);
	}
	
	public float currentRads() {
		return this.player.getDataWatcher().getWatchableObjectFloat(RADS_WATCHER);
	}
	
	public int currentRadsInt() {
		return (int) this.player.getDataWatcher().getWatchableObjectFloat(RADS_WATCHER);
	}
	
	public boolean removeRadX(float amount) {
		float radX = this.player.getDataWatcher().getWatchableObjectFloat(RADX_WATCHER);
		boolean below = amount >= radX;
		radX -= (amount < radX ? amount : radX);
		this.player.getDataWatcher().updateObject(RADX_WATCHER, radX);
		return below;
	}
	
	public boolean addRadX(float amount) {
		float radX = this.player.getDataWatcher().getWatchableObjectFloat(RADX_WATCHER);
		boolean over = amount + radX > this.maxRadX;
		radX = (amount + radX <= this.maxRadX ? amount + radX : this.maxRadX);
		this.player.getDataWatcher().updateObject(RADX_WATCHER, radX);
		return over;
	}
	
	public void addAllRadX() {
		float radX = this.player.getDataWatcher().getWatchableObjectFloat(RADX_WATCHER);
		radX = maxRadX;
		this.player.getDataWatcher().updateObject(RADX_WATCHER, radX);
	}
	
	public void removeAllRadX() {
		float radX = this.player.getDataWatcher().getWatchableObjectFloat(RADX_WATCHER);
		radX = 0F;
		this.player.getDataWatcher().updateObject(RADX_WATCHER, radX);
	}
	
	public float currentRadX() {
		return this.player.getDataWatcher().getWatchableObjectFloat(RADX_WATCHER);
	}
	
	public int currentRadXInt() {
		return (int) this.player.getDataWatcher().getWatchableObjectFloat(RADX_WATCHER);
	}
}