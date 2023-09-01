package net.zeeraa.novacore.spigot.abstraction.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.zeeraa.novacore.spigot.abstraction.enums.Attribute;
import net.zeeraa.novacore.spigot.abstraction.enums.EquipmentSlot;
import net.zeeraa.novacore.spigot.abstraction.enums.Operation;

public class AttributeInfo {
	private Attribute attribute;
	private double value;
	private Operation operation;
	private Collection<EquipmentSlot> equipmentSlots;

	public AttributeInfo(Attribute attribute, double value, Operation operation) {
		// defaulting to HAND, but if you want to change to ALL go for it
		this(attribute, value, operation, EquipmentSlot.HAND);
	}

	public AttributeInfo(Attribute attribute, double value, Operation operation, EquipmentSlot... equipmentSlots) {
		this(attribute, value, operation, new ArrayList<>(Arrays.asList(equipmentSlots)));
	}

	public AttributeInfo(Attribute attribute, double value, Operation operation, Collection<EquipmentSlot> equipmentSlots) {
		this.attribute = attribute;
		this.value = value;
		this.operation = operation;
		if (operation == null) {
			this.operation = Operation.ADD_NUMBER;
		}
		this.equipmentSlots = equipmentSlots;
		if (equipmentSlots == null || equipmentSlots.isEmpty()) {
			List<EquipmentSlot> al = new ArrayList<>();
			al.add(EquipmentSlot.ALL);
			this.equipmentSlots = al;
		}
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public double getValue() {
		return value;
	}

	public Operation getOperation() {
		return operation;
	}

	public Collection<EquipmentSlot> getEquipmentSlots() {
		return equipmentSlots;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public void setEquipmentSlots(Collection<EquipmentSlot> equipmentSlots) {
		this.equipmentSlots = equipmentSlots;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public void setValue(double value) {
		this.value = value;
	}
}