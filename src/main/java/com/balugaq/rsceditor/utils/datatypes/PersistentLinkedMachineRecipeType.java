package com.balugaq.rsceditor.utils.datatypes;

import com.balugaq.rsceditor.api.objects.types.LinkedMachineRecipe;
import com.balugaq.rsceditor.utils.KeyUtil;
import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class PersistentLinkedMachineRecipeType implements PersistentDataType<PersistentDataContainer, LinkedMachineRecipe> {

    public static final PersistentDataType<PersistentDataContainer, LinkedMachineRecipe> TYPE = new PersistentLinkedMachineRecipeType();

    public static final NamespacedKey NAME = KeyUtil.newKey("name");
    public static final NamespacedKey CHOOSE_ONE = KeyUtil.newKey("choose_one");
    public static final NamespacedKey FOR_DISPLAY = KeyUtil.newKey("for_display");
    public static final NamespacedKey HIDE = KeyUtil.newKey("hide");
    public static final NamespacedKey PROCESSING_TIME = KeyUtil.newKey("processing_time");
    public static final NamespacedKey LINKED_INPUTS = KeyUtil.newKey("linked_inputs");
    public static final NamespacedKey LINKED_OUTPUTS = KeyUtil.newKey("linked_outputs");
    public static final NamespacedKey FREE_OUTPUTS = KeyUtil.newKey("free_outputs");

    @Override
    @Nonnull
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    @Nonnull
    public Class<LinkedMachineRecipe> getComplexType() {
        return LinkedMachineRecipe.class;
    }

    @Override
    @Nonnull
    public PersistentDataContainer toPrimitive(@Nonnull LinkedMachineRecipe complex, @Nonnull PersistentDataAdapterContext context) {
        final PersistentDataContainer container = context.newPersistentDataContainer();

        container.set(NAME, PersistentDataType.STRING, complex.getName());
        container.set(CHOOSE_ONE, DataType.BOOLEAN, complex.isChooseOne());
        container.set(FOR_DISPLAY, DataType.BOOLEAN, complex.isForDisplay());
        container.set(HIDE, DataType.BOOLEAN, complex.isHide());
        container.set(PROCESSING_TIME, PersistentDataType.INTEGER, complex.getProcessingTime());
        Map<Integer, ItemStack> inputs = complex.getLinkedInputs();
        ItemStack[] inputArray = new ItemStack[inputs.size() * 2];
        int index = 0;
        for (Map.Entry<Integer, ItemStack> entry : inputs.entrySet()) {
            inputArray[index] = new ItemStack(Material.BARRIER, entry.getKey() + 1); // first item is used to store the linked slot, + 1 for preventing 0 amount
            inputArray[index + 1] = entry.getValue(); // second item is the actual item
            index += 2;
        }
        container.set(LINKED_INPUTS, DataType.ITEM_STACK_ARRAY, inputArray);

        // for output, same as input, but with different key
        Map<Integer, ItemStack> outputs = complex.getLinkedOutputs();
        ItemStack[] outputArray = new ItemStack[outputs.size() * 2];
        index = 0;
        for (Map.Entry<Integer, ItemStack> entry : outputs.entrySet()) {
            outputArray[index] = new ItemStack(Material.BARRIER, entry.getKey() + 1);
            outputArray[index + 1] = entry.getValue();
            index += 2;
        }
        container.set(LINKED_OUTPUTS, DataType.ITEM_STACK_ARRAY, outputArray);

        container.set(FREE_OUTPUTS, DataType.ITEM_STACK_ARRAY, complex.getFreeOutputs());
        return container;
    }

    @Override
    @Nonnull
    public LinkedMachineRecipe fromPrimitive(@Nonnull PersistentDataContainer primitive, @Nonnull PersistentDataAdapterContext context) {
        final String name = primitive.getOrDefault(NAME, PersistentDataType.STRING, "");
        final boolean chooseOne = primitive.getOrDefault(CHOOSE_ONE, DataType.BOOLEAN, false);
        final boolean forDisplay = primitive.getOrDefault(FOR_DISPLAY, DataType.BOOLEAN, false);
        final boolean hide = primitive.getOrDefault(HIDE, DataType.BOOLEAN, false);
        final int processingTime = primitive.getOrDefault(PROCESSING_TIME, PersistentDataType.INTEGER, 0);
        final ItemStack[] inputArray = primitive.get(LINKED_INPUTS, DataType.ITEM_STACK_ARRAY);
        if (inputArray == null) {
            return new LinkedMachineRecipe(name, chooseOne, forDisplay, hide, processingTime, new HashMap<>(), new HashMap<>(), new ItemStack[0]);
        }
        final ItemStack[] outputArray = primitive.get(LINKED_OUTPUTS, DataType.ITEM_STACK_ARRAY);
        if (outputArray == null) {
            return new LinkedMachineRecipe(name, chooseOne, forDisplay, hide, processingTime, new HashMap<>(), new HashMap<>(), new ItemStack[0]);
        }
        final ItemStack[] freeOutputArray = primitive.get(FREE_OUTPUTS, DataType.ITEM_STACK_ARRAY);

        Map<Integer, ItemStack> inputs = new HashMap<>();
        for (int i = 0; i < inputArray.length; i += 2) {
            inputs.put(inputArray[i].getAmount() - 1, inputArray[i + 1]); // -1 for removing the prevention of 0 amount
        }

        Map<Integer, ItemStack> outputs = new HashMap<>();
        for (int i = 0; i < outputArray.length; i += 2) {
            outputs.put(outputArray[i].getAmount() - 1, outputArray[i + 1]);
        }

        return new LinkedMachineRecipe(name, chooseOne, forDisplay, hide, processingTime, inputs, outputs, freeOutputArray);
    }
}
