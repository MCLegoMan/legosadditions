package com.mclegoman.legosadditions.common.block;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.PersistentState;

public class LAEnderBarrelInventory extends PersistentState {
	public SimpleInventory ENDER_BARREL = new SimpleInventory(54);
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList itemsTag = new NbtList();

		for (int slot = 0; slot < ENDER_BARREL.size(); slot++) {
			ItemStack stack = ENDER_BARREL.getStack(slot);
			if (!stack.isEmpty()) {
				NbtCompound slotTag = new NbtCompound();
				slotTag.putInt("Slot", slot);
				stack.writeNbt(slotTag);
				itemsTag.add(slotTag);
			}
		}

		nbt.put("Items", itemsTag);
		return nbt;
	}

	public static LAEnderBarrelInventory readNbt(NbtCompound tag) {
		LAEnderBarrelInventory state = new LAEnderBarrelInventory();
		NbtList itemsTag = tag.getList("Items", 10);

		for (int i = 0; i < itemsTag.size(); i++) {
			NbtCompound slotTag = itemsTag.getCompound(i);
			int slot = slotTag.getInt("Slot");
			ItemStack stack = ItemStack.fromNbt(slotTag);
			state.ENDER_BARREL.setStack(slot, stack);
		}

		return state;
	}

	public static Type<LAEnderBarrelInventory> type = new Type<>(
			LAEnderBarrelInventory::new,
			LAEnderBarrelInventory::readNbt,
			null
	);
}
